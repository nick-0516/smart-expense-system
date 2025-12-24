#!/bin/bash
set -e

cd ~/microservices/smart-expense-system/user-service

echo "Pulling latest code..."
git pull

echo "Building project..."
mvn clean package -DskipTests -pl user-service -am

echo "Restarting user-service via systemd..."
sudo systemctl restart user-service

echo "User service restarted successfully"
