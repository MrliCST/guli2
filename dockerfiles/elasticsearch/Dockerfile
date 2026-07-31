FROM elasticsearch:7.17.6

LABEL maintainer="Lion Li"

# 替换为 JDK 17.0.13，修复 cgroup v2 兼容性问题 (JDK-8287073)
ADD https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.13%2B11/OpenJDK17U-jdk_x64_linux_hotspot_17.0.13_11.tar.gz /tmp/jdk.tar.gz
RUN tar -xzf /tmp/jdk.tar.gz -C /usr/share/elasticsearch/ \
    && rm -rf /usr/share/elasticsearch/jdk \
    && mv /usr/share/elasticsearch/jdk-17.0.13+11 /usr/share/elasticsearch/jdk \
    && rm -f /tmp/jdk.tar.gz
