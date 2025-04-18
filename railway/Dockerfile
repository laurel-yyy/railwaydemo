FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY src ./src

# 构建应用
RUN apt-get update && apt-get install -y maven && \
    mvn package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre

WORKDIR /app

# 复制构建产物
COPY --from=builder /app/target/*.jar app.jar

# 设置时区
ENV TZ=Asia/Shanghai

# 创建日志目录
RUN mkdir -p /app/logs && \
    chmod 777 /app/logs

# 暴露端口
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]