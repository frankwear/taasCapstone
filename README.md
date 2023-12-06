# taasCapstone
Expand route planning funct
Overview
This document provides instructions on how to set up and run the TaaS web application. This application is structured with a Java-based backend using Maven for dependency management, Apache Tomcat as the server, and JSP (JavaServer Pages) for the frontend.

Prerequisites:
Java JDK [11]
Apache Maven [3.6.3]
Apache Tomcat [10.1.5]
A suitable IDE (e.g.,IntelliJ IDEA, Eclipse, or VSCode)

Installation:
Step 1: Clone the Repository
Clone the repository to your local machine

bash:
git clone [https://github.com/frankwear/taasCapstone]
cd [taasCapstone]

Step 2: Build the Project with Maven
Navigate to the project root directory and run:


mvn clean install
This command compiles the project and generates a WAR (Web Application Archive) file in the target directory.
The POM.XML file is included in the project main folder.

Step 3: Configuring Tomcat

Download and install Apache Tomcat.
Place the generated .war file from the target directory into the webapps directory of your Tomcat installation.

Running the Application

Starting the Tomcat Server

1) Navigate to the bin directory in your Tomcat installation folder.
2) Start the Tomcat server.
3) Now add the Tomcat Local to the IDE to create a connection between Frontend and Backend.

For Windows: Run startup.bat.
For Linux/Mac: Run startup.sh.

Accessing the Application

1) Open a web browser.
2) Go to http://localhost:8080/[war-file-name], replacing [war-file-name] with the name of your WAR file, minus the .war extension.
3) You should see the index.jsp page of your application.

Troubleshooting

Tomcat Doesn't Start: Ensure that the JAVA_HOME environment variable is set and points to your JDK installation.
Application Not Accessible: Verify that Tomcat is running and the WAR file is correctly placed in the webapps directory.

Additional Notes
- If you make changes to your Java code, remember to recompile and redeploy the WAR file.
- For more detailed configuration of your Tomcat server, refer to the official Apache Tomcat documentation.
- To run the backend demonstration code, go to src/test/java/com/down2thewire/TaasApplicationTest.java and run it.
- 
