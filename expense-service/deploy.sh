#!/bin/bash
cd ~/microservices/smart-expense-system/expense-service

echo "Pulling latest code..."
git pull

echo "Building project..."
mvn clean package -DskipTests

echo "Stopping old instance..."
pkill -f "expenseservice-0.0.1-SNAPSHOT.jar"

echo "Starting new instance..."
nohup java -jar target/expenseservice-0.0.1-SNAPSHOT.jar --server.port=8082 > app.log 2>&1 &
