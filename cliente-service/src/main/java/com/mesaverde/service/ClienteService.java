package com.mesaverde.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mesaverde.entity.Cliente;
import com.mesaverde.repository.ClienteRepository;

@Service
public class ClienteService {
	@Autowired
	private ClienteRepository clienteRepository;
	
	public void createCliente( Cliente cliente) {
		
		clienteRepository.save(cliente);
	}
     public List<Cliente> getListCliente(){
    	 return clienteRepository.findAll();
    	 
     }
     
     public Cliente getBuscarCliente(Integer id) {
    	 
    	 return clienteRepository.getById(id);
     }
      
     public Cliente updateCliente(Cliente cliente) {
    	 return clienteRepository.save(cliente);
     }
     
     
     
    public void deleteCliente(Integer id) {
    	
    	clienteRepository.deleteById(id);
    
    
     }
     
}
