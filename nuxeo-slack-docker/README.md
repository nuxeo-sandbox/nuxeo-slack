# nuxeo-slack Docker Image

This module is responsible for building the nuxeo-slack Docker image.


It's possible to skip Docker build by setting default `skipDocker` property value to `true` in `pom.xml` file.

```bash
# Skipping Docker build
mvn -DskipDocker=true clean install
```

## Test

Set the app bot token in nuxeo_conf/nuxeo.conf

Start the container

Use the following curl command to test that the integration is working correctly

```curl
curl --location --request POST 'http://localhost:8080/nuxeo/site/api/v1/automation/Notification.SendSlackNotification' \
--header 'Authorization: Basic QWRtaW5pc3RyYXRvcjpBZG1pbmlzdHJhdG9y' \
--header 'Content-Type: application/json' \
--data-raw '{
    "params": {
        "channel":"general",
        "message":"Salut!!!"
    }
}'
```
