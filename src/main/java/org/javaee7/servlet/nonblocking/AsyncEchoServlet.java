/*
 * IBM Confidential
 *
 * OCO Source Materials
 *
 * WLP Copyright IBM Corp. 2015
 *
 * The source code for this program is not published or otherwise divested 
 * of its trade secrets, irrespective of what has been deposited with the 
 * U.S. Copyright Office.
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 */
@WebServlet(urlPatterns = { "/AsyncEchoServlet" }, asyncSupported = true)
public class AsyncEchoServlet extends HttpServlet {

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            ServletOutputStream output = response.getOutputStream();
            output.println("<html>");
            output.println("<head>");
            output.println("<title>Reading asynchronously</title>");
            
            output.println("<meta charset=\"utf-8\"></meta>");
            output.println("<title>Servlet 3.1 WAS Classic tests</title>");
            output.println("<style>");
            output.println(".frm1{padding: 15px;background-color: #9666af; margin-bottom: 10px;}");
            output.println(".frm2{padding-left: 25px; font-family: Verdana; color: #440055;}");
            output.println(".big{font-size: 26px; color: white;}");
            output.println(".small{font-size: 12px;}");
            output.println("button, select{padding: 5px; padding-left: 20px; padding-right: 20px; margin:10px; width: 270px}");
            output.println("</style>");
            output.println("</head>");
            output.println("<body>");
            output.println("<body>");
            output.println("<div class=\"frm1\">");
            output.println("<div class=\"big\"> WAS Java EE 7 Sample - Non-blocking I/O using Servlet 3.1</div>");
            output.println("</div>");
            output.println("<div class=\"frm2\">"); 
            output.println("</div>");
            
            output.println("</head>");
            output.println("<body>");
            output.println("<h3>The following post data was read and written asynchronously using a combined Servlet 3.1 readListener and writeListener</h3>");

            AsyncContext context = request.startAsync();
            ServletInputStream input = request.getInputStream();
            input.setReadListener(new ReadWriteEchoListener(input, output, context));

        } catch (IOException ex) {
            Logger.getLogger(AsyncEchoServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
