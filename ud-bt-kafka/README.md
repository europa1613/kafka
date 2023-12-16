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