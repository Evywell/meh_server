package fr.evywell.robgame;

import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.AuthClient;
import fr.evywell.common.network.BasicServerInitializer;
import fr.evywell.common.network.Packet;
import fr.evywell.robgame.cli.WorldServerCLI;
import fr.evywell.robgame.config.*;
import fr.evywell.robgame.network.GameServerHandler;
import fr.evywell.robgame.network.WorldServer;
import fr.evywell.robgame.world.World;
import io.netty.buffer.ByteBuf;

import java.io.File;

public class Main {

    private static String resourcesPath;
    private static Config config;
    private static WorldServer server;

    public static void main(String[] args) throws Exception {
        // Chargement du fichier de configuration
        String projectPath = System.getProperty("user.dir");
        resourcesPath = projectPath + File.separator + "resources" + File.separator;
        config = new Config(resourcesPath + "config.json");
        config.setResourcePath(resourcesPath);
        config
            .configHandler(new RealmConfigHandler()) // Gère la configuration du realm
            .configHandler(new VMapConfigHandler()) // Gère la configuration des vmaps
            .configHandler(new DatabaseConfigHandler()) // Gère la configuration des bases de données
            .configHandler(new AuthConfigHandler()); // Gère la configuration du serveur d'authentification

        Container.setInstance(fr.evywell.robgame.Service.CONFIG, config);

        World world = new World(config);
        Container.setInstance(fr.evywell.robgame.Service.WORLD, world);
        server = new WorldServer(world, 1337);
        server.handle(new BasicServerInitializer(null, new GameServerHandler(server)));
        server.start();

        WorldServerCLI worldServerCLI = new WorldServerCLI(world, server);
        worldServerCLI.initCommands();
        initAuthSecret();

        startMysql();
        world.start();
        worldServerCLI.handle();
        server.loop();

        System.out.println("System halting");
        worldServerCLI.shutdown();
        System.exit(0);
    }

    public static void initAuthSecret() {
        Log.info("Récupération de la clé secret auprès du serveur d'authentification...");
        AuthConfig authConfig = (AuthConfig) config.get("auth");
        AuthClient client = new AuthClient(authConfig.ip, authConfig.port);
        Packet pck = new Packet(1);
        pck.putString("a_client_id");
        client.setClientId(authConfig.clientId);
        try {
            client.connect();
            ByteBuf buffer = client.sendClientIdRequest(pck).get();
            byte[] bytes;
            int length = buffer.readableBytes();

            if (buffer.hasArray()) {
                bytes = buffer.array();
            } else {
                bytes = new byte[length];
                buffer.getBytes(buffer.readerIndex(), bytes);
            }
            Packet packet = new Packet(bytes);
            server.setSecret(packet.readString());
            Log.info("Clé secret ajoutée au serveur");
            client.close();
        } catch (Exception e) {
            Log.error("Impossible de contacter le serveur d'authentification pour récupérer la clé secret");
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }

    public static void startMysql() {
        DatabaseConfig dbConfig = (DatabaseConfig) config.get("database");
        // Base Characters
        Database databaseChars = new Database(
            dbConfig.characters.host,
            dbConfig.characters.port,
            dbConfig.characters.database,
            dbConfig.characters.user,
            dbConfig.characters.password
        );
        Log.info("Connexion à la base de données MySQL...");
        if (databaseChars.connect()) {
            Log.info("Connexion à la base de données MySQL (characters) réussie");
        } else {
            Log.info("Connexion à la base de données MySQL (characters) échouée");
        }

        // On stock l'instance dans le container
        Container.setInstance(Service.SERVICE_DATABASE_CHARACTERS, databaseChars);
        // Base Characters
        Database databaseWorld = new Database(
            dbConfig.world.host,
            dbConfig.world.port,
            dbConfig.world.database,
            dbConfig.world.user,
            dbConfig.world.password
        );
        Log.info("Connexion à la base de données MySQL...");
        if (databaseWorld.connect()) {
            Log.info("Connexion à la base de données MySQL (world) réussie");
        } else {
            Log.info("Connexion à la base de données MySQL (world) échouée");
        }

        // On stock l'instance dans le container
        Container.setInstance(Service.SERVICE_DATABASE_WORLD, databaseWorld);
    }

    public static void stopMysql()
    {
        Log.info("Arrêt du service MySQL");
        ((Database) Container.getInstance(Service.SERVICE_DATABASE_CHARACTERS)).disconnect();
    }

}
