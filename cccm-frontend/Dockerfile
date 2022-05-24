FROM node:lts-alpine as build

WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./
# RUN npm install

# Copy rest of the files, including dist folder
COPY . .

# Build the project
# RUN npm run build

FROM nginx:alpine as run

## Remove default nginx index page
RUN rm -rf /opt/app-root/src/*
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx

# Copy from the stahg 1
COPY --from=build /app/dist /opt/app-root/src
COPY --from=build /app/nginx.conf /etc/nginx/nginx.conf

EXPOSE 8000

ENTRYPOINT ["nginx", "-g", "daemon off;"]