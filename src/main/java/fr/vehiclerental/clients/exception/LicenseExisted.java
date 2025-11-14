package fr.vehiclerental.clients.exception;

public class LicenseExisted extends RuntimeException {
    public LicenseExisted() {
        super("La license du prénommé client existe deja dans la base de données !");
    }
}
