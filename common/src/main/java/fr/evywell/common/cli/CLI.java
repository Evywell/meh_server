package fr.evywell.common.cli;

import java.util.ArrayList;
import java.util.List;

public class CLI {

    private List<CommandInterface> commands = new ArrayList<CommandInterface>();

    public CLI addCommand(CommandInterface command) {
        this.commands.add(command);
        return this;
    }

    public void executeCommand(String command) {
        for (CommandInterface c : this.commands) {
            if (c.canHandle(command)) {
                c.handle(command);
                return;
            }
        }
    }

}
