FROM openjdk:8
LABEL MAINTAINER=raudbjorn@gmail.com

ENV JAVA_OPTS -Djava.library.path=/usr/src/native-bins/
ENV APPLICATION_SECRET super-secret-string

EXPOSE 9000

COPY ./target/universal/knapsack-optimizer-service-1.0-SNAPSHOT.zip /usr/src/
COPY ./lib/*.so* /usr/src/native-bins/

RUN apt-get install -y unzip
WORKDIR /usr/src/
RUN unzip /usr/src/knapsack-optimizer-service-1.0-SNAPSHOT.zip
ENTRYPOINT ["/usr/src/knapsack-optimizer-service-1.0-SNAPSHOT/bin/knapsack-optimizer-service"]