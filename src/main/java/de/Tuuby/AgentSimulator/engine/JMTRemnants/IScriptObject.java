package de.Tuuby.AgentSimulator.engine.JMTRemnants;

import java.util.Enumeration;

// Interface for the methods that a ScriptObject should have
public interface IScriptObject {
    Enumeration<Object> keys();
    Enumeration<Object> elements();
    Object put(Object key, Object value);
    void putAsItem(Object key, Object value);
    Object get(Object key);
    Object getFromItem(Object key);
    Object getProto();
    void setProto(Object proto);
    Object remove(Object key);
}
