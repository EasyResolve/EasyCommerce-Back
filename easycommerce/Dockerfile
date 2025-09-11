FROM amazoncorretto:23 AS build
WORKDIR /la27_backend

RUN yum update -y && yum install -y maven && yum clean all

COPY . .
RUN mvn clean package -DskipTests

FROM amazoncorretto:23
WORKDIR /la27_backend

COPY --from=build /la27_backend/target/*.jar app.jar
EXPOSE 8080

CMD [ "java", "-jar", "app.jar"  ]