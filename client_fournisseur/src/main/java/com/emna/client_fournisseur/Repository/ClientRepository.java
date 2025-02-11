package com.emna.client_fournisseur.Repository;

import com.emna.client_fournisseur.entites.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Integer> {

}
