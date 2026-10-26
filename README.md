# KHC - Hospital Information Management System (HIMS)
### Backend (Spring Boot 3/4 + Java 17) & Frontend (React.js + Vite)

A complete Hospital Information Management System implementing the navigation hierarchy and role-based access control:

```
+---------------------------------------------------------------------------------------------------------+
|     MASTER          |       REGISTRATION            |         BILLING           |        REPORT         |
+---------------------+-------------------------------+---------------------------+-----------------------+
| * ADMIN             | * OP NEW                      | * OP SERVICE BILLING      | * OP BILLING          |
| * USER CREATION     | * OP REVIEW                   | * DOCTOR NAME MODIFY      | * REVENUE (SUMMARY +  |
| * SERVICE MASTER    | * PATIENT DETAIL UPDATE       |   - OP BILL               |   SERVICE WISE)       |
|   (TARIFF & RATES)  | * DOCTOR NAME MODIFY          | * PAYMENT TYPE MODIFY     | * OP REGISTRATION     |
|                     |   - REGISTRATION              |                           |                       |
|                     | * PATIENT TYPE MODIFY         |                           |                       |
|                     | * PAYMENT TYPE MODIFY         |                           |                       |
+---------------------------------------------------------------------------------------------------------+
```

---

## 🔐 Role-Based Access Control (RBAC)

The system enforces role-based navigation and permissions:

| User Account | Default Password | Role | Access Scope & Menus |
|---|---|---|---|
| **`admin`** | `admin123` | **`ADMIN`** | **ALL RIGHTS**: Full access to all 4 pillars (`MASTER`, `REGISTRATION`, `BILLING`, `REPORT`). |
| **`user1`** | `user123` | **`USER`** | **SELECTED MENUS ONLY**: Registration (`OP NEW`, `OP REVIEW`, `PATIENT UPDATE`), Billing (`OP SERVICE BILLING`), and `OP REGISTRATION REPORT`. Master menu is locked. |
| **`cashier1`** | `cashier123` | **`USER`** | **SELECTED MENUS ONLY**: Billing & Financial Reports (`OP SERVICE BILLING`, `MODIFY PAYMENT`, `OP BILLING REPORT`, `REVENUE REPORT`). |

---

## 🛠️ Service Master (Tariff & Amount Master)

Configured under **MASTER > SERVICE MASTER** (`/services`):
- **Service Name:** e.g., `12-Lead Electrocardiogram (ECG)`, `Complete Blood Count (CBC)`, `Digital Chest X-Ray`
- **Header:** Category tag (`ECG`, `LABTEST`, `CBC`, `RADIOLOGY`, `PROCEDURE`, `CONSULTATION`)
- **Department:** `Cardiology`, `Pathology`, `Radiology`, `Biochemistry`, etc.
- **OP RATE (₹):** Outpatient tariff amount (auto-populates in OP Service Billing)
- **IP RATE (₹):** Inpatient tariff amount
- **is_active:** Boolean active flag (`true` / `false`)

---

## 🚀 Quick Start Guide (Step-by-Step)

### Prerequisites
- **Java 17+** (`java -version`)
- **Node.js & npm** (`node -v`, `cmd.exe /c npm -v`)

---

### Step 1: Run the Backend (Spring Boot)

The backend comes pre-configured with **In-Memory H2 Database** and **Demo Seed Data**:
```cmd
cd C:\Users\Queen\Downloads\KHC-HIMS
mvnw.cmd spring-boot:run
```

- **Backend API:** `http://localhost:8080/api`
- **H2 Console:** `http://localhost:8080/h2-console` *(JDBC URL: `jdbc:h2:mem:khcdb`, User: `sa`, Password: blank)*
- **To switch to PostgreSQL:** Run with `-Dspring-boot.run.profiles=postgres`

---

### Step 2: Run the Frontend (React.js)

```cmd
cd C:\Users\Queen\Downloads\KHC-HIMS\frontend
npm run dev
```

- **Frontend Application:** `http://localhost:3000`

---

## 📡 REST API Reference

| Module | Method | Endpoint | Description |
|---|---|---|---|
| **Auth** | `POST` | `/api/auth/login` | Authenticate user & retrieve allowed menus |
| **Master** | `GET` | `/api/master/services` | List service tariffs (with search & header filter) |
| **Master** | `POST` | `/api/master/services` | Create new service tariff |
| **Master** | `PUT` | `/api/master/services/{id}` | Update service rate / is_active |
| **Master** | `DELETE`| `/api/master/services/{id}` | Delete service rate |
| **Master** | `GET` | `/api/master/admin/stats` | System KPIs & counts |
| **Master** | `GET` | `/api/master/doctors` | List doctors |
| **Master** | `POST` | `/api/master/doctors` | Add doctor |
| **Master** | `GET` | `/api/master/users` | List staff accounts |
| **Master** | `POST` | `/api/master/users` | Create user with role & allowed menus |
| **Registration**| `POST` | `/api/registration/op-new` | OP New Outpatient ticket |
| **Registration**| `POST` | `/api/registration/op-review`| OP Review Follow-up visit |
| **Registration**| `PUT` | `/api/registration/patient-update`| Update patient demographics |
| **Registration**| `PUT` | `/api/registration/modify-doctor` | Modify doctor on OP ticket |
| **Registration**| `PUT` | `/api/registration/modify-patient-type` | Modify patient type on OP ticket |
| **Registration**| `PUT` | `/api/registration/modify-payment-type` | Modify payment type on OP ticket |
| **Billing** | `POST` | `/api/billing/op-service` | Generate OP invoice from Service Master |
| **Billing** | `PUT` | `/api/billing/modify-doctor` | Modify doctor on OP bill |
| **Billing** | `PUT` | `/api/billing/modify-payment-type` | Modify payment mode on OP bill |
| **Report** | `GET` | `/api/reports/op-billing` | Outpatient billing ledger |
| **Report** | `GET` | `/api/reports/revenue` | Summary & Service-wise revenue |
| **Report** | `GET` | `/api/reports/op-registration` | Registration footfall report |
