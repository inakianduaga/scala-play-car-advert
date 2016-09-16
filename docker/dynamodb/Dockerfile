FROM java:7
MAINTAINER Inaki Anduaga <inaki@inakianduaga.com>
# Based on https://hub.docker.com/r/peopleperhour/dynamodb/~/dockerfile/

RUN mkdir /var/dynamodb_wd
RUN mkdir /var/dynamodb_local
WORKDIR /var/dynamodb_wd

# Download dynamoDB local binaries
RUN wget -q http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz && \
    tar zxvf dynamodb_local_latest.tar.gz && rm dynamodb_local_latest.tar.gz

# install aws cli to interact w/ dynamoDB
RUN apt-get update && \
  apt-get install -y python2.7 && \
  curl "https://bootstrap.pypa.io/get-pip.py" -o "get-pip.py" && \
  python get-pip.py && \
  pip install awscli

# Install script
COPY start.sh /scripts/start.sh

# Default table we create on startup
ENV TABLE ScalaPlayCarAdverts

# Mocked AWS credentials (needed for dynamoDB)
ENV AWS_ACCESS_KEY_ID 123
ENV AWS_SECRET_ACCESS_KEY 123

ENV TERM xterm
EXPOSE 8000

# Default command
CMD ["/scripts/start.sh"]


