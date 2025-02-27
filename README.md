# HouseWhisper Scheduler Server

## Overview

The HouseWhisper Scheduler Server is a Spring Boot application that provides various scheduling-related APIs. These APIs allow clients to check agent availability, find available time slots, find available agents, and get AI-recommended time slots based on client metadata and available time slots.

## API Endpoints

### 1. Check Availability

**Endpoint:** `/schedule/check-availability`  
**Method:** `POST`  
**Description:** Checks the availability of agents for a given time slot.

**Request Body:**
```json
{
  "agents": ["agent1", "agent2"],
  "startTime": "2023-11-01T09:00:00",
  "endTime": "2023-11-01T10:00:00"
}
```

**Response:**
- `200 OK` with a boolean indicating availability.

### 2. Find Available Time Slots

**Endpoint:** `/schedule/find-available-time-slots`  
**Method:** `POST`  
**Description:** Finds available time slots for agents within a specified time frame.

**Request Body:**
```json
{
  "agents": ["agent1", "agent2"],
  "durationInMinutes": 60,
  "startTimeFrame": "2023-11-01T09:00:00",
  "endTimeFrame": "2023-11-01T17:00:00"
}
```

**Response:**
- `200 OK` with a list of available time slots.
- `204 No Content` if no available time slots are found.

### 3. Find Available Agents

**Endpoint:** `/schedule/find-available-agents`  
**Method:** `POST`  
**Description:** Finds available agents for the given time slots.

**Request Body:**
```json
[
  {
    "start_time": "2023-11-01T09:00:00",
    "end_time": "2023-11-01T10:00:00"
  },
  {
    "start_time": "2023-11-01T11:00:00",
    "end_time": "2023-11-01T12:00:00"
  }
]
```

**Response:**
- `200 OK` with a list of available agent IDs.
- `204 No Content` if no available agents are found.

### 4. Get AI-Recommended Time Slots

**Endpoint:** `/schedule/ai-recommended-time-slots`  
**Method:** `POST`  
**Description:** Gets AI-recommended time slots for a client based on available time slots.

**Request Body:**
```json
{
  "clientId": "client1",
  "availableTimeSlots": [
    {
      "start_time": "2023-11-01T09:00:00",
      "end_time": "2023-11-01T10:00:00"
    },
    {
      "start_time": "2023-11-01T11:00:00",
      "end_time": "2023-11-01T12:00:00"
    }
  ]
}
```

**Response:**
- `200 OK` with an `AIRecommendedTimeSlotDTO` containing recommended time slots and reasoning.

### 5. Initialize System

**Endpoint:** `/init/init`  
**Method:** `POST`  
**Description:** Initializes the system.

**Response:**
- `200 OK` on successful initialization.

### 6. Generate Random Description

**Endpoint:** `/init/generateRandomDescription`  
**Method:** `GET`  
**Description:** Generates a random description.

**Response:**
- `200 OK` with a string containing the generated description.

## Data Transfer Objects (DTOs)

### CheckAvailabilityDTO

```java
public class CheckAvailabilityDTO {
    private List<String> agents;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
```

### CompleteAvailabilityDTO

```java
public class CompleteAvailabilityDTO {
    private List<String> agents;
    private int durationInMinutes;
    private LocalDateTime startTimeFrame;
    private LocalDateTime endTimeFrame;
}
```

### AvailableTimeSlotsDTO

```java
public class AvailableTimeSlotsDTO {
    private LocalDateTime start_time;
    private LocalDateTime end_time;
}
```

### AIRecommendedTimeSlotDTO

```java
public class AIRecommendedTimeSlotDTO {
    private String clientId;
    private String reason;
    private List<AvailableTimeSlotsDTO> availableTimeSlots;
}
```

## Services

### SchedulingService

Provides methods to check agent availability, find available time slots, find available agents, and get AI-recommended time slots.

### InitService

Provides methods to initialize the system and generate random descriptions.

### TaskService

Provides methods to manage tasks.

### ClientService

Provides methods to manage clients.

### AgentService

Provides methods to manage agents.
