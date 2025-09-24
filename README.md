# Smart Expense & Budgeting System

A microservices-based backend system for tracking daily expenses, managing budgets, sending alerts, and generating financial insights.  
Built with **Spring Boot 3.5.6**, **Java 21**, **Spring Cloud**, **Kafka**, and **Docker**.

---

## Features
- User authentication with **JWT**
- Track daily expenses by category
- Set monthly budgets with alerts
- Async notifications (email/SMS mock) using **Kafka**
- Analytics & trends (top categories, monthly savings)
- Service registry, API gateway, centralized config
- Observability with Actuator, Prometheus & Grafana
- Containerized with Docker, CI/CD pipeline via GitHub Actions

---

## Microservices
| Service              | Responsibility                          | Port  |
|----------------------|------------------------------------------|-------|
| **User Service**     | Manage users, authentication (JWT)      | 8081  |
| **Expense Service**  | Add, update, delete, list expenses      | 8082  |
| **Budget Service**   | Monthly category budgets, limit checks  | 8083  |
| **Notification Service** | Alerts when budgets are exceeded   | 8084   |
| **Analytics Service** | Trends, reports, and financial insights| 8085  |

---

## Architecture
![Architecture Diagram](docs/architecture.png)

*High-level architecture with services, Kafka, API Gateway, and DB per service.*  
(Will add the diagram later in `/docs/architecture.png`)

---

## Tech Stack
- **Backend**: Java 21, Spring Boot 3.5.6, Spring Data JPA, Spring Security (JWT)
- **Databases**: MySQL / H2 (per service)
- **Messaging**: Apache Kafka
- **Service Discovery**: Spring Cloud Eureka
- **Gateway**: Spring Cloud Gateway
- **Config Management**: Spring Cloud Config
- **Resilience**: Resilience4j (circuit breaker, retries)
- **Observability**: Spring Boot Actuator, Micrometer, Prometheus, Grafana
- **DevOps**: Docker, Docker Compose, GitHub Actions CI/CD

---

## Getting Started

### Prerequisites
- Java 21
- Maven 3.9+
- Docker & Docker Compose
- Git

### Clone the repository
```bash
git clone https://github.com/<your-username>/smart-expense-system.git
cd smart-expense-system
```

### Run a service locally
```
cd user-service
mvn spring-boot:run
Visit: http://localhost:8081/h2-console
```
