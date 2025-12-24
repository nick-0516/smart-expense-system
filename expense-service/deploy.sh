#!/bin/bash
set -e

cd ~/microservices/smart-expense-system/expense-service

echo "Pulling latest code..."
git pull

echo "Building project..."
mvn clean package -DskipTests

echo "Restarting expense-service via systemd..."
sudo systemctl restart expense-service

echo "Expense service restarted successfully"
