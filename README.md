# Java Code Assignment – Fulfilment Application

## About the Project

This project is a Java-based fulfilment application developed as part of a coding assignment.

The application manages fulfilment-related operations involving:

- Products
- Stores
- Warehouses
- Locations
- Fulfilments

The project provides REST APIs and implements business rules for warehouse and fulfilment management.

The original assignment requirements can be found in:

[CODE_ASSIGNMENT](assignment/CODE_ASSIGNMENT.md)

---

## Architecture

The application follows Hexagonal Architecture principles by separating business logic from external implementations.

The main responsibilities are organized into the following areas:

### Domain

Contains the core business models and domain-related logic.

### Ports

Defines contracts and interfaces used by the application.

### Use Cases

Contains the application business operations.

### Adapters

Contains implementations for communication with external systems, including:

- Database adapters
- REST API adapters

### Validators

Validation logic is separated from the main business logic.

Dedicated validation classes are used for:

- Warehouse validation
- Fulfilment validation

This separation improves maintainability, readability, and testability.

---

## Project Structure

The application is organized based on business responsibilities.

Main modules include:

- `products`
- `stores`
- `warehouses`
- `location`
- `fulfilment`

The warehouse and fulfilment modules follow a separation of responsibilities using areas such as:

- Domain models
- Ports
- Use cases
- Adapters
- Validators

---

## Technologies Used

- Java 17
- Quarkus 3.13.3
- Maven
- Hibernate ORM with Panache
- REST Jackson
- PostgreSQL
- JUnit 5
- Mockito
- REST Assured
- JaCoCo
- OpenAPI Generator

---

## Prerequisites

Before running the application, make sure the following are available:

- Java 17
- Docker Desktop
- Git

Docker Desktop may be required when the application uses PostgreSQL through Quarkus Dev Services.

---

## Running the Application

### Windows

Run the application in development mode:

```powershell
.\mvnw.cmd quarkus:dev
```

The application will start in Quarkus development mode.

---

## Building the Application

To build the application, run:

```powershell
.\mvnw.cmd clean package
```

---

## Running Tests

To run the tests:

```powershell
.\mvnw.cmd test
```

---

## Code Coverage

The project uses JaCoCo for source code coverage tracking.

The latest coverage results are:

| Coverage Type | Coverage |
|---|---:|
| Class Coverage | 82% |
| Method Coverage | 89% |
| Line Coverage | 87% 

The source code line coverage is above the required **80%** target.

---

## Application Screenshots

Screenshots demonstrating the application functionality and code coverage are included in the project.

---

## Assignment Reference

The original assignment requirements are available here:

[CODE_ASSIGNMENT](assignment/CODE_ASSIGNMENT.md)