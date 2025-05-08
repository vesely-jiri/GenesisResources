# Deployable Genesis resources

Fully deployable registration system for users of Genesis Resources.

## Table of contents

- [Overview](#overview)
- [Stack](#stack)
- [Installation](#installation)
- [Usage](#usage)
- [API](#api)

## Overview
Frontend is based on React framework.

Backend is based on simple RESTful API built on SpringBoot framework. API 
allows client to perform CRUD operations on users. User can be created, 
read, updated or deleted. Backend can be run in 4 modes(profiles):
- development(dev) - using PostgreSQL database and simple credentials
- production(prod) - (default) using ready to use PostgreSQL database and settings

Database is based on PostgreSQL and H2.

## Stack

- Java 21(LTS)
- Maven
- SpringBoot
- Spring Data JPA + Hibernate
- H2
- PostgreSQL
- Logback + SLF4J
- JUnit + Mockito

## Installation
Requirements:
- Git             - For cloning project
- Java 21(LTS)^   - For running backend application
- Maven           - For building backend application
- Docker          - For running services in containers
- Bash            - For executing init and start scripts through GenesisResources CLI

1.) Clone repository
```
git clone https://github.com/vesely-jiri/GenesisResources
```

2.) Navigate to project directory
```
cd GenesisResources
```

3.) Run start script with build flag
```
./start.sh -s development -b
```
```
./start.sh -s production -b
```

4.) Choose password for chosen profile database or let script generate one using (openssl base64 12)
```
Enter new database password for .env.<profile> (generate):
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
- Implement frontend
- Implement Docker/GitHub secrets for database credentials
- Implement GitHub actions for CI/CD