# Utilisation de l'image Maven pour la construction
FROM maven:3.9.2-eclipse-temurin-17 AS build
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# Copier tout le reste et compiler l'application
COPY src /app/src
RUN mvn clean package -DskipTests

# Utilisation d'une image Java pour exécuter l'application
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copier le JAR généré depuis l'étape précédente
COPY --from=build /app/target/Open_Docker.jar /app/Open_Docker.jar

# Port exposé pour l'application
EXPOSE 8080

# Commande pour lancer l'application Spring Boot
ENTRYPOINT ["java", "-jar", "/app/Open_Docker.jar"]
