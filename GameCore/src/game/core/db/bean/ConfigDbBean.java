/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.db.bean;

/**
 *
 * @author Administrator
 */
public class ConfigDbBean
{

    private int serverId;
    private int lsId;
    private String db_game;
    private String db_public_data;
    private String db_log_info;
    private String username;
    private String password;
    private String serverIdList;
    private String openTime;
    private String heartHttp;
    private String errorlog;
    private String login_data;
    private String ip;
    private int port;
    private String privatekey;
    private int serverType;
    private String serverName;
    private String publicIp;
    private int publicPort;
    private int rechargePort;
    private int backgrand_port;
    private String biBakDir;

    /**
     * 是否开启网络日志，1开启/0关闭
     */
    private int openNet;
    private String sysLogHost;
    private String sysLogPort;
    private String sysLogProtocol;

    /**
     * 4399
     */
    private String bi4399Dir;
    private String bi4399Host;
    private String bi4399Port;
    private String bi4399Protocol;
    private int    bi4399OpenNet;
    private int    bi4399Sql;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getLsId() {
        return lsId;
    }

    public void setLsId(int lsId) {
        this.lsId = lsId;
    }

    public String getDb_game() {
        return db_game;
    }

    public void setDb_game(String db_game) {
        this.db_game = db_game;
    }

    public String getDb_public_data() {
        return db_public_data;
    }

    public void setDb_public_data(String db_public_data) {
        this.db_public_data = db_public_data;
    }

    public String getDb_log_info() {
        return db_log_info;
    }

    public void setDb_log_info(String db_log_info) {
        this.db_log_info = db_log_info;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServerIdList() {
        return serverIdList;
    }

    public void setServerIdList(String serverIdList) {
        this.serverIdList = serverIdList;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getHeartHttp() {
        return heartHttp;
    }

    public void setHeartHttp(String heartHttp) {
        this.heartHttp = heartHttp;
    }

    public String getErrorlog() {
        return errorlog;
    }

    public void setErrorlog(String errorlog) {
        this.errorlog = errorlog;
    }

    public String getLogin_data() {
        return login_data;
    }

    public void setLogin_data(String login_data) {
        this.login_data = login_data;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPrivatekey() {
        return privatekey;
    }

    public void setPrivatekey(String privatekey) {
        this.privatekey = privatekey;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPublicIp() {
        return publicIp;
    }

    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    public int getPublicPort() {
        return publicPort;
    }

    public void setPublicPort(int publicPort) {
        this.publicPort = publicPort;
    }

    public int getRechargePort() {
        return rechargePort;
    }

    public void setRechargePort(int rechargePort) {
        this.rechargePort = rechargePort;
    }

    public int getBackgrand_port() {
        return backgrand_port;
    }

    public void setBackgrand_port(int backgrand_port) {
        this.backgrand_port = backgrand_port;
    }

    public String getBiBakDir() {
        return biBakDir;
    }

    public void setBiBakDir(String biBakDir) {
        this.biBakDir = biBakDir;
    }

    public int getOpenNet() {
        return openNet;
    }

    public void setOpenNet(int openNet) {
        this.openNet = openNet;
    }

    public String getSysLogHost() {
        return sysLogHost;
    }

    public void setSysLogHost(String sysLogHost) {
        this.sysLogHost = sysLogHost;
    }

    public String getSysLogPort() {
        return sysLogPort;
    }

    public void setSysLogPort(String sysLogPort) {
        this.sysLogPort = sysLogPort;
    }

    public String getSysLogProtocol() {
        return sysLogProtocol;
    }

    public void setSysLogProtocol(String sysLogProtocol) {
        this.sysLogProtocol = sysLogProtocol;
    }

    public String getBi4399Dir() {
        return bi4399Dir;
    }

    public void setBi4399Dir(String bi4399Dir) {
        this.bi4399Dir = bi4399Dir;
    }

    public String getBi4399Host() {
        return bi4399Host;
    }

    public void setBi4399Host(String bi4399Host) {
        this.bi4399Host = bi4399Host;
    }

    public String getBi4399Port() {
        return bi4399Port;
    }

    public void setBi4399Port(String bi4399Port) {
        this.bi4399Port = bi4399Port;
    }

    public String getBi4399Protocol() {
        return bi4399Protocol;
    }

    public void setBi4399Protocol(String bi4399Protocol) {
        this.bi4399Protocol = bi4399Protocol;
    }

    public int getBi4399OpenNet() {
        return bi4399OpenNet;
    }

    public void setBi4399OpenNet(int bi4399OpenNet) {
        this.bi4399OpenNet = bi4399OpenNet;
    }

    public int getBi4399Sql() {
        return bi4399Sql;
    }

    public void setBi4399Sql(int bi4399Sql) {
        this.bi4399Sql = bi4399Sql;
    }
}
