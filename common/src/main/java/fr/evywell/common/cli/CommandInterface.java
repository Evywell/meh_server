package fr.evywell.common.cli;

public interface CommandInterface {

    boolean canHandle(String command);
    void handle(String command);

}
