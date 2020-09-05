package fr.evywell.robgame.cli.commands;

import fr.evywell.common.cli.CommandInterface;
import fr.evywell.common.container.Container;
import fr.evywell.common.logger.Log;
import fr.evywell.robgame.Service;
import fr.evywell.robgame.game.spell.SpellManager;

public class ReloadSpellsCommand implements CommandInterface {

    @Override
    public boolean canHandle(String command) {
        return "reloadspells".equals(command);
    }

    @Override
    public void handle(String command) {
        SpellManager spellManager = (SpellManager) Container.getInstance(Service.SPELL_MANAGER);
        if (spellManager == null) {
            Log.error("Cannot reload spells. Reason: SpellManager not available");
            return;
        }
        Log.info("Rechargement du système de sorts...");
        spellManager.reload();
        Log.info("Rechargement du système de sorts terminé !");
    }
}
