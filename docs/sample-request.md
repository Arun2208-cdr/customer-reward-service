**Description**
Fetches reward summary for a specific customer over a given date range.
If the date range is omitted, rewards for the last 3 months are calculated by default.

**Request Type**
- GET

**Query Parameters**
| Parameter    | Type   | Required  | Description                          | Example        |
|--------------|--------|-----------|--------------------------------------|----------------|
| customerId   | Long   |  Yes     | Unique ID of the customer             | 1              |
| start        | String |  No      | Start date in ISO format (yyyy-MM-dd) | 2025-06-01     |
| end          | String |  No      | End date in ISO format (yyyy-MM-dd)   | 2025-10-31     |

**Example cURL Command**
curl -X GET "http://localhost:8080/loyalty/api/rewards?customerId=1&start=2025-06-01&end=2025-11-30"
OR
http://localhost:8080/loyalty/api/rewards?customerId=1&start=2025-06-01&end=2025-11-30