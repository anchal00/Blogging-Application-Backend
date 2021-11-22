This is a replica of a Blogging Applicaton's Backend , implemented using Java and Spring boot framework and MySQL with JDBC template for persistence!

Implemented and tested on the following specs and versions -

1. MySQL VERSION - **8.0.27-0**
2. OS - **ubuntu0.20.04.1**
3. Java 11 
4. Maven version - **Apache Maven 3.6.3**

**Steps to create DB schema ->**

Ensure that you have a user '**admin**' created already in your MySQL server, with password '**admin123**'

1. DB schema can be build using the following command
   1. cd src/main/resources/
   2. mysql -u admin -p -e 'source db.sql';
   
2. To build and run the server , make sure you are in the dir **/BloggingApplication/** i.e root project dir and execute the command - **mvn spring-boot:run**
   
3. Access the swagger documentation at localhost:8080/swagger-ui.html