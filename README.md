# 📚 Bookstore RESTful API – JAX-RS Coursework

## 🔍 Overview

This project implements a RESTful backend for a **Bookstore** application using **Java JAX-RS**. It simulates an e-commerce environment with CRUD functionality for books, authors, customers, shopping carts, and orders.

The project is developed using **JAX-RS (Jersey)**, with **JSON** as the data exchange format and **Postman** for API testing. All data is stored in-memory using standard Java collections (`ArrayList`, `HashMap`)—no external databases or frameworks are used as per coursework regulations.

---

## 🎯 Coursework Objectives

- Apply RESTful principles and JAX-RS to design scalable APIs.
- Implement resource classes using standard HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
- Handle request and response bodies in JSON.
- Implement clean exception handling using `ExceptionMapper`.
- Model e-commerce entities such as books, authors, customers, shopping carts, and orders.
- Execute and document API tests using **Postman**.

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java (JDK 17) | Backend logic & in-memory data models |
| JAX-RS (Jersey) | REST API implementation |
| JSON | Request & Response payloads |
| Postman | API testing |
| ArrayList / HashMap | In-memory storage (No DB allowed) |

---

## 🧩 Resource Classes & Endpoints

### 🔹 `BookResource` (`/books`)
- `POST /books` – Add a new book  
- `GET /books` – Get all books  
- `GET /books/{id}` – Get book by ID  
- `PUT /books/{id}` – Update a book  
- `DELETE /books/{id}` – Delete a book  

### 🔹 `AuthorResource` (`/authors`)
- `POST /authors`  
- `GET /authors`  
- `GET /authors/{id}`  
- `PUT /authors/{id}`  
- `DELETE /authors/{id}`  
- `GET /authors/{id}/books` – List all books by author  

### 🔹 `CustomerResource` (`/customers`)
- `POST /customers`  
- `GET /customers`  
- `GET /customers/{id}`  
- `PUT /customers/{id}`  
- `DELETE /customers/{id}`  

### 🔹 `CartResource` (`/customers/{customerId}/cart`)
- `POST /customers/{customerId}/cart/items`  
- `GET /customers/{customerId}/cart`  
- `PUT /customers/{customerId}/cart/items/{bookId}`  
- `DELETE /customers/{customerId}/cart/items/{bookId}`  

### 🔹 `OrderResource` (`/customers/{customerId}/orders`)
- `POST /customers/{customerId}/orders`  
- `GET /customers/{customerId}/orders`  
- `GET /customers/{customerId}/orders/{orderId}`  

---

## 🧱 Data Models

- `Book`: title, authorId, ISBN, publicationYear, price, stock  
- `Author`: id, name, biography  
- `Customer`: id, name, email, password  
- `Cart`: List of BookItems (bookId, quantity)  
- `Order`: Order summary from cart items  

---

## 🚫 Exception Handling

Handled using custom exceptions and `ExceptionMapper` for proper REST responses:

| Exception | Mapped Status | Trigger Scenario |
|----------|----------------|------------------|
| `BookNotFoundException` | `404 Not Found` | Non-existent book ID |
| `AuthorNotFoundException` | `404 Not Found` | Invalid author ID |
| `CustomerNotFoundException` | `404 Not Found` | Missing customer |
| `InvalidInputException` | `400 Bad Request` | Invalid payload |
| `OutOfStockException` | `400 Bad Request` | Not enough book stock |
| `CartNotFoundException` | `404 Not Found` | Empty cart access |

All error responses return JSON objects:
```json
{
  "error": "Author Not Found",
  "message": "Author with ID 999 does not exist."
}
