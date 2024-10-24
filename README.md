# Guesser Microservice Application

## Description
The **Guesser Project** is a microservice-based application built with the Java Spring framework. It is designed to provide highly scalable and secure guessing game functionality. The project leverages several modern technologies and tools including:
- **Spring Boot** for rapid application development.
- **Docker** and **Kubernetes** for containerization and orchestration.
- **Apache Kafka** for services communication
- **Spring Security** for authentication and authorization.
- **Spring Cloud** for microservice infrastructure management.
- **API Gateway** for routing and load balancing.
- Hosted on **AWS** infrastructure with services like EKS
- **PostgreSQL** as the main database.
- **Redis** for caching and optimized performance.

---

## Table of Contents
1. [Project Description](#description)
2. [Tech Stack](#tech-stack)
3. [Features](#features)
4. [Installation](#installation)
5. [Deployment](#deployment)
6. [API Documentation](#api-documentation)
7. [Contact](#contact)
8. [Licence](#licence)

---

## Tech Stack
- **Backend**: Java, Spring Boot, Spring Cloud, Spring Security
- **Database**: PostgreSQL
- **Cache**: Redis
- **Message Broker**: Apache Kafka
- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **API Gateway**: Spring Cloud Gateway
- **Cloud Hosting**: AWS (Amazon Web Services)
## Features
- **Authentication & Authorization**: Secure authentication with Spring Security, JWT tokens.
- **API Gateway**: Centralized API Gateway for routing requests to microservices.
- **Microservice Architecture**: Built using Spring Cloud to handle service discovery, load balancing, and fault tolerance.
- **Database Support**: Integrated with PostgreSQL for persistent data storage.
- **Services communication**: Uses Apache Kafka for consisten and robust services communication
- **Caching**: Uses Redis for caching frequently accessed data to improve performance.
- **Containerization**: The application is containerized using Docker for easy deployment.
- **Kubernetes Orchestration**: Supports Kubernetes for scaling and managing services in production environments.
- **AWS Hosting**: The application is hosted on AWS, leveraging services like EKS and RDS.

---

## Installation

### Requirements
Make sure the following dependencies are installed on your system:
- **Java 21**
- **Maven**
- **Docker** and **Docker Compose**
- **Kubernetes**
- **AWS CLI** (if deploying on AWS)

### Installation Guide

1. **Clone the repository**:
```bash
git clone
``` 
2. **Navigate to root folder**:
```bash
cd guesser-microservice
```
3. **Create .env file from .env-example**
```bash
cp .env-example .env
```
4. **Fill in the .env file with data**

---

## Deployment

### Docker Deployment Guide
1. **In the root directory run the following commands**:
```bash
docker-compose --build

docker-compose up
```

---

### Kubernetes Deployment Guide
1. **Navigate to Kubernetes folder**: 
```bash
cd ./kubernetes
```
2. **Specify kubernetes secret variables**

3. **Deploy pods in the following order**: 
```bash
kubectl apply -f ./secrets

kubectl apply -f ./databases

kubectl apply -f ./message-broker

kubectl apply -f ./services
```

## API Documentation

### Overview
The Guesser API is designed to route all requests through an **API Gateway** for centralized handling of authentication, routing, and load balancing. Users should direct all requests to the API Gateway URL provided below.

**Base URL (API Gateway)**:  
- **Local**: `http://localhost:<gateway-port>`  
- **Production**: `http://api-gateway`

All services (game, authentication) are exposed via the gateway, and individual microservices should not be accessed directly.

### Endpoints

#### Authentication
1. **Sign up**: 
`POST /auth/signup`

**Description**:  
Create a new user

**Request**:
- **Method**: `POST`

**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    "gameId": "12345",
    "message": "Game started. Make your guess."
  }
```
**Error responses**:
- 403 Forbidden: User exists, but not activated
- 401 Unauthorized: User already exists or bad credentials

2. **Log in**: 
`GET /auth/login`

**Description**:  
Log in existing account

**Request**:
- **Method**: `GET`

**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    <jwt-token>
  }
```
**Error responses**:
- 403 Forbidden: User exists, but not activated
- 401 Unauthorized: Bad credentials
- 404 Not Found: User with given credentials doesn't exist

3. **Refresh token**: 
`GET /auth/refresh-token`

**Description**:  
Refresh access token

**Request**:
- **Method**: `GET`
- **Headers**:
  - `Authorization`: `Bearer <your-token>`
  - `refreshToken`: `<your-refresh-token>`

**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    <new-access-token>
  }
```
**Error responses**:
- 401 Unauthorized: Cookie or value is missing
4. **Confirm mail**: 
`GET /auth/refresh-token`

**Description**:  
Confirm Mail

**Request**:
- **Method**: `GET`
- **Request Parameters**:
    - token (String): Mail confirmation token
**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    Email has been successfully confirmed.
  }
```

#### Game

1. **Start game**: 
`GET /game/start`

**Description**:  
Start new game

**Request**:
- **Method**: `POST`
- **Headers**:
  - `Authorization`: `Bearer <your-token>`
  - `X-Game-User-Id`: `<your-id>`
- **Body**:
```json
topic(String): Topic for the riddle
```
**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    Riddle 
  }
```
**Error responses**:
- 401 Unauthorized: Access token is missing
- 400 Bad Request: Invalid topic provided

2. **Guess**: 
`GET /game/{gameId}/start`

**Description**:  
Guess a riddle

**Request**:
- **Method**: `POST`
- **Headers**:
  - `Authorization`: `Bearer <your-token>`
  - `X-Game-User-Id`: `<your-id>`
- **Body**:
- **URL Parameters**:
    - gameId (Long) : Game Id 
```json
Guess(String): Guess for the riddle
```
**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    "message": "You won",
    "hint": "null",
    "gameSattus": "WON" 
  }
```
**Error responses**:
- 401 Unauthorized: Access token is missing
- 404 Not Found: Game not found
- 400 Bad Request: Game has already ended

3. **Get games**: 
`GET /game/games`

**Description**:  
Confirm Mail

**Request**:
- **Method**: `GET`
- **Headers**:
  - `Authorization`: `Bearer <your-token>`
  - `X-Game-User-Id`: `<your-id>`
**Response**:
- **Status**: `200 OK`
- **Body**:
```json
  {
    {
        "id": "1",
        "hints":[
            {
                "hint": "I have four legs"
            },
            {
                "hint": "I bark"
            }
        ],
        "answer": "Dog",
    }
  }
```
**Error responses**:
- 401 Unauthorized: Access token is missing

---

## Contact

If you have any questions, issues, or suggestions regarding this project, feel free to reach out:

- **Email**: jarik012005@gmail.com
- **GitHub Issues**: [Click on me](https://github.com/mstitel-0/Guesser-game-application/issues)

---

## Licence

This project is licensed under the **MIT License**. You are free to use, modify, and distribute this software as long as you include the original copyright and license notice in any copies of the software.

See the [LICENSE](./LICENSE) file for more details
