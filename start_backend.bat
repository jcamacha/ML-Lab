@echo off
set SPRING_DATASOURCE_URL=jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:***@gmail.com
set SPRING_MAIL_PASSWORD=*** PORT=8080

cd /d C:\Users\juanp\Documents\Proyectos\proyecto-final-web\web_project\web_project\java-backend
echo ========================================
echo  ML Lab - Iniciando Backend (Spring Boot)
echo  Puerto: 8080
echo  Swagger: http://localhost:8080/swagger-ui/index.html
echo ========================================
call mvn spring-boot:run
pause
