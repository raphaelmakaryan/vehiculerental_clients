package fr.vehiclerental.clients.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClientService clientService;

    @Override
    public void run(String... args) throws Exception {
        clientService.saveInitialData();
    }
}