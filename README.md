# Genesis resources

RESTful API that manages registration system for users

## Table of contents

- [Overview](#overview)
- [Stack](#stack)
- [Installation](#installation)
- [Usage](#usage)
- [API](#api)

## Overview

Project is based on simple RESTful API built on SpringBoot framework. API 
allows client to perform CRUD operations on users. User can be created, 
read, updated or deleted. Backend can be run in 4 modes(profiles):
- development(dev) - using PostgreSQL database and simple credentials
- production(prod) - using ready to use PostgreSQL database and settings
- test(test) - using H2 database
- no database(nodb) - does not run with any database

prod profile is used by default

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

1.) Clone repository
```
git clone https://github.com/vesely-jiri/GenesisResources
```

2.) Navigate to project directory
```
cd GenesisResources
```

3.) Install dependencies
```
mvn install
```

4.) Run application
```
mvn spring-boot:run
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