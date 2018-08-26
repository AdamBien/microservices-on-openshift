#!/bin/sh
mvn clean package && docker build -t com.airhacks/consumer .
docker rm -f consumer || true && docker run -d -p 8080:8080 -p 4848:4848 --name consumer com.airhacks/consumer 
