# 🚀 Spring Boot REST CRUD API — Products

A production-ready REST API built with **Spring Boot 3**, **MySQL**, and **JPA/Hibernate**.

---

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

## 📁 Project Structure

```
src/main/java/com/example/crudapi/
├── CrudApiApplication.java       # Entry point
├── controller/
│   └── ProductController.java    # REST endpoints
├── service/
│   └── ProductService.java       # Business logic
├── repository/
│   └── ProductRepository.java    # DB queries
├── model/
│   └── Product.java              # JPA entity
├── dto/
│   ├── ProductRequest.java       # Input DTO
│   ├── ProductResponse.java      # Output DTO
│   └── ApiResponse.java          # Wrapper
└── exception/
    ├── ResourceNotFoundException.java
    ├── DuplicateResourceException.java
    └── GlobalExceptionHandler.java
```

---

## ⚙️ Setup & Run

### 1. Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8 running locally

### 2. Configure Database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/cruddb?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
```

### 3. Run the Application
```bash
./mvnw spring-boot:run
```
App starts on: `http://localhost:8080`

### 4. Run Tests
```bash
./mvnw test
```

---

## 🌐 API Endpoints

Base URL: `http://localhost:8080/api/v1/products`

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

### Pagination & Sorting (on GET /)
```
?page=0&size=10&sortBy=price&direction=desc
```

---

## 📝 Request / Response Examples

### Create Product
```http
POST /api/v1/products
Content-Type: application/json

{
  "name": "Laptop Pro",
  "description": "High performance laptop",
  "price": 1299.99,
  "stock": 50,
  "category": "Electronics"
}
```

### Success Response
```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "Laptop Pro",
    "description": "High performance laptop",
    "price": 1299.99,
    "stock": 50,
    "category": "Electronics",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
}
```

### Validation Error Response
```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "price": "Price must be greater than 0",
    "name": "Product name is required"
  }
}
```

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
- ✔ Unit tests with Mockito

---

## 📌 Notes

- Deletion is **soft** — products are marked `active=false`, not physically removed
- All timestamps are auto-managed by Hibernate
- Database is auto-created if it doesn't exist (`createDatabaseIfNotExist=true`)
