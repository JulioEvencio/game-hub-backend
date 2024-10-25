package julioigreja.gamehub.services;

public interface EmailService {

    void sendEmailRegister(String username, String email);

    void sendEmailGamePublished(String username, String gameName, String gameSlug, String email);

    void sendEmailPasswordUpdate(String username, String email);

    void sendEmailPasswordRecovery(String username, String accessToken, String email);

}
