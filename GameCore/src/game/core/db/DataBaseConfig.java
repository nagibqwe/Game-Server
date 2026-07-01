package game.core.db;

import java.util.Properties;

public class DataBaseConfig
{
    private final String url;
    private final String username;
    private final String password;

    public DataBaseConfig(String url, String username, String password)
    {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Properties getProperties()
    {
        Properties properties = new Properties();
        properties.put("url", url);
        properties.put("username", username);
        properties.put("password", password);
        //开启mybatis的poolping功能九 零一起玩 www.9017 5.com
        properties.put("poolPingEnabled",true);
        //SELECT NOW()其实是定期执行的sql
        properties.put("poolPingQuery","SELECT NOW()");
        //表示无数据库操作3600000ms（1h）执行一次poolping
        properties.put("poolPingConnectionsNotUsedFor",3600000);
        return properties;
    }

    public String getUrl()
    {
        return url;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
}
