FROM nimmis/java-centos:oracle-8-jdk as jdk8
EXPOSE 2890
ADD target/etrace-collector.tar.gz /etrace-collector/
WORKDIR /etrace-collector
RUN ls

ENTRYPOINT ["sh","run.sh", "start"]


