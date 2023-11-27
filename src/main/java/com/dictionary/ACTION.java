package com.dictionary;

public enum ACTION {
    LADD, LREMOVE, LEDIT, LLOOKUP,
    DADD, DREMOVE, DEDIT, DLOOKUP;

    @Override
    public String toString() {
        return switch (this) {
            case LADD -> "0";
            case LREMOVE -> "1";
            case LEDIT -> "2";
            case LLOOKUP -> "3";
            case DADD -> "4";
            case DREMOVE -> "5";
            case DEDIT -> "6";
            default -> "7";
        };
    }

    public static ACTION parseAction(String s) {
        char action = s.charAt(0);
        return switch (action) {
            case '0' -> LADD;
            case '1' -> LREMOVE;
            case '2' -> LEDIT;
            case '3' -> LLOOKUP;
            case '4' -> DADD;
            case '5' -> DREMOVE;
            case '6' -> DEDIT;
            default -> DLOOKUP;
        };
    }
}
