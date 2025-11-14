package fr.vehiclerental.clients.service;

import fr.vehiclerental.clients.entity.Client;
import fr.vehiclerental.clients.exception.LicenseExisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientDao clientDao;

    /**
     * Methode qui appellera l'api Licenses
     * @param idLicense License du client en string
     * @return Vrai ou faux
     */
    public boolean requestLicense(String idLicense) {
        String verifyLicense = "http://localhost:8086/licenses/" + idLicense;
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(verifyLicense, Boolean.class);
    }

    /**
     * Méthode pour crée un client
     * @param client Information du client
     * @param clientDao DAO de Client
     * @return Vrai ou faux
     */
    public boolean createClient(Client client, ClientDao clientDao) {
        if (requestLicense(client.getNumber_license())) {
            clientDao.save(client);
            return true;
        } else {
            throw new LicenseExisted();
        }
    }

    /**
     * Méthode de modification d'un client
     * @param findindClient Client trouvé
     * @param clientBodyRequest Information du client donné via la requete
     * @param clientDao DAO de Client
     */
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

    /**
     * Méthode pour crée des faux clients
     */
    public void saveInitialData() {
        createClientFake();
        createClientFake2();
        createClientFake3();
        createClientFake4();
    }
}
