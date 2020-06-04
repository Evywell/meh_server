package fr.evywell.common.opcode;

import java.util.*;

public abstract class OpcodeHandler {

    protected Map<Integer, Handler> opcodes;

    public OpcodeHandler() {
        this.opcodes = new Hashtable<>();
    }

    public abstract void load();

    public Handler getHandler(int opcode) throws Exception {
        if (this.opcodes.containsKey(opcode)) {
            return this.opcodes.get(opcode);
        }

        throw new Exception("No handler found for opcode " + opcode);
    }

}
