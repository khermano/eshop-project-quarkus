# eshop-project-quarkus

- available at: https://github.com/khermano/eshop-project-quarkus/tree/test_app
- this project is edited for load testing

This repository contains a migration of the backend of the monolithic application to the microservice
architecture with the Quarkus framework.

This migration was done for the master thesis, which can be found here:
https://is.muni.cz/auth/rozpis/tema?fakulta=1433;balik=58;tema=418130;uplne_info=1.

The most relevant part of the project we migrated can be found here:
https://github.com/khermano/433511_Master_thesis_project.

The original project without changes can be found here:
https://github.com/fi-muni/PA165

## **Prerequisites**
- Java 21
- Maven 3.9.6
- newest Docker, or Podman 4.9.4 with podman-docker (see Docker/Podman )

## About the app

- every service of this repository has to be up and running for the application to work correctly as well as Consul 
  which is running in Docker or Podman
- you can see if all services are up and running at http://localhost:8500/ui/
- application endpoints will then be available at http://localhost:8080/eshop-rest
- the application consists of 1 Docker image and 5 microservices:

|        Service        |    Port     |
|:---------------------:|:-----------:|
| Consul (Docker image) | 8500 & 8501 |
|     user-service      |    8091     |
|   category-service    |    8092     |
|    product-service    |    8093     |
|     order-service     |    8094     |
|  api-gateway-service  |    8080     |

## Docker/Podman

- before you run the script for the first time, to be sure you have the right version of Consul
- `docker pull consul:1.7`
  - this command download and Consul image (choose 'docker.io/library/consul')
- `docker run -d --rm --name consul -p 8500:8500 -p 8501:8501 consul:1.7 agent -dev -ui -client=0.0.0.0 -bind=0.0.0.0 --https-port=8501`
    - to start the consul image
- `docker kill consul`
    - to stop the consul image

#### Podman:
- if you are having Podman, you can install podman-docker package to use docker aliases
- after that you should be able to use Docker commands in Podman 

## **Start application**

- you can use one of the two starting scripts available in /scripts directory
- or you can use docker-compose.yml file

### Run the script to start the application locally:

- try your Consul image for the first time to be sure you have it available (see Docker/Podman section)
- `cd eshop-project-quarkus/scripts/`
- `./start_app.sh` or
- `./start_app_with_tests.sh`
- wait for the message "All services available."
- application endpoints are now available at http://localhost:8080/eshop-rest/ (see Swagger UI)

### Stop the script:
- press any button and wait for "Application shutdown completed."

### Start app using docker-compose.yml:

- `cd eshop-project-quarkus/`
- `docker compose up -d --build`
    - this command build images and then in background start containers defined in a docker-compose.yml file, detaching it from the current shell
- `docker compose down`
    - this command stops and removes containers created by docker-compose.yml

### Individual services:

*Run Consul image (Consul server) [port 8500 & 8501]:*
- `docker run -d --rm --name consul -p 8500:8500 -p 8501:8501 consul:1.7 agent -dev -ui -client=0.0.0.0 -bind=0.0.0.0 --https-port=8501`
  - to start the consul image
- `docker kill consul`
  - to stop the consul image

*Build and run user-service [port 8091]:*
- `cd eshop-project-quarkus/user-service/`
- `mvn clean install`
- `java -jar target/quarkus-app/quarkus-run.jar`

*Build and run categoryService [port 8092]:*
- `cd eshop-project-quarkus/category-service/`
- `mvn clean install`
- `java -jar target/quarkus-app/quarkus-run.jar`

*Build and run productService [port 8093]:*
- `cd eshop-project-quarkus/product-service/`
- `mvn clean install`
- `java -jar target/quarkus-app/quarkus-run.jar`

*Build and run orderService [port 8094]:*
- `cd eshop-project-quarkus/order-service/`
- `mvn clean install`
- `java -jar target/quarkus-app/quarkus-run.jar`

*Build and run apiGateway [port 8080]:*
- `cd eshop-project-quarkus/api-gateway-service/`
- `mvn clean install`
- `java -jar target/quarkus-app/quarkus-run.jar`

## Development

### Quarkus Dev Mode

