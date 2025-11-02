FROM node:20-alpine AS build
WORKDIR /app
COPY . .
RUN npm ci && npm run build

FROM nginx:alpine
COPY --from=build /app/dist/feature-flag-admin/browser /usr/share/nginx/html
EXPOSE 4200
CMD ["nginx","-g","daemon off;"]