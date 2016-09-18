#!/bin/bash

set -o nounset
set -o errexit
set -o pipefail

# Enable job control
# http://stackoverflow.com/questions/690266/why-cant-i-use-job-control-in-a-bash-script
set -m

# Launch dynamoDB in background
java -Djava.library.path=. -jar DynamoDBLocal.jar -inMemory --sharedDb -port 8000 &

# Provision de dynamoDB table
aws dynamodb create-table \
    --table-name $TABLE \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
	--key-schema AttributeName=id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=1,WriteCapacityUnits=1 \
    --region=eu-west-1 \
    --endpoint=http://localhost:8000

# Bring dynamoDB process to the foreground
fg