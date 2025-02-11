package com.emna.client_fournisseur.Service;

import com.emna.client_fournisseur.dao.Request.ClientRequest;
import com.emna.client_fournisseur.entites.Client;

import java.util.List;

public interface ClientService {
    void deleteClient(Integer id);
    Client updateClient(Integer id, Client client);
    Client AddClient( ClientRequest request);
    List<Client> getAllClients();
    Client getClientById(Integer id);
}
