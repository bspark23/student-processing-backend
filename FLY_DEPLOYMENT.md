# Backend Deployment Guide - Fly.io

## Fly.io Deployment

### Prerequisites
- Fly.io account (sign up at [fly.io](https://fly.io))
- Fly CLI installed ([installation guide](https://fly.io/docs/hands-on/install-flyctl/))
- Docker installed (for local testing)

### Step 1: Install Fly CLI
```bash
# Windows (PowerShell)
iwr https://fly.io/install.ps1 -useb | iex

# macOS/Linux
curl -L https://fly.io/install.sh | sh
```

### Step 2: Login to Fly.io
```bash
fly auth login
```

### Step 3: Initialize Fly App
```bash
# In the backend directory
fly launch --no-deploy
```

This will:
- Create a `fly.toml` configuration file (already provided)
- Set up your app on Fly.io
- Choose a region (recommend `iad` for US East)

### Step 4: Create PostgreSQL Database
```bash
fly postgres create --name student-processing-db --region iad
```

### Step 5: Attach Database to App
```bash
fly postgres attach --app student-processing-backend student-processing-db
```

This automatically sets the `DATABASE_URL` environment variable.

### Step 6: Deploy
```bash
fly deploy
```

## Configuration Files

### fly.toml
- App configuration
- Resource allocation (512MB RAM, 1 CPU)
- Health checks on `/api/health`
- Auto-scaling settings

### Dockerfile
- Multi-stage build for optimal image size
- Uses OpenJDK 17
- Builds with Maven
- Exposes port 8080

## Environment Variables

Fly.io automatically provides:
- `DATABASE_URL` - PostgreSQL connection string (when database is attached)
- `PORT` - Server port (set to 8080)

Additional variables you can set:
```bash
fly secrets set JAVA_OPTS="-Xmx512m"
```

## Testing Deployment

After deployment, test these endpoints:
- Health check: `https://your-app.fly.dev/api/health`
- Students API: `https://your-app.fly.dev/api/students`

## Useful Fly.io Commands

```bash
# View logs
fly logs

# Check app status
fly status

# Scale app
fly scale count 1

# Set secrets/environment variables
fly secrets set KEY=value

# SSH into running app
fly ssh console

# View app info
fly info

# Restart app
fly restart
```

## Database Management

```bash
# Connect to PostgreSQL
fly postgres connect -a student-processing-db

# View database info
fly postgres list
```

## Monitoring

- View metrics: `https://fly.io/apps/your-app-name`
- Check logs: `fly logs`
- Monitor health: Built-in health checks on `/api/health`

## Scaling

```bash
# Scale to 2 instances
fly scale count 2

# Scale memory
fly scale memory 1024

# Scale CPU
fly scale vm shared-cpu-2x
```

## Troubleshooting

### Common Issues
1. **Build fails**: Check Dockerfile and ensure Java 17 compatibility
2. **Database connection fails**: Verify DATABASE_URL is set correctly
3. **Port binding error**: Ensure PORT environment variable is used
4. **Memory issues**: Increase memory allocation in fly.toml

### Debug Commands
```bash
# View app logs
fly logs

# Check app status
fly status

# View configuration
fly config show

# SSH into app for debugging
fly ssh console
```

## Cost Optimization

- **Free tier**: 3 shared-cpu-1x VMs with 256MB RAM
- **Auto-scaling**: Configured to scale to 0 when idle
- **Resource limits**: Set appropriate CPU/memory limits

## API Documentation

Once deployed, your API will be available at:
`https://your-app-name.fly.dev/api`

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

## Security

- HTTPS enforced by default
- Environment variables stored as secrets
- Database connections encrypted
- CORS configured for your frontend domain