package game.core.net.Config;

/**
 * 服务器信息
 *
 *
 */
public class ServerEnum
{
    public static final int GAMETEST_SERVER = 0;//游戏测试服
    public static final int GAME_SERVER = 1;//游戏正式服
    public static final int LOGIN_SERVER = 2;//登录服
    public static final int PUBLIC_SERVER = 3;//公共服
    public static final int FIGHT_SERVER_LISTEN = 4;//战斗服服务端
    public static final int FIGHT_CLIENT_LIMIT = 10;//战斗服客户端上限值
    public static final int SocialServer = 11 ;  //社区服务器

    private int id;

    private String ip;

    private int port;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

}
