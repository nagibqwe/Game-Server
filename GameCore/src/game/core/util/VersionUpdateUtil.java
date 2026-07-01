package game.core.util;

import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class VersionUpdateUtil
{
    protected static Logger log = LogManager.getLogger(VersionUpdateUtil.class);
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
                long beginTime = TimeUtils.Time();
                String str = versionUpdateKey() + CodedUtil.encodeBase64(ZipUtil.compress(saveString));
                long useTime = TimeUtils.Time() - beginTime;
                if (useTime > 3000)
                {
                    log.error("压缩玩家数据所用时间:" + useTime);
                }
                return str;
            }
            catch (IOException ex)
            {
                log.error(ex, ex);
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
            long beginTime = TimeUtils.Time();
            String parseString = loadString.replaceFirst(versionUpdateKey(), "");
            String str = ZipUtil.uncompress(CodedUtil.decodeBase64(parseString));
            long useTime = TimeUtils.Time() - beginTime;
            if (useTime > 3000)
            {
                log.error("解压缩玩家数据所用时间:" + useTime);
            }
            return str;
        }
        else
        {
            return loadString;
        }
    }
}
