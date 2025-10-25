package com.mesaverde.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mesaverde.entity.Auditoria;
import org.springframework.amqp.rabbit.core.RabbitTemplate; // ✅ USAR RabbitTemplate directamente
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageProducerService {

    @Value("${app.queue.name}")
    private String queueName;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(Auditoria log) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            String message = mapper.writeValueAsString(log);

            rabbitTemplate.convertAndSend("", queueName, message);

            System.out.println("📦 Mensaje de Auditoría enviado a la cola: " + queueName + ", Objeto: " + message);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Problemas al enviar el mensaje de auditoría: " + e.getMessage());
        }
    }
}