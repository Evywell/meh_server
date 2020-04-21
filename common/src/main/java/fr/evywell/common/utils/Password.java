package fr.evywell.common.utils;

import org.mindrot.jbcrypt.BCrypt;

public class Password {

    private int cost;

    public Password(int cost) {
        this.cost = cost;
    }

    public Password() {
        this(12);
    }

    public String hash(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }

    public String hash(String password) {
        return this.hash(password, BCrypt.gensalt(this.cost));
    }

    public boolean verify(String password, String hash) {
        return BCrypt.checkpw(password, hash);
    }

}
