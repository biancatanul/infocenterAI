# InfoCenter AI

An AI-powered chatbot application designed for UVT students to access university information quickly and efficiently through natural language conversations.

## Features

- **AI-Powered Chatbot**: Get instant answers about admissions, scholarships, courses, and deadlines
- **User Authentication**: Secure registration and login with role-based access (Student/Admin)
- **Conversation Management**: View, organize, and delete chat history
- **Admin Dashboard**: Monitor system statistics, manage users, and view all conversations

## Tech Stack

- **Backend**: Spring Boot (Java)
- **Frontend**: HTML, CSS, JavaScript
- **Database**: PostgreSQL
- **AI Integration**: N8N Workflow + External AI Service
- **Authentication**: BCrypt password hashing

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6 or higher
- Git

## Installation

1. **Clone the repository**
```bash
git clone https://github.com/biancatanul/infocenterAI
cd infocenterAI
```

2. **Set up PostgreSQL database**
```bash
psql -U postgres
CREATE DATABASE infocenter_db;
\q

cd database
psql -U postgres -d infocenter_db -f database_schema.sql
```

3. **Configure application properties**

Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/infocenter_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
n8n.webhook.url=https://n8n-service-scmr.onrender.com/webhook/message
```

4. **Install dependencies**
```bash
mvn clean install
```

5. **Run the application**
```bash
mvn spring-boot:run
```

6. **Access the application**

Open your browser and navigate to: `http://localhost:8080/login.html`

## Usage

### For Students
- Register/login with your email
- Start a new chat or continue existing conversations
- Ask questions about university information
- Manage your conversation history

### For Administrators
- Access the Admin Panel for system oversight
- View statistics (total students, admins, conversations)
- Monitor all users and their conversations
- Manage platform-wide content

## API Endpoints

- **Authentication**: `/api/auth/*`
- **Chat Operations**: `/api/chat/*`
- **Admin Operations**: `/api/admin/*`

## Architecture

The application follows a layered architecture:
- **Model Layer**: User, Conversation, Message entities
- **Repository Layer**: Data access with Spring Data JPA
- **Service Layer**: Business logic (AuthService, ChatService, N8nAIService)
- **Controller Layer**: REST API endpoints

## Future Improvements

- JWT token authentication for enhanced security
- File upload support for document analysis
- Message search functionality
- WebSocket integration for real-time messaging
- Multi-language support
- Advanced analytics dashboard

## License

This project is part of the Individual Project course at UVT (2025-2026).
