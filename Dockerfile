FROM eclipse-temurin:21-jre-alpine

# Allow overriding the agent version at build time
ARG OTEL_AGENT_VERSION=2.20.1
ARG JAR_FILE
ARG OTEL_AGENT_URL=https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v${OTEL_AGENT_VERSION}/opentelemetry-javaagent.jar
# Create directories
RUN mkdir -p /app /otel

# Copy your application JAR
COPY ${JAR_FILE} /app/app.jar

# Download the OpenTelemetry Java agent (cached by layer)
# If your CI blocks external downloads, prefer baking the agent into the image or mounting it at runtime.
RUN wget -qO /otel/opentelemetry-javaagent.jar "${OTEL_AGENT_URL}" \
  && chmod 644 /otel/opentelemetry-javaagent.jar

# Default environment variables for auto-instrumentation.
# Override at runtime (kubectl/Helm/Flux) as needed.
ENV OTEL_SERVICE_NAME=rss-radar \
    OTEL_EXPORTER_OTLP_ENDPOINT=http://alloy:4318 \
    OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf \
    OTEL_TRACES_EXPORTER=otlp \
    OTEL_METRICS_EXPORTER=otlp \
    OTEL_LOGS_EXPORTER=otlp


ENTRYPOINT ["sh", "-c", "java ${JAVA_TOOL_OPTIONS} -javaagent:/otel/opentelemetry-javaagent.jar -jar /app/app.jar"]
