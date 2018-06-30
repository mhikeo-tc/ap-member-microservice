# Topcoder - Member Service
Microservice for member profile, stats, financial, skills and external accounts

## Prerequisites

- Java 8 with Maven 3
- Docker and Docker Compose

## General Deployment:
This software is deployed to AWS Elastic Beanstalk by CircleCI.

##Branches

* Any commits to ```dev``` will will trigger a build and deploy to the _dev_ AWS environment
* Any commits to ```master``` will trigger a build and deploy to the _prod_ AWS environment

##Development Flow:
This repo uses typical Gitflow (```feature/[feature name]```, ```hotfix/[fix name]```, ...etc). Generally changes to prod should be merged from dev to master. Hotfixes should be merged to master and dev at the same time.

##Additional Notes

* The _circle.yml_ file controls the build - see this file if you need to confirm if your commit will deploy anything
* Circle-ci builds can be easily cancelled - please do so if you accidentally trigger an undesired build

# Local Setup For Dependent Services
Please follow instructions mentioned in docs/LocalSetup.docx

## Start Microservice

Build And Package:

```
mvn clean compile package
```

Then start the service using the following command in the 'local' folder:

```
./run.sh
```

You may use git bash under windows to run shell script.

## Verification

If the maven build process was successful the unit tests ran without error.

you can use the Postman collections to `docs/` directory to test with the APIs.  

Refer to the Swagger docs for API spec:  https://github.com/appirio-tech/ap-member-microservice/blob/dev/swagger.yaml
