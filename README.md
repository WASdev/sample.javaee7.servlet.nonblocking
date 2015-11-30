  
# Sample Java EE 7 - Non-blocking I/O using Servlet 3.1


This application demonstrates how to use non-blocking I/O by using new APIs added to ServletInputStream and ServletOutputStream. 

The example includes usage of ReadListener and WriteListener interface APIs.

## Getting Started

Browse the code to see what it does, or build and run it yourself.


## Running in Eclipse

1. Download and install [Eclipse with the WebSphere Developer Tools](https://developer.ibm.com/wasdev/downloads/liberty-profile-using-eclipse/).
2. Create a new Liberty Profile Server.
3. Clone this repository.
4. Import the sample into Eclipse using *File -> Import -> Maven -> Existing Maven Projects* option.
5. Deploy the sample into Liberty server. Right click on the project and select *Run As -> Run on Server* option. Find and select the Liberty profile server and press *Finish*.
6. Go to: [http://localhost:9080/sample.javaee7.servlet.nonblocking/](http://localhost:9080/sample.javaee7.servlet.nonblocking/)

## Running with Maven

This project can be build with [Apache Maven](http://maven.apache.org/). The project uses [Liberty Maven Plug-in][] to automatically download and install Liberty profile runtime from the [Liberty repository](https://developer.ibm.com/wasdev/downloads/). Liberty Maven Plug-in is also used to create, configure, and run the application on the Liberty server. 

Use the following steps to run the application with Maven:

1. Execute full Maven build. This will cause Liberty Maven Plug-in to download and install Liberty profile server.
    ```bash
    $ mvn clean install
    ```

2. To run the server with the Servlet Non-Blocking application execute:
    ```bash
    $ mvn liberty:run-server
    ```

Once the server is running, the application will be available under [http://localhost:9080/sample.javaee7.servlet.nonblocking/](http://localhost:9080/sample.javaee7.servlet.nonblocking/).

## Deploying to Bluemix

Click the button below to deploy your own copy of this application to [Bluemix](https://bluemix.net).

[![Deploy to Bluemix](https://bluemix.net/deploy/button.png)](https://bluemix.net/deploy?repository=https://github.com/WASdev/sample.javaee7.servlet.nonblocking)

Once the application is deployed and running in bluemix, it will be available under 
[http://MYAPPNAME.mybluemix.net/sample.javaee7.servlet.nonblocking/](http://MYAPPNAME.mybluemix.net/sample.javaee7.servlet.nonblocking/).
