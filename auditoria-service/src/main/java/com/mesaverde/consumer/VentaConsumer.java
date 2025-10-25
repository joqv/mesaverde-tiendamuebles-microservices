package com.mesaverde.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mesaverde.entity.Auditoria;
import com.mesaverde.repository.AuditoriaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VentaConsumer {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @RabbitListener(queues = "${app.queue.name}")
    public void receiveMessage(String message) {
        System.out.println("Mensaje de auditoría recibido de la cola: " + message);

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            Auditoria auditoria = mapper.readValue(message, Auditoria.class);
            auditoriaRepository.save(auditoria);

            System.out.println("✅ Auditoría guardada exitosamente: Venta ID " + auditoria.getVentaid());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ ERROR al procesar o guardar el mensaje de auditoría: " + e.getMessage());
        }
    }
}