- Quarkus is providing us with great development mode
- use `./mvnw compile quarkus:dev` in any project you want to develop
- more info can be found e.g.: https://www.youtube.com/watch?v=Ewpk5kGfbko&ab_channel=SebastianDaschner and
  https://quarkus.io/guides/dev-mode-differences

## Endpoints info

- you can see if all services are up and running at http://localhost:8500/ui/
- all endpoints are available with Swagger UI at http://localhost:8080/eshop-rest/ (see Swagger UI)
- you can use a verification script to make all example calls (the script should be executed once per one application run to work properly because of POST methods)
    - `cd eshop-project-quarkus/scripts/`
    - `./verification.sh`

**Users**

- **GET http://localhost:8080/eshop-rest/users**
    - *returns all users*
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/users" | jq`


- **GET http://localhost:8080/eshop-rest/users/{id}**
    - *returns user according to id*
    - **parameters**:
        - id:
            - id of the user
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/users/1" | jq`

**Categories**

- **GET http://localhost:8080/eshop-rest/categories**
    - *returns all categories*
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/categories" | jq`


- **GET http://localhost:8080/eshop-rest/categories/{id}**
    - *returns category according to id*
    - **parameters**:
        - id:
            - id of the category
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/categories/1" | jq`

**Orders**
- **GET http://localhost:8080/eshop-rest/orders**
    - *returns all orders according to the given parameters*
    - **parameters**:
        - status:
            - Available values :
                - ALL: return all orders
                - RECEIVED: orders with StateOrder RECEIVED
                - CANCELED: orders with StateOrder CANCELED
                - SHIPPED: orders with StateOrder SHIPPED
                - DONE: orders with StateOrder DONE
        - last_week:
            - Available values :
                - true - returns orders by status created in last 7 days
                - false - returns all orders defined by status
    - e.g.:
        - `curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL" | jq`
        - `curl -X GET "http://localhost:8080/eshop-rest/orders?status=ALL&last_week=TRUE" | jq`


- **GET http://localhost:8080/eshop-rest/orders/by_user_id/{user_id}**
    - *returns all orders created by a user with the given id*
    - **parameters**:
        - user_id:
            - id of the user
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/orders/by_user_id/1" | jq`


- **GET http://localhost:8080/eshop-rest/orders/{id}**
    - *returns order with given id*
    - **parameters**:
        - id:
            - id of the order
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/orders/1" | jq`


- **POST http://localhost:8080/eshop-rest/orders/{order_id}**
    - *perform one action on the order, either canceling, shipping, or finishing the order*
    - *the only allowed changes of state are: RECEIVED -> CANCELED, RECEIVED -> SHIPPED, SHIPPED -> DONE*
    - **parameters**:
        - order_id:
            - id of the order
        - action:
            - Available values:
                - CANCEL: RECEIVED -> CANCELED
                - SHIP: RECEIVED -> SHIPPED
                - FINISH: SHIPPED -> DONE
    - e.g.: `curl -X POST "http://localhost:8080/eshop-rest/orders/2?action=FINISH" | jq`


**Products**

- **GET http://localhost:8080/eshop-rest/products**
    - *returns all products*
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products" | jq`


- **GET http://localhost:8080/eshop-rest/products/{id}**
    - *returns the product with the given id*
    - **parameters**:
        - id:
            - id of the product
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/1" | jq`


- **DELETE http://localhost:8080/eshop-rest/products/{id}**
    - *deletes a product with the given id*
    - **parameters**:
        - id:
            - id of the product
    - e.g.: `curl -X DELETE "http://localhost:8080/eshop-rest/products/1" | jq`


- **POST http://localhost:8080/eshop-rest/products/create**
    - *create a new product*
    - **request body**:
        - image:
            - image file
        - imageMimeType (required if the image is added):
            - image type
        - name (required, must be unique):
            - name of the product
        - description:
            - product description
        - color:
            - Available values: BLACK, WHITE, RED, GREEN, BLUE, ORANGE, YELLOW, AZURE, MAGENTA, BROWN, PINK, GREY, UNDEFINED
        - price (required):
            - price of one piece of product
        - currency (required):
            - Available values: CZK, EUR, USD
        - categoryId (required):
            - id of category that exists
    - e.g.: `curl -X POST -H "Content-Type: application/json" --data '{"name":"test","description":"test","color":"UNDEFINED","price":"200", "currency":"CZK", "categoryId":"1"}' "http://localhost:8080/eshop-rest/products/create" | jq`


