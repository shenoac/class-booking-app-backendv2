# Design Overview: Art E-commerce Platform

## 1. Project Summary
This project is a full-stack e-commerce platform that allows artists to sell their artwork online. The platform uses a microservices architecture, with services dedicated to user management, artwork handling, e-commerce functionality, and more.

The backend is built with Java/Quarkus and Node.js/Express, while the frontend uses React/TypeScript. The project aims to implement secure user authentication, role-based access control, and payment processing.

---

## 2. High-Level Architecture

### Microservices Overview:
The platform is divided into several independent microservices. Each service is responsible for a specific domain of functionality and can be scaled or deployed independently.

### Components:
- **Frontend (React/TypeScript)**: Displays the artwork and handles user interactions like browsing, purchases, and managing profiles.
- **User Management Service (Java/Quarkus)**: Handles user registration, authentication, role management (Buyer/Artist/Admin), and JWT-based security.
- **Artwork Management Service (Node.js/Express)**: Manages CRUD operations on artwork, allowing artists to upload their work and buyers to browse the collection.
- **E-commerce and Payment Service (Node.js/Express)**: Manages orders, payments, and purchases using third-party payment integration (e.g., Stripe).
- **Recommendation Engine (Future Enhancement)**: Suggests artwork to users based on browsing and purchase history (potentially using Python or Node.js).
- **Database (PostgreSQL)**: A relational database used to store user data, artwork metadata, and order information.

---

## 3. Microservices Breakdown

### 3.1 User Management Service (Java/Quarkus)
- **Responsibilities**:
    - User registration (buyers register on the site; artists by invitation).
    - Role-based access control using JWT.
    - CRUD operations on user profiles.
    - Token-based authentication and authorization.

- **Endpoints**:
    - `POST /register/buyer`: Registers a new buyer.
    - `POST /register/artist`: Registers a new artist (by invitation).
    - `POST /login`: Authenticates a user and returns a JWT.
    - `GET /users/buyers` and `GET /users/artists`: (Future) Admin-only endpoints to fetch users by role.

- **Technologies**:
    - Java
    - Quarkus
    - JWT (JSON Web Token)
    - PostgreSQL

### 3.2 Artwork Management Service (Node.js/Express)
- **Responsibilities**:
    - CRUD operations on artwork data.
    - Artists can upload new artwork (with images).
    - Buyers can browse available artwork.

- **Endpoints**:
    - `GET /artworks`: Fetch a list of all available artworks.
    - `POST /artworks`: (Artists only) Upload new artwork.
    - `GET /artworks/:id`: View detailed information about a specific artwork.

- **Technologies**:
    - Node.js
    - Express
    - PostgreSQL (or potentially a media storage system for artwork images)

### 3.3 E-commerce & Payment Service (Node.js/Express)
- **Responsibilities**:
    - Order processing (buyer purchases artwork).
    - Integration with third-party payment providers (e.g., Stripe).
    - Maintaining order history and processing payments securely.

- **Endpoints**:
    - `POST /orders`: Create a new order for a buyer.
    - `GET /orders/:id`: Fetch details of an order (buyer-specific).

- **Technologies**:
    - Node.js
    - Express
    - Stripe (or another payment gateway)

### 3.4 Recommendation Engine (Future Enhancement)
- **Responsibilities**:
    - Analyze user browsing and purchase data.
    - Recommend artwork to users based on their preferences.
    - Use machine learning models for better personalization.

- **Technologies**:
    - Python or Node.js
    - Machine Learning Models (e.g., collaborative filtering)

---

## 4. Security and Authentication

### JWT (JSON Web Token)
- **Purpose**: All communication between the frontend and backend services will be authenticated using JWT tokens. Users will receive a token upon login, which will be used to authorize further requests.

- **Claims**:
    - The JWT will include claims such as the user's email and role (e.g., "BUYER", "ARTIST", "ADMIN").

- **Token Validation**:
    - Each service will validate the JWT to ensure the user has the correct permissions for the requested action.

---

## 5. Database Design

### PostgreSQL:
- **User Management**:
    - Table: `users`
    - Fields: `id`, `email`, `password`, `role`, `name`, `created_at`, etc.

- **Artwork Management**:
    - Table: `artworks`
    - Fields: `id`, `artist_id`, `title`, `description`, `price`, `image_url`, `created_at`, etc.

- **Orders and Payments**:
    - Table: `orders`
    - Fields: `id`, `buyer_id`, `artwork_id`, `price`, `status`, `created_at`, etc.

---

## 6. Communication Between Services

### API Gateway (Optional)
- **Purpose**: An API gateway can be used to route requests to the correct microservice and manage authentication, rate-limiting, logging, etc.
- **Technologies**: Nginx or any cloud-based API gateway (e.g., AWS API Gateway).

### RESTful Communication:
- Each microservice will expose REST APIs that are called by the frontend or other services. These APIs will validate JWTs and ensure role-based access control.

### Messaging (Optional for Future Scalability):
- For asynchronous communication (e.g., sending notifications or logging activity), a message broker like RabbitMQ or Kafka could be used.

---

## 7. Future Enhancements

- **Password Reset Functionality**: Users can reset their password via email.
- **Integration with Stripe or Payment Processor**: Secure payment processing for artwork purchases.
- **Recommendation System**: Personalized artwork recommendations based on browsing and purchase history.
- **Enhanced Admin Dashboard**: Additional tools for admins to manage users, artwork, and orders.

---

## 8. Running the Project

### Backend:
1. Set up PostgreSQL locally or using a cloud provider like Heroku.
2. Clone this repository and navigate to the `/backend` folder.
3. Run `mvn clean install` to build the backend services.
4. Use `mvn quarkus:dev` to start the Quarkus user management service.

### Frontend:
1. Navigate to the `/frontend` folder.
2. Run `npm install` to install dependencies.
3. Use `npm run dev` to start the React frontend.

---

## 9. Conclusion
This project leverages microservices architecture to decouple the main functionalities, allowing independent development, deployment, and scaling. Each service focuses on a specific domain, such as user management, artwork handling, and payments, making the platform both modular and flexible for future enhancements.

