# QuickTalk

This is a real-time chat application built using WebSocket technology. The application allows users to chat in private and group rooms, with persistent messages stored in a MySQL database. The frontend is built with React, while the backend is developed using Spring Boot.

## Features

- **Real-Time Communication**: Supports private and group chat using WebSockets.
- **User Authentication**: Secure login and user management.
- **Persistent Messaging**: Messages are saved in the MySQL database for future reference.
- **Interactive Frontend**: Responsive UI built with React.
- **API Documentation**: Comprehensive API documentation available via Swagger UI.

## Technology Stack

- **Frontend**: React
- **Backend**: Spring Boot
- **Database**: MySQL
- **WebSockets**: For real-time chat functionality
- **Swagger**: For API documentation

## Getting Started

### Prerequisites

- **Node.js**: For running the React frontend.
- **Java 8 or above**: For running the Spring Boot backend.
- **MySQL 8 or above**: For the database.
- **Maven 3.9 or above**: For managing Java dependencies.

### Setup

1. **Clone the repository**:
    ```bash
    git https://github.com/charley-v/quicktalk.git
    cd quicktalk
    ```

2. **Setup the MySQL Database**:
    - Navigate to the database directory:
      ```bash
      cd database
    - Run the schema provided in `schema.sql` to set up the database and tables

3. **Configure the Backend**:
    - Navigate to the backend directory:
      ```bash
      cd backend
      ```
    - Create an `application.properties` file with your MySQL credentials in `src/main/resources/application.properties`:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/quicktalkdb
      spring.datasource.username=your-username
      spring.datasource.password=your-password
      ```

4. **Run the Spring Boot Backend**:
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

5. **Start the React Frontend**:
    - Navigate to the frontend directory:
      ```bash
      cd ../frontend
      ```
    - Install dependencies and start the React app:
      ```bash
      npm install
      npm start
      ```

## Database Schema

The database schema is available in `database/schema.sql`. Below is the relational schema diagram:

![Schema Diagram](img/schema_diagram.png)


## API Documentation

The backend APIs are documented using Swagger. You can access the Swagger UI by running the backend application and navigating to:

```bash
http://localhost:8080/swagger-ui.html
```

A screenshot of the Swagger UI is shown below:

![Swagger UI](img/swagger_ui.png)

Alternatively, you can view the [Swagger Documentation (PDF)](img/QuickTalk_API_Documentation.pdf) directly which provides a comprehensive overview of all backend API endpoints, including details on request and response formats, HTTP methods, parameters, and example payloads. It serves as a complete reference for developers to understand how to interact with the WebSocket-based chat application's backend.