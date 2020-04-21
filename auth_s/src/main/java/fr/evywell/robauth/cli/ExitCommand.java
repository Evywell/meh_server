package fr.evywell.robauth.cli;

import fr.evywell.common.cli.CommandInterface;
import fr.evywell.robauth.network.AuthServer;

public class ExitCommand implements CommandInterface {

    private AuthServer server;

    public ExitCommand(AuthServer server) {
        this.server = server;
    }

    @Override
    public boolean canHandle(String command) {
        return "exit".equals(command);
    }

    @Override
    public void handle(String command) {
        this.server.stop();
    }
}
