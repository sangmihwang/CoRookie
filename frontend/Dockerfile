FROM node

RUN mkdir /app
WORKDIR /app

RUN mkdir ./dist

ADD ./dist ./dist

RUN npm install yarn
RUN yarn install
RUN yarn add serve

# RUN rm /etc/nginx/conf.d/default.conf

# COPY ./nginx.conf /etc/nginx/conf.d

ENTRYPOINT [ "yarn", "serve", "-s", "dist" ]