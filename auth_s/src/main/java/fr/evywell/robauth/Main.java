package fr.evywell.robauth;

import fr.evywell.common.config.Config;
import fr.evywell.common.container.Container;
import fr.evywell.common.container.Service;
import fr.evywell.common.database.Database;
import fr.evywell.common.logger.Log;
import fr.evywell.common.network.BasicServerInitializer;
import fr.evywell.common.network.Server;
import fr.evywell.robauth.cli.AuthServerCLI;
import fr.evywell.robauth.config.AuthConfigHandler;
import fr.evywell.robauth.config.DatabaseConfigHandler;
import fr.evywell.robauth.network.AuthServer;
import fr.evywell.robauth.network.AuthServerHandler;

import java.io.File;
import java.util.Scanner;

public class Main {

    private static String resourcesPath;
    private static Config config;

    public static void main(String[] args) {
        // Pré configuration
        String projectPath = System.getProperty("user.dir");
        resourcesPath = projectPath + File.separator + "resources" + File.separator;
        config = new Config(resourcesPath + "config.json");
        config
            .configHandler(new AuthConfigHandler())
            .configHandler(new DatabaseConfigHandler());

        AuthConfigHandler.AuthConfig authConfig = (AuthConfigHandler.AuthConfig) config.get("auth");
        AuthServer server = new AuthServer(authConfig.port);
        server.handle(new BasicServerInitializer(null, new AuthServerHandler(server)));
        try {
            startMySQL();
            server.start();
            AuthServerCLI cli = new AuthServerCLI(server);
            Scanner scanner = new Scanner(System.in);
            while (server.isRunning()) {
                System.out.print("> ");
                String command = scanner.nextLine();
                cli.executeCommand(command);
            }
            stopMySQL();
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startMySQL() {
        DatabaseConfigHandler.DatabaseConfig db = (DatabaseConfigHandler.DatabaseConfig) config.get("database");
        Database database = new Database(db.host, db.port, db.database, db.user, db.password);
        Log.info("Connexion à la base de données MySQL...");
        if (database.connect()) {
            Log.info("Connexion à la base de données MySQL réussie");
        } else {
            Log.info("Connexion à la base de données MySQL échouée");
        }

        // On stock l'instance dans le container
        Container.setInstance(Service.SERVICE_DATABASE_AUTH, database);
    }

    private static void stopMySQL() {
        Database database = (Database) Container.getInstance(Service.SERVICE_DATABASE_AUTH);
        Log.info("Fermeture de la connexion à la base de données MySQL...");
        database.disconnect();
    }

}