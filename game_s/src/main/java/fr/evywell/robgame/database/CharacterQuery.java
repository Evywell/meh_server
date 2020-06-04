package fr.evywell.robgame.database;

public class CharacterQuery {

    public static final String
            SEL_CHARACTERS_BY_USER = "SELECT UUID, name, created_at FROM characters WHERE account_uuid = ?",
            SEL_CHARACTER_BY_UUID = "SELECT UUID, name, map_id, pos_x, pos_y, pos_z FROM characters WHERE account_uuid = ? AND UUID = ?";

}
