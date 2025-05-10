# Deployable Genesis Resources

A fully deployable user registration system for Genesis Resources.

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Installation](#installation)
- [Usage](#usage)
- [API](#api)

## Overview

The frontend is built using the React(Vite) framework using JSX + SWC.

The backend exposes a simple RESTful API developed with the Spring Boot framework. The API allows the client to perform CRUD operations on users. Users can be created, read, updated, or deleted.
The application can be run in two different profiles:

- **Development (dev)**: Uses a H2 database in backend, React Vite HMR for hot reloading, and optimized settings for development environments. 
- **Production (prod)**: Utilizing PostgreSQL database with preconfigured database and optimized settings for production environments.

## Tech Stack

The following technologies are used in this project:

| **Technology**                | **Purpose**                                   |
|-------------------------------|-----------------------------------------------|
| **Java 21 (LTS)**              | Core language for backend development         |
| **Maven**                      | Dependency management and build automation    |
| **Spring Boot**                | Framework for building Java-based applications|
| **Spring Data JPA + Hibernate**| ORM for database interaction                  |
| **H2**                         | In-memory database for development            |
| **PostgreSQL**                 | Relational database for production            |
| **Logback + SLF4J**            | Logging framework                             |
| **JUnit + Mockito**            | Testing frameworks                            |
| **React (Vite)**               | Frontend development framework and build tool |


## Installation

### Requirements:

| **Tool**                   | **Purpose**                                   |
|----------------------------|-----------------------------------------------|
| **Git**                     | For cloning project                          |
| **Java 21(LTS)^**           | For running backend application              |
| **Maven (development)**     | For building backend application             |
| **Docker**                  | For running services in containers           |
| **Bash**                    | For executing init and start scripts through GenesisResources CLI |

**1.) Open terminal supporting bash and locate to desired project directory where GenesisResources will be cloned**
```
cd /path/to/apps
```

**2.) Clone repository**
```
git clone https://github.com/vesely-jiri/GenesisResources
```

**3.) Navigate to project directory**
```
cd GenesisResources
```

**4.) Run start script with build flag**
```
./manager.sh development start --b
```
```
./manager.sh production start --b
```

**5.) Choose database password for chosen profile or let script generate one using (openssl base64 12)**
```
Enter new database password for .env.<profile> (generate):
```

**6.) App is ready to be used**
```
#Backend default port
http://localhost:8080
#Frontend default port
http://localhost:3000
```

## Usage

### API Endpoints

##### GET

```
GET http://localhost:8080/api/v1/users
GET http://localhost:8080/api/v1/users?detail=true
GET http://localhost:8080/api/v1/users/{id}
GET http://localhost:8080/api/v1/users/{id}?detail=true
```

##### POST

```
POST http://localhost:8080/api/v1/users
```
```
{
    "firstName": "John",
    "lastName": "Doe",
    "personId": "1234567890"
}
```

##### PUT

```
PUT http://localhost:8080/api/v1/users
```
```
{
    "id": 6,
    "firstName": "Martin",
    "lastName": "Hora"
}
```

##### DELETE

```
DELETE http://localhost:8080/api/v1/users/{id}
```

# TODO
- Implement Docker/GitHub secrets for database credentials
- Implement GitHub actions for CI/CD
