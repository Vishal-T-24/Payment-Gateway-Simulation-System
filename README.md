# Payment Gateway Simulation System
A backend system that simulates a real-world payment gateway, 
allowing merchants to register, generate API keys, and process 
simulated payment transactions securely.

## Tech Stack
- Java & Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Redis
- RESTful APIs

## Features
- Merchant registration with auto-generated public & secret keys
- Payment processing secured via API key authentication
- Financial ledger to track all merchant transactions
- ACID-compliant transaction ledger with optimistic locking
- Idempotency keys to prevent duplicate payments
- JWT-based authentication for Admin access (RBAC)
- Redis caching to reduce repeated database calls

## How to Run
1. Clone the repository
   git clone https://github.com/Vishal-T-24/Payment-Gateway-Simulation-System.git
2. Configure your PostgreSQL and Redis connection in
   src/main/resources/application.properties
3. Run the application
   ./mvnw spring-boot:run
4. Test APIs using Postman on http://localhost:8080

## API Testing
All endpoints tested and validated using Postman.
