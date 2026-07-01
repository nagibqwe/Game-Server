package com.backend.utils;

import org.nutz.ioc.impl.PropertiesProxy;

public class ServerKeyUtil {

    private static final String confFile = "/conf/conf.properties";

    public static String getKey(String name) {
        PropertiesProxy property = new PropertiesProxy(confFile);
        return property.getOrDefault(name, "");
    }

    public static String getGameID() {
        return getKey("gameID");
    }

    public static String getPlatUriRootPath() {
        return getKey("GmpInternelUrl");
    }

    public static String GetIP() {
        return getKey("IP");
    }

    public static String GetRequestKey() {
        return getKey("GETREQUESTKEY");
    }

    public static String GetLSRequestKey() {
        return getKey("REQUESTLOGINSERVERKEY");
    }

    public static String GetPSRuquestKey() {
        return getKey("REQUESTPUBLICSERVERKEY");
    }

    public static String GetXingeAccessId(String type) {
        String key = "";
        if (type.equals("android")) {
            key = "XingeAccessId";
        } else if (type.equals("ios")) {
            key = "XingeAccessIDIOS";
        }
        return getKey(key);
    }

    public static String GetXingeSecretKey(String type) {
        String key = "";
        if (type.equals("android")) {
            key = "XingeSecretKey";
        } else if (type.equals("ios")) {
            key = "XingeKeyIOS";
        }
        return getKey(key);
    }

}
