package org.cibertec.service;

import org.cibertec.entity.Log;
import org.cibertec.repository.LogRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Service
public class MessageConsumerService {

	@Autowired
	private LogRepository logRepository;

	@RabbitListener(queues = "${app.queue.name}")
	public void receiveMessage(String message) {
		try {
			System.out.println("Mensaje recibido de la cola: " + message);
			
			ObjectMapper mapper = new ObjectMapper();
			mapper.registerModule(new JavaTimeModule());
			
			Log log = mapper.readValue(message, Log.class);
			
			logRepository.save(log);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

