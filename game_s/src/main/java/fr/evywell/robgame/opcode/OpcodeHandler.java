package fr.evywell.robgame.opcode;

import fr.evywell.robgame.world.character.CharacterListHandler;

import java.util.*;

import static fr.evywell.robgame.opcode.Opcode.CMSG_ACCOUNT_CHARACTER_LIST;

public class OpcodeHandler {

    private Map<Integer, Handler> opcodes;

    public OpcodeHandler() {
        this.opcodes = new Hashtable<>();
        this.load();
    }

    private void load() {
        this.opcodes.put(CMSG_ACCOUNT_CHARACTER_LIST, new CharacterListHandler());
    }

    public Handler getHandler(int opcode) throws Exception {
        if (this.opcodes.containsKey(opcode)) {
            return this.opcodes.get(opcode);
        }

        throw new Exception("No handler found for opcode " + opcode);
    }

}
