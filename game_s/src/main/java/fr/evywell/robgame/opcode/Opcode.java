package fr.evywell.robgame.opcode;

public class Opcode {
    /*
     * CMSG: CLIENT MESSAGE
     * SMSG: SERVER MESSAGE
     */

    // CHALLENGES 0 - 99
    public static final int WORLD_AUTHENTICATION_CHALLENGE = 0x00;

    // ERRORS 100 - 199
    public static final int WORLD_AUTHENTICATION_CHALLENGE_FAILED = 0x64; // 100
    public static final int SMSG_GET_ACCOUNT_CHARACTER_LIST_ERROR = 0x65;

    // ACCOUNT MANAGEMENT 200 - 299
    public static final int CMSG_ACCOUNT_CHARACTER_LIST = 0xC8; // 200
    public static final int SMSG_ACCOUNT_CHARACTER_LIST = 0xC9;

}
