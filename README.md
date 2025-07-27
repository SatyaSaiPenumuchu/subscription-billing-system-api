# Subscription-Based API Billing System

A robust backend system to manage API access using subscription plans, API key-based authentication, usage tracking, rate limiting, and simulated monthly billing. Designed to emulate a real-world developer tools backend.

---

## 🔧 Tech Stack

- **Spring Boot** (REST APIs)
- **Spring Security** (API Key Auth)
- **JPA + Hibernate** (ORM)
- **MySQL**
- **Rate Limiting**: Bucket4j or Custom
- **JUnit + Mockito** (Testing)
- **Swagger** (API Docs)
- **Postman** Collection
- **Docker** 
- **CI/CD**: GitHub Actions / Jenkins

---

## 🧩 Core Features

### ✅ 1. User & API Key Management
- User registration & login
- API key generation
- Role-based access (Admin, Developer, Billing)
- Secured endpoints via API key

### 📦 2. Subscription Plan Management
- Admin CRUD for plans (Free, Pro, Premium)
- Each plan includes:
  - Request quota
  - Rate limits (e.g., 10 req/sec)
  - Monthly cost
  - Feature toggles

### 📊 3. Usage Tracking & Analytics
- Track every API request:
  - API key
  - Timestamp
  - Endpoint hit
  - Response status
- Daily/Monthly usage reports

### 🚫 4. Rate Limiting & Quota Enforcement
- Per-user rate limiting using interceptors/filters
- Block calls when usage exceeds monthly quota

### 💰 5. Simulated Billing Engine
- Monthly usage aggregation
- Plan matching
- Invoice generation
- Mark invoices as paid/unpaid (mock)

---

## 📁 Project Structure

```text
com.example.subscriptionapi
│
├── controller       # REST Controllers
├── service          # Business Logic
├── repository       # Data Access
├── model            # JPA Entities
│   ├── User
│   ├── SubscriptionPlan
│   ├── ApiKey
│   ├── ApiUsage
│   └── Invoice
├── config           # Configuration Files
├── security         # API Key Auth & Roles
└── scheduler        # Monthly Billing Jobs
