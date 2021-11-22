**Steps to create DB schema ->**

MySQL VERSION - **8.0.27-0**
OS - **ubuntu0.20.04.1**

Ensure that you have a user '**admin**' created already in your MySQL server, with password '**admin123**'

1. DB schema can be build using the following command
   1. cd src/main/resources/
   2. mysql -u admin -p -e 'source db.sql';
   