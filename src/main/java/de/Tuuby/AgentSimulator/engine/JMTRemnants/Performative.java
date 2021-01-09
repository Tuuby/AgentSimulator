package de.Tuuby.AgentSimulator.engine.JMTRemnants;

import java.util.Enumeration;
import java.util.Vector;

// Java representation of the KQML concept Performative
public class Performative extends ScriptObject {

    // Constructor for a new Performative with a parameter for the type
    public Performative(String type) {
        super(type);
    }

    // Constructor for a new Performative with 2 parameters for the type and the content
    public Performative(String type, Object content) {
        super(type);
        put(":content", content);
    }

    // Method to return the type of Performative
    public String getType() {
        return getName();
    }

    // Method to write the whole Performative to a String object
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append("(").append(getName());
        Enumeration<Object> keyElems = this.keys();

        while (keyElems.hasMoreElements()) {
            Object key = keyElems.nextElement();
            res.append(" ").append(key.toString()).append(" ");
            Object elem = this.get(key);

            if (elem instanceof Vector) {
                int i;
                Vector v = (Vector)elem;
                res.append("(");

                for (i = 0; i < v.size() - 1; i++)
                    res.append(v.elementAt(i).toString()).append(" ");

                if (i < v.size())
                    res.append(v.elementAt(i).toString()).append(")");

                else
                    res.append(")");
            } else
                res.append(elem.toString());
        }
        res.append(")");
        return res.toString();
    }
}
