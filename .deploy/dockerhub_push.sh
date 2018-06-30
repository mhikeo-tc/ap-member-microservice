#!/bin/bash
set -e
# Define script variables
DEPLOY_DIR="$( cd "$( dirname "$0" )" && pwd )"
WORKSPACE=$PWD
ENV=$1
TAG_SUFFIX=$2
TAG="$ENV.$TAG_SUFFIX"
ENV_JAVA_PARAMS=' '

# Production specific Java process arguments
if [ "$ENV" = "PROD" ]; then
  ENV_JAVA_PARAMS='-Xmx3g -Dnewrelic.environment=production -javaagent:$NEWRELIC_JAR'
fi

DOCKER_REPO=appiriodevops/ap-member-microservice

cd $DEPLOY_DIR/docker

echo "Generating Dockerfile"
cat Dockerfile.template | sed -e "s/@ENV_JAVA_PARAMS@/$ENV_JAVA_PARAMS/g" > Dockerfile

echo "Generate sumo config files"
cat sumo.conf.template | sed -e "s/@env@/${ENV}/g" > sumo.conf
cat sumo-sources.json.template | sed -e "s/@env@/${ENV}/g" > sumo-sources.json

echo "Copying deployment files to docker folder"
cp $WORKSPACE/service/target/member-microservice*.jar member-microservice.jar
cp $WORKSPACE/service/src/main/resources/member-service.yaml member-service.yaml

echo "Logging into docker"
echo "############################"
docker login -e $DOCKER_EMAIL -u $DOCKER_USER -p $DOCKER_PASSWD

echo "Building docker image $DOCKER_REPO:$TAG"
docker build -t $DOCKER_REPO:$TAG .

echo "Pushing image to docker $DOCKER_REPO:$TAG"
docker push $DOCKER_REPO:$TAG

