package fr.evywell.robgame.database;

public class CharacterQuery {

    public static final String
            SEL_CHARACTERS_BY_USER = "SELECT uuid, name, created_at FROM characters WHERE account_guid = ?",
            SEL_CHARACTER_BY_uuid = "SELECT uuid, name, map_id, pos_x, pos_y, pos_z FROM characters WHERE account_uuid = ? AND uuid = ?";

}
