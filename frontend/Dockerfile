# 构建阶段
FROM node:20-alpine AS build-stage

WORKDIR /app

# 复制并安装依赖
COPY package*.json ./
RUN npm install

# 复制源代码和配置文件
COPY . .

# 构建生产版本
RUN npm run build

# 生产阶段
FROM nginx:stable-alpine AS production-stage

# 复制构建产物
COPY --from=build-stage /app/dist /usr/share/nginx/html

# 复制Nginx配置 - 确保文件存在且路径正确
COPY ./nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 运行Nginx
CMD ["nginx", "-g", "daemon off;"]