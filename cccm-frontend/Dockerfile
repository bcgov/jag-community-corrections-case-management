FROM node:lts-alpine as build

WORKDIR /app

# Copy the package.json and install dependencies
COPY package*.json ./
RUN npm install

# Copy rest of the files
COPY . .

# Build the project
RUN npm run build


FROM nginx:alpine as run

COPY ./.nginx/nginx.conf /etc/nginx/nginx.conf

## Remove default nginx index page
RUN rm -rf /usr/share/nginx/html/*
RUN chmod g+rwx /usr/share/nginx
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx

# Copy from the stahg 1
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 8000

ENTRYPOINT ["nginx", "-g", "daemon off;"]