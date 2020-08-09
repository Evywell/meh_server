package fr.evywell.robgame.database;

public class WorldQuery {

    public static final String
        SEL_MAPS_IN_WORLD = "SELECT map_id, max_players, grid_w, grid_h FROM maps WHERE available = 1",
        SEL_MAP_CREATURES = "SELECT uuid, creature_template_id, pos_x, pos_y, pos_z, orientation FROM creatures WHERE map_id = ?",
        SEL_CREATURES_TEMPLATE = "SELECT id, name FROM creatures_templates",
        SEL_AURAS = "SELECT ID, name, description, flags FROM auras",
        SEL_SPELLS = "SELECT ID, name, description, cost, resource_type, cooldown, range_length, cast_time, gcd, gcd_category, duration, school, flags",
        SEL_EFFECTS_OF_SPELL = "SELECT SPELL_ID, EFFECT_ID, positions, value1, value2 FROM spells_effects WHERE SPELL_ID = ? ORDER BY position ASC",
        SEL_EFFECTS_OF_AURA = "SELECT SPELL_ID, EFFECT_ID, positions, value1, value2 FROM auras_effects WHERE AURA_ID = ? ORDER BY position ASC";

}
