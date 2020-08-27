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

    // CMSG

    // ACCOUNT MANAGEMENT 200 - 299
    public static final int CMSG_ACCOUNT_CHARACTER_LIST = 0xC8; // 200

    // WORLD
    public static final int CMSG_INVOKE_CHARACTER_IN_WORLD = 0x12C; // 300
    public static final int CMSG_INVOKE_CHARACTER_CLIENT_READY = 0x12E; // 302
    // MOVING
    public static final int CMSG_MOVE_CHANGE = 0x131; // 305
    public static final int CMSG_MOVE_STOP = 0x132; // 311
    public static final int CMSG_CAST_SPELL = 0x133;

    // SMSG
    public static final int SMSG_ACCOUNT_CHARACTER_LIST = 0xC9;
    public static final int SMSG_INVOKE_CHARACTER_IN_WORLD = 0x12D; // 301
    public static final int SMSG_INVOKE_NEW_CHARACTER_IN_WORLD = 0x12E; // 302: Message envoyé aux personnes proches du nouveau joueur
    public static final int SMSG_INVOKE_CHARACTER_CLIENT_READY = 0x12F; // 303
    public static final int SMSG_NOTIFY_PLAYER_GAME_OBJECT_STATE = 0x130; // 304
    public static final int SMSG_MOVE_UPDATE = 0x131; // 305
    public static final int SMSG_GAME_OBJECT_INFO = 0x132; // 306
    public static final int SMSG_UPDATE_OBJECT = 0x133; // 307

    public static final int SMSG_COMBAT_KILL = 0x258; // 600

}
