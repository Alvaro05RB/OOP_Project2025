# OOP_Project2025

## Overview
This project was developed as part of the Object-Oriented Programming (OOP) course at Universidad Politécnica de Madrid.  
It implements a matchmaking system using Java and MySQL, allowing the creation and management of tournaments, teams, and players. Following object-oriented design principles and persistence with Hibernate.

## Features
- Create and manage tournaments with configurable settings  
- Register teams and individual players  
- Match players and teams automatically based on defined criteria  
- Store and retrieve tournament data using MySQL and Hibernate ORM  
- Object-oriented architecture with modular class design  
- Dockerized database setup for local testing and deployment

## My Contribution
This was a group project, and my main contributions included:
- Designed and implemented the core model classes: `Player`, `User`, and `Admin`  
- Developed the `UserController`, including user authentication features (`login`, `logout`, `signup`)  
- Ensured integration between the user layer and database through Hibernate  
- Collaborated in debugging and testing the persistence layer

## Tech Stack
- **Language:** Java 22  
- **Build Tool:** Maven  
- **Database:** MySQL (Docker, port 3306)  
- **ORM:** Hibernate  
- **JSON Processing:** Gson  

## Dependencies
| Dependency | Version | Purpose |
|-------------|----------|----------|
| Java | 22 | Main programming language |
| MySQL | 8.0.28 | Database (Docker container) |
| Hibernate Core | 5.4.24.Final | ORM for database persistence |
| Gson | 2.11.0 | JSON serialization/deserialization |

## Setup and Execution
1. Clone this repository  
   ```bash
   git clone https://github.com/Alvaro05RB/OOP_Project2025.git
2. Start the MySQL Docker container (default port 3306, no password required).
3. Run the project
Open the project in your IDE (e.g., IntelliJ IDEA or Eclipse) and run App.java from the src/main/java directory.
