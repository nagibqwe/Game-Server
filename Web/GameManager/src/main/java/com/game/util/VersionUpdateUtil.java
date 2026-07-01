package com.game.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 * @author Administrator
 */
public class VersionUpdateUtil
{
    protected static Logger log = LoggerFactory.getLogger(VersionUpdateUtil.class);
    private static final String Version = "20150414";

    public static String versionUpdateKey()
    {
        return "#" + Version + "#";
    }

    public static String dataSave(String saveString)
    {
        return dataSave(saveString, 1024);
    }

    public static String dataSave(String saveString, int clen)
    {
        if (saveString.length() > clen && !saveString.startsWith(versionUpdateKey()))
        {
            try
            {
                String str = versionUpdateKey() + CodedUtil.encodeBase64(ZipUtil.compress(saveString));
                return str;
            }
            catch (IOException ex)
            {
                log.error("", ex);
            }
            return saveString;
        }
        else
        {
            return saveString;
        }
    }

    public static String dataLoad(String loadString) throws Exception
    {
        if (loadString.startsWith(versionUpdateKey()))
        {
            String parseString = loadString.replaceFirst(versionUpdateKey(), "");
            String str = ZipUtil.uncompress(CodedUtil.decodeBase64(parseString));
            return str;
        }
        else
        {
            return loadString;
        }
    }
}
