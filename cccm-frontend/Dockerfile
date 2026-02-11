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

## Create necessary directories for nginx
RUN mkdir -p /opt/app-root/src /opt/app-root/etc/nginx.d /opt/app-root/etc/nginx.default.d /var/log/nginx /var/cache/nginx /run

## Set permissions for OpenShift
RUN chmod -R g+rwx /opt/app-root /var/cache/nginx /var/run /var/log/nginx /run \
    && chgrp -R 0 /opt/app-root /var/cache/nginx /var/run /var/log/nginx /run \
    && chmod -R g=u /opt/app-root /var/cache/nginx /var/run /var/log/nginx /run

# Copy from the stage 1 build result
COPY --from=build /app/dist /opt/app-root/src/cccm
# COPY --from=build /app/public/config.js /opt/app-root/src/cccm/config.js
COPY --from=build /app/nginx.conf /etc/nginx/nginx.conf

# Ensure proper permissions on copied files
RUN chmod -R g+rwx /opt/app-root/src \
    && chgrp -R 0 /opt/app-root/src \
    && chmod -R g=u /opt/app-root/src

EXPOSE 8000

ENTRYPOINT ["nginx", "-g", "daemon off;"]