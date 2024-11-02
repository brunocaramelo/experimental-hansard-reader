# Hansard Reader
Microservice for reading data from the [British Hansard API](http://www.data.parliament.uk/dataset), transforming and
publishing the results on Apache Kafka, to later be processed by Apache Spark driver scripts.

## Running the service
Run docker-compose:
```shell script
git clone https://github.com/bgasparotto-engineering/hansard-reader
cd hansard-reader
docker-compose up -d
```

Then run the main class `HansardReaderApplication.java`

## Interacting with the service
1. Produce Kafka messages to `message.scheduler.run-hansard-update` using the schema from `RunUpdate.avsc`;
2. Check the logs where the consumed messages will be processed and new messages published as the result.

### Generating Avro source code
This project uses [Gradle Avro Plugin](https://github.com/davidmc24/gradle-avro-plugin) for generating Java classes for schemas defined in `.avsc`
files:
```shell script
./gradlew generateAvroJava
```
