# XYZ Hub

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)


XYZ Hub is a RESTful web service for access and management of geo data.

## Getting Started

These instructions will build the project, run the docker image & execute all the integration test cases.

### Compile the project

This will compile the project without running the unit & integration tests

```
mvn -e clean compile
```

### Compile the project with unit test cases

This will compile the project with unit test cases but without integration tests

```
mvn -e clean compile test
```

### Compile the project with integration test cases

This will compile the project with unit test cases & integration tests

Make sure the service is running on localhost:8080 for this integration test to execute.
Refer to docker section for running docker container.

```
mvn -e clean verify
```

Or run the entire life-cycle

```
mvn -e clean install
```

### Compile the project without integration test cases

```
mvn -e clean install -DskipITs
```

### Package the project

This will package the project with unit test cases and generate the deployable artifacts

```
mvn -e clean package
```

### Start the dependent docker container(s)

This will start the dependencies in containers (Redis, Postgres).
If the containers are already running, this will throw away the existing containers and run them again.
```
mvn -e clean install -P docker
```

OR if you want to start XYZ HUB together:
```
mvn -e clean install -P docker -DstartHub=true
```

OR (if the JAR was already built)
```
cd deployment
./deploy.sh
```

mvn release:clean release:prepare release:perform

### Release a new version using:
     
 ```bash
mvn release:clean release:prepare release:perform -DtagNameFormat=@{project.version}
mvn release:prepare -DdryRun=true -DtagNameFormat=@{project.version}
```

## Open Api 3 Generator

The contract-openapi3.yaml is generated from the openapi_src.yaml by using the openapi-converter tool available in:

https://gerrit.it.here.com/#/admin/projects/CommunityPlatform/backend/xyz-tools/openapi-converter

## Deployment

https://devops-deployment.internal.community.nw.ops.here.com/job/Deployment%20Pipelines/job/Middleware%20Pipelines/view/XYZ%20HUB/

## Built With

* [Vertx](http://vertx.io/)
* [Maven](https://maven.apache.org/)

## Code Style Formatters
Use Google Java Code style
Guide: https://google.github.io/styleguide/javaguide.html

Formater templates:
* [Eclipse] (https://raw.githubusercontent.com/google/styleguide/gh-pages/eclipse-java-google-style.xml)
* [IntelliJ] (https://raw.githubusercontent.com/google/styleguide/gh-pages/intellij-java-google-style.xml)


## Search cloud watch logs
Note: The pattern is case sensitive and no regular expression, its just a string:

**Search for an exception or something we need:**

aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1532593451832 --filter-pattern "Exception"
aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1532593451832 --filter-pattern "exception"
aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-prd --start-time 1534889000000 --filter-pattern "uLR169hHzN0jUAlLbY_PnQ"

aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-prd --start-time 1534896000000 --filter-pattern "exception"
aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-prd --start-time 1534896000000 --filter-pattern "exception"


aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1533115000000 --filter-pattern "ZrScUnjiamVo00000cta"
aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1533120187021 --log-stream-name '2018/08/01/[$LATEST]c58d317e732243ddb1e329d1f37d93dc'


**When we found an event we can search for that event, update the timestamp to the timestamp of the event:**

aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1532593519185 --log-stream-name '2018/07/26/[$LATEST]6ece4b66e5ed4f7ab092cfbe9dcc0c25'

**Note**: You may think that the **eventId** allows you to search for all log entries of that request, but the **eventId** is different for every single log line!

aws logs filter-log-events --profile prd --log-group-name /aws/lambda/xyz-psql-java-cit --start-time 1535827280000 --end-time 1535827283600 --filter-pattern "AQLRu2LnQ94w000000Va"



Links
	- https://currentmillis.com/
	- https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/FilterAndPatternSyntax.html


	- https://docs.aws.amazon.com/AmazonCloudWatchLogs/latest/APIReference/API_FilterLogEvents.html
	- https://docs.aws.amazon.com/AmazonCloudWatch/latest/logs/SearchDataFilterPattern.html

