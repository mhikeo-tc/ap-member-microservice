# Create the MemberProfile table
aws dynamodb create-table --table-name MemberProfile --attribute-definitions AttributeName=userId,AttributeType=N AttributeName=handleLower,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH --global-secondary-indexes '[{"IndexName":"handleLower-index","KeySchema":[{"AttributeName":"handleLower","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Loads member profile data
aws dynamodb batch-write-item --request-items file://member-profile.json --endpoint-url http://$IP:7777

# Create RatingsDistribution table
aws dynamodb create-table --table-name RatingsDistribution --attribute-definitions AttributeName=track,AttributeType=S AttributeName=subTrack,AttributeType=S --key-schema AttributeName=track,KeyType=HASH AttributeName=subTrack,KeyType=RANGE --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create Externals.Accounts table
aws dynamodb create-table --table-name Externals.Accounts --attribute-definitions AttributeName=userId,AttributeType=S AttributeName=synchronizedAt,AttributeType=N AttributeName=accountType,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH AttributeName=accountType,KeyType=RANGE --global-secondary-indexes '[{"IndexName":"synchronizedAt-index","KeySchema":[{"AttributeName":"synchronizedAt","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create External.Links table
aws dynamodb create-table --table-name Externals.Links --attribute-definitions AttributeName=key,AttributeType=S AttributeName=synchronizedAt,AttributeType=N AttributeName=userId,AttributeType=N --key-schema AttributeName=userId,KeyType=HASH AttributeName=key,KeyType=RANGE --global-secondary-indexes '[{"IndexName":"synchronizedAt-index","KeySchema":[{"AttributeName":"synchronizedAt","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create MemberStatsHistory table
aws dynamodb create-table --table-name MemberStatsHistory --attribute-definitions AttributeName=handleLower,AttributeType=S AttributeName=userId,AttributeType=N --key-schema AttributeName=userId,KeyType=HASH --global-secondary-indexes '[{"IndexName":"handleLower-index","KeySchema":[{"AttributeName":"handleLower","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create MemberEnteredSkills table
aws dynamodb create-table --table-name MemberEnteredSkills --attribute-definitions AttributeName=handleLower,AttributeType=S AttributeName=userId,AttributeType=N --key-schema AttributeName=userId,KeyType=HASH --global-secondary-indexes '[{"IndexName":"handleLower-index","KeySchema":[{"AttributeName":"handleLower","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create MemberAggregatedSkills table
aws dynamodb create-table --table-name MemberAggregatedSkills --attribute-definitions AttributeName=handleLower,AttributeType=S AttributeName=userId,AttributeType=N --key-schema AttributeName=userId,KeyType=HASH --global-secondary-indexes '[{"IndexName":"handleLower-index","KeySchema":[{"AttributeName":"handleLower","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create MemberStats table
aws dynamodb create-table --table-name MemberStats --attribute-definitions AttributeName=handleLower,AttributeType=S AttributeName=userId,AttributeType=N --key-schema AttributeName=userId,KeyType=HASH --global-secondary-indexes '[{"IndexName":"handleLower-index","KeySchema":[{"AttributeName":"handleLower","KeyType":"HASH"}],"Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Create MemberProfileTrait table
aws dynamodb create-table --table-name MemberProfileTrait --attribute-definitions AttributeName=userId,AttributeType=N AttributeName=traitId,AttributeType=S --key-schema AttributeName=userId,KeyType=HASH  AttributeName=traitId,KeyType=RANGE --global-secondary-indexes '[{"IndexName":"traitId-index","KeySchema":[{"AttributeName":"traitId","KeyType":"HASH"}], "Projection":{"ProjectionType":"ALL"}, "ProvisionedThroughput": {"ReadCapacityUnits": 2, "WriteCapacityUnits": 2}}]' --region us-east-1 --provisioned-throughput ReadCapacityUnits=4,WriteCapacityUnits=2 --endpoint-url http://$IP:7777

# Loads member stats data
aws dynamodb batch-write-item --request-items file://member-stats.json --endpoint-url http://$IP:7777
