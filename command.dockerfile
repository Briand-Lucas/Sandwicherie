FROM openjdk:8
VOLUME /tmp
ADD target/category-service-1.0.jar command-service.jar
RUN bash -c 'touch /commandservice.jar'
ENTRYPOINT ["java", ""-Djava.security.egd=file:/dev/./urandom","-jar", "/command-service.jar"]
