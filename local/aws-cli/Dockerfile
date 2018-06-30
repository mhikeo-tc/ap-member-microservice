FROM garland/aws-cli-docker

WORKDIR /data

COPY init-dynamodb.sh .
COPY member-profile.json .
COPY member-stats.json .

COPY config /root/.aws/
COPY credentials /root/.aws/
RUN chmod +x ./init-dynamodb.sh

CMD ./init-dynamodb.sh
