aws --profile $1 cloudformation update-stack --stack-name ap-member-service-stack --template-body file://`pwd`/ap-member-service-stack.json
sleep 120
aws --profile $1 cloudformation update-stack --stack-name ap-member-service-external-accounts-stack --template-body file://`pwd`/ap-member-service-external-accounts-stack.json
sleep 120
aws --profile $1 cloudformation update-stack --stack-name ap-member-service-external-linkss-stack --template-body file://`pwd`/ap-member-service-external-links-stack.json