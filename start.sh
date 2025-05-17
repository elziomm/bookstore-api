#!/bin/bash

echo "Aguardando o MongoDB iniciar..."
while ! timeout 1 bash -c "echo > /dev/tcp/$MONGO_URL/27017"; do
    echo "MongoDB ainda não está pronto, aguardando..."
    sleep 2
done

echo "Construindo o projeto..."
./mvnw clean package -DskipTests

echo "Iniciando a aplicação..."
MONGO_URL=$MONGO_URL java -jar target/*-runner.jar
