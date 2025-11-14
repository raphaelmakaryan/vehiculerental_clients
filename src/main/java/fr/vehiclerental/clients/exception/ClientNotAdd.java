package fr.vehiclerental.clients.exception;

public class ClientNotAdd extends RuntimeException {
    public ClientNotAdd() {
        super("Votre client n'a pas été ajoutée !");
    }
}
