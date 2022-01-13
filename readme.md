This is a replica of a Blogging Applicaton's Backend , implemented using Java and Spring boot framework and MySQL with JDBC template for persistence!

Implemented and tested on the following specs and versions -

1. MySQL VERSION - **8.0.27-0**
2. OS - **ubuntu0.20.04.1**
3. Java 11 
4. Maven version - **Apache Maven 3.6.3**

**For localhost ->**
**************************************************************************************
**Note = Refer to application.properties file to make a few changes required to run the server on local machine(Without docker)**

**Steps to create DB schema ->**
Ensure that you have a user '**admin**' created already in your MySQL server, with password '**admin123**'

1. DB schema can be build using the following command
   1. cd src/main/resources/
   2. mysql -u admin -p -e 'source db.sql';
   
2. To build and run the server , make sure you are in the dir **/BloggingApplication/** i.e root project dir and execute the command - **mvn spring-boot:run**
   
3. Access the swagger documentation at localhost:8085/swagger-ui.html

************************************************************************************************************

**To run the API server on a docker container follows the steps as listed below ->**

**Prereqs- Docker**
**Docker can be installed by doing ->**
sudo apt-get update
sudo apt-get install wget
wget -qO- https://get.docker.com/ | sh
sudo usermod -aG docker $USER
sudo service docker start
newgrp docker

****************************************************************************************************
Note - Instead of step 1 listed below, the docker image for this backend server can also be downloaded directly from Docker Hub

From here -> https://hub.docker.com/r/anchal82199/blogappbackend
****************************************************************************************************

1. Use the Dockerfile from root project dir to create a docker image for the API server
   - Ensure that you are in dir BloggingApplication/
   - Run the command to create a jar file
     - **mvn clean package**
     - This will create the jar file inside **target/** dir
   - Run the command to build docker image for the application
     - **sudo docker build -t blogappbackend .**
     - 
2. Create a new network for the MYSQL container and blogappbackend docker containers , so that both of them can talk to each other
   - Run the command  **sudo docker network create blogapp-net** to make a network 'blogapp-net'
   - 
3. Since the application uses MYSQL for persistence , docker image for MYSQL is also required
   - To fetch the MYSQL image and run it as container, run the command **docker run --name mysql-standalone --network blogapp-net -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=test -e MYSQL_USER=admin -e MYSQL_PASSWORD=admin123 -d mysql:8.0.27**
  
4. Now we have to create the DB schema inside the running MYSQL container 
   -  Copy the 'src/main/resources/db.sql' file to the running mysql container first
      -  You can use the command **docker ps** to get the container id
      -  Then use the command **docker cp <path-to-db.sql> <mysql-container-id>:/**
      -  To run the db.sql file inside mysql container , first get inside 
         the mysql container using command **docker exec -it <mysql-container-id> bash**
      - Now run the command **mysql -u root -p -e 'source db.sql'** once you are inside the container
      - You can check if the DB schema got create in MYSQL or not by logging into mysql console on the container as root user with command **mysql -u root -p** 
5. Now run the blogappbackend container on network blogapp-net (as previously created), with command 
      **docker run --network=blogapp-net -p8085:8085 blogappbackend**
      This will bind the port 8085 on docker container to port 8085 on the host machine

6. Once the server starts successfully on port 8085 of container , you can start making requests at **localhost:8085**
   - Go to  **localhost:8085/swagger-ui.html** for swagger doc