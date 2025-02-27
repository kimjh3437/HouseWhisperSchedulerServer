# HouseWhisper Scheduler Server

## Overview

The HouseWhisper Scheduler Server is a Spring Boot application that provides various scheduling-related APIs. These APIs allow clients to check agent availability, find available time slots, find available agents, and get AI-recommended time slots based on client metadata and available time slots.

## API Endpoints

### Service URL : https://housewhisper.azurewebsites.net

### 1. Check Availability

**Endpoint:** `/schedule/check-availability`  
**Method:** `POST`  
**Description:** Checks the availability of agents for a given time slot.

**Request Body:**
```json
{
  "agents": ["agent1", "agent2", "agent3"],
  "startTime": "2025-02-27T17:00:00",
  "endTime": "2025-02-27T19:30:00"
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
  "agents": ["agent1", "agent2", "agent3"],
  "durationInMinutes": 60,
  "startTimeFrame": "2025-02-27T16:00:00",
  "endTimeFrame": "2025-02-27T20:00:00"
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
    "start_time": "2025-03-01T09:00:00",
    "end_time": "2025-03-01T10:00:00"
  },
  {
    "start_time": "2025-03-02T11:00:00",
    "end_time": "2025-03-04T15:00:00"
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
  "clientId": "client3",
  "availableTimeSlots": [
    {
      "start_time": "2025-03-01T09:00:00",
      "end_time": "2023-11-01T10:00:00"
    },
    {
      "start_time": "2025-03-01T11:00:00",
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

## Models

### Agent

The `Agent` class represents an agent in the system. It contains the following fields:
- `agent_id`: A unique identifier for the agent.
- `tasks`: A list of task IDs assigned to the agent.

### Client

The `Client` class represents a client in the system. It contains the following fields:
- `client_id`: A unique identifier for the client.
- `personal`: An instance of `ClientPersonal` containing personal information about the client.
- `metadata`: A list of `ClientMetadata` instances containing metadata about the client.

### ClientPersonal

The `ClientPersonal` class contains personal information about a client. It includes the following fields:
- `client_id`: A unique identifier for the client.
- `firstname`: The first name of the client.
- `lastname`: The last name of the client.
- `email`: The email address of the client.

### ClientMetadata

The `ClientMetadata` class contains metadata about a client. It includes the following fields:
- `client_id`: A unique identifier for the client.
- `description`: A description of the client's preferences or habits.
- `event_time`: The time associated with the metadata event.

### WorkTask

The `WorkTask` class represents a task assigned to agents. It includes the following fields:
- `task_id`: A unique identifier for the task.
- `details`: An instance of `WorkTaskDetail` containing detailed information about the task.

### WorkTaskDetail

The `WorkTaskDetail` class contains detailed information about a task. It includes the following fields:
- `taskType`: The type of the task.
- `description`: A description of the task.
- `taskStartTime`: The start time of the task.
- `taskEndTime`: The end time of the task.
- `clients`: A list of client IDs associated with the task.
- `agents`: A list of agent IDs assigned to the task.

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
