#  Customer Reward Service

##  Overview
This Spring Boot application calculates **customer reward points** based on their monthly transactions.  
Rewards follow a **2-tier system**:
- **1 point** for every dollar spent over **$50**
- **2 points** for every dollar spent over **$100**

You can retrieve the **reward summary** for a single customer over a **specific date range**.

---

##  Tech Stack
| Component | Technology |
|------------|-------------|
| **Backend Framework** | Spring Boot 3.x |
| **Language** | Java 17 |
| **Database** | H2 (In-Memory) |
| **Persistence** | Spring Data JPA |
| **Build Tool** | Maven |
| **Validation & Exception Handling** | Spring Boot Starter Validation, ControllerAdvice |
| **Testing** | JUnit 5, Mockito |

---

##  Endpoints

| Method | Endpoint | Description                                                                                                                      |
|--------|-----------|----------------------------------------------------------------------------------------------------------------------------------|
| **GET** | `/loyalty/api/rewards?customerId={customerId}&start=yyyy-MM-dd&end=yyyy-MM-dd` | Fetch customer's reward summary for the given date range, if date range is not given then last 3 months is considered as default|

---

##  Example Response

{
  "customerId": 1,
  "name": "Arun Kumar",
  "phone": "9876543210",
  "city": "Chennai",
  "totalRewards": 115,
  "monthlyRewards": [
      { "month": "2025-07", "rewardPoints": 90 },
      { "month": "2025-08", "rewardPoints": 60 },
      { "month": "2025-09", "rewardPoints": 80 }
  ]
}

##  Build and Run the Application

###  Prerequisites
- Java 17 or later
- Maven 3.8+ installed

### ️ Build the Project

mvn clean install

##  Run the Application
mvn spring-boot:run

The application will start at:
http://localhost:8080/loyalty

## Access the H2 Database Console
http://localhost:8080/loyalty/h2-console

- JDBC URL:jdbc:h2:mem:rewardsdb
- Username:sa
- Password:(leave blank)

## Notes

- The database is in-memory (H2) — data is recreated on every app start.
- schema.sql defines the database schema.
- data.sql seeds initial customers and transactions (spanning June–October 2025). 

