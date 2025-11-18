package fr.vehiclerental.clients.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.vehiclerental.clients.entity.Client;
import fr.vehiclerental.clients.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TestController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClientService clientService;

    @Test
    public void hello() {
        try {
            mockMvc.perform(get("/")).andExpect(status().isOk()).andExpect(content().string("Welcome to the VehicleRental Company Client API !"));
        } catch (Exception e) {
            System.err.println("Sa ne respecte pas ce qui est demandé !");
            e.printStackTrace();
        }
    }

    @Test
    public void allClients() throws Exception {
        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].firstName").value("Jean"))
                .andExpect(jsonPath("$[1].firstName").value("Marie"));
    }

    @Test
    public void oneClients() throws Exception {
        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName").value("Jean"));
    }

    @Test
    public void addClients() throws Exception {
        Client client = new Client();
        client.setFirstName("Toto");
        client.setLastName("skusku");
        client.setBirthday(LocalDate.parse("1010-10-20"));
        client.setNumberlicense("1234567891231");
        client.setObtaining_license(2004);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String clientJson = objectMapper.writeValueAsString(client);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").isBoolean())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    public void putClients() throws Exception {
        Client client = new Client();
        client.setFirstName("Toto");
        client.setLastName("skusku");
        client.setBirthday(LocalDate.parse("1010-10-20"));
        client.setNumberlicense("1234567891231");
        client.setObtaining_license(2004);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String clientJson = objectMapper.writeValueAsString(client);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(clientJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Votre client a été modifié !"));
    }

    @Test
    public void deleteClient() throws Exception {
        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Votre client a été supprimé !"));
    }


}
