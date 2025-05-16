#!/bin/bash

echo "Iniciando o banco de dados MongoDB..."
docker-compose up -d mongo

echo "Construindo o projeto..."
mvn clean package -DskipTests

echo "Iniciando a aplicação..."
java -jar target/*-runner.jar
