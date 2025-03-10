# Utiliser une image de base Java
FROM eclipse-temurin:21-jdk

VOLUME /tmp

# Définir le répertoire de travail
WORKDIR /app
# Ajouter un label pour identifier l'image
LABEL maintainer="edytchokouani@gmail.com"

# Copier le fichier JAR de l'application dans le conteneur
COPY target/notes-0.0.1-SNAPSHOT.jar notes-0.0.1-SNAPSHOT.jar

# Exposer le port que l'application utilisera
EXPOSE 8082

# Définir la commande par défaut pour exécuter l'application
ENTRYPOINT ["sh", "-c", "java  $JAVA_OPTS -jar notes-0.0.1-SNAPSHOT.jar"]