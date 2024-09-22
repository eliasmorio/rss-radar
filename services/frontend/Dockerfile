FROM node:alpine AS build

WORKDIR /app

COPY package.json .

RUN npm install \
&& npm install -g @angular/cli

COPY . .

RUN npm run build

FROM nginx:alpine AS runtime

COPY --from=build /app/dist/frontend /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]


