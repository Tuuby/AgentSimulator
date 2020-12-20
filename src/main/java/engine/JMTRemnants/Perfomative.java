package engine.JMTRemnants;

import java.util.Enumeration;
import java.util.Vector;

public class Perfomative extends ScriptObject {

    public Perfomative(String type) {
        super(type);
    }

    public Perfomative(String type, int content) {
        super(type);
        put(":contenct", content);
    }

    public String getType() {
        return getName();
    }

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
