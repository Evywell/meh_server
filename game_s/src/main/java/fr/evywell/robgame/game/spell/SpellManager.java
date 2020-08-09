package fr.evywell.robgame.game.spell;

import fr.evywell.common.database.Database;
import fr.evywell.common.database.PreparedStatement;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.database.WorldQuery;
import fr.evywell.robgame.world.World;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.evywell.robgame.game.spell.Spell.*;
import static java.util.Map.entry;

public class SpellManager {

    private Map<Integer, String> effectRegistry;
    private Map<Integer, SpellInfo> spellRegistry;
    private Map<Integer, AuraInfo> auraRegistry;
    private Database worldDb;

    public SpellManager(Database worldDb) {
        this.worldDb = worldDb;
        this.effectRegistry = new HashMap<>();
        this.spellRegistry = new HashMap<>();
        this.auraRegistry = new HashMap<>();
        this.loadEffects();
    }

    public SpellInfo getSpellInfo(int spellId) {
        return hasSpell(spellId) ? this.spellRegistry.get(spellId) : null;
    }

    public boolean hasSpell(int spellId) {
        return spellRegistry.containsKey(spellId);
    }

    public void loadFromDb() throws SQLException {
        // Chargement des spells
        this.loadSpellsFromDb();
        // Chargement des auras
        this.loadAurasFromDb();
    }

    private void loadAurasFromDb() throws SQLException {
        Statement stmt = worldDb.executeStatement(WorldQuery.SEL_AURAS);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            AuraInfo auraInfo = new AuraInfo();
            auraInfo.ID = rs.getInt(1);
            auraInfo.name = rs.getString(2);
            auraInfo.description = rs.getString(3);
            auraInfo.flags = rs.getInt(4);
            addToRegistry(auraInfo);
            this.loadAuraEffectsFromDb(auraInfo);
        }
    }

    private void loadAuraEffectsFromDb(AuraInfo auraInfo) throws SQLException {
        PreparedStatement stmt = worldDb.getPreparedStatement(WorldQuery.SEL_EFFECTS_OF_AURA);
        stmt.setInt(1, auraInfo.ID);
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        List<EffectInfo> effectInfos = new ArrayList<>();
        while (rs.next()) {
            int effectID = rs.getInt(2);
            if (!effectRegistry.containsKey(effectID)) {
                Log.error(String.format("Cannot load aura %s. Reason: Unknown effect %d", auraInfo.name, effectID));
                removeFromRegistry(auraInfo);
                return;
            }
            EffectInfo effectInfo = new EffectInfo(effectID);
            effectInfo.value1 = rs.getInt(4);
            effectInfo.value2 = rs.getInt(5);
            effectInfos.add(effectInfo);
        }

        EffectInfo[] effects = new EffectInfo[effectInfos.size()];
        effects = effectInfos.toArray(effects);
        auraInfo.setEffects(effects);
    }

    private void loadSpellsFromDb() throws SQLException {
        Statement stmt = worldDb.executeStatement(WorldQuery.SEL_SPELLS);
        ResultSet rs = stmt.getResultSet();
        while (rs.next()) {
            SpellInfo spellInfo = new SpellInfo();
            spellInfo.ID = rs.getInt(1);
            spellInfo.name = rs.getString(2);
            spellInfo.description = rs.getString(3);
            spellInfo.cost = rs.getInt(4);
            spellInfo.resourceType = rs.getByte(5);
            spellInfo.cooldown = rs.getFloat(6);
            spellInfo.range = rs.getInt(7);
            spellInfo.castTime = rs.getFloat(8);
            spellInfo.gcd = rs.getFloat(9);
            spellInfo.gcdCategory = rs.getInt(10);
            spellInfo.duration = rs.getFloat(11);
            spellInfo.school = rs.getByte(12);
            spellInfo.flags = rs.getInt(13);
            addToRegistry(spellInfo);
            this.loadSpellEffectsFromDb(spellInfo);
        }
    }

    private void loadSpellEffectsFromDb(SpellInfo spellInfo) throws SQLException {
        PreparedStatement stmt = worldDb.getPreparedStatement(WorldQuery.SEL_EFFECTS_OF_SPELL);
        stmt.setInt(1, spellInfo.ID);
        stmt.execute();
        ResultSet rs = stmt.getResultSet();
        List<EffectInfo> effectInfos = new ArrayList<>();
        while (rs.next()) {
            int effectID = rs.getInt(2);
            if (!effectRegistry.containsKey(effectID)) {
                Log.error(String.format("Cannot load spell %s. Reason: Unknown effect %d", spellInfo.name, effectID));
                removeFromRegistry(spellInfo);
                return;
            }
            EffectInfo effectInfo = new EffectInfo(effectID);
            effectInfo.value1 = rs.getInt(4);
            effectInfo.value2 = rs.getInt(5);
            effectInfos.add(effectInfo);
        }

        EffectInfo[] effects = new EffectInfo[effectInfos.size()];
        effects = effectInfos.toArray(effects);
        spellInfo.setEffects(effects);
    }

    private void loadEffects() {
        effectRegistry = Map.ofEntries(
            entry(Effect.APPLY_AURA, "effectApplyAura"),
            entry(Effect.SCHOOL_DAMAGE, "effectSchoolDamage"),
            entry(Effect.DECREASE_MOVEMENT_SPEED_PERCENT, "effectDecreaseMovementSpeedPercent"),
            entry(Effect.INSTANT_KILL, "effectInstantKill")
        );
    }

    private void addToRegistry(SpellInfo spellInfo) {
        spellRegistry.put(spellInfo.ID, spellInfo);
    }

    private void removeFromRegistry(SpellInfo spellInfo) {
        spellRegistry.remove(spellInfo.ID);
    }

    private void addToRegistry(AuraInfo auraInfo) {
        auraRegistry.put(auraInfo.ID, auraInfo);
    }

    private void removeFromRegistry(AuraInfo auraInfo) {
        auraRegistry.remove(auraInfo.ID);
    }

}
