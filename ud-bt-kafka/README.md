### Install
```sh
brew install kakfa
#output
==> kafka
To start kafka now and restart at login:
  brew services start kafka
Or, if you don't want/need a background service you can just run:
  /usr/local/opt/kafka/bin/kafka-server-start /usr/local/etc/kafka/server.properties
```
### Go to Foler
- Open Finder
- `CMD` + `SHIFT` + `g`
- Enter: `/usr/local/Cellar/kafka/bin`

### Start Zookeeper and Kafka Server
**Terminal 1: Zookeeper (`localhost:2181`)**
```sh
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
```
**Terminal 2: Kafka (`localhost:9092`)**
```sh
kafka-server-start /usr/local/etc/kafka/server.properties
```

### Basic Command
```sh
kafka-topics --list --bootstrap-server localhost:9092

kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic first-topic

kafka-topics --describe --bootstrap-server localhost:9092 --topic first-topic

kafka-console-consumer --bootstrap-server localhost:9092 --topic first-topic

kafka-console-producer --broker-list localhost:9092 --topic first-topic

kafka-topics --delete --bootstrap-server localhost:9092 --topic first-topic    
```                       

**Note:**
For delete add the following in kafka `server.properties`
`delete.topic.enable=true`

### Consume Message using deserialization option
```sh
#https://developer.confluent.io/tutorials/kafka-console-consumer-primitive-keys-values/kafka.html
kafka-console-consumer --topic example --bootstrap-server localhost:9092 \
 --from-beginning \
 --property print.key=true \
 --property key.separator=" : " \
 --max-messages 10 \
 --key-deserializer "org.apache.kafka.common.serialization.StringDeserializer" \
 --value-deserializer "org.apache.kafka.common.serialization.IntegerDeserializer"
#-----------------------
kafka-console-consumer --topic orders --bootstrap-server localhost:9092 --from-beginning --property print.key=true

#Print key and value
kafka-console-consumer --topic orders --bootstrap-server localhost:9092 --from-beginning --property print.key=true  --value-deserializer "org.apache.kafka.common.serialization.IntegerDeserializer"
#output
MacBookPro	10
MacBookPro	10
MacBookPro	10
MacBookPro	10
MacBookPro	10
MacBookPro	10
MacBookPro	10
```

### Confluent Kafka
- https://www.confluent.io/installation/
- Sign up
- Click on Installation Guide for ZIP/TAR
```sh
curl -O https://packages.confluent.io/archive/7.5/confluent-7.5.2.zip
unzip unzip confluent-7.5.2.zip

## Add to PATH
# open .bash_profile (.bashrc or .zshrc)
export CONFLUENT_HOME=$HOME/confluent-7.5.2
export PATH=$PATH:$CONFLUENT_HOME/bin

```

### Start/Stop Confluent Kafka (Requires JDK 8 or 11)
- `confluent local services start`
- `confluent local services stop`
```sh
confluent local services start
#output
    Using CONFLUENT_CURRENT: /var/folders/k4/kb60p3ts7dqd8n5n_zpqlxb80000gn/T/confluent.955162
    Starting ZooKeeper
    ZooKeeper is [UP]
    Starting Kafka
    Kafka is [UP]
==> Error: the Confluent CLI requires Java version 1.8 or 1.11. 
    See https://docs.confluent.io/current/installation/versions-interoperability.html .
    If you have multiple versions of Java installed, you may need to set JAVA_HOME to the version you want Confluent to use.

sdk install java 11.0.21-amzn

confluent local services start
arvins-mac @ ~
 [2] → confluent local services start
 #output
        The local commands are intended for a single-node development environment only, NOT for production usage. See more: https://docs.confluent.io/current/cli/index.html
        As of Confluent Platform 8.0, Java 8 will no longer be supported.

        Using CONFLUENT_CURRENT: /var/folders/k4/kb60p3ts7dqd8n5n_zpqlxb80000gn/T/confluent.955162
        ZooKeeper is [UP]
        Kafka is [UP]
        Starting Schema Registry
        Schema Registry is [UP]
        Starting Kafka REST
        Kafka REST is [UP]
        Starting Connect
        Connect is [UP]
        Starting ksqlDB Server
        ksqlDB Server is [UP]
        Starting Control Center
        Control Center is [UP]
```

#### Schema Registry - Local
- http://localhost:8081/schemas

