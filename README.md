# Clinic Management API

A Spring Boot REST API for managing a care home facility — patients, staff, beds, and shift rosters.

Built as a backend companion to the [JavaFX Clinic Management System](../Clinic-management-system/).

## Tech Stack

- Java 21
- Spring Boot 3.5
- Spring Data JPA + H2 (in-memory)
- Bean Validation (Jakarta)
- Springdoc OpenAPI (Swagger UI)
- JUnit 5 + Spring Boot Test

## How to Run

```bash
# From the clinic-management-api directory
./gradlew bootRun
```

Server starts at `http://localhost:8080`.

On first launch the app seeds default data:
- Staff: Dr. Smith (D001), Nurse Joy (N001), Nurse May (N002), Manager Mike (M001)
- Patients: Alice (P001), Kenn (P002), Sam (P003)
- Beds: 18 beds across 2 wards × 3 rooms × 3 beds

## Interactive API Docs (Swagger UI)

```
http://localhost:8080/swagger-ui.html
```

All endpoints are documented and can be tested directly in the browser.

## Database Console

```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:clinicdb
Username: sa  |  Password: (empty)
```

## API Endpoints

### Patients `/api/patients`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/patients` | List all patients |
| GET | `/api/patients/{id}` | Get patient by ID |
| GET | `/api/patients/unassigned` | List patients without a bed |
| POST | `/api/patients` | Register new patient |
| PUT | `/api/patients/{id}` | Update patient name/gender |
| DELETE | `/api/patients/{id}/discharge` | Discharge patient (clears bed) |

### Staff `/api/staff`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/staff` | List all staff |
| GET | `/api/staff?role=DOCTOR` | Filter by role |
| GET | `/api/staff/{id}` | Get staff by ID |
| POST | `/api/staff` | Add new staff member |
| PUT | `/api/staff/{id}` | Update name/role |
| DELETE | `/api/staff/{id}` | Remove staff member |
| PATCH | `/api/staff/{id}/password` | Change password |

### Beds `/api/beds`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/beds` | List all beds |
| GET | `/api/beds/vacant` | List vacant beds |
| GET | `/api/beds/{id}` | Get bed by ID |
| POST | `/api/beds/{id}/assign` | Assign patient to bed |
| POST | `/api/beds/{id}/release` | Release patient from bed |
| POST | `/api/beds/{id}/move` | Move patient to another bed |

### Shifts `/api/shifts`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/shifts` | List all shift assignments |
| GET | `/api/shifts?staffId=N001` | Shifts for a staff member |
| GET | `/api/shifts?day=MONDAY` | Shifts on a given day |
| POST | `/api/shifts` | Assign a shift |
| DELETE | `/api/shifts` | Remove a shift assignment |

## Example Requests

```bash
# Register a new patient
curl -X POST http://localhost:8080/api/patients \
  -H "Content-Type: application/json" \
  -d '{"patientId":"P004","name":"Jane","gender":"FEMALE"}'

# Assign patient P001 to bed W1-R1-B1
curl -X POST http://localhost:8080/api/beds/W1-R1-B1/assign \
  -H "Content-Type: application/json" \
  -d '{"patientId":"P001"}'

# Assign a morning shift to Nurse Joy on Monday
curl -X POST http://localhost:8080/api/shifts \
  -H "Content-Type: application/json" \
  -d '{"staffId":"N001","day":"MONDAY","shift":"MORNING"}'
```

## Running Tests

```bash
./gradlew test
```

14 unit tests covering PatientService, BedService, and StaffService.
