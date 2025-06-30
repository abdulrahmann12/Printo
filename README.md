# 🖨️ Printo - Online Printing Service Backend

**Printo** is a backend system for an online custom printing service (similar to Printo.in), built using **Java Spring Boot**.  
It allows users to register, upload designs, place orders, and manage delivery addresses.

---

## 🚀 Features

- 🔐 JWT Authentication (Login & Register)
- 🧑‍💼 User Roles (Admin, Customer)
- 🛍️ Product Management (CRUD)
- 📦 Orders & Order Items
- 🏷️ Product Variants with EAV (Entity-Attribute-Value) model
- 🖼️ Cloudinary Image Upload Integration
- 📫 User Address Book
- 📄 Order Tracking
- 🔎 Search & Filter Products
- 🛠️ Admin Panel (Manage Products, Users, Orders)
- 📚 Swagger API Documentation

---

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 3**
- **Spring Security + JWT**
- **Spring Data JPA**
- **MySQL**
- **Cloudinary (for image upload)**
- **MapStruct (for DTO Mapping)**
- **Lombok**
- **Swagger / OpenAPI**
- **Validation API**
- **DevTools**

---

## 🧪 Testing

- ✅ JUnit & Mockito
- ✅ Spring Security Test

---

## 🔧 Project Structure

├── auth # Authentication logic
├── config # Security and general config
├── controller # REST controllers
├── dto # Data Transfer Objects
├── entity # JPA Entities
├── exception # Custom exceptions and handlers
├── mapper # MapStruct mappers
├── repository # JPA repositories
├── service # Service layer
└── utils # Utility classes


---

## 🖼️ EAV Design (Product Variants)

Products can have dynamic attributes (size, paper type, color, etc.)  
Implemented using a flexible Entity-Attribute-Value structure.

---

## 🔐 Authentication

- Login with JWT
- Access/Refresh Token mechanism
- Role-based access control

---

## 📷 Cloudinary Integration

Used for image uploads via API.  
Uploaded images are stored on Cloudinary and linked with product entries.

---

## 📬 How to Run the Project

1. Clone the repo:
   ```bash
   git clone https://github.com/abdulrahmann12/Printo.git
2. Set up MySQL DB

3. Add your config in application.properties:
  spring.datasource.url=jdbc:mysql://localhost:3306/printo
  spring.datasource.username=root
  spring.datasource.password=yourpassword
  cloudinary.api-key=...
  cloudinary.api-secret=...

4. Run the app:
   ./mvnw spring-boot:run

5. Visit Swagger docs:
     http://localhost:8080/swagger-ui/index.html

