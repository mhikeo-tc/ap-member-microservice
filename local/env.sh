export AUTH_DOMAIN="topcoder-dev.com"

# IP to which the docker container ports are mapped
export IP="192.168.99.100"
export DOCKER_IP="$IP"

# OLTP database config
export OLTP_USER="informix"
export OLTP_PW="1nf0rm1x"
export OLTP_URL="jdbc:informix-sqli://local.topcoder-dev.com:2021/tcs_catalog:INFORMIXSERVER=informixoltp_tcp;IFX_LOCK_MODE_WAIT=5;OPTCOMPIND=0;STMT_CACHE=1;"

# DW database config
export DW_USER="informix"
export DW_PW="1nf0rm1x"
export DW_URL="jdbc:informix-sqli://local.topcoder-dev.com:2021/tcs_catalog:INFORMIXSERVER=informixoltp_tcp;IFX_LOCK_MODE_WAIT=5;OPTCOMPIND=0;STMT_CACHE=1;"

# DynamoDB configuration
export DYNAMODB_URL="http://local.topcoder-dev.com:7777"

# Zookeeper and Kafka
export ZOOKEEPER_HOSTS_LIST="local.topcoder-dev.com:2181"

# Elastic Search URL
export ELASTIC_SEARCH_URL="http://local.topcoder-dev.com:9200"
export MEMBER_ES_INDEX="members"

export TC_JWT_KEY="secret"

export DB_CONNECTION="jdbc:informix-sqli://$DOCKER_IP:2021/tcs_catalog:INFORMIXSERVER=informixoltp_tcp;"
export DB_USER="informix"
export DB_PASSWORD="1nf0rm1x"
export ES_HOST_PORT=$DOCKER_IP:9200
export DYNAMODB_URL=http://$DOCKER_IP:7777
export DYNAMO_STREAMS_URL=http://$DOCKER_IP:7777
export REDIS_HOST=$DOCKER_IP:6379
export CONF_FILENAME="members-es"