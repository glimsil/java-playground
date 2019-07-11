## Simple Java RabbitMQ PoC

This poc uses a rabbitmq broker running on localhost.
Docker may be an option to run the broker locally. To do that, just type:

```
	docker run --name docker-rabbit -p 15672:15672 -p 5672:5672 rabbitmq
```
By default RabbitMQ will listen on port on port 5672 and management UI on 15672.
The default credentials is guest:guest

Run `RabbitmqApplication` to start the consumers (user consumer and transaction consumer). The exchanges, queues and dead letter queues will be created, automatically, if does not exist.
Run `PublisherExample` to publish simple messages to user and transaction queues.
