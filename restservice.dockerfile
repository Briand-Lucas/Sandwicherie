FROM openjdk:8
VOLUME /tmp
ADD target/category-service-1.0.jar category-service.jar
RUN bash -c 'touch /restservice.jar'
ENTRYPOINT ["java", ""-Djava.security.egd=file:/dev/./urandom","-jar", "/category-service.jar"]
