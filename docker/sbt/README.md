SBT Dockerfile
==============

> Dockerfile with SBT as entrypoint

### Server

To run the dev server inside the docker container, run 

```sh
docker run -it --rm -v $PWD:/root -p 80:9000 --name CarAdvertsServer sbt run
```

you can also optionally specify an environment file where configuration can be read from using `--env-file=PATH_TO_FILE` command
