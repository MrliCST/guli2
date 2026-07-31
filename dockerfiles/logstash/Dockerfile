FROM logstash:7.17.6

LABEL maintainer="Lion Li"

USER root

# 替换为 JDK 17.0.13，修复 cgroup v2 兼容性问题 (JDK-8287073)
ADD https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_linux_hotspot_17.0.13_11.tar.gz /tmp/jdk.tar.gz
RUN tar -xzf /tmp/jdk.tar.gz -C /usr/share/logstash/ \
    && rm -rf /usr/share/logstash/jdk \
    && mv /usr/share/logstash/jdk-17.0.13+11 /usr/share/logstash/jdk \
    && rm -f /tmp/jdk.tar.gz

USER logstash
