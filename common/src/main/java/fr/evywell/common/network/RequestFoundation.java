package fr.evywell.common.network;

import com.jsoniter.JsonIterator;

import java.io.IOException;

public class RequestFoundation {

    private int _cmd;
    private JsonIterator body;

    public RequestFoundation(int _cmd, JsonIterator body) {
        this._cmd = _cmd;
        this.body = body;
    }

    public static RequestFoundation fromString(String buffer) throws IOException {
        JsonIterator iter = JsonIterator.parse(buffer);
        iter.readArray();
        int _cmd = iter.readInt();
        iter.readArray();
        return new RequestFoundation(_cmd, iter);
    }

    public int getCmd() {
        return _cmd;
    }

    public JsonIterator getBody() {
        return body;
    }
}
