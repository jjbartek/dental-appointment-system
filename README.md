# Dental Appointment System
## 📝 About DAS

Dental Appointment System is a REST API application that is currently in development. Its goal is to simplify and automate the appointment management process for dental clinics. Here are some of the features that have been implemented:

 * Management and scheduling of patient visits,
 * Management of the patient database,
 * Administration of staff and available services,
 * JWT authentication,
 * Swagger API Documentation

User roles determine their access to the API:
 - ADMIN - manages employees, services (+ same as RECEPTIONIST)
 - RECEPTIONIST - manages patients, appointments
 - EMPLOYEE - can view visits, patients and their treatment histories

## 🛠️Stack
![Java, Spring, Hibernate, Mysql](https://skillicons.dev/icons?i=java,spring,hibernate,mysql)
## ▶ Run
1. Setup MySQL database and update variables in **application.properties**
2. Open the project in your IDE and run **DASApplication.java**
3. You can access API documentation at localhost:8080/api/v1/swagger-ui/index.html

## 📄Documentation preview
![Swagger documentation preview](https://i.imgur.com/E7jfAVP.png)
