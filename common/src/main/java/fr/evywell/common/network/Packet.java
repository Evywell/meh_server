package fr.evywell.common.network;

import java.nio.ByteBuffer;

public class Packet {

    private ByteBuffer readableBuffer;
    private int currentPos;
    private int opcode = 0;
    private int length;


    public Packet(byte[] buffer) {
        this.readableBuffer = ByteBuffer.wrap(buffer);
        this.currentPos = 0;
        this.length = buffer.length;
        if (buffer.length >= 4) {
            this.opcode = readableBuffer.getInt();
        }
    }

    public Packet() {
        this.readableBuffer = ByteBuffer.allocate(4096);
        this.currentPos = 0; // Après l'opcode
        this.length = 0;
    }

    public Packet(int opcode) {
        this();
        this.opcode = opcode;
        putInt(opcode);
    }

    public int getOpcode() {
        return opcode;
    }

    public String readString() {
        int length = readInt(); // La taille de la chaine de caractères
        byte[] bytes = new byte[length];
        readableBuffer.get(bytes, 0, length);
        currentPos += length;
        return new String(bytes);
    }

    public int readInt() {
        int value = readableBuffer.getInt();
        currentPos += 4; // 4 octets = 32 bits
        return value;
    }

    public float readFloat() {
        float value = readableBuffer.getFloat();
        currentPos += 4;
        return value;
    }

    public long readLong() {
        long value = readableBuffer.getLong();
        currentPos += 8;
        return value;
    }

    public Packet putString(String value) {
        byte[] bytes = value.getBytes();
        readableBuffer.putInt(bytes.length); // On précise la taille de la chaine
        readableBuffer.put(bytes); // On écrit la chaine
        length += 4 + bytes.length;
        return this;
    }

    public Packet putInt(int value) {
        readableBuffer.putInt(value);
        length += 4;
        return this;
    }

    public Packet putLong(long value) {
        readableBuffer.putLong(value);
        length += 8;
        return this;
    }

    public Packet putFloat(float value) {
        readableBuffer.putFloat(value);
        length += 4;
        return this;
    }

    public byte[] getBytes() {
        return readableBuffer.array();
    }

}
