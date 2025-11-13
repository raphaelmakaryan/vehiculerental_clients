package fr.vehiclerental.clients.service;

import fr.vehiclerental.clients.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ClientService {

    @Autowired
    ClientDao clientDao;


    public void editClientService(Client findindClient, Client clientBodyRequest, ClientDao clientDao) {
        findindClient.setFirstName(clientBodyRequest.getFirstName());
        findindClient.setLastName(clientBodyRequest.getLastName());
        findindClient.setNumberlicense(clientBodyRequest.getNumberLicense());
        findindClient.setBirthday(clientBodyRequest.getBirthday());
        findindClient.setObtaining_license(clientBodyRequest.getObtainingLicense());
        clientDao.save(findindClient);
    }

    public void createClientFake() {
        Client client = new Client();
        client.setFirstName("Jean");
        client.setLastName("Dupont");
        client.setNumberlicense("1234567890123");
        client.setBirthday(LocalDate.parse("1985-03-15"));
        client.setObtaining_license(2010);
        clientDao.save(client);
    }

    public void createClientFake2() {
        Client client = new Client();
        client.setFirstName("Marie");
        client.setLastName("Martin");
        client.setNumberlicense("9876543210987");
        client.setBirthday(LocalDate.parse("1992-07-22"));
        client.setObtaining_license(0);
        clientDao.save(client);
    }

    public void createClientFake3() {
        Client client = new Client();
        client.setFirstName("Pierre");
        client.setLastName("Bernard");
        client.setNumberlicense("5555666677778");
        client.setBirthday(LocalDate.parse("1988-11-08"));
        client.setObtaining_license(2001);
        clientDao.save(client);
    }

    public void createClientFake4() {
        Client client = new Client();
        client.setFirstName("Sophie");
        client.setLastName("Leclerc");
        client.setNumberlicense("1111222233334");
        client.setBirthday(LocalDate.parse("1995-09-30"));
        client.setObtaining_license(1999);
        clientDao.save(client);
    }

    public void saveInitialData() {
        createClientFake();
        createClientFake2();
        createClientFake3();
        createClientFake4();
    }

    public void createClient(Client client, ClientDao clientDao) {
        clientDao.save(client);
    }
}