- **PUT http://localhost:8080/eshop-rest/products/{id}**
    - *update the price for one product (It is not allowed to change the price by more than 10%!)*
    - **parameters**:
        - id:
            - id of product to be updated
    - **request body**:
        - value (required):
            - It is not allowed to change the price by more than 10%!
        - currency (required):
            - Available values: CZK, EUR, USD
    - e.g.: `curl -X PUT -H "Content-Type: application/json" --data '{"value":"16.33","currency":"CZK"}' "http://localhost:8080/eshop-rest/products/4" | jq`


- **POST http://localhost:8080/eshop-rest/products/{id}/categories**
    - *adds a new category to the product*
    - **parameters**:
        - id (required):
            - id of the product to be updated
    - **request body**:
        - id (required):
            - id of existing category we want to add to the product
    - e.g.: `curl -X POST -H "Content-Type: application/json" --data '5' "http://localhost:8080/eshop-rest/products/2/categories" | jq`
    - **original project request body**:
        - *it was changed because only the ID parameter of the required body is used for adding a category*
            - id (required):
                - id of the existing category we want to add to the product
            - name:
                - name of the existing category we want to add to the product (we need only ID of existing category we want to add)
    - e.g.: in the original project: `curl -X POST -i -H "Content-Type: application/json" --data '{"id":"5","name":"Presents"}' http://localhost:8080/eshop-rest/products/2/categories`


- **GET http://localhost:8080/products/{id}/currentPrice**
    - *not part of the original project!*
    - *get the current price of the product with the given id*
    - **parameters**:
        - id (required):
            - id of the product
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/2/currentPrice" | jq`


- **GET http://localhost:8080/products/getCurrencyRate/{currency1}/{currency2}**
    - *not part of the original project!*
    - *get the currency rate for a given currency pair*
    - **parameters**:
        - currency1 (required):
            - Available values: CZK, EUR, USD
        - currency2 (required):
            - Available values: CZK, EUR, USD
    - e.g.: `curl -X GET "http://localhost:8080/eshop-rest/products/getCurrencyRate/CZK/EUR" | jq`

## Swagger UI

- is available at http://localhost:8080/eshop-rest/
- allows to visualize and interact with the API's resources without having any of the implementation logic in place
- more info here: https://swagger.io/tools/swagger-ui/?ref=the-best-recipe

## Health info details

- the status of individual services can be also seen on the Consul server: http://localhost:8500/ui/
- application uses SmallRye Health to provide information about the health of the services
- more details about the health of the application can be found on endpoints:
    - user-service: http://localhost:8091/q/health
    - category-service: http://localhost:8092/q/health
    - product-service: http://localhost:8093/q/health
    - order-service: http://localhost:8094/q/health
    - api-gateway-service: http://localhost:8080/eshop-rest/q/health

## For testing
- you only need to run:
    - `cd eshop-project-springboot/scripts/`
    - `./start_app.sh` or
  - `./start_app_with_tests.sh`

- run Postgres services in Docker/Podman individually:
    - for user-service [port 5431]: `docker run --rm   --name dev-postgres-user-quarkus  -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=user-quarkus  -p 5431:5432  postgres`
    - for category-service [port 5432]: `docker run --rm   --name dev-postgres-category-quarkus  -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=category-quarkus  -p 5432:5432  postgres`
    - for product-service [port 5433]: `docker run --rm   --name dev-postgres-product-quarkus  -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=product-quarkus  -p 5433:5432  postgres`
    - for order-service [port 5434]: `docker run --rm   --name dev-postgres-order-quarkus  -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=order-quarkus  -p 5434:5432  postgres`

## Locust
- quick easy instruction can be found here https://www.youtube.com/watch?v=SOu6hgklQRA&ab_channel=NicolaiGram
- install it: `pip install locust`
- start it: `locust -f ~/path_to_your_directory/eshop-project-quarkus/scripts/locust/locust.py`
- use it on http://localhost:8089