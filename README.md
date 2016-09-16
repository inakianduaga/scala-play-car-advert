scala-play-car-advert
=====================

> Scala Play car advert example application

## Interacting with production app

Use the [Swagger UI official-hosted client](http://petstore.swagger.io/?url=http://host_goes_here_once_deployed/swagger/car/adverts/spec.yml) to test the Rest application in production. 

## Local Setup

The entire application is dockerized and can be run locally easily w/ minimal dependencies.

*Requirements*: [Docker](https://docs.docker.com/engine/installation/), [Docker Compose](https://docs.docker.com/compose/install/) must be installed

From the `./docker` folder
1. Build docker containers for dynamoDB and SBT by running `docker-compose build`
2. Spin up Play app & DynamoDB: `docker-compose up -d`. First run will take a while because SBT needs to populate the cache. App will be reachable on localhost:9000
3. API can be tested through the [Swagger UI](http://petstore.swagger.io/?url=http://localhost:9000/swagger/car/adverts/spec.yml). Local DynamoDB [provides a shell UI](http://localhost:8000/shell/) as well       

## Deployment

TODO

## Notes:

TODO

