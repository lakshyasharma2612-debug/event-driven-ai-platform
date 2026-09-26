# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

# Build event-contracts first
COPY event-contracts/pom.xml event-contracts/pom.xml
COPY event-contracts/src event-contracts/src

RUN mvn -f event-contracts/pom.xml clean install -DskipTests

# Build analysis service
COPY ["analysis service/pom.xml" ,"analysis service/pom.xml"]
COPY ["analysis service/src", "analysis service/src/"]

RUN mvn -f "analysis service/pom.xml" clean package -DskipTests


# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --create-home appuser

COPY --from=builder ["/build/analysis service/target/*.jar", "app.jar"]

RUN mkdir -p /app/uploads \
    && chown -R appuser:appuser /app

USER appuser

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]