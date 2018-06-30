aws --profile $1 cloudformation create-stack --stack-name ap-member-service-stack --template-body file://`pwd`/ap-member-service-stack.json
sleep 120
aws --profile $1 cloudformation create-stack --stack-name ap-member-service-external-accounts-stack --template-body file://`pwd`/ap-member-service-external-accounts-stack.json
sleep 120
aws --profile $1 cloudformation create-stack --stack-name ap-member-service-external-links-stack --template-body file://`pwd`/ap-member-service-external-links-stack.json