# Spring SSE Demo


### Requirements
- Java 11
- Gradle
- Docker compose

### Env variables
- `AWS_CBOR_DISABLE=1`


### Sending a message to Kinesis from Local Stack

`aGV5IHRoaXMgY2FtZSBmcm9tIGtpbmVzaXM=` is a base64 encoded string that stands for `hey this came from kinesis`

```bash
aws kinesis put-record --stream-name sse-demo-stream --endpoint-url http://localhost:4568 --data aGV5IHRoaXMgY2FtZSBmcm9tIGtpbmVzaXM= --partition-key test-key
```

The output should show:
```bash
Message received from Kinesis: hey this came from kinesis
```

### Receiving messages from API

```bash
curl -X GET http://localhost:8080/notifications/stream
```

As the application receives Kinesis records, it will be streaming them to the endpoint

```
data:{"id":"e26aaf68-1f3c-4fca-925e-69cf99e52f50","message":"hey this came from kinesis"}

data:{"id":"c98ef315-10e6-42f0-b055-372c544a40a9","message":"hey this came from kinesis"}
```
