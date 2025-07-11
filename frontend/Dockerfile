FROM node:20 AS build

WORKDIR /app

ARG NODE_ENV=production
ENV NODE_ENV=$NODE_ENV

ARG VITE_API_BASE_URL
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}

COPY ./frontend/package*.json ./
RUN npm install --include=dev

COPY ./frontend/ .

RUN npm run build



FROM node:20-alpine AS development

WORKDIR /app

COPY frontend/package*.json ./
RUN npm install

COPY ./frontend .

EXPOSE 3000
CMD ["npm", "run", "dev"]



FROM nginx:alpine AS production

COPY --from=build /app/dist /usr/share/nginx/html

COPY frontend/nginx.production.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]