# Customer API

Welcome to the **Customer API** project! This project provides a RESTful API for managing customer data. It is built
using the Spring Boot framework and includes features such as data persistence, API endpoints for CRUD operations, and
integration with a PostgreSQL database.

## Technologies Used

- **Spring Boot 3.1.5:** The foundation of the application, providing a robust and flexible framework for Java
  development.
- **PostgreSQL:** The chosen relational database for storing customer data.
- **Spring Data JPA:** Simplifies the data access layer, making it easy to interact with the database.
- **Lombok:** Reduces boilerplate code with annotations for generating getters, setters, and other common methods.
- **Flyway:** Manages database migrations, ensuring smooth updates to the schema.
- **SnakeYAML:** A YAML parser and emitter for Java, used for configuration.
- **JavaFaker:** Generates realistic fake data for testing purposes.
- **Spring Web:** Provides support for building RESTful web services.
- **TestContainers:** Enables integration testing with Docker containers for dependencies such as PostgreSQL.
- **JUnit Jupiter:** The testing framework for writing and running tests.

## Project Structure

The project is organized into the following main components:

- **Customer Entity:** Represents the customer data model with JPA annotations.
- **Customer Controller:** Defines RESTful endpoints for managing customer data.
- **Customer Service:** Implements business logic and interacts with the data access layer.
- **Data Access Services (JDBC and JPA):** Implement the data access interface and handle database operations.
- **Database Configuration:** Contains configuration files for the PostgreSQL database.
- **Docker Configuration:** Includes Docker Compose files for local development and an AWS Elastic Beanstalk Dockerrun
  file for deployment.

## Getting Started

1. Ensure you have Java 17 installed.
2. Set up a PostgreSQL database and update the `application.yml` file with the appropriate connection details.
3. Run the application using Maven: `mvn spring-boot:run`.
4. Access the API at [http://localhost:8080/api/v1/customers](http://localhost:8080/api/v1/customers).

## Building and Packaging

The project uses Maven for building and packaging. To build the project, run:

```bash
mvn clean install
```

To create a Docker image using JIB, run:

```bash
mvn jib:build
```

## Running Tests

The project includes unit tests and integration tests. Run the tests using:

```bash
mvn test
```

## Docker Compose for Development

For local development, a Docker Compose file (`docker-compose.ym`l) is provided. It starts a PostgreSQL container and the application container. Run:

```bash
docker-compose up
```
## AWS Elastic Beanstalk Deployment

For deployment to AWS Elastic Beanstalk, a Dockerrun file (`Dockerrun.aws.json`) is provided. Update the `SPRING_DATASOURCE_URL` environment variable before deploying.

## Contributing

Contributions are welcome! Feel free to open issues or pull requests.