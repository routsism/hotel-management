# 🏨 Hotel Management App

A full-stack web application for managing hotel operations, including room reservations, user registration/login, and CRUD operations for hotels, rooms, and reservations.

Built with **Spring Boot (Java)** on the backend and **Angular** on the frontend.

---

## 🚀 Features

- 🛏️ Room and Hotel Management (CRUD)
- 📅 Reservation system with date updates and user-specific queries
- 🔐 User Authentication & Role-based Authorization (Admin, Employee, Guest)
- 🧑‍💼 Registration/Login functionality
- 📦 RESTful API with secure endpoints

---

## ⚙️ Tech Stack

| Layer      | Technology                                      |
|------------|-------------------------------------------------|
| Backend    | Java 17, Spring Boot, Spring Security, JPA (Hibernate) |
| Frontend   | Angular, Angular Material, Bootstrap, CSS       |
| Database   | MySQL                                           |
| Build Tool | Gradle (`./gradlew bootRun`)                    |

---

## 🧱 Database Setup

Option 1: MySQL CLI
 
mysql -u your-username -p hotel_management_db < db/schema.sql

Option 2: MySQL Workbench
Open Workbench
Select your schema (hotel_management_db)
File → Open SQL Script → Choose schema.sql
Execute

## Admin User Setup

An admin is pre-inserted via SQL script:
username: from application-test.properties
password: from application-test.properties

## ▶️ Run the Backend

Using IntelliJ or terminal:
bash:
∙ cd backend/hotel-managment
∙ ./gradlew bootRun

## ▶️ Run the Frontend

∙ cd frontend\hotel-management-frontend
∙ npm install
∙ ng serve


Visit: http://localhost:4200

## 🔐 API Endpoints & Role Access

🔓 Public
| Method | Endpoint              | Description              |
| ------ | --------------------- | ------------------------ |
| POST   | `/api/users/register` | Register a new user      |
| POST   | `/api/auth/**`        | Authentication endpoints |
| GET    | `/api/hotels/**`      | View hotel info          |
| GET    | `/api/rooms/**`       | View rooms               |

🔐 Admin Only
| Method | Endpoint               | Description            |
| ------ | ---------------------- | ---------------------- |
| POST   | `/api/hotels`          | Create a hotel         |
| PUT    | `/api/hotels/**`       | Update hotel details   |
| DELETE | `/api/hotels/**`       | Delete a hotel         |
| POST   | `/api/rooms`           | Add a new room         |
| PUT    | `/api/rooms/**`        | Update a room          |
| DELETE | `/api/rooms/**`        | Delete a room          |
| GET    | `/api/reservations`    | View all reservations  |
| PUT    | `/api/reservations/**` | Update any reservation |
| DELETE | `/api/reservations/**` | Delete any reservation |
| GET    | `/api/users/**`        | Manage user data       |


👨‍💼 Employee Access
Full access to:

∙ /api/rooms/**
∙ /api/reservations/**

🙍 Guest Access
| Method | Endpoint                    | Description              |
| ------ | --------------------------- | ------------------------ |
| POST   | `/api/reservations`         | Create reservation       |
| GET    | `/api/reservations/user/*`  | View their reservations  |
| PUT    | `/api/reservations/*/dates` | Update their reservation |
| DELETE | `/api/reservations/**`      | Cancel reservation       |


## 🧠 Future Improvements

Docker support for simplified deployment

Unit and integration testing

Role management via admin dashboard



















 
