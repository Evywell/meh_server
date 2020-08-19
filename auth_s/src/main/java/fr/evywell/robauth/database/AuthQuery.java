package fr.evywell.robauth.database;

public class AuthQuery {

    public static final String AUTH_SEL_ACCOUNT_INFO = "SELECT GUID, username, email, password, salt, date_ban_end, token FROM accounts WHERE username = ?";
    public static final String AUTH_UPD_ACCOUNT_TOKEN = "UPDATE accounts SET token = ? WHERE GUID = ?";
    public static final String AUTH_UPD_ACCOUNT_LAST_LOGIN = "UPDATE accounts SET last_logged_in = ? WHERE GUID = ?";
    public static final String AUTH_SEL_CLIENT_INFO = "SELECT id, name, client_id, ip, secret FROM clients WHERE client_id = ?";
    public static final String AUTH_SEL_CLIENT_INFO_BY_CODE = "SELECT secret FROM clients WHERE name = ?";
    public static final String AUTH_UPD_CLIENT_SECRET = "UPDATE clients SET secret = ? WHERE id = ?";

}
