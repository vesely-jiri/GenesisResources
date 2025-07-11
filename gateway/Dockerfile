FROM nginx:alpine

COPY gateway/nginx.production.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]