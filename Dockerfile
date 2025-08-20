# Imagen base de Java 17 (Temurin = Adoptium)
FROM eclipse-temurin:17-jdk

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiar el JAR generado por Maven (ajusta el nombre del jar real que aparece en /target)
COPY target/miapp-0.0.1-SNAPSHOT.jar app.jar

# Render espera que la app escuche en $PORT (variable de entorno)
ENV PORT=8081

# Exponer el puerto
EXPOSE 8081

# Comando para ejecutar tu aplicación
ENTRYPOINT ["java","-jar","app.jar"]