### Maven Config 
##### See full [pom.xml](orderproducer/pom.xml)
#### Configure Confluent Maven Repository
```xml
<repositories>
	<repository>
		<id>confluent</id>
		<url>https://packages.confluent.io/maven/</url>
		<releases>
			<enabled>true</enabled>
		</releases>
		<snapshots>
			<enabled>true</enabled>
		</snapshots>
	</repository>
</repositories>
```
#### Add Maven Dependencies - `avro`, `kafka-avro-serializer` & `avro-maven-plugin`
```xml
<!-- https://mvnrepository.com/artifact/org.apache.avro/avro -->
<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro</artifactId>
    <version>1.11.3</version>
</dependency>
<!-- https://mvnrepository.com/artifact/io.confluent/kafka-avro-serializer -->
<dependency>
    <groupId>io.confluent</groupId>
    <artifactId>kafka-avro-serializer</artifactId>
    <version>7.5.1</version>
</dependency>
<!-- https://mvnrepository.com/artifact/org.apache.avro/avro-maven-plugin -->
<dependency>
    <groupId>org.apache.avro</groupId>
    <artifactId>avro-maven-plugin</artifactId>
    <version>1.11.3</version>
</dependency>
```
#### Add Maven Build Plugin - `avro-maven-plugin`
```xml
<build>
	<plugins>
		<plugin>
			<groupId>org.apache.avro</groupId>
			<artifactId>avro-maven-plugin</artifactId>
			<version>1.11.3</version>
			<executions>
				<execution>
					<phase>generate-sources</phase>
					<goals>
						<goal>schema</goal>
					</goals>
					<configuration>
						<sourceDirectory>${project.basedir}/src/main/resources/</sourceDirectory>
						<outputDirectory>${project.basedir}/src/main/java</outputDirectory>
					</configuration>
				</execution>
			</executions>
		</plugin>
	</plugins>
</build>
```

#### Avro Producer
- `KafkaAvroSerializer`
- `schema.registry.url`
```java
package com.kafka.examples.avro;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class OrderAvroProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("value.serializer", KafkaAvroSerializer.class.getName()); // Avro Serializer
        props.put("schema.registry.url", "http://localhost:8081"); // Schema registry

        Order order = new Order("Doe, John", "iPhone", 5);

        try (KafkaProducer<String, Order> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, Order> record = new ProducerRecord<>("orders-avro", order.getCustomer()
                                                                                            .toString(), order); // String types are CharSequence
            //async send
            producer.send(record, (metadata, e) -> {
                System.out.println("OrderAvroProducer:.metadata: " + metadata);
                if (e != null) {
                    System.out.println("OrderAvroProducer: Exception: " + e.getMessage());
                }
            });
            System.out.println("OrderAvroProducer: Message sent!");
        } catch (Exception e) {
            System.out.println("OrderAvroProducer: Exception2: " + e.getMessage());
        }
    }
}
```
### Advanced Producer Configuration
```sh
zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties
kafka-server-start /usr/local/etc/kafka/server.properties

kafka-topics --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 10 --topic orders-partitioned
kafka-topics --list --bootstrap-server localhost:9092

arvins-mac @ ~
 [6] → kafka-topics --describe --topic orders-partitioned --bootstrap-server localhost:9092
Topic: orders-partitioned	TopicId: 73UOL1AYS9qiStJL7UqG4Q	PartitionCount: 10	ReplicationFactor: 1	Configs:
	Topic: orders-partitioned	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 3	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 4	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 5	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 6	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 7	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 8	Leader: 0	Replicas: 0	Isr: 0
	Topic: orders-partitioned	Partition: 9	Leader: 0	Replicas: 0	Isr: 0


```

#### Producer Configurtion Properties:
- https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html

### Message Delivery Idempotency - No Duplicate Messages
Kafka Producer API along with the Broker supports 3 semantics for message delivery.
- At least once (Default)
- At most once
- Only(Exactly) once - Idempotent

#### Producer Transactions
For a `KafkaProducer producer = new KafkaProducer(props)`
- Use `ProducerConfig.TRANSACTIONAL_ID_COFIG` - producer id for `producer`.
  - Used by broker to uniquely identify Producer to handle transactions.
- Use `ProducerConfig.MAX_BLOCK_MS_COFIG`
- `producer.initTransactions();`
- `producer.beginTransaction();`
- `producer.commitTransaction();`
- `producer.abortTransaction();`
 
### Consumers and Commits
- Consumer Groups
- One of the Brokers as Consumer Group Coordinator
- Consumer Group Leader
- Consumer Heartbeat with Group Coordinator
- Consumer Group Rebalancing (Consumers join/leave)
  - Consumer Group Lead does the Rebalancing when a consumer(s) join/leave.
- Offset Commits - Special topic `__consumer_offsets`
- Property `auto.commit.interval.ms`
- Property `auto.commit.offset`. Enabled by default. Commits every 5s or the next poll.
- `consumer.poll(duration)` along with above interval play a role in offset commit.
- `consumer.close()` also commits offsets.
- `consumer.commitSync()` - Blocking
- `consumer.commitAsync()` - Non-Blocking (Overloaded)
  -  `consumer.commitAsync()`
  -  `consumer.commitAsync(<OffsetCommitCallback>callback)`
  -  `consumer.commitAsync(<TopicPartition, OffsetAndMetadata><Map>, <OffsetCommitCallback>callback)`

#### ConsumerRebalanceListener
- Implement `ConsumerRebalanceListener` 
- Two methods to implement 
  - `onPartitionsRevoked(Collection<TopicPation>)` 
    - Any offsets that are processed but are yet to be committed.
    - `consumer.commitSync(currentOffsets)`
  - `onPartitionsAssigned(Collection<TopicPation>)`
