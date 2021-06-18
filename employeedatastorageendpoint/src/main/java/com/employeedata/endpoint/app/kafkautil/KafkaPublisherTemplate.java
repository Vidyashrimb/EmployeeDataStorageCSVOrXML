package com.employeedata.endpoint.app.kafkautil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaPublisherTemplate {
	private KafkaTemplate<String, String> kafkaTemplate;

	@Autowired
	KafkaPublisherTemplate(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendMessage(String message, String topicName) {
		kafkaTemplate.send(topicName, message);
	}
}
