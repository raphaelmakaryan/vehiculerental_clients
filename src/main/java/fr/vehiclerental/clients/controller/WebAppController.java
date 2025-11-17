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
        return clientService.allClients();
    }

    @Operation(summary = "Voir un client spécifique de la base de données", description = "Requête pour la récupération d'un client de la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientDTO.class))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ClientNotFindException.class)))})
    @RequestMapping(path = "/clients/{id}", method = RequestMethod.GET)
    public List<ClientDTO> getClients(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int id) {
        return clientService.oneClient(id);
    }

    @Operation(summary = "Crée un nouveau client dans la base de données", description = "Requête pour crée/ajouter client dans la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été ajoutée !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur de license", value = "{\n" + "    \"success\": false,\n" + "    \"message\": \"La license du prénommé client existe deja dans la base de données !\"\n" + "}"), @ExampleObject(name = "Erreur générale", value = "{\n" + "    \"success\": false,\n" + "    \"message\": \"Votre client n'a pas été ajoutée !\"\n" + "}")}))})
    @RequestMapping(value = "/clients", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addClientPost(@Validated @RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client, clientDao));
    }

    @Operation(summary = "Mettre à jour un client dans la base de données", description = "Requête pour mettre a jour un client dans la base de données ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été modifié !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur générale", value = "{\n" + "  \"localDateTime\": \"2025-11-03T08:25:00\",\n" + "  \"message\": \"Client not found with ID : 1\",\n" + "  \"status\": 404\n" + "}")
    }))})
    @PutMapping("/clients/{id}")
    public ResponseEntity<Map<String, Object>> editClient(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int idUSer, @Validated @RequestBody Client clientRequest) {
        return ResponseEntity.ok(clientService.editClientService(idUSer, clientRequest, clientDao));
    }

    @Operation(summary = "Supprimer un client de la base de données", description = "Requête pour supprimer un client de la base de données")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Opération réussi", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\n" + "    \"success\": true,\n" + "    \"message\": \"Votre client a été supprimé !\"\n" + "}"))), @ApiResponse(responseCode = "405", description = "Échec de l'opération ", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "Erreur générale", value = "{\n" + "  \"localDateTime\": \"2025-11-03T08:25:00\",\n" + "  \"message\": \"Client not found with ID : 1 \",\n" + "  \"status\": 404\n" + "}")
    }))})
    @DeleteMapping("/clients/{id}")
    public ResponseEntity<Map<String, Object>> deleteClient(@Parameter(description = "Identifiant du compte du client", required = true) @PathVariable(value = "id") int idUSer) {
        return ResponseEntity.ok(clientService.deleteClientService(idUSer));
    }
}

