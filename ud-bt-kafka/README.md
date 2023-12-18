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




