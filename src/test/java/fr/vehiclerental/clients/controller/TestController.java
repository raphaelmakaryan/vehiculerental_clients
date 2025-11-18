package fr.vehiclerental.clients.controller;

import fr.vehiclerental.clients.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

}
