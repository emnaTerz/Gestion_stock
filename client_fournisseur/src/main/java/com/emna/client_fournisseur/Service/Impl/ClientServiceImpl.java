package com.emna.client_fournisseur.Service.Impl;

import com.emna.client_fournisseur.Repository.ClientRepository;
import com.emna.client_fournisseur.Service.ClientService;
import com.emna.client_fournisseur.dao.Request.ClientRequest;
import com.emna.client_fournisseur.entites.Client;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void deleteClient(Integer id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client updateClient(Integer id, Client client) {
        return clientRepository.findById(id).map(existingClient -> {
            existingClient.setFirstName(client.getFirstName());
            existingClient.setLastName(client.getLastName());
            existingClient.setTelNumbers(client.getTelNumbers());
            existingClient.setAddress(client.getAddress());
            existingClient.setNumber(client.getNumber());
            return clientRepository.save(existingClient);
        }).orElseThrow(()->  new RuntimeException("User not found with id: " + id));
    }

    @Override

  public  Client AddClient(ClientRequest request) {
        Client NewClient = new Client ();
        NewClient.setFirstName(request.getFirstName());
        NewClient.setLastName(request.getLastName());
        NewClient.setEmail(request.getEmail());
        NewClient.setAddress(request.getAddress());
        NewClient.setNumber(request.getNumber());
        NewClient.setTelNumbers(request.getTelNumbers());

       return clientRepository.save(NewClient);
    }

    @Override
    public List<Client> getAllClients() {
        List <Client> clients = clientRepository.findAll();
        return clients;
    }

    @Override
    public Client getClientById(Integer id) {
       return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Client not found"));
    }
}
