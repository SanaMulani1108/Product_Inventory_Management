# 🚀 Stockify - Product Inventory Management System

---
Overview

• Built a full-stack Product Inventory Management System using Java 17, Spring Boot 3, and MySQL 8 with a clean MVC layered architecture.
• Designed and use 11 RESTful API endpoints with pagination, sorting, search, category filtering, price-range filtering, and low-stock alerts.
• Implemented Spring Data JPA repositories with custom JPQL queries, Hibernate ORM auto DDL, and audit timestamps.
• Applied Jakarta Bean Validation for request DTOs and a @RestControllerAdvice global exception handler returning structured JSON responses.
• Developed an interactive dashboard UI (HTML/CSS/JS) with real-time search, CRUD modals, soft-delete/restore, and live inventory stats served as Spring static resources.
• Wrote JUnit 5 unit tests using Mockito to cover service-layer business logic including duplicate validation and soft-delete behavior.

## 📦 Tech Stack

| Layer        | Technology                  |
|--------------|-----------------------------|
| Framework    | Spring Boot 3.2             |
| Language     | Java 17                     |
| Database     | MySQL 8                     |
| ORM          | Spring Data JPA / Hibernate |
| Validation   | Jakarta Bean Validation     |
| Boilerplate  | Lombok                      |
| Testing      | JUnit 5 + Mockito           |


---

## ⚙️ Setup & Run

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8 

---

### CRUD Operations

| Method   | Endpoint                 | Description              |
|----------|--------------------------|--------------------------|
| `POST`   | `/`                      | Create a new product     |
| `GET`    | `/`                      | Get all products (paged) |
| `GET`    | `/{id}`                  | Get product by ID        |
| `PUT`    | `/{id}`                  | Update a product         |
| `DELETE` | `/{id}`                  | Soft-delete a product    |
| `PATCH`  | `/{id}/restore`          | Restore deleted product  |

### Search & Filter

| Method | Endpoint                      | Description               |
|--------|-------------------------------|---------------------------|
| `GET`  | `/search?name=laptop`         | Search by name            |
| `GET`  | `/category/{category}`        | Filter by category        |
| `GET`  | `/price-range?min=10&max=100` | Filter by price range     |
| `GET`  | `/low-stock?threshold=5`      | Get low-stock products    |
| `GET`  | `/stats/categories`           | Product count by category |

---

## ✅ Features

- ✔ Full CRUD with soft-delete & restore
- ✔ Pagination & sorting on list endpoint
- ✔ Search by name (partial match)
- ✔ Filter by category & price range
- ✔ Low-stock alerts endpoint
- ✔ Category statistics endpoint
- ✔ Input validation with meaningful messages
- ✔ Global exception handling
- ✔ Consistent API response wrapper
- ✔ Audit timestamps (createdAt, updatedAt)

---
<img width="1366" height="644" alt="image" src="https://github.com/user-attachments/assets/a93f4f3f-e516-423c-b622-a24e3f5b0f8f" />
<img width="1366" height="639" alt="image" src="https://github.com/user-attachments/assets/d776f58c-806b-445b-86ec-e01de3337541" />
<img width="1366" height="643" alt="image" src="https://github.com/user-attachments/assets/3cf427fc-baf4-46f4-ba98-a49ff748fab0" />




