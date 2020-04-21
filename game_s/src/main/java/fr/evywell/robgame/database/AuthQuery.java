package fr.evywell.robgame.database;

public class AuthQuery {
    public static final String
        UPD_REALM_UPTIME = "UPDATE realms SET uptime = ? WHERE id = ?",
        SEL_ACCOUNT_BY_TOKEN = "SELECT id, username, date_ban_end, last_logged_in FROM users WHERE token = ?";
}
