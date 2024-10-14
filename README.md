# Manual And Automation API Testing Project 
This project involves testing the [Platzi Fake API](https://fakeapi.platzi.com/) using various tools and technologies including **Postman**, **Newman**, **Jenkins**, and **RestAssured**. The purpose is to automate the testing of API endpoints to ensure their reliability and performance.

[![Java](https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=java&logoColor=white)](https://www.oracle.com/java/)
[![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white)](https://www.postman.com/)
[![Newman](https://img.shields.io/badge/Newman-00BFFF?style=for-the-badge&logoColor=white)](https://github.com/postmanlabs/newman)
[![Jenkins](https://img.shields.io/badge/Jenkins-D24939?style=for-the-badge&logo=jenkins&logoColor=white)](https://www.jenkins.io/)
[![TestNG](https://img.shields.io/badge/TestNG-25A162?style=for-the-badge&logoColor=white)](https://testng.org/doc/)
[![RestAssured](https://img.shields.io/badge/RestAssured-4CAF50?style=for-the-badge&logoColor=white)](https://rest-assured.io/)

## Tools & Technologies
- **Postman**: Used for manual API testing, sending requests, and verifying responses.
- **Newman**: Command-line tool to run Postman collections for API testing automation.
- **Jenkins**: Continuous Integration (CI) server to automatically run API tests on code commits or at scheduled intervals.
- **RestAssured**: Java library for automated testing of REST APIs.


## Test Scenarios

### 1. Postman Testing

Postman is used to manually test the Platzi Fake API. The test scenarios cover the following endpoints:
- **GET /products**: Fetch all products.
- **GET /products/{id}**: Fetch a specific product by ID.
- **POST /products**: Add a new product.
- **PUT /products/{id}**: Update an existing product by ID.
- **DELETE /products/{id}**: Delete a product by ID.

Assertions in Postman check:
- **Status codes** (200, 201, 404, etc.).
- **Response body content** to verify valid product data.
- **Response time** to ensure optimal API performance.

### 2. Newman Automation

**Newman** is used to run Postman collections from the command line or as part of a CI/CD pipeline. This makes it easy to automate tests across different environments.

### 3. Continuous Testing with Jenkins

**Jenkins** automates the test execution process. Using Jenkins, we can configure a pipeline that triggers API tests on code changes or at scheduled intervals. The pipeline runs both Newman and RestAssured tests.

### 4. RestAssured Automation
**RestAssured** is used for writing automated tests in Java. It simplifies validating REST APIs, allowing programmatic assertions on status codes, response bodies, headers, and more.

## Conclusion
This project automates API testing of the Platzi Fake API using Postman, Newman, Jenkins, and RestAssured. With automated testing integrated into CI/CD, we can ensure that the API is consistently reliable and performs well over time.
