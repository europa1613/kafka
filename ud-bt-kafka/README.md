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
- 
  