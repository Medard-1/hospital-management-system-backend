# Hospital Management System (Mid-Term Project Examination)
**Student:** Musinguzi Medard  
**Student ID:** 26601  
**Course:** Web Technology and Internet · AUCA  
**Project:** Hospital Management System  

---

## 1. System Overview & Architecture
This project is built using **Spring Boot 3**, **Spring Data JPA**, and **PostgreSQL**. It follows the standard student-level three-tier layered architecture mirroring the Kigali Clinic API:

```
src/main/java/auca/ac/hospitalmanagementsystem/
│
├── model/                     <-- JPA Entities & Enums
│   ├── AdmissionStatus.java   <-- Enum (ADMITTED, UNDER_TREATMENT, DISCHARGED, TRANSFERRED)
│   ├── Qualification.java     <-- Entity (@ManyToMany with Nurse, @JsonIgnore on nurses)
│   ├── Ward.java              <-- Entity (wardNumber, name)
│   ├── Nurse.java             <-- Entity (@ManyToOne Ward, @ManyToMany Qualification)
│   ├── Inpatient.java         <-- Entity (unique admissionNumber)
│   ├── Admission.java         <-- Entity (@ManyToOne Inpatient, @ManyToOne Nurse)
│   └── DischargeReport.java   <-- Entity (@ManyToOne Admission)
│
├── repository/                <-- Spring Data JPA Repositories
│   ├── QualificationRepository.java
│   ├── WardRepository.java
│   ├── NurseRepository.java
│   ├── InpatientRepository.java
│   ├── AdmissionRepository.java
│   └── DischargeReportRepository.java
│
├── service/                   <-- Business Logic Layer (No DTOs, No try/catch)
│   ├── QualificationService.java
│   ├── WardService.java
│   ├── NurseService.java
│   ├── InpatientService.java
│   ├── AdmissionService.java
│   ├── DischargeReportService.java
│   └── DashboardService.java
│
└── controller/                <-- REST Controllers (Mapping messages to HttpStatus)
    ├── QualificationController.java
    ├── WardController.java
    ├── NurseController.java
    ├── InpatientController.java
    ├── AdmissionController.java
    ├── DischargeReportController.java
    └── DashboardController.java
```

---

## 2. Database Setup (`application.properties`)
Make sure PostgreSQL is running and database `hospital_db` is created:
```sql
CREATE DATABASE hospital_db;
```

Properties in `src/main/resources/application.properties`:
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

## 3. How to Run the Project
Open terminal in `hospital-management-system` folder:
```powershell
.\mvnw.cmd spring-boot:run
```
Or run `HospitalManagementSystemApplication.java` directly in your IDE (IntelliJ IDEA / Eclipse / VS Code).

---

## 4. Requirement Breakdown & Live Demo Explanations

### Part A: Domain Model and CRUD (15 Marks)
- **A1:** Five entities (`Qualification`, `Ward`, `Nurse`, `Inpatient`, `Admission`) + `AdmissionStatus` enum.
  - UUID generated with `@GeneratedValue(strategy = GenerationType.AUTO)`.
  - `@JsonIgnore` placed on `Qualification.nurses` to prevent circular JSON loops.
- **A2:** CRUD endpoints for all entities:
  - `POST /api/{path}/save` returns `201 CREATED` (`"{Entity} saved successfully"`).
  - `GET /api/{path}/all` returns `200 OK`.
  - `GET /api/{path}/{id}`, `PUT /api/{path}/update/{id}`, `DELETE /api/{path}/delete/{id}` return `404 NOT_FOUND` (`"The {entity} with that id does not exist"`) or `200 OK`.

### Part B: Derived Queries (15 Marks, 5 each)
*All solved strictly by method names (no `@Query`)*:
- **B1:** `GET /api/inpatients/by-last-name?lastName=musinguzi`
  - Query: `findByLastNameIgnoreCaseOrderByFirstNameAsc(String lastName)`
- **B2:** `GET /api/admissions/by-status?status=ADMITTED`
  - Query: `findByStatusOrderByAdmissionDateAsc(AdmissionStatus status)`
- **B3:** `GET /api/admissions/between?start=2026-10-01&end=2026-10-31`
  - Query: `findByAdmissionDateBetweenOrderByAdmissionDateAsc(LocalDate start, LocalDate end)`

### Part C: JPQL with Joins (20 Marks, 5 each)
*All solved strictly using `@Query`*:
- **C1:** `GET /api/nurses/by-qualification?name=midwifery`
  - Query: `SELECT n FROM Nurse n JOIN n.qualifications q WHERE LOWER(q.name) = LOWER(:name)`
