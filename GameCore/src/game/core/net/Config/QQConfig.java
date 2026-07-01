package game.core.net.Config;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/12/18 15:41
 */
public class QQConfig {
    /**
     * appID
     */
    private String appId;
    /**
     * 秘钥
     */
    private String appKey;
    /**
     * 游戏ID
     */
    private String gameId;
    /**
     * app名称
     */
    private String appName;
    /**
     * 路径
     */
    private String logUrl;
    /**
     * 屏蔽词地址
     */
    private String uicFilterUrl ="https://openapi.minigame.qq.com/v3/user/uic_filter";
    /**
     * 是否开启网络日志，1开启/0关闭
     */
    private int openLog;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public int getOpenLog() {
        return openLog;
    }

    public void setOpenLog(int openLog) {
        this.openLog = openLog;
    }

    public String getUicFilterUrl() {
        return uicFilterUrl;
    }

    public void setUicFilterUrl(String uicFilterUrl) {
        this.uicFilterUrl = uicFilterUrl;
    }

    @Override
    public String toString() {
        return "QQConfig{" +
                "appId='" + appId + '\'' +
                ", appName='" + appName + '\'' +
                ", logUrl='" + logUrl + '\'' +
                ", appKey='" + appKey + '\'' +
                ", openLog=" + openLog +
                '}';
    }
}
