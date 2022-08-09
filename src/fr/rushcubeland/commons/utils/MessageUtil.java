package fr.rushcubeland.commons.utils;

public enum MessageUtil {

    ACCOUNT_NOT_FOUND("§cUne erreur est survenue en essayant de récupérer votre compte, veuillez rapporter l'erreur à un membre du staff."),
    NO_PERM("§cVous n'avez pas la permission de faire ceci !"),
    SPECIFY_PLAYER("§cVeuillez spécifier un joueur !"),
    SPECIFY_VALID_PLAYER("§cVeuillez spécifier un joueur valide !"),
    NO_SERVERS_FOUND("§cAucun serveur trouvé, veuillez rapporter l'erreur à un membre du staff."),
    ALREADY_CONNECTED_TO_LOBBY("§cVous etes déjà connecté à un Lobby !"),
    UNKNOWN_PLAYER("§cCe joueur n'existe pas !"),
    PLAYER_NOT_ONLINE("Le joueur n'est pas en-ligne !");

    private final String message;

    MessageUtil(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
