package de.Tuuby.AgentSimulator.engine.JMTRemnants;

import java.util.Hashtable;

// Necessary for Performative
public class ScriptObject extends Hashtable<Object, Object> implements IScriptObject {

    final static String PROTO_SLOT = "proto";
    private String name;

    public ScriptObject(String id) {
        super();
        name = id;
    }

    public ScriptObject(String id, int initialSize) {
        super(initialSize);
        name = id;
    }

    public void putAsItem(Object key, Object value) {
        put(key, new Item(value));
    }

    public Object getFromItem(Object key) {
        Item item = (Item)get(key);
        return item != null ? item.obj : null;
    }

    public Object getProto() {
        return getFromItem(PROTO_SLOT);
    }

    public void setProto(Object proto) {
        putAsItem(PROTO_SLOT, proto);
    }

    public String getName() {
        return name;
    }
}
