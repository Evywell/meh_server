package fr.evywell.robgame.cli;

import fr.evywell.common.cli.CLI;
import fr.evywell.robgame.cli.commands.ExitCommand;
import fr.evywell.robgame.cli.commands.ReloadSpellsCommand;
import fr.evywell.robgame.network.WorldServer;
import fr.evywell.robgame.world.World;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WorldServerCLI extends CLI {

    private World world;
    private WorldServer worldServer;
    private InputStream in;
    private Thread thread;
    private boolean running;
    private List<Byte> buffer;

    public WorldServerCLI(World world, WorldServer worldServer, InputStream in) {
        this.world = world;
        this.worldServer = worldServer;
        this.in = in;
        running = false;
        buffer = new CopyOnWriteArrayList<>();
    }

    public WorldServerCLI(World world, WorldServer worldServer) {
        this(world, worldServer, System.in);
    }

    public void initCommands() {
        addCommand(new ReloadSpellsCommand())
        .addCommand(new ExitCommand(worldServer));
    }

    public void handle() {
        running = true;
        WorldServerCLI cli = this;
        System.out.print("> ");
        thread = new Thread((new Runnable() {
            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(500);
                        if (isCommandLaunched()) {
                            cli.executeCommand(getCommand());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }));
        thread.start();
    }

    private String getCommand() {
        try {
            int len = in.available();
            if (len < 2) {
                // Il n'y a qu'un retour à la ligne
                return "";
            }
            byte[] buffer = new byte[len - 1];
            in.read(buffer, 0, len - 1);
            in.skip(len);
            String data =  new String(buffer, StandardCharsets.UTF_8); // On retire le \n du buffer
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isCommandLaunched() {
        try {
            return in.available() > 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void shutdown() {
        running = false;
    }
}
