# Class Booking System for Art Classes

## Project Overview
This project is a class booking system that allows users to schedule and manage art class bookings. It is built with a **Java/Quarkus** backend and a **React/TypeScript** frontend. The system aims to manage the classes, users, and bookings efficiently, and it could be extended into a microservices architecture if additional features like payments are introduced.

## Goals
- Implement secure user authentication and authorization for students and instructors.
- Role-based access control using JWT.
- Manage class schedules, bookings, and user profiles.
- Integrate with PostgreSQL for persistent data storage.
- Future possibility of splitting into microservices (e.g., payments).

## Architecture
- **Backend (Java/Quarkus)**: Handles user management, class scheduling, booking management, and authentication.
- **Frontend (React/TypeScript)**: Provides a user interface for booking art classes, viewing schedules, and managing profiles.
- **Database (PostgreSQL)**: Stores class information, user profiles, and booking records.
- **Authentication**: JSON Web Tokens (JWT) are used for secure user authentication and role management.

## Technologies Used
- **Quarkus (Java)**: Backend for handling business logic and API requests.
- **React (TypeScript)**: Frontend for user interaction and class booking management.
- **PostgreSQL**: Database for storing user, class, and booking information.
- **JWT**: Used for securing user authentication and authorization.

## Running the Project
1. Set up PostgreSQL locally or using a cloud provider like Heroku or AWS.
2. Clone this repository.
3. Navigate to the `/backend` folder and follow the instructions to set up the Quarkus backend.
4. Navigate to the `/frontend` folder and follow the instructions to run the React app.
5. Ensure the frontend and backend communicate via REST APIs.

## Future Enhancements
- Payments module
- Bookings feature
- Classes feature
- Profiles feature

