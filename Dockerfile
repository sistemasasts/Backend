# Usamos una imagen ligera de Java 8
FROM eclipse-temurin:8-jre

# Definimos el puerto (por defecto Spring Boot usa 8080)
EXPOSE 8440

# Copiamos el archivo .jar generado (asegúrate de hacer 'mvn package' primero)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-Xmx512m", "-jar", "/app.jar"]