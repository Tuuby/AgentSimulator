package engine.JMTRemnants;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.Arrays;

public class Item implements Serializable {
    Object obj;
    Object field;

    Item(Object o) {
        obj = o;
        field = null;
    }

    Item(Object o, Object f) {
        obj = o;
        field = f;
    }

    final Item lVal() {
        Item it = this;
        Object obj = this.obj;
        while (obj instanceof ScriptObject) {
            ScriptObject lo = (ScriptObject)obj;
            it = (Item)lo.get(ScriptObject.PROTO_SLOT);
            if(it != null)
                break;
            else {
                it = (Item)lo.get(ScriptObject.PROTO_SLOT);
                if (it != null)
                    obj = it.obj;
                else
                    return null;
            }
        }
        this.obj = obj;
        return it;
    }

    final Object rVal() throws NoSuchFieldException, IllegalAccessException {
        if (field instanceof String) {
            Item it = lVal();
            if (it == null)
                throw new NullPointerException(((ScriptObject)this.obj).getName() + "." + field);

            if (obj instanceof ScriptObject)
                return it.obj;
            else {
                Class cl = obj instanceof Class ? (Class)obj : obj.getClass();
                if (cl.isArray() && field.equals("length"))
                    return Array.getLength(obj);
                try {
                    Field f = cl.getField((String)field);
                    return f.get(obj);
                } catch (NoSuchFieldException e) {
                    cl = getInnerClass(cl, (String)field);
                    if (cl != null)
                        return cl;
                    else
                        throw new NoSuchFieldException(Arrays.toString(e.getStackTrace()));
                }
            }
        } else if (field != null) {
            return Array.get(obj, (Integer)field);
        }
        else
            return obj;
    }

    final boolean exists() {
        if(field instanceof String) {
            if(obj instanceof ScriptObject)
                return ((ScriptObject)obj).containsKey(field);
            else {
                Class cl = obj instanceof Class ? (Class)obj : obj.getClass();
                try {
                    cl.getField((String)field);
                    return true;
                }
                catch(NoSuchFieldException e) {
                    return false;
                }
            }
        } else if(field instanceof Integer) {
            int index = (Integer) field;
            return 0 <= index && index < Array.getLength(obj);
        }
        else
            return obj != null && field == null;
    }

    final Item assign(Object rhs) throws NoSuchFieldException, IllegalAccessException {
        if(field instanceof String) {
            if(obj instanceof ScriptObject) {
                ((ScriptObject)obj).putAsItem(field, rhs);
            } else {
                Class cl = obj instanceof Class ? (Class)obj : obj.getClass();
                Field f = cl.getField((String)field);
                f.set(obj, rhs);
            }
        } else if(field != null) {
            Array.set(obj, (Integer) field, rhs);
        } else
            obj = rhs;
        return this;
    }

