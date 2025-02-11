package com.emna.client_fournisseur.Controller;

import com.emna.client_fournisseur.Service.ClientService;
import com.emna.client_fournisseur.dao.Request.ClientRequest;
import com.emna.client_fournisseur.entites.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/client")
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping ("/addclient")

    public ResponseEntity<Client> createClient(@RequestBody ClientRequest clientRequest) {
        Client savedClient = clientService.AddClient(clientRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedClient);
    }
}
