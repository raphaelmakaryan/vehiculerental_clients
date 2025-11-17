package fr.vehiclerental.clients.service;

import fr.vehiclerental.clients.entity.Client;
import fr.vehiclerental.clients.entity.ClientDTO;
import fr.vehiclerental.clients.exception.BadRequestException;
import fr.vehiclerental.clients.exception.ClientNotFindException;
import fr.vehiclerental.clients.exception.LicenseExisted;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    ClientDao clientDao;

    /**
     * Méthode pour récuperer tout les clients
     *
     * @return Liste des clients
     */
    public List<ClientDTO> allClients() {
        return clientDao.findAll().stream().map(c -> new ClientDTO(c.getId(), c.getFirst_name(), c.getLast_name(), c.getNumberLicense(), c.getBirthday(), c.getObtaining_license())).collect(Collectors.toList());
    }

    /**
     * Méthode pour récuperer un client précis
     *
     * @param id id du client
     * @return Les informations du client
     */
    public List<ClientDTO> oneClient(int id) {
        try {
            return clientDao.findById(id).stream().map(c -> new ClientDTO(c.getId(), c.getFirst_name(), c.getLast_name(), c.getNumberLicense(), c.getBirthday(), c.getObtaining_license())).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ClientNotFindException(id);
        }
    }

    /**
     * Methode qui appellera l'api Licenses
     *
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
     *
     * @param client    Information du client
     * @param clientDao DAO de Client
     * @return Vrai ou faux
     */
    public Map<String, Object> createClient(Client client, ClientDao clientDao) {
        if (requestLicense(client.getNumber_license())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Votre client a été ajoutée !");
            clientDao.save(client);
            return response;
        } else {
            throw new LicenseExisted();
        }
    }

    /**
     * Méthode de modification d'un client
     *
     * @param findindClient     Client trouvé
     * @param clientBodyRequest Information du client donné via la requete
     * @param clientDao         DAO de Client
     */
    public void editClient(Client findindClient, Client clientBodyRequest, ClientDao clientDao) {
        findindClient.setFirstName(clientBodyRequest.getFirstName());
        findindClient.setLastName(clientBodyRequest.getLastName());
        findindClient.setNumberlicense(clientBodyRequest.getNumberLicense());
        findindClient.setBirthday(clientBodyRequest.getBirthday());
        findindClient.setObtaining_license(clientBodyRequest.getObtainingLicense());
        clientDao.save(findindClient);
    }

    /**
     * Méthode de vérifiction pour la modifiation d'un client
     *
     * @param idUser        Id du client
     * @param clientRequest Information de la requete
     * @param clientDao     DAO
     * @return Réponse
     */
    public Map<String, Object> editClientService(int idUser, Client clientRequest, ClientDao clientDao) {
        try {
            List<Client> clientVerification = clientDao.findById(idUser);
            if (clientVerification == null || clientVerification.isEmpty()) {
                throw new ClientNotFindException(idUser);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Votre client a été modifié !");
                editClient(clientVerification.get(0), clientRequest, clientDao);
                return response;
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }

    }

    /**
     * Méthode pour la suppresion d'un client
     * @param idUSer id du client
     * @return Réponse
     */
    public Map<String, Object> deleteClientService(int idUSer) {
        List<Client> client = clientDao.findById(idUSer);
        if (client == null || client.isEmpty()) {
            throw new ClientNotFindException(idUSer);
        } else {
            clientDao.delete(client.get(0));
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Votre client a été supprimé !");
            return response;
        }
    }


    public void createClientFake() {
        Client client = new Client();
        client.setFirstName("Jean");
        client.setLastName("Dupont");
        client.setNumberlicense("1234567890123");
        client.setBirthday(LocalDate.parse("1965-03-15"));
        client.setObtaining_license(2010);
        clientDao.save(client);
    }

    public void createClientFake2() {
        Client client = new Client();
        client.setFirstName("Marie");
        client.setLastName("Martin");
        client.setNumberlicense("9876543210987");
        client.setBirthday(LocalDate.parse("1992-07-22"));
        client.setObtaining_license(2005);
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
        client.setBirthday(LocalDate.parse("2020-09-30"));
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
