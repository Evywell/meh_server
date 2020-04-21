package fr.evywell.robgame.database;

public class CharacterQuery {

    public static final String SEL_CHARACTERS_BY_USER = "SELECT UUID, name, created_at FROM characters WHERE account_uuid = ?";

}
