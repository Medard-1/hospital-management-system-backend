# Hospital Management System (Mid-Term Project Examination)
**Student:** Musinguzi Medard  
**Student ID:** 26601  
**Course:** Web Technology and Internet · AUCA  
**Project:** Hospital Management System  

---

## Project Structure

```
src/main/java/auca/ac/hospitalmanagementsystem/
│
├── model/
│   ├── AdmissionStatus.java
│   ├── Qualification.java
│   ├── Ward.java
│   ├── Nurse.java
│   ├── Inpatient.java
│   ├── Admission.java
│   └── DischargeReport.java
│
├── repository/
├── service/
└── controller/
```

---

## Database Setup

Make sure PostgreSQL is running, then create the database:

```sql
CREATE DATABASE hospital_db;
```

Configure `src/main/resources/application.properties`:

```properties
spring.application.name=hospital-management-system
spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
spring.datasource.username=postgres
spring.datasource.password=Admin

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
```

---

## Steps to Run the API

1. Clone the repository:
   ```bash
   git clone https://github.com/Medard-1/hospital-management-system-backend.git
   cd hospital-management-system-backend
   ```

2. Make sure PostgreSQL is running and `hospital_db` exists.

3. Run the application:
   ```powershell
   mvn spring-boot:run
   ```
   Or if Maven is not installed globally:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. The API will be available at `http://localhost:8080`.

---

## Postman Collection

Import `hospital-management-system-midterm.postman_collection.json` from the root of this repository into Postman.  
It contains one request per requirement (A2, B1, B2, B3, C1, C2, C3, C4, D1, D2, D3, D4, E1, E2, E3, F1, F2, Bonus1, Bonus2), each with success and error cases.
