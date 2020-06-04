package fr.evywell.robclient.opcode;

import fr.evywell.robclient.Application;
import fr.evywell.robclient.auth.LoginHandler;
import fr.evywell.robclient.auth.TicketHandler;

public class AuthOpcodeHandler extends fr.evywell.common.opcode.OpcodeHandler {

    private Application app;

    public AuthOpcodeHandler(Application app) {
        super();
        this.app = app;
        this.load();
    }

    @Override
    public void load() {
        this.opcodes.put(OpcodeAuth.AUTH_LOGIN_SUCCEED, new LoginHandler(app, true));
        this.opcodes.put(OpcodeAuth.AUTH_LOGIN_FAILED, new LoginHandler(app, false));
        this.opcodes.put(OpcodeAuth.AUTH_OBTAIN_TICKET, new TicketHandler(app));
    }
}
