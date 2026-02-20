# banking-app (Spring Boot 3.5.0 + Java 21)

Microservices assignment implementation:

- Eureka Server (8761)
- API Gateway (8080)
- User Service (8081)
- Account Service (8082)
- Transaction Service (8083)
- React Frontend (3000)

## Prereqs
- Java 21
- Maven 3.9+
- Node 18+ (or 20+)

## How to run (dev)

### 1) Start Eureka
```bash
cd eureka-server
mvn spring-boot:run
```

### 2) Start other services (each in a new terminal)
```bash
cd api-gateway && mvn spring-boot:run
cd user-service && mvn spring-boot:run
cd account-service && mvn spring-boot:run
cd transaction-service && mvn spring-boot:run
```

### 3) Start frontend
```bash
cd frontend
npm install
npm start
```

Open:
- Eureka: http://localhost:8761
- Frontend: http://localhost:3000

## Sample credentials (seeded)
- user1: `alice@example.com` / `Password@123`
- user2: `bob@example.com` / `Password@123`

## API endpoints (via Gateway :8080)

### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Accounts
- `POST /api/accounts` (create account)
- `GET /api/accounts` (list my accounts)
- `GET /api/accounts/{id}`
- `GET /api/accounts/{id}/balance`

### Transactions
- `POST /api/transactions/transfer`
- `GET /api/transactions/account/{accountId}`

## Notes / limitations
- H2 in-memory per service (data resets on restart).
- Transfer uses a simple “saga-like” flow: debit then credit; if credit fails after debit, it attempts a refund.
- For the assignment scope, internal calls reuse the same user JWT.

