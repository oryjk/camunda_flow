package com.betalpha.fosun.user;

import java.util.HashMap;
import java.util.Map;

public final class MockUserUtils {

    private MockUserUtils() {
    }

    private static final HashMap<String, String> pUserMap = new HashMap<>();

    static {
        pUserMap.put("p1", "P_user_01");
        pUserMap.put("p2", "P_user_02");
        pUserMap.put("p3", "P_user_03");
    }

    private static final HashMap<String, String> vUserMap = new HashMap<>();

    static {
        vUserMap.put("v1", "V_user_01");
        vUserMap.put("v2", "V_user_02");
        vUserMap.put("v3", "V_user_03");
    }


    private static final HashMap<String, String> iUserMap = new HashMap<>();

    static {
        iUserMap.put("i1", "I_user_01");
        iUserMap.put("i2", "I_user_02");
        iUserMap.put("i3", "I_user_03");
    }

    private static final HashMap<String, String> aUserMap = new HashMap<>();

    static {
        aUserMap.put("a1", "A_user");
    }

    private static final HashMap<String, String> bUserMap = new HashMap<>();

    static {
        bUserMap.put("b1", "B_user");
    }

    public static Map<String, String> getIUserMap() {
        return iUserMap;
    }

    public static Map<String, String> getPUserMap() {
        return pUserMap;
    }

    public static Map<String, String> getVUserMap() {
        return vUserMap;
    }

    public static Map<String, String> getAUserMap() {
        return aUserMap;
    }

    public static Map<String, String> getBUserMap() {
        return bUserMap;
    }
}
