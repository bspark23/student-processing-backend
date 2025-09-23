# Backend Deployment Guide - Render

## Render Deployment

### Prerequisites
- GitHub account
- Render account (sign up at [render.com](https://render.com))

### Step 1: Push to GitHub
```bash
# Run the setup script
./setup-git.sh  # or setup-git.bat on Windows

# Follow the instructions to push to GitHub
```

### Step 2: Deploy to Render
1. Go to [Render](https://render.com)
2. Click "New +" → "Web Service"
3. Connect your GitHub repository
4. Choose your backend repository
5. Configure the service:
   - **Name**: `student-processing-backend`
   - **Environment**: `Java`
   - **Build Command**: `./mvnw clean package -DskipTests`
   - **Start Command**: `java -Dserver.port=$PORT -jar target/student-processing-0.0.1-SNAPSHOT.jar`

### Step 3: Add Database
1. In Render dashboard, click "New +" → "PostgreSQL"
2. Configure:
   - **Name**: `student-processing-db`
   - **Database Name**: `studentdb`
   - **User**: `postgres`
3. Copy the **External Database URL**

### Step 4: Configure Environment Variables
In your web service settings, add:
- **DATABASE_URL**: (paste the PostgreSQL External Database URL)
- **JAVA_OPTS**: `-Xmx512m`

### Step 5: Deploy
Render will automatically build and deploy your application.

## Testing Deployment

After deployment, test these endpoints:
- Health check: `https://your-app.onrender.com/api/health`
- Students API: `https://your-app.onrender.com/api/students`

## Environment Variables

Render requires:
- `DATABASE_URL` - PostgreSQL connection string (from your PostgreSQL service)
- `PORT` - Server port (automatically set by Render)

## Configuration Files

- `render.yaml` - Render service configuration (optional)
- `application.properties` - Spring Boot configuration with environment variable support

## Troubleshooting

### Common Issues
1. **Build fails**: Check Java version (should be 17+)
2. **Database connection fails**: Ensure PostgreSQL service is running and DATABASE_URL is correct
3. **Port binding error**: Verify `server.port=${PORT:8085}` in application.properties
4. **Memory issues**: Add `JAVA_OPTS=-Xmx512m` environment variable

### Logs
View logs in the Render dashboard to debug issues.

## API Documentation

Once deployed, your API will be available at:
`https://your-app-name.onrender.com/api`

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

## Performance Notes

- Render free tier may have cold starts
- Database connections are limited on free tier
- Consider upgrading for production use