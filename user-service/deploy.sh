#!/bin/bash
cd ~/microservices/smart-expense-system/user-service

echo "Pulling latest code..."
git pull

echo "Building project..."
mvn clean package -DskipTests

echo "Stopping old instance..."
pkill -f "user-service-0.0.1-SNAPSHOT.jar"

echo "Starting new instance..."
nohup java -jar target/user-service-0.0.1-SNAPSHOT.jar --server.port=8081 > app.log 2>&1 &