- **C2:** `GET /api/wards/free`
  - Query: `SELECT w FROM Ward w WHERE NOT EXISTS (SELECT n FROM Nurse n WHERE n.ward = w) ORDER BY w.wardNumber ASC`
- **C3:** `GET /api/qualifications/unused`
  - Query: `SELECT q FROM Qualification q WHERE q.nurses IS EMPTY`
- **C4:** `GET /api/inpatients/of-nurse/{nurseId}`
  - Query: `SELECT DISTINCT a.inpatient FROM Admission a WHERE a.nurse.id = :nurseId`
  - Returns `404` (`"The nurse with that id does not exist"`) if nurse id is not found.

### Part D: Business Rules (25 Marks)
- **D1 (Safe Saving):** `POST /api/admissions/save`
  - Date validation: if `admissionDate.isBefore(LocalDate.now())` -> `400 BAD REQUEST` (`"Admission date cannot be in the past"`).
  - Derived check: `existsByNurseIdAndAdmissionDateAndStatusNot(nurseId, date, AdmissionStatus.TRANSFERRED)` -> if true, `409 CONFLICT` (`"Nurse already receives an admission on that date"`).
  - Status reset: New admission always forced to `ADMITTED`.
- **D2 (Status Flow):** `PATCH /api/admissions/{id}/status?status=...`
  - Allowed moves:
    - `ADMITTED` -> `UNDER_TREATMENT` or `TRANSFERRED`
    - `UNDER_TREATMENT` -> `DISCHARGED` or `TRANSFERRED`
    - `DISCHARGED`, `TRANSFERRED` -> none
  - Disallowed moves return `400 BAD REQUEST` (`"Cannot change status from X to Y"`).
  - General `PUT /api/admissions/update/{id}` preserves existing status and does not overwrite it.
- **D3 (Add Qualification):** `PUT /api/nurses/{nurseId}/qualifications/{qualificationId}`
  - Returns `404` if nurse or qualification not found.
  - Returns `409` (`"The nurse already has that qualification"`) if already held.
  - Returns `200` (`"Qualification added to the nurse"`).
- **D4 (Change Ward):** `PATCH /api/nurses/{nurseId}/ward?wardNumber=4`
  - Derived check: `wardRepository.findByWardNumber(wardNumber)` (404 if not found).
  - Derived check: `nurseRepository.existsByWard(ward)` -> `409` (`"That ward is taken by another nurse"`).
  - Returns `200` (`"Ward changed successfully"`).

### Part E: DischargeReport (15 Marks, 5 each)
- **E1:** `POST /api/discharge-reports/save?admissionId=...`
  - Checks if admission exists (404) and is `DISCHARGED` (400 if not).
  - Automatically sets `issuedDate = LocalDate.now()`.
  - Returns `201 CREATED` (`"Discharge report saved successfully"`).
- **E2:** `GET /api/discharge-reports/inpatient/{admissionNumber}`
  - JPQL: `SELECT d FROM DischargeReport d WHERE d.admission.inpatient.admissionNumber = :admissionNumber ORDER BY d.issuedDate DESC`
- **E3:** Delete & Protect:
  - `DELETE /api/discharge-reports/delete/{id}` -> 200 or 404.
  - `DELETE /api/admissions/delete/{id}` -> if `dischargeReportRepository.existsByAdmission(...)` returns true -> `409 CONFLICT` (`"That admission has discharge reports, delete them first"`).

### Part F: Reports and Bulk Updates (10 Marks, 5 each)
- **F1:** `GET /api/admissions/stats/by-status`
  - JPQL: `SELECT a.status, COUNT(a) FROM Admission a GROUP BY a.status`
- **F2:** `PATCH /api/admissions/cancel-day?nurseId=...&date=2026-10-20`
  - JPQL: `@Modifying @Query("UPDATE Admission a SET a.status = ... WHERE a.nurse.id = :nurseId AND a.admissionDate = :date AND a.status <> ...DISCHARGED")`
  - Service is `@Transactional`, returns `"{count} admissions cancelled"`.

### Bonus Features (+10 Marks)
- **Bonus 1 (+5):** `GET /api/dashboard/summary`
  - Returns LinkedHashMap: `{"inpatients": 5, "nurses": 3, "freeWards": 1, "admissionsToday": 0, "admitted": 4, "dischargeReports": 6}`.
- **Bonus 2 (+5):** `GET /api/admissions/page?page=0&size=5&sort=admissionDate,desc`
  - Returns `Page<Admission>` using Spring Data `Pageable`.

---

## 5. Postman Collection
Import `hospital_system.json` into Postman. It contains preconfigured requests for all seed data and all 18 requirements + bonus features, including both success cases and failing test cases (400, 404, 409).
