package com.mesaverde.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mesaverde.entity.Cliente;

import com.mesaverde.service.ClienteService;

import java.util.List;
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService service;
    
    @GetMapping("/listartodo")
    public List<Cliente> getListCliente(){
    	
    	return service.getListCliente();
    }
    
    
    @GetMapping("/{clienteId}")
    public Cliente gestBuscarCliente (Integer id) {
    	return service.getBuscarCliente(id);
    }
    @PutMapping("/")
    public Cliente updateCliente(Cliente cliente) {
    	
    	return service.updateCliente(cliente);
    	
    }
    
    @DeleteMapping("/{clienteId}")
    public void deleteCliente(Integer id) {
    	
    	service.deleteCliente(id);
    }
}
