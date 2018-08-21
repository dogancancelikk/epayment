
This is simple rest based payment transfer service using spring boot 2.0.4 maven 3.x java 1.8 . This service allows you to create account and transfer money from one account to another.

## Building and Running
This application is packaged as a jar which has Tomcat 8 embedded. No Tomcat or JBoss installation is necessary. You run it using the java -jar command.

- Clone this repository
- Make sure you are using JDK 1.8 and Maven 3.x
- You can build the project and run the tests by running mvn clean package
- Once successfully built, you can run the service by this command:
```
java -jar -Dspring.profiles.active=test target/transfer-0.0.1-SNAPSHOT.jar 
```
