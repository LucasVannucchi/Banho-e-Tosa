FROM eclipse-temurin:21-jdk-jammy
LABEL authors="lucas.vannucchi"
LABEL description="Banho e Tosa - Spring Boot Application"

WORKDIR /app

# Copia o jar gerado pelo Maven
COPY target/banhoetosa-0.0.1-SNAPSHOT.jar app.jar

# Usuário não-root
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080
ENV JAVA_OPTS="-Xmx512m -Xms256m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]