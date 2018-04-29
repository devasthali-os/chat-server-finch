FROM ubuntu:latest

RUN apt-get update && apt-get install -q -y --no-install-recommends wget
RUN apt-get install -q -y unzip
RUN apt-get install -q -y curl
RUN apt-get install -q -y vim

RUN mkdir /opt/java8

RUN wget --quiet --no-check-certificate -c --header "Cookie: oraclelicense=accept-securebackup-cookie" http://download.oracle.com/otn-pub/java/jdk/8u171-b11/512cd62ec5174c3487ac17c61aaa89e8/jdk-8u171-linux-x64.tar.gz

RUN tar -zxvf jdk-8u171-linux-x64.tar.gz -C /opt/java8 --strip-components=1

CMD ls -l /opt/java8

ENV JAVA_HOME /opt/java8
ENV PATH $JAVA_HOME/bin:$PATH

COPY chatServerApi/target/scala-2.12/chatServerApi.jar /home/chatServerApi.jar

EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/home/chatServerApi.jar"]
