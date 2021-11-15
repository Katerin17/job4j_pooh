# MultiThreadServer
***Analogue of RabbitMQ***

The application launches Socket and waits for clients.

Clients can be of two types: publisher and subscriber.

Server has two modes: queue and topic.

### Queue

The publisher sends a request to add data to the specified queue (for example, "weather") with a parameter value (for example, "temperature = 18"). The message is placed at the end of the queue (weather). If there is no queue in the service, then it must be created.

The subscriber sends request to get data from the queue. The message is poll from the head of the queue.

If there are requests from multiple subscribers for the same queue, they receive messages in the order.

Each message in the queue can only be got by one subscriber.

### Topic

The publisher sends a request to add data to the specified topic (for example, "weather") with a parameter value (for example, "temperature = 18"). The message is placed at the end of each individual queue of each subscriber (if this topic is in it's subscriptions). If there is no topic in the service, then the data is ignored.

The subscriber sends request to get data from the specified topic. The message is poll from the head of the subscriber's individual queue. If there is no topic in the service, then it must be created.

When receiving data from a topic for the first time, an individual empty queue is created for the subscriber.

Thus, each subscriber has its own unique queue with data.
