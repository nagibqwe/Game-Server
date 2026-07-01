package com.gm.project.gmtool.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ServerKeyUtil {

    private static final String confFile = "conf.properties";

    public static String getKey(String name) {
        InputStream inStream = ServerKeyUtil.class.getClassLoader().getResourceAsStream(confFile);
        Properties prop = new Properties();
        try {
            prop.load(inStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (String) prop.getOrDefault(name, "");
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
