# Stage 1: Build stage
FROM node:20-alpine AS build

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

ARG NODE_ENV=production
RUN if [ "$NODE_ENV" = "production" ]; then npm run build; fi

# Stage 2: Production/Development stage
FROM node:20-alpine AS development

WORKDIR /app

COPY --from=build /app /app

ARG NODE_ENV=production
ENV NODE_ENV=${NODE_ENV}

CMD if [ "$NODE_ENV" = "production" ]; then npm run build && npm run start; else npm run dev; fi