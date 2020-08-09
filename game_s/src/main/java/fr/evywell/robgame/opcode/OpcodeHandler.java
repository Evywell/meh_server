package fr.evywell.robgame.opcode;

import fr.evywell.robgame.game.handlers.CastSpellHandler;
import fr.evywell.robgame.game.handlers.MovementHandler;
import fr.evywell.robgame.world.character.CharacterListHandler;
import fr.evywell.robgame.world.character.InvokeCharacterClientReadyHandler;
import fr.evywell.robgame.world.character.InvokeCharacterInWorldHandler;

public class OpcodeHandler extends fr.evywell.common.opcode.OpcodeHandler {

    public OpcodeHandler() {
        this.load();
    }

    public void load() {
        this.opcodes.put(Opcode.CMSG_ACCOUNT_CHARACTER_LIST, new CharacterListHandler());
        this.opcodes.put(Opcode.CMSG_INVOKE_CHARACTER_IN_WORLD, new InvokeCharacterInWorldHandler());
        this.opcodes.put(Opcode.CMSG_INVOKE_CHARACTER_CLIENT_READY, new InvokeCharacterClientReadyHandler());
        this.opcodes.put(Opcode.CMSG_MOVE_CHANGE, new MovementHandler());
        this.opcodes.put(Opcode.CMSG_CAST_SPELL, new CastSpellHandler());
    }

}
