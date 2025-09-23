@echo off
echo 🚀 Preparing Backend for Railway Deployment...

echo 🧹 Cleaning previous builds...
call mvnw.cmd clean

echo 📦 Packaging application...
call mvnw.cmd package -DskipTests

echo ✅ Backend build complete!
echo 📁 JAR file created: target/student-processing-0.0.1-SNAPSHOT.jar
echo 🚂 Ready to deploy to Railway!

if exist "target\student-processing-0.0.1-SNAPSHOT.jar" (
    echo ✅ JAR file verified
) else (
    echo ❌ JAR file not found! Build may have failed.
)

pause