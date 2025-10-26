package com.mesaverde.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mesaverde.entity.Auditoria;
import com.mesaverde.repository.AuditoriaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = "${app.queue.name}")
    public void receiveMessage(String message) {

        try {
            System.out.println("Mensaje recibido de la cola: " + message);

            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            Auditoria auditoria = mapper.readValue(message, Auditoria.class);

            auditoriaRepository.save(auditoria);

            System.out.println("Auditoria guardado en la BD.");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
