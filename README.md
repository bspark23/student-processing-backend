# Student Processing System - Backend

A Spring Boot REST API for managing student data with file processing capabilities.

## Features

- Student CRUD operations with pagination and search
- File processing (Excel generation, CSV conversion, database import)
- Data export (Excel, CSV, PDF)
- PostgreSQL database integration
- RESTful API endpoints

## Technologies

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Apache POI (Excel processing)
- OpenCSV (CSV processing)
- iText (PDF generation)
- Maven

## API Endpoints

### Students
- `GET /api/students` - Get all students with pagination
- `GET /api/students/{id}` - Get student by ID
- `GET /api/students/student/{studentId}` - Get student by student ID
- `GET /api/students/classes` - Get all class names
- `GET /api/students/stats` - Get statistics
- `POST /api/students` - Create new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### File Processing
- `POST /api/files/generate-excel` - Generate Excel file with sample data
- `POST /api/files/excel-to-csv` - Convert Excel to CSV
- `POST /api/files/upload-csv` - Upload CSV to database

### Export
- `GET /api/export/excel` - Export students to Excel
- `GET /api/export/csv` - Export students to CSV
- `GET /api/export/pdf` - Export students to PDF

### Health
- `GET /api/health` - Health check endpoint

## Environment Variables



## Local Development

1. Install PostgreSQL and create a database named `studentdb`
2. Update `application.properties` with your database credentials
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## Deployment
deployed to fly.io


## Database Schema

The application uses JPA/Hibernate to automatically create the following table:

```sql
CREATE TABLE students (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    class_name VARCHAR(255) NOT NULL,
    dob DATE NOT NULL,
    score INTEGER NOT NULL
);
```
