package fr.evywell.common.network;

public class MalformedRequestException extends Exception {

    public MalformedRequestException(String message) {
        super(message);
    }

    public MalformedRequestException() {
        super();
    }

}
