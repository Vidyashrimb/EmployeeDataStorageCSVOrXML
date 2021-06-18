package com.employeedata.service.app.kafkautil;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import com.employeedata.service.app.constants.EmployeeDataConstants;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name(EmployeeDataConstants.KAFKA_SAVE_DATA_CONSUMER_TOPIC).build();
	}

	@Bean
	public NewTopic topic2() {
		return TopicBuilder.name(EmployeeDataConstants.KAFKA_UPDATE_DATA_CONSUMER_TOPIC).build();
	}

}
