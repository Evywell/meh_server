package fr.evywell.common.utils;

import java.util.Random;

public class RandomString {
    private Random random;
    private char[] buffer;
    private char[] symbols;

    public static final String chars = "AaBbCcDdEeFfG$gHhIiJjKkLlMmNn$OoPpQqRrSsTtUuVvWwX$zYyZz0123456789";

    public RandomString(int length, Random random) {
        this.random = random;
        this.buffer = new char[length];
        this.symbols = chars.toCharArray();
    }

    public RandomString(int length) {
        this(length, new Random());
    }

    public RandomString() {
        this(25, new Random());
    }

    public String nextString() {
        for (int i = 0; i < this.buffer.length; i++) {
            this.buffer[i] = symbols[random.nextInt(symbols.length)];
        }
        return new String(this.buffer);
    }

}
