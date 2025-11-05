package fr.vehiclerental.clients.service;

import fr.vehiclerental.clients.entity.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    public void editClientService(Client findindClient, Client clientBodyRequest, ClientDao clientDao) {
        findindClient.setFirstName(clientBodyRequest.getFirstName());
        findindClient.setLastName(clientBodyRequest.getLastName());
        findindClient.setNumberlicense(clientBodyRequest.getNumberLicense());
        findindClient.setBirthday(clientBodyRequest.getBirthday());
        findindClient.setObtaining_license(clientBodyRequest.getObtainingLicense());
        clientDao.save(findindClient);
    }

    public void createClient(Client client, ClientDao clientDao) {
        clientDao.save(client);
    }
}
