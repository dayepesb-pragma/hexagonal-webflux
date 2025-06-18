FROM --platform=linux/amd64 amazoncorretto:17-alpine-jdk AS builder

RUN apk add tzdata
RUN cp /usr/share/zoneinfo/America/Bogota /localtime

RUN jlink --compress 2 --module-path /opt/jdk/jdk-17/jmods/\
 --add-modules java.base,java.logging,java.xml\
,jdk.unsupported,java.sql,java.naming,java.desktop\
,java.management,java.security.jgss,java.instrument\
,jdk.management,jdk.crypto.cryptoki\
 --no-header-files --no-man-pages --output /jlinked

FROM --platform=linux/amd64 amazoncorretto:17-alpine-jdk

COPY --from=builder /localtime /etc/localtime/
RUN echo "America/Bogota" > /etc/timezone && addgroup -S user && adduser -S user -G user

USER user

ENV JAVA_HOME /opt/jdk
ENV PATH $JAVA_HOME/bin:$PATH

COPY --from=builder /jlinked /opt/jdk/

ADD target/hexagonal-webflux-0.0.1-SNAPSHOT.jar hexagonal-webflux-0.0.1-SNAPSHOT.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/hexagonal-webflux-0.0.1-SNAPSHOT.jar"]

