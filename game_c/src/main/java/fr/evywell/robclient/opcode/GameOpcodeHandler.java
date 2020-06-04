package fr.evywell.robclient.opcode;

import fr.evywell.common.opcode.OpcodeHandler;
import fr.evywell.robclient.Application;
import fr.evywell.robclient.game.character.AccountCharacterListHandler;
import fr.evywell.robclient.game.character.InvokeCharacterClientReadyHandler;
import fr.evywell.robclient.game.character.InvokeCharacterInWorldHandler;
import fr.evywell.robclient.game.gameobject.NotifyPlayerGameObjectStateHandler;

public class GameOpcodeHandler extends OpcodeHandler {

    private Application app;

    public GameOpcodeHandler(Application app) {
        super();
        this.app = app;
        this.load();
    }

    @Override
    public void load() {
        this.opcodes.put(OpcodeGame.SMSG_ACCOUNT_CHARACTER_LIST, new AccountCharacterListHandler(app));
        this.opcodes.put(OpcodeGame.SMSG_INVOKE_CHARACTER_IN_WORLD, new InvokeCharacterInWorldHandler(app));
        this.opcodes.put(OpcodeGame.SMSG_INVOKE_CHARACTER_CLIENT_READY, new InvokeCharacterClientReadyHandler(app));
        this.opcodes.put(OpcodeGame.SMSG_NOTIFY_PLAYER_GAME_OBJECT_STATE, new NotifyPlayerGameObjectStateHandler(app));
    }
}