    final Item mult(Object y) throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        Object x = rVal();
        try {
            Number a = (Number)x, b = (Number)y;
            if((a instanceof Integer || a instanceof Long) && (b instanceof Integer || b instanceof Long)) {
                long res = a.longValue() * b.longValue();
                if (res < Integer.MIN_VALUE || Integer.MAX_VALUE < res)
                    return assign(res);
                else
                    return assign((int) res);
            } else
                return assign(a.doubleValue() * b.doubleValue());
        } catch(ClassCastException e) {
            throw new InvalidParameterException("The Operands are incompatible");
        }
    }

    final Item div(Object y) throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        Object x = rVal();
        if(x instanceof Number && y instanceof Number) {
            Number a = (Number)x, b = (Number)y;
            if(a instanceof Integer) {
                if(b instanceof Integer)
                    return assign(a.intValue() / b.intValue());
                else if(b instanceof Long)
                    return assign((int) (a.longValue() / b.longValue()));
                else
                    return assign(a.doubleValue() / b.doubleValue());
            } else if(a instanceof Long) {
                if(b instanceof Integer || b instanceof Long)
                    return assign(a.longValue() / b.longValue());
                else
                    return assign(a.doubleValue() / b.doubleValue());
            } else
                return assign(a.doubleValue() / b.doubleValue());
        }
        else
            throw new InvalidParameterException("The Operands are incompatible");
    }

    final Item mod(Object y) throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        Object x = rVal();
        if(x instanceof Number && y instanceof Number) {
            Number a = (Number)x, b = (Number)y;
            if(b instanceof Integer) {
                if(a instanceof Integer)
                    return assign(a.intValue() % b.intValue());
                else if(a instanceof Long)
                    return assign((int) (a.longValue() % b.longValue()));
                else
                    return assign(a.doubleValue() % b.doubleValue());
            } else if(b instanceof Long) {
                if(a instanceof Integer || a instanceof Long)
                    return assign(a.longValue() % b.longValue());
                else
                    return assign(a.doubleValue() % b.doubleValue());
            } else
                return assign(a.doubleValue() % b.doubleValue());
        }
        else
            throw new InvalidParameterException("The Operands are incompatible");
    }

    final Item add(Object y) throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        Object x = rVal();
        if(x instanceof Number && y instanceof Number) {
            Number a = (Number)x, b = (Number)y;
            if((a instanceof Integer || a instanceof Long) && (b instanceof Integer || b instanceof Long)) {
                long res = a.longValue() + b.longValue();
                if(res < Integer.MIN_VALUE || Integer.MAX_VALUE < res)
                    return assign(res);
                else
                    return assign((int) res);
            } else
                return assign(a.doubleValue() + b.doubleValue());
        } else if(x instanceof String) {
            return assign((String)x + y);
        } else if(y instanceof String) {
            return assign(x.toString() + y);
        }
        else
            throw new InvalidParameterException("The Operands are incompatible");
    }

    final Item sub(Object y) throws NoSuchFieldException, IllegalAccessException, InvalidParameterException {
        Object x = rVal();
        if(x instanceof Number && y instanceof Number) {
            Number a = (Number)x, b = (Number)y;
            if((a instanceof Integer || a instanceof Long) && (b instanceof Integer || b instanceof Long)) {
                long res = a.longValue() - b.longValue();
                if(res < Integer.MIN_VALUE || Integer.MAX_VALUE < res)
                    return assign(res);
                else
                    return assign((int) res);
            } else
                return assign(a.doubleValue() - b.doubleValue());
        }
        else
            throw new InvalidParameterException("The Operands are incompatible");
    }

    final int compareTo(Object y) throws NoSuchFieldException, IllegalAccessException, NullPointerException, InvalidParameterException {
        Object x = rVal();
        if(x == null)
            throw new NullPointerException("The object is null");
        else if(x.equals(y))
            return 0;

        if(x instanceof Number && y instanceof Number) {
            Number a = (Number)x, b = (Number)y;
            if((a instanceof Integer || a instanceof Long) && (b instanceof Integer || b instanceof Long))
                return a.longValue() < b.longValue() ? -1 : 1;
            else
                return a.doubleValue() < b.doubleValue() ? -1 : 1;
        } else if(x instanceof Character && y instanceof Character) {
            Character a = (Character)x, b = (Character)y;
            return a < b ? -1 : 1;
        } else if(x instanceof String && y instanceof String) {
            String a = (String)x, b = (String)y;
            return a.compareTo(b);
        }
        else
            throw new InvalidParameterException("The Operands are incompatible");
    }

    public static Class getInnerClass(Class c, String name) {
        Class[] inners = c.getClasses();
        for (Class inner : inners)
            if (inner.getName().equals(c.getName() + "$" + name))
                return inner;
        return null;
    }

    public String toString() {
        if (obj == null)
            return "null";
        else if (obj instanceof ScriptObject)
            return ((ScriptObject)obj).getName() + (field != null ? ("." + field) : "");
        else
            return obj.toString() + (field != null ? ("." + field) : "");
    }
}

class Break extends Item {
    private static Break breakIt = new Break();

    Break() {
        super(null);
    }

    Break(Object obj) {
        super(obj);
    }

    public static Break breakItem() {
        return breakIt;
    }
}

class Return extends Break {
    Return(Object obj) {
        super(obj);
    }
}