# FlightMetasearch

Sample Application with tow APIs build using Java Dropwizard framework. Search API to trigger searches asynchronously and then poll API to keep pulling data until search has completed


Prerequisite
---

- MySQL server should be installed and running on your machine
- Redis should be installed and running on your machine
- You have Java 8 and Maven installed on your machine

How to start the FlightMetasearch application
---

1. Run `mvn clean install` command in project folder to build your application
2. Create empty database `create database flights` in MySQL on your machine
3. Update `flights-metasearch/config.yml` and `test/resources/config.yml` to adjust MySQL and Redis connection settings and endpoints
4. Run `java -jar target/flights-metasearch-0.0.1-SNAPSHOT.jar db migrate config.yml` to create database migrations. This will also load sample data into database
5. Start application with `java -jar target/flights-metasearch-0.0.1-SNAPSHOT.jar server config.yml`
6. To check that your application is running; enter url `http://localhost:8080`
7. For APIs path, parameters and details; access swagger at url `http://localhost:8080/swagger`


Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
