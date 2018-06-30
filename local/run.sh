source env.sh
docker-compose up -d

# you may have to make sure  documents are indexed successfully by waiting longer time
#sleep 250

#java -jar ../service/target/member-microservice*.jar server $PWD/../service/src/main/resources/member-service.yaml

#docker-compose down
