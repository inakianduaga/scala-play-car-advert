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

For example, to view all items in a table, use

```js
var params = {
    TableName: "ScalaPlayCarAdverts"
};

docClient.scan(params, function(err, data) {
    if (err)
        console.log(JSON.stringify(err, null, 2));
    else
        console.log(JSON.stringify(data, null, 2));
});
```