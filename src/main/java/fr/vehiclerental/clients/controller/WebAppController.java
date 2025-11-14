package fr.vehiclerental.clients.controller;

import fr.vehiclerental.clients.entity.Client;
import fr.vehiclerental.clients.entity.ClientDTO;
import fr.vehiclerental.clients.exception.BadRequestException;
import fr.vehiclerental.clients.exception.ClientNotAdd;
import fr.vehiclerental.clients.exception.ClientNotFindException;
import fr.vehiclerental.clients.service.ClientDao;
import fr.vehiclerental.clients.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class WebAppController {
    private final ClientService clientService;
    private final ClientDao clientDao;

    public WebAppController(ClientService clientService, ClientDao clientDao) {
        this.clientService = clientService;
        this.clientDao = clientDao;
    }

    @Operation(summary = "Home page")
    @RequestMapping("/")
    public String index() {
        return "Welcome to the VehicleRental Company Client API !";
    }

    @Operation(summary = "Voir tous les clients de la base de données ", description = "Requête pour la récupération de tous les clients de la base de données ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class)))})
    @GetMapping("/clients")
    public List<ClientDTO> clientsJPA() {
        return clientDao.findAll().stream().map(c -> new ClientDTO(c.getId(), c.getFirst_name(), c.getLast_name(), c.getNumberLicense(), c.getBirthday(), c.getObtaining_license())).collect(Collectors.toList());
    }

    @Operation(summary = "Voir un client spécifique de la base de données", description = "Requête pour la récupération d'un client de la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientNotFindException.class)))})
    @RequestMapping(path = "/clients/{id}", method = RequestMethod.GET)
    public List<ClientDTO> getClients(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int id) {
        try {
            return clientDao.findById(id).stream().map(c -> new ClientDTO(c.getId(), c.getFirst_name(), c.getLast_name(), c.getNumberLicense(), c.getBirthday(), c.getObtaining_license())).collect(Collectors.toList());
        } catch (Exception exception) {
            throw new ClientNotFindException(id);
        }
    }

    @Operation(summary = "Crée un nouveau client dans la base de données", description = "Requête pour crée/ajouter client dans la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été ajoutée !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur de license", value = "{\n" + "    \"success\": false,\n" + "    \"message\": \"La license du prénommé client existe deja dans la base de données !\"\n" + "}"), @ExampleObject(name = "Erreur générale", value = "{\n" + "    \"success\": false,\n" + "    \"message\": \"Votre client n'a pas été ajoutée !\"\n" + "}")}))})
    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addClientPost(@Validated @RequestBody Client client) {
        Map<String, Object> response = new HashMap<>();
        if (clientService.createClient(client, clientDao)) {
            response.put("success", true);
            response.put("message", "Votre client a été ajoutée !");
        }
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "Mettre à jour un client dans la base de données", description = "Requête pour mettre a jour un client dans la base de données ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été modifié !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur générale", value = "{\n" + "  \"localDateTime\": \"2025-11-03T08:25:00\",\n" + "  \"message\": \"Client not found with ID : 1\",\n" + "  \"status\": 404\n" + "}")
    }))})
    @PutMapping("/clients/{id}")
    public ResponseEntity<Map<String, Object>> editClient(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int idUSer, @Validated @RequestBody Client clientRequest) {
        try {
            List<Client> clientVerification = clientDao.findById(idUSer);
            Map<String, Object> response = new HashMap<>();
            if (clientVerification == null || clientVerification.isEmpty()) {
                throw new ClientNotFindException(idUSer);
            } else {
                clientService.editClientService(clientVerification.get(0), clientRequest, clientDao);
                response.put("success", true);
                response.put("message", "Votre client a été modifié !");
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @Operation(summary = "Supprimer un client de la base de données", description = "Requête pour supprimer un client de la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été supprimé !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur générale", value = "{\n" + "  \"localDateTime\": \"2025-11-03T08:25:00\",\n" + "  \"message\": \"Client not found with ID : 1 \",\n" + "  \"status\": 404\n" + "}")

    }))})
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Map<String, Object>> deleteClient(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int idUSer) {
        List<Client> client = clientDao.findById(idUSer);
        if (client == null || client.isEmpty()) {
            throw new ClientNotFindException(idUSer);
        } else {
            clientDao.delete(client.get(0));
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Votre client a été supprimé !");
            return ResponseEntity.ok(response);
        }
    }
}

