package com.xywy.activityrouter;

/**
 * Created by bobby on 16/11/22.
 */
public class ExtraTypes {
    public static final int STRING = -1;
    public static final int INT = 1;
    public static final int LONG = 2;
    public static final int BOOL = 3;
    public static final int SHORT = 4;
    public static final int FLOAT = 5;
    public static final int DOUBLE = 6;
    public static final int BYTE = 7;
    public static final int CHAR = 8;
    public static final int SERIALIZABLE = 9;
    private String[] intExtra;
    private String[] longExtra;
    private String[] booleanExtra;
    private String[] shortExtra;
    private String[] floatExtra;
    private String[] doubleExtra;
    private String[] byteExtra;
    private String[] charExtra;
    private String[] serializableExtra;

    public String[] getIntExtra() {
        return intExtra;
    }

    public void setIntExtra(String... intExtra) {
        this.intExtra = intExtra;
    }

    public String[] getLongExtra() {
        return longExtra;
    }

    public void setLongExtra(String... longExtra) {
        this.longExtra = longExtra;
    }

    public String[] getBooleanExtra() {
        return booleanExtra;
    }

    public void setBooleanExtra(String... booleanExtra) {
        this.booleanExtra = booleanExtra;
    }

    public String[] getShortExtra() {
        return shortExtra;
    }

    public void setShortExtra(String... shortExtra) {
        this.shortExtra = shortExtra;
    }

    public String[] getFloatExtra() {
        return floatExtra;
    }

    public void setFloatExtra(String... floatExtra) {
        this.floatExtra = floatExtra;
    }

    public String[] getDoubleExtra() {
        return doubleExtra;
    }

    public void setDoubleExtra(String... doubleExtra) {
        this.doubleExtra = doubleExtra;
    }

    public String[] getByteExtra() {
        return byteExtra;
    }

    public void setByteExtra(String... byteExtra) {
        this.byteExtra = byteExtra;
    }

    public String[] getCharExtra() {
        return charExtra;
    }

    public void setCharExtra(String[] charExtra) {
        this.charExtra = charExtra;
    }

    public String[] getSerializableExtra() {
        return serializableExtra;
    }

    public void setSerializableExtra(String[] serializableExtra) {
        this.serializableExtra = serializableExtra;
    }

    public int getType(String name) {
        if (arrayContain(intExtra, name)) {
            return INT;
        }
        if (arrayContain(longExtra, name)) {
            return LONG;
        }
        if (arrayContain(booleanExtra, name)) {
            return BOOL;
        }
        if (arrayContain(shortExtra, name)) {
            return SHORT;
        }
        if (arrayContain(floatExtra, name)) {
            return FLOAT;
        }
        if (arrayContain(doubleExtra, name)) {
            return DOUBLE;
        }
        if (arrayContain(byteExtra, name)) {
            return BYTE;
        }
        if (arrayContain(charExtra, name)) {
            return CHAR;
        }
        if(arrayContain(serializableExtra, name)) {
            return SERIALIZABLE;
        }
        return STRING;
    }

    private static boolean arrayContain(String[] array, String value) {
        if (array == null) {
            return false;
        }
        for (String s : array) {
            if (s.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
