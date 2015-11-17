/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
/*
 * COPYRIGHT LICENSE: This information contains sample code provided in source
 * code form. You may copy, modify, and distribute these sample programs in any 
 * form without payment to IBM for the purposes of developing, using, marketing 
 * or distributing application programs conforming to the application programming 
 * interface for the operating platform for which the sample code is written. 
 * 
 * Notwithstanding anything to the contrary, IBM PROVIDES THE SAMPLE SOURCE CODE 
 * ON AN "AS IS" BASIS AND IBM DISCLAIMS ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING, 
 * BUT NOT LIMITED TO, ANY IMPLIED WARRANTIES OR CONDITIONS OF MERCHANTABILITY, 
 * SATISFACTORY QUALITY, FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND ANY WARRANTY OR 
 * CONDITION OF NON-INFRINGEMENT. IBM SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE USE OR
 * OPERATION OF THE SAMPLE SOURCE CODE. IBM HAS NO OBLIGATION TO PROVIDE
 * MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS OR MODIFICATIONS TO THE SAMPLE
 * SOURCE CODE.
 * 
 * (C) Copyright IBM Corp. 2014.
 * 
 * All Rights Reserved. Licensed Materials - Property of IBM.  
 */
package org.javaee7.servlet.nonblocking;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

/**
 *
 */
public class ReadWriteEchoListener implements ReadListener, WriteListener {

    private final boolean listenerRegistered = false;
    private ServletInputStream inputStream = null;
    private AsyncContext context = null;
    private final ServletOutputStream outputStream;
    private AtomicBoolean inProgress = new AtomicBoolean();
    private final AtomicBoolean completePending;

    public ReadWriteEchoListener(ServletInputStream in, ServletOutputStream out, AsyncContext ac) {
        this.inputStream = in;
        this.context = ac;
        this.outputStream = out;
        this.inProgress = new AtomicBoolean(false);
        this.completePending = new AtomicBoolean(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.WriteListener#onWritePossible()
     */
    @Override
    public synchronized void onWritePossible() throws IOException {

        if (inputStream.isFinished())
            onAllDataRead();
        else
            writeOuput();

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ReadListener#onAllDataRead()
     */
    @Override
    public void onAllDataRead() throws IOException {
        if (completePending.get() == true) {
            if (completePending.compareAndSet(outputStream.isReady(), false)) {
                outputStream.println("</body></html>");
                context.complete();
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ReadListener#onDataAvailable()
     */
    @Override
    public void onDataAvailable() throws IOException {
        if (!listenerRegistered) {
            outputStream.setWriteListener(this);
        }
        writeOuput();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.ReadListener#onError(java.lang.Throwable)
     */
    @Override
    public void onError(Throwable arg0) {
        Logger.getLogger(ReadWriteEchoListener.class.getName()).log(Level.SEVERE, null, arg0);
        context.complete();
    }

    private void writeOuput() throws IOException {

        if (inProgress.compareAndSet(false, true)) {
            synchronized (this) {
                inProgress.set(inputStream.isReady() && !inputStream.isFinished() && outputStream.isReady());
                while (inProgress.get()) {
                    int len = -1;
                    byte b[] = new byte[1024];
                    len = inputStream.read(b);
                    if (len != -1) {
                        String data = new String(b, 0, len);
                        outputStream.println("<br> Data Read : length = " + len + " : " + data);
                    }
                    inProgress.set(inputStream.isReady() && !inputStream.isFinished() && outputStream.isReady());
                }
            }
        }
    }

}
