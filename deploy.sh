#!/bin/bash

echo "🚀 Preparing Backend for Railway Deployment..."

# Clean and package
echo "🧹 Cleaning previous builds..."
./mvnw clean

echo "📦 Packaging application..."
./mvnw package -DskipTests

echo "✅ Backend build complete!"
echo "📁 JAR file created: target/student-processing-0.0.1-SNAPSHOT.jar"
echo "🚂 Ready to deploy to Railway!"

# Check if JAR was created successfully
if [ -f "target/student-processing-0.0.1-SNAPSHOT.jar" ]; then
    echo "✅ JAR file verified: $(ls -lh target/student-processing-0.0.1-SNAPSHOT.jar)"
else
    echo "❌ JAR file not found! Build may have failed."
    exit 1
fi