FROM openjdk:alpine
#FROM java:8-jdk
MAINTAINER Qi Yan <yanqi@betalpha.com>
RUN apk add  --update bash curl

ADD ./target/backend-fosun.jar /app/

COPY ./mosek/linux/mosek.jar /thirdparty/mosek/

COPY ./mosek/linux/libcilkrts.so.5 /thirdparty/mosek/
COPY ./mosek/linux/libiomp5.so /thirdparty/mosek/
COPY ./mosek/linux/libmosek64.so.8.0 /thirdparty/mosek/
COPY ./mosek/linux/libmosekjava8_0.so /thirdparty/mosek/
COPY ./mosek/linux/libmosekscopt8_0.so /thirdparty/mosek/
COPY ./mosek/linux/libmosekxx8_0.so /thirdparty/mosek/

COPY ./mosek/mosek.lic /root/mosek/

CMD ["java", "-Xmx8g", "-Dloader.path=/thirdparty/mosek/", "-Djava.library.path=/thirdparty/mosek/", "-jar", "/app/bar-svc-rest.jar"]

