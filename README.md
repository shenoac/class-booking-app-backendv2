# User Management System for Art E-commerce Platform

## Project Overview
This project is a part of a full-stack e-commerce platform that allows artists to sell their artwork online. The user management system is built using **Java/Quarkus** and integrated with a **React/Node.js/Express** frontend.

## Goals
- Implement secure user authentication and authorization (Artists and Buyers).
- Role-based access control using JWT.
- Manage user profiles with CRUD operations.
- Integrate with PostgreSQL for user persistence.
- Use microservices-like architecture to separate user management from other services.

## High-Level Architecture
- **Backend (Java/Quarkus)**: Handles user management, including registration, login, role management, and authentication.
- **Frontend (React/TypeScript)**: Displays artwork and handles user interactions like browsing and purchases.
- **E-commerce (Node.js/Express)**: Provides APIs for listing artwork and handling orders and payments.
- **Database (PostgreSQL)**: Stores user data, artwork, and order information.
- **Authentication**: JSON Web Tokens (JWT) are used for secure authentication and authorization.

## Technologies Used
- **Quarkus (Java)**: Backend for user management.
- **React (TypeScript)**: Frontend for displaying the artwork and handling user actions.
- **Node.js/Express**: API for e-commerce functionality.
- **PostgreSQL**: Relational database for user data and artwork storage.
- **JWT**: Authentication mechanism for secure APIs.

## Running the Project
1. Set up PostgreSQL on Heroku/AWS.
2. Clone this repository.
3. Follow instructions in the `/backend` folder to set up Quarkus.
4. Follow instructions in the `/frontend` folder to run the React app.
5. Connect both services via REST APIs.

## Future Enhancements
- Password reset functionality.
- Integration with a third-party payment service (e.g., Stripe).
- Artwork recommendations for users based on browsing history.

