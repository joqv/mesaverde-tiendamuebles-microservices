package com.mesaverde.service;

import com.mesaverde.dto.VentaMessage;
import com.mesaverde.entity.Auditoria;
import com.mesaverde.repository.AuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    public void guardarAuditoria(VentaMessage message) {

        Auditoria auditoria = new Auditoria();
        auditoria.setVentaid(message.getVentaId());
        auditoria.setTotal(message.getTotal());

        if (message.getFecha() != null) {
            auditoria.setFecha(message.getFecha());
        } else {
            auditoria.setFecha(LocalDateTime.now());
        }

        auditoriaRepository.save(auditoria);
    }
}