#!/bin/bash

echo "🔧 Setting up Backend Git repository..."

# Initialize git if not already initialized
if [ ! -d ".git" ]; then
    git init
    echo "✅ Git repository initialized"
else
    echo "ℹ️  Git repository already exists"
fi

# Add all files
echo "📁 Adding files to git..."
git add .

# Check if there are changes to commit
if git diff --staged --quiet; then
    echo "ℹ️  No changes to commit"
else
    # Commit
    echo "💾 Committing changes..."
    git commit -m "Initial commit: Student Processing System Backend"
    echo "✅ Changes committed"
fi

echo ""
echo "🚀 Next steps:"
echo "1. Create a new repository on GitHub (e.g., 'student-processing-backend')"
echo "2. Run: git remote add origin https://github.com/yourusername/student-processing-backend.git"
echo "3. Run: git branch -M main"
echo "4. Run: git push -u origin main"
echo ""
echo "🚂 Railway Deployment:"
echo "1. Connect this GitHub repository to Railway"
echo "2. Add PostgreSQL database service"
echo "3. Deploy automatically!"