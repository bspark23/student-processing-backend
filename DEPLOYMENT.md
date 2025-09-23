# Backend Deployment Guide

## Railway Deployment

### Prerequisites
- GitHub account
- Railway account (sign up at [railway.app](https://railway.app))

### Step 1: Push to GitHub
```bash
# Run the setup script
./setup-git.sh  # or setup-git.bat on Windows

# Follow the instructions to push to GitHub
```

### Step 2: Deploy to Railway
1. Go to [Railway](https://railway.app)
2. Click "New Project"
3. Select "Deploy from GitHub repo"
4. Choose your backend repository
5. Railway will automatically detect the Spring Boot application

### Step 3: Add Database
1. In your Railway project, click "New Service"
2. Select "PostgreSQL"
3. Railway will automatically set the DATABASE_URL environment variable

### Step 4: Configure Environment
Railway automatically provides:
- `DATABASE_URL` - PostgreSQL connection string
- `PORT` - Server port (Railway will set this)

### Step 5: Deploy
Railway will automatically build and deploy your application using:
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -jar target/student-processing-0.0.1-SNAPSHOT.jar`

## Testing Deployment

After deployment, test these endpoints:
- Health check: `https://your-app.railway.app/api/health`
- Students API: `https://your-app.railway.app/api/students`

## Environment Variables

Railway automatically provides:
- `DATABASE_URL` - Full PostgreSQL connection string
- `DATABASE_USERNAME` - Database username
- `DATABASE_PASSWORD` - Database password
- `PORT` - Server port

## Configuration Files

- `railway.json` - Railway deployment configuration
- `Procfile` - Alternative startup command
- `application.properties` - Spring Boot configuration with environment variable support

## Troubleshooting

### Common Issues
1. **Build fails**: Check Java version (should be 17+)
2. **Database connection fails**: Ensure PostgreSQL service is running
3. **Port binding error**: Verify `server.port=${PORT:8085}` in application.properties

### Logs
View logs in the Railway dashboard to debug issues.

## API Documentation

Once deployed, your API will be available at:
`https://your-app-name.railway.app/api`

### Available Endpoints
- `GET /api/health` - Health check
- `GET /api/students` - Get all students
- `POST /api/students` - Create student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student
- `POST /api/files/generate-excel` - Generate Excel file
- `POST /api/files/excel-to-csv` - Convert Excel to CSV
- `POST /api/files/upload-csv` - Upload CSV to database
- `GET /api/export/excel` - Export to Excel
- `GET /api/export/csv` - Export to CSV
- `GET /api/export/pdf` - Export to PDF