# FROM 指定基础镜像
FROM openjdk:8-jdk-alpine
# ARG 构建参数
ARG JAR_FILE=build/libs/*.jar
# COPY 复制文件
COPY target/fsgplus-*.jar app.jar
# 暴露端口
EXPOSE 9000
# CMD 容器启动命令
CMD java -jar /app.jar