FROM openjdk:8-alpine

COPY target/uberjar/clj-guestbook.jar /clj-guestbook/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/clj-guestbook/app.jar"]
