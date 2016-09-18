scala-play-car-advert
=====================

[![Build Status][travis-image]][travis-url] [![Coverage Status][coveralls-image]][coveralls-url]

> Scala Play Restful example app w/ AWS DynamoDB integration

This is a small exercise app to demonstrate integration of a Restful service w/ AWS DynamoDB using the Scala Play framework  

## Interacting with production app

Use the [Swagger UI official-hosted client](http://petstore.swagger.io/?url=http://scala-play-car-advert.herokuapp.com/swagger/car/adverts/spec.yml) to test the Rest application in production. 

## Local Setup

The entire application **is dockerized** and can be run locally easily w/ minimal dependencies.

**Requirements:**
 [Docker](https://docs.docker.com/engine/installation/), [Docker Compose](https://docs.docker.com/compose/install/) must be installed
 
**Installation:**

From the `./docker` folder
1. Build docker containers for dynamoDB and SBT by running `docker-compose build`
2. Spin up Play app & DynamoDB: `docker-compose up -d` (first run will take a while because SBT needs to populate the cache). App will be reachable on [http://localhost:9000](http://localhost:9000)
3. API can be tested through the [Swagger UI](http://petstore.swagger.io/?url=http://localhost:9000/swagger/car/adverts/spec.yml) endpoint. Local DynamoDB [provides a shell UI](http://localhost:8000/shell/) as well to query DB directly       

## Tests:

- [x] There are unit tests for the main Model classes / Storage service. 
- [x] Code coverage is generated automatically on Travis and sent to [Coveralls](https://coveralls.io/github/inakianduaga/scala-play-car-advert) for tracking changes / history
- [ ] Currently no integration test (those could be generated by using the Swagger auto-generated client)

## Deployment

[Travis CI](https://travis-ci.org/inakianduaga/scala-play-car-advert/) is used for building and deploying the application on production (also runs tests on PR/Commit/Merges)

- [x] Tagged commits merged to master generate a [GitHub release](https://github.com/inakianduaga/scala-play-car-advert/releases) with the Scala jar
- [x] Production app runs on Heroku, with database running on AWS DynamoDB 

## Task Goals
 
Service should:

- [x]  have functionality to return list of all car adverts;
- [ ]  optional sorting by any field specified by query parameter, default sorting - by **id**; This was only partially implemented due to lack of time, currently sorts by id only
- [x] have functionality to return data for single car advert by id;
- [x] have functionality to add car advert;
- [x] have functionality to modify car advert by id;
- [x] have functionality to delete car advert by id;
- [x] have validation (see required fields and fields only for used cars);
- [x] accept and return data in JSON format
- [x] Service should be able to handle CORS requests from any domain.
- [x] do not use **GlobalSettings** or any globals like **Play.configuration** or **Play.application**, use dependency injection instead.
 
[travis-url]: https://travis-ci.org/inakianduaga/scala-play-car-advert
[travis-image]: https://travis-ci.org/inakianduaga/scala-play-car-advert.svg?branch=master

[coveralls-url]: https://coveralls.io/r/inakianduaga/scala-play-car-advert
[coveralls-image]: https://coveralls.io/repos/inakianduaga/scala-play-car-advert/badge.png
