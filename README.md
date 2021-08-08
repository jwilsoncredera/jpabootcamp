# JPA Bootcamp

### General
 
This application demonstrates multiple approaches for persistence in Java 
using a _Spring Data Repository_ pattern, 
and a _Spring Data JPA DAO & DTO_ based pattern.


### Running application

This application contains two distinct execution modes. 

1. It can be run as a stand-alone application which connects to a _MySQL database_ and executes the contents of the *run* method in **Module5Application.java**.

2. JUnit tests can be executed that create an in-memory _H2 database_, which is wiped after all of the tests have finished executing.

#### Stand-alone Mode
To run the stand-alone application, follow the steps below.
 
1. Check to see if mysql is installed. Run the following command from your terminal:

```
which mysql
``` 
If it does not return a value (e.g. `/usr/local/bin/mysql`), run these commands:
```
brew install mysql
brew postinstall mysql
```

2. Run the following commands to start the database server:

```
mysql.server start
mysql -u root -e "CREATE SCHEMA IF NOT EXISTS Module5;"
```
 
Liquibase will automatically create the tables and seed data when the application runs for the first time.

3. Right click **Module5Application.java** in your IDE and click "Run".

Alternatively, execute the following commands from the directory of this readme:
```
./mvnw clean install
java -jar target/module5-0.0.1-SNAPSHOT.jar
```

##### JUnit Test Mode
The application will be able to run in the second "test" mode without any outside setup scripts.


The JUnit tests are in `src/test/java/com/credera/bootcamp/module5`. 
They can be run with `./mvnw clean test` from the directory of this readme,
 or by right clicking on them in your IDE and selecting "Run".


### Liquibase

Liquibase will automatically create schemas and enter seed data when the application starts.

To see the SQL it will execute, you need a database to connect to.
Liquibase will only generate content that would be new for that database.

To make a new H2 database, follow these steps:
1. Download the latest H2 **Jar File** at http://www.h2database.com/html/download.html
2. Open a terminal
3. Go to the folder the JAR file downloaded to and execute the following command (the version number may differ): 
`java -jar ./h2-1.4.199.jar`
4. A browser window will open to `http://172.20.0.1:8082`
5. Click connect

To generate SQL, follow these steps:
1. Edit `src/main/resources/liquibase.properties` to point to your database
(they already point to the H2 database created above).
2. Open a terminal
3. Go to the folder this readme is in
3. Execute the following command: `mvn liquibase:updateSQL`
4. The SQL will be in `target/liquibase/migrate.sql`

To generate a changelog XML file from your model @Entity objects:
1. Edit `src/main/resources/liquibase.properties` to point to your database
(they already point to the H2 database created above).
2. Open a terminal
3. Go to the folder this readme is in
4. Execute the following command: `mvn liquibase:diff`
5. The SQL will be in `target/liquibase/liquibase-diff-changeLog.xml`


### Reference Documentation
For further reference, please consider the following sections:

* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#boot-features-jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#boot-features-validation)
* [Liquibase Migration](https://docs.spring.io/spring-boot/docs/2.5.3/reference/htmlsingle/#howto-execute-liquibase-database-migrations-on-startup)

### Guides
The following guides illustrate how to use some features concretely:

* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
* [Validating Form Input](https://spring.io/guides/gs/validating-form-input/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Source
Generated using https://start.spring.io/
