package fr.evywell.common.network;

import com.jsoniter.output.JsonStream;

import java.util.HashMap;
import java.util.Map;

public class Packet {

    private int _cmd;
    private Map<String, Object> data = new HashMap<>();

    public Packet add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public void setCmd(int _cmd) {
        this._cmd = _cmd;
    }

    public String toJson() {
        StringBuilder builder = new StringBuilder();
        builder.append("[").append(this._cmd).append(",");
        builder.append(JsonStream.serialize(this.data));
        builder.append("]");
        return builder.toString();
    }
}
