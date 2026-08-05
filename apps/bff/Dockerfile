FROM node:22-alpine AS build
WORKDIR /workspace
COPY package.json package-lock.json* ./
COPY apps/web/package.json apps/web/package.json
COPY apps/bff/package.json apps/bff/package.json
RUN npm install
COPY apps/bff apps/bff
RUN npm run build --workspace @operations-hub/bff

FROM node:22-alpine
WORKDIR /app
ENV NODE_ENV=production
COPY --from=build /workspace/node_modules /app/node_modules
COPY --from=build /workspace/apps/bff/dist /app/dist
USER node
EXPOSE 3000
CMD ["node", "dist/main.js"]
