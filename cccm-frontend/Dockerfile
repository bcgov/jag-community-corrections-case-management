
FROM node:lts-alpine
RUN npm install -g http-server

WORKDIR /app/cccm

COPY package*.json ./
RUN yarn install
COPY . ./
RUN yarn build

EXPOSE 8080
CMD [ "http-server", "dist", "--host", "0.0.0.0"]