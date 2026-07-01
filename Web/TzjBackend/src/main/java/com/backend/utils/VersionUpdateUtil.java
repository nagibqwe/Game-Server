/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.backend.utils;

import java.io.IOException;

/**
 * @author Administrator
 */
public class VersionUpdateUtil {
    protected static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(VersionUpdateUtil.class);
    private static String Version = "20150414";

    public static String versionUpdateKey() {
        return "#" + Version + "#";
    }

    public static String dataSave(String saveString) {
        return dataSave(saveString, 1024);
    }

    public static String dataSave(String saveString, int clen) {
        if (saveString.length() > clen && !saveString.startsWith(versionUpdateKey())) {
            try {
                long beginTime = System.currentTimeMillis();
                String str = versionUpdateKey() + CodedUtil.encodeBase64(ZipUtil.compress(saveString));
                long useTime = System.currentTimeMillis() - beginTime;
                if (useTime > 3000) {
                    log.error("压缩数据所用时间:" + useTime);
                }
                return str;
            } catch (IOException ex) {
                log.error(ex, ex);
            }
            return saveString;
        } else {
            return saveString;
        }
    }

    public static String dataLoad(String loadString) throws Exception {
        if (loadString.startsWith(versionUpdateKey())) {
            long beginTime = System.currentTimeMillis();
            String parseString = loadString.replaceFirst(versionUpdateKey(), "");
            String str = ZipUtil.uncompress(CodedUtil.decodeBase64(parseString));
            long useTime = System.currentTimeMillis() - beginTime;
            if (useTime > 3000) {
                log.error("解压缩数据所用时间:" + useTime);
            }
            return str;
        } else {
            return loadString;
        }
    }
}
