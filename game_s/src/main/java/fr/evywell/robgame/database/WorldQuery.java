package fr.evywell.robgame.database;

public class WorldQuery {

    public static final String
        SEL_MAPS_IN_WORLD = "SELECT map_id, max_players, grid_w, grid_h FROM maps WHERE available = 1",
        SEL_MAP_CREATURES = "SELECT UUID, creature_template_id, pos_x, pos_y, pos_z, orientation FROM creatures WHERE map_id = ?",
        SEL_CREATURES_TEMPLATE = "SELECT id, name FROM creatures_templates";

}
