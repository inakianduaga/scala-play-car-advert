DynamoDB Local image
=====================

> Runs a local copy of dynamoDB, automatically provisioned with a table upon startup 

## Usage:

```sh
$ docker run --rm -p SOME_PORT:8000 -e "TABLE=SOME_TABLE" dynamodb
```

where the `TABLE` environment variable is used to provision the dynamoDB table upon container startup.

## Shell access

The dynamoDB exposes a shell on http://localhost:PORT/shell/, where port is where the dynamoDB container is listening