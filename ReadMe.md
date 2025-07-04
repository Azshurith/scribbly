# 📓 Scribbly

[![codecov](https://codecov.io/gh/Azshurith/scribbly/branch/master/graph/badge.svg?token=9ZF8NWC2QB)](https://codecov.io/gh/Azshurith/scribbly)
![License](https://img.shields.io/github/license/Azshurith/scribbly)
![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)
![Last Commit](https://img.shields.io/github/last-commit/Azshurith/scribbly)

Scribbly is a simple and elegant blogging platform built with Spring Boot. It allows users to register, login, and create, edit, view, and delete blog posts with ease.

## ✨ Features

- User registration and login
- Password encryption with BCrypt
- Post creation, viewing, editing, and deletion
- Pagination for posts list
- Role-based access control (authenticated users only for write operations)
- Thymeleaf-based UI
- Spring Cache support for improved performance

## 🛠️ Tech Stack

- Java 17+
- Spring Boot (Web, Security, Data JPA, Cache)
- Thymeleaf
- H2 Database (can be swapped with MySQL/PostgreSQL)
- Maven or Gradle
- Bootstrap 5 (via CDN)
---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven or Gradle

### Running the Application

```bash
./mvnw spring-boot:run
```

Then open your browser and go to `http://localhost:8080`.

### Build

```bash
./mvnw clean package
```

---

## 🔐 Authentication

- Users can register and login.
- All post creation, editing, and deletion requires authentication.

---

## 📂 Folder Structure

- `controller` — Spring MVC controllers
- `model` — JPA entities (Post, User)
- `repository` — Spring Data JPA interfaces
- `service` — Business logic and cache handling
- `security` — Custom authentication and filters
- `templates` — Thymeleaf HTML templates

---

## 🧪 Testing

Add test classes under `src/test/java/` for unit and integration testing.

---

## 📄 License

This project is open-source and available under the MIT License.
