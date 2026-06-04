FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM tomcat:9.0-jdk17-corretto

# Cài curl để dùng trong health check script
RUN yum install -y curl && yum clean all

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/target/*.war /usr/local/tomcat/webapps/ROOT.war

RUN sed -i 's/port="8005" shutdown="SHUTDOWN"/port="-1" shutdown="SHUTDOWN"/g' \
    /usr/local/tomcat/conf/server.xml

# Dùng grep kiểm tra trước khi sed để tránh silent fail
RUN grep -q 'AccessLogValve' /usr/local/tomcat/conf/server.xml && \
    sed -i '/<Valve className="org.apache.catalina.valves.AccessLogValve"/i \
    <Valve className="org.apache.catalina.valves.RemoteIpValve" remoteIpHeader="x-forwarded-for" protocolHeader="x-forwarded-proto" \/>' \
    /usr/local/tomcat/conf/server.xml || echo "AccessLogValve not found, skipping"

RUN printf '#!/bin/bash\n\
catalina.sh start\n\
echo "Waiting for Tomcat..."\n\
until curl -sf http://localhost:8080/ > /dev/null 2>&1; do\n\
    sleep 3\n\
done\n\
echo "Ready!"\n\
tail -f /usr/local/tomcat/logs/catalina.out\n' > /start.sh \
&& chmod +x /start.sh

EXPOSE 8080
CMD ["/start.sh"]