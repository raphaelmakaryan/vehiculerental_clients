package fr.vehiclerental.clients.service;

import fr.vehiclerental.clients.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientDao extends JpaRepository<Client, Integer> {
    List<Client> findById(int id);

    List<Client> findAll();

    void delete(Client client);

    Client save(Client client);
}
