/*
 * Copyright 2020 Wuyi Chen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bharath.kafka.orderconsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

/**
 * Example of assigning the consumer to partitions instead of subscribing a topic.
 *
 * @author  Wuyi Chen
 * @date    06/05/2020
 * @version 1.0
 * @since   1.0
 */
public class AssignParitionsExample {
	public static void main(String[] args) {
		Properties props = new Properties();
		props.put("bootstrap.servers",  "localhost:9092");
		props.put("key.deserializer",   "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");

		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
		
		List<PartitionInfo> partitionInfos = null;
		partitionInfos = consumer.partitionsFor("SimpleConsumerTopic1");                                        // Get all the available partitions for that topic.

		List<TopicPartition> partitions = new ArrayList<>();
		if (partitionInfos != null) {   
		    for (PartitionInfo partition : partitionInfos) {
		        partitions.add(new TopicPartition(partition.topic(), partition.partition()));
		    }
		    consumer.assign(partitions);                                                                     // Assign the consumer to specific partitions instead of scribing a topic.           

		    while (true) {
		        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));

		        for (ConsumerRecord<String, String> record: records) {
		            System.out.printf("topic = %s, partition = %s, offset = %d, customer = %s, country = %s%n",
		                record.topic(), record.partition(), record.offset(), record.key(), record.value());
		        }
		        consumer.close();
		       // consumer.commitSync();
		    }
		}
	}
}
