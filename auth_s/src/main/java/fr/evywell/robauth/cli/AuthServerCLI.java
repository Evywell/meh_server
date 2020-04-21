package fr.evywell.robauth.cli;

import fr.evywell.common.cli.CLI;
import fr.evywell.robauth.network.AuthServer;

public class AuthServerCLI extends CLI {

    public AuthServerCLI(AuthServer server) {
        this.addCommand(new ExitCommand(server));
    }

}
