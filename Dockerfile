FROM openjdk:8u121-jre-alpine
MAINTAINER Automation Rhapsody https://automationrhapsody.com/

WORKDIR /var/flights-metasearch

ADD target/flights-metasearch-0.0.1-SNAPSHOT.jar /var/flights-metasearch/flights-metasearch-1.0.0.jar
ADD config.yml /var/flights-metasearch/config.yml

CMD java -jar flights-metasearch-1.0.0.jar db migrate /var/flights-metasearch/config.yml

CMD java -jar flights-metasearch-1.0.0.jar server /var/flights-metasearch/config.yml

EXPOSE 8080 8081