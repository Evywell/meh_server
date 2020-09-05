package fr.evywell.robgame.cli.commands;

import fr.evywell.common.cli.CommandInterface;
import fr.evywell.robgame.network.WorldServer;

public class ExitCommand implements CommandInterface {

    private WorldServer worldServer;

    public ExitCommand(WorldServer worldServer) {
        this.worldServer = worldServer;
    }

    @Override
    public boolean canHandle(String command) {
        return "exit".equals(command);
    }

    @Override
    public void handle(String command) {
        worldServer.stop();
    }
}