- Pass it to subscribe
  - `consumer.subscribe(Collection<String>topics, ConsumerRebalanceListener)`

#### Consumer Configuration Properties
- https://docs.confluent.io/platform/current/installation/configuration/consumer-configs.html

#### Standalone or Simple Consumers
- Not part of any Consumer Group
- No rebalancing
- `auto.offset.reset` must be set. `latest` or `earliest`.
- No `consumer.subscribe(topics)` instead use `assign(partitions)`, see below.
- Get the `PartitionInfo` from `consumer.partitionsFor(topicName)`
  - If new partitions are created, this consumer will not know.
  - There must be mechanism to know the new partitions.
- Create and Assign Partitions by own - `consumer.assign(Collection<TopicPartition>)`
- Cannot Add a Simple Consumer to existing consumer group that is active and already processing records.

#### Offset Commit APIs
- Inorder to use the offset commit APIs (`commitSync()` & `commitAsync()`), `group.id` must be set in the Consumer Configuration.

### Stream Processing
#### Create Stream Topics
```sh
kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-dataflow-input

kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-dataflow-output 
```
#### Define Stream
```java
package com.kafka.examples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;

import java.util.Properties;

public class DataFlowStream {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-dataflow");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()
                                                                      .getClass()
                                                                      .getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()
                                                                        .getClass()
                                                                        .getName());

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("streams-dataflow-input");
        stream.foreach((key, value) -> System.out.println("Key: " + key + " Value: " + value));

        Topology topology = builder.build();

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

        Runtime.getRuntime()
               .addShutdownHook(new Thread(streams::close));

    }
}

```

#### Test Stream

- Start above java app
- Test with `kafka-console-producer`
- Send messages and check Java Console

```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic streams-dataflow-input
```

#### Describe Stream Topology
```java
System.out.println(topology.describe());
```
**Output:**
```sh
Topologies:
   Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000 (topics: [streams-dataflow-input])
      --> KSTREAM-FOREACH-0000000001
    Processor: KSTREAM-FOREACH-0000000001 (stores: [])
      --> none
      <-- KSTREAM-SOURCE-0000000000
```

### Write to output Stream
#### Start console producer and consumer
```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic streams-dataflow-input

kafka-console-consumer --bootstrap-server localhost:9092 \
    --topic streams-dataflow-output \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.StringDeserializer
```
#### Write to output topic
```java
StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("streams-dataflow-input");
        stream.foreach((key, value) -> System.out.println("Key: " + key + " Value: " + value));

        //Write to output topic
        stream.to("streams-dataflow-output");

        Topology topology = builder.build();
        System.out.println(topology.describe());
```
#### Test
- Start the app
- send message in console producer
- check console consumer
**New Topology**
- FOREACH `Sink`
- Output Topic `Sink`
```sh
Topologies:
   Sub-topology: 0
    Source: KSTREAM-SOURCE-0000000000 (topics: [streams-dataflow-input])
      --> KSTREAM-FOREACH-0000000001, KSTREAM-SINK-0000000002
    Processor: KSTREAM-FOREACH-0000000001 (stores: [])
      --> none
      <-- KSTREAM-SOURCE-0000000000
    Sink: KSTREAM-SINK-0000000002 (topic: streams-dataflow-output)
      <-- KSTREAM-SOURCE-0000000000
```

### Word Count
- `KTable` - local storage and stateful
#### Create Topics
```sh
kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-wordcount-input

kafka-topics --create \
    --bootstrap-server localhost:9092 \
    --replication-factor 1 \
    --partitions 1 \
    --topic streams-wordcount-output 
```
#### WordCountStream
```java
package com.kafka.examples;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Arrays;
import java.util.Properties;

public class WordCountStream {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-dataflow");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String()
                                                                      .getClass()
                                                                      .getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String()
                                                                        .getClass()
                                                                        .getName());
        props.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0); // by default 1

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> stream = builder.stream("streams-wordcount-input");
        KGroupedStream<String, String> kGroupedStream = stream.flatMapValues(value -> Arrays.asList(value.toUpperCase()
                                                                                                         .split(" ")))
                                                              .groupBy((k, v) -> v);
        KTable<String, Long> kCountTable = kGroupedStream.count();
        kCountTable.toStream()
              .to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));

        Topology topology = builder.build();
        System.out.println(topology.describe());

        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

        Runtime.getRuntime()
               .addShutdownHook(new Thread(streams::close));

    }
}
```
#### Test
- Start console producer and consumer
```sh
kafka-console-producer --bootstrap-server localhost:9092 --topic streams-wordcount-input

kafka-console-consumer --bootstrap-server localhost:9092 \
    --topic streams-wordcount-output \
    --from-beginning \
    --property print.key=true \
    --property print.value=true \
    --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
    --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
```


### Spring Boot Kafka
```sh
curl -kv  -H "Content-Type: application/json" http://localhost:8080/api/users -d '{"name": "john", "age": 35, "favGenre": "Horror"}'
```






