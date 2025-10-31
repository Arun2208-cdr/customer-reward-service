# Customer Reward Service

## Overview
This Spring Boot application calculates customer reward points based on their monthly transactions.
Rewards follow a 2-tier system:
- 1 point for every dollar spent over 50
- 2 points for every dollar spent over 100

## Endpoints
| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | `/api/rewards` | Fetch all customer rewards summary |
| GET | `/api/rewards/{customerId}` | Fetch single customer's reward summary |
| GET | `/api/rewards/period?start=yyyy-MM-dd&end=yyyy-MM-dd` | Fetch rewards in a date range |

## Example Response
```json
{"customerId":1,"name":"Arun Kumar","phone":"9876543210","city":"Chennai","totalRewards":1200,"monthlyRewards":{"2024-01":100,"2024-02":300}}
```

## Build & Run
```bash
mvn clean install
mvn spring-boot:run
```
