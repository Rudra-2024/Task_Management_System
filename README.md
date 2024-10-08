# Task_Management_System
Approach and Assumptions
Approach

Architecture:
Spring Boot: Used for rapid development and easy configuration.
JWT Authentication: Secure user authentication and authorization.
REST API: Provides a standard interface for interaction with the task management system.

Database:
Relational Database: Used to store user and task data, configured via JPA (Java Persistence API).
Entity Relationships: Tasks are related to users, and operations are secured based on user ownership.

Testing:
Unit Testing: Ensures individual components (service methods, controllers) work as expected.
Integration Testing: Verifies that the entire system (including database interactions) functions correctly.


Assumptions

User Authentication:
JWT tokens are used for authentication.
User roles and permissions are handled appropriately.

Error Handling:
Custom exceptions and error handling are implemented to provide meaningful error messages.

API Security:
Endpoints are secured based on user roles and permissions.
Sensitive data is handled securely.

Data Validation:
Input data is validated both at the controller level (e.g., using annotations) and service level.

Development Environment:
The application is developed with a local environment in mind, with default configurations set for ease of development and testing.