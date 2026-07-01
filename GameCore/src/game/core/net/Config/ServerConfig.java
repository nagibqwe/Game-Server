package game.core.net.Config;

import game.core.db.DataBaseConfig;
import game.core.db.ServerConfigException;
import game.core.db.bean.ConfigDbBean;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

public class ServerConfig {
    //    private static final Logger logger = LogManager.getLogger(ServerConfig.class);
    private DataBaseConfig loginDBConfig = null;
    private DataBaseConfig gameDBConfig = null;
    private DataBaseConfig publicDBConfig = null;
    private DataBaseConfig configDBConfig = null;
    private static String verifyUrl = "http://x8.iwgame.test/api/usr/vtk.do";
    private static int httpPort = 8999;
    private static int serverPort = 8000;
    private static String serverName = "";
    private static String serverPlatform = "";
    private static int serverId = 0;
    private static int LsId = 0;
    private static String logdrivers = "com.mysql.cj.jdbc.Driver";
    private static String logurl = "jdbc:mysql://localhost:3306/tzj_game_log?autoReconnect=true&characterEncoding=UTF-8";
    private static String loguser = "root";
    private static String logpassword = "root";
    private static String logvalidationquery = "select 1";
    private static String openTime = "2015-01-01 00:00:00";
    private static String privateKey = "r9h$t!1*63^2rjet6hdj2";
    private static String httpHeartUrl = "";
    private static int backPort = 7000;
    private static final List<Integer> serverIdList = new ArrayList();
    // FIXME 该字段目前无用
    private static String errorLogUrl = "";
    private boolean loaded = false;
    private static final ServerConfig instance = new ServerConfig();
    private static int serverType = 0;
    private static String langType = "cn";
    private static String publicIp = "";
    private static int publicPort = 0;
    private static String gameServerIp = "127.0.0.1";
    private static String showServerId = "";
    private static String version = "0.0.0.0";
    private static int isShenHe = 0;
    private static String serverOrderUrl = "http://tzj-xpt.iwgame.cc/api/usr/vtk.do";
    private static String rechargeNotifyUrl = "";
    private static int checkLoginTime = 30;//检测登录时间
    private static int checkRechargeTime = 30;//检测充值时间
    private static String peopleLevels = "";//设置当前游戏支持人数等级为
    private static int isrebate = 0;//是否支持返利，0不支持，1要支持，根据运营要求来填写
    /**
     * 充值回调端口
     */
    private static int rechargePort = 8888;

    private static BIConfig biConfig = new BIConfig();

    private static BI4399Config bi4399Config = new BI4399Config();

    private static QQConfig qqConfig = new QQConfig();

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException {
        if (this.loaded) {
            System.out.println("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "privatekey":
                    case "serverType":
                    case "login-data":
                    case "recharge":
                    case "openTime":
                    case "serverId":
                    case "language":
                    case "biSysLog":
                    case "version":
                    case "bi4399SysLog":
                    case "isShenHe":
                    case "gameServerIp":
                    case "serverOrderUrl":
                    case "rechargeNotifyUrl":
                    case "checkLoginTime":
                    case "checkRechargeTime":
                    case "peopleLevels":
                    case "isrebate":
                        this.setData(child);
                        break;
                    case "public":
                        this.setPublicInfo(child);
                        break;
                    case "serverInfo":
                        this.setServerInfo(child);
                        break;
                    case "db-public-data":
                        this.publicDBConfig = this.getDBConf(child);
                        break;
                    case "config-db":
                        this.configDBConfig = this.getDBConf(child);
                        break;
                    case "db-login-data":
                        this.loginDBConfig = this.getDBConf(child);
                        break;
                    case "heartHttp":
                        this.setHttpHeartData(child);
                        break;
                    case "db-log-info":
                        this.setLogData(child);
                        break;
                    case "backgrand":
                        this.setBackgrandPort(child);
                        break;
                    case "errorlog":
                        this.setErrorlogURL(child);
                        break;
                    case "db-game":
                        this.gameDBConfig = this.getDBConf(child);
                        break;
                    case "serverIdList":
                        this.setServerIdList(child);
                        break;
                    case "qq":
                        this.setQQLog(child);
                        break;
                    default:
                        System.out.println("unknow node [" + name + "]");
                }
            }
        }

        //以前是读取数据库
//        InetAddress addr = InetAddress.getLocalHost();

        if (gameServerIp.contains("127.0.0.1")){
            gameServerIp = getHostIp();
        }
        this.loaded = true;
    }

    private void setQQLog(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName.toLowerCase()) {
                case "openlog":
                    getQqConfig().setOpenLog("1".equals(attValue) ? 1 : 0);
                    break;
                case "appkey":
                    getQqConfig().setAppKey(attValue);
                    break;
                case "appid":
                    getQqConfig().setAppId(attValue);
                    break;
                case "gameid":
                    getQqConfig().setGameId(attValue);
                    break;
                case "appname":
                    getQqConfig().setAppName(attValue);
                    break;
                case "logurl":
                    getQqConfig().setLogUrl(attValue);
                    break;
                case "uicfilterurl":
                    getQqConfig().setUicFilterUrl(attValue);
                    break;
                default:
                    System.out.println("ServerConfig BiQQLog unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private String getHostIp() {
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress ip = (InetAddress) addresses.nextElement();
                    if (ip instanceof Inet4Address
                            && !ip.isLoopbackAddress() // loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                            && !ip.getHostAddress().contains(":")) {
//                        System.out.println("本机的IP = " + ip.getHostAddress());
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean loadConfigDB(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();

        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "config-db":
                        this.configDBConfig = this.getDBConf(child);
                        break;
                    case "serverId":
                        if (!this.setData(child)) {
                            return false;
                        }
                        break;
                    default:
                        System.out.println("unknow node [" + name + "]");
                }
            }
        }

        return true;
    }

    public void setGameServerConfigInfo(ConfigDbBean bean) {
        if (this.loaded) {
            System.out.println("has loaded ServerConfig,setServerConfigInfo---->ConfigDbBean.getServerId()== [" + bean.getServerId() + "]");
        } else {
            this.gameDBConfig = new DataBaseConfig(this.url(bean.getDb_game()), bean.getUsername(), bean.getPassword());
            this.publicDBConfig = new DataBaseConfig(this.url(bean.getDb_public_data()), bean.getUsername(), bean.getPassword());
            logurl = this.url(bean.getDb_log_info());
            loguser = bean.getUsername();
            logpassword = bean.getPassword();
            this.setServerIdListByStr(bean.getServerIdList());
            openTime = bean.getOpenTime();
            httpHeartUrl = bean.getHeartHttp();
            errorLogUrl = bean.getErrorlog();
            verifyUrl = bean.getLogin_data();
            privateKey = bean.getPrivatekey();
            serverType = bean.getServerType();
            serverPort = bean.getPort();
            serverName = bean.getServerName();
            serverId = bean.getServerId();
            serverPlatform = "";
            LsId = bean.getLsId();
            this.loaded = true;
            publicIp = bean.getPublicIp();
            publicPort = bean.getPublicPort();
            gameServerIp = bean.getIp();
            showServerId = bean.getIp();
            backPort = bean.getBackgrand_port();
            rechargePort = bean.getRechargePort();
            biConfig = new BIConfig(bean.getBiBakDir(), bean.getOpenNet(), bean.getSysLogHost(), bean.getSysLogPort(), bean.getSysLogProtocol());
            bi4399Config = new BI4399Config(bean.getBi4399Dir(), bean.getBi4399OpenNet(), bean.getBi4399Host(), bean.getBi4399Port(), bean.getBi4399Protocol(), bean.getBi4399Sql());
        }
    }

    private String url(String uu) {
        return "jdbc:mysql://" + uu + "?autoReconnect=true&characterEncoding=UTF-8&nullCatalogMeansCurrent=true";
    }

    private DataBaseConfig getDBConf(Node node) {
        String url = null;
        String username = null;
        String password = null;
        NamedNodeMap attributes = node.getAttributes();

        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "username":
                    username = attValue;
                    break;
                case "url":
                    url = attValue + "&nullCatalogMeansCurrent=true";;
                    break;
                case "password":
                    password = attValue;
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }

        if (url != null && !url.isEmpty() && username != null && !username.isEmpty() && password != null && !password.isEmpty()) {
            return new DataBaseConfig(url, username, password);
        } else {
            throw new ServerConfigException();
        }
    }

    private boolean setData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            int id;
            switch (attName) {
                case "privatekey":
                    privateKey = attValue;
                    break;
                case "serverType":
                    serverType = Integer.parseInt(attValue);
                    break;
                case "rechargePort":
                    rechargePort = Integer.parseInt(attValue);
                    break;

                case "bi4399Dir":
                    bi4399Config.setBi4399Dir(attValue);
                    break;
                case "bi4399OpenNet":
                    bi4399Config.setBi4399OpenNet(Integer.parseInt(attValue));
                    break;
                case "bi4399Host":
                    bi4399Config.setBi4399Host(attValue);
                    break;
                case "bi4399Port":
                    bi4399Config.setBi4399Port(attValue);
                    break;
                case "bi4399Protocol":
                    bi4399Config.setBi4399Protocol(attValue);
                    break;
                case "bi4399Sql":
                    bi4399Config.setBi4399Sql(Integer.parseInt(attValue));
                    break;

                case "biBakDir":
                    biConfig.setBiBakDir(attValue);
                    break;
                case "openNet":
                    biConfig.setOpenNet(Integer.parseInt(attValue));
                    break;
                case "sysLogHost":
                    biConfig.setSysLogHost(attValue);
                    break;
                case "sysLogPort":
                    biConfig.setSysLogPort(attValue);
                    break;
                case "sysLogProtocol":
                    biConfig.setSysLogProtocol(attValue);
                    break;
                case "verifyUrl":
                    verifyUrl = attValue;
                    break;
                case "language":
                    langType = attValue;
                    break;
                case "openTime":
                    openTime = attValue;
                    break;
                case "httpPort":
                    httpPort = Integer.parseInt(attValue);
                    break;
                case "lsId":
                    id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    LsId = id;
                    break;
                case "serverId":
                    id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    serverId = id;
                    break;
                case "version":
                    version = attValue;
                    break;
                 case "isShenHe":
                     isShenHe = Integer.parseInt(attValue);
                     break;
                case "gameServerIp":
                    gameServerIp = attValue;
                    break;
                case "serverOrderUrl":
                    serverOrderUrl = attValue;
                    break;
                case "rechargeNotifyUrl":
                    rechargeNotifyUrl = attValue;
                    break;
                case "checkLoginTime":
                    checkLoginTime = Integer.parseInt(attValue);
                    break;
                case "checkRechargeTime":
                    checkRechargeTime = Integer.parseInt(attValue);
                    break;
                case "peopleLevels":
                    peopleLevels = attValue;
                    break;
                case "isrebate":
                    isrebate  = Integer.parseInt(attValue);
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }

        return true;
    }

    private void setLogData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "username":
                    loguser = attValue;
                    break;
                case "validationquery":
                    logvalidationquery = attValue;
                    break;
                case "url":
                    logurl = attValue + "&nullCatalogMeansCurrent=true";
                    break;
                case "password":
                    logpassword = attValue;
                    break;
                case "drivers":
                    logdrivers = attValue;
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }

    }

    private void setServerIdList(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if ("serverIdList".equals(attName)) {
                this.setServerIdListByStr(attValue);
            } else {
                System.out.println("setServerIdList unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setServerIdListByStr(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        while (tokenizer.hasMoreTokens()) {
            String d = tokenizer.nextToken();
            int i = Integer.parseInt(d);
            serverIdList.add(i);
        }
    }

    public static boolean isRightServerId(int serverId) {
        return serverIdList.contains(serverId);
    }

    public DataBaseConfig getConfigDBConfig() {
        return this.configDBConfig;
    }

    public DataBaseConfig getLoginDBConfig() {
        this.checkLoaded();
        return this.loginDBConfig;
    }

    public DataBaseConfig getGameDBConfig() {
        this.checkLoaded();
        return this.gameDBConfig;
    }

    public DataBaseConfig getPublicDBConfig() {
        this.checkLoaded();
        return this.publicDBConfig;
    }

    public static String getLoginVerifyUrl() {
        return verifyUrl;
    }

    public static int getLoginHtttpPort() {
        return httpPort;
    }

    public static String getLogDrivers() {
        return logdrivers;
    }

    public static String getLogUrl() {
        return logurl;
    }

    public static String getLogUser() {
        return loguser;
    }

    public static String getLogPassword() {
        return logpassword;
    }

    public static String getLogValidationquery() {
        return logvalidationquery;
    }

    public static List<Integer> getServerIdList() {
        return serverIdList;
    }

    public static String getServerOpenTime() {
        return openTime;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    private void checkLoaded() {
        if (!this.loaded) {
            throw new ServerConfigException();
        }
    }

    private void setHttpHeartData(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "host":
                    httpHeartUrl = attValue;
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setServerInfo(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            String var7 = attName.toLowerCase();
            switch (var7) {
                case "servername":
                    serverName = attValue;
                    break;
                case "lsid":
                    LsId = Integer.parseInt(attValue);
                    break;
                case "port":
                    serverPort = Integer.parseInt(attValue);
                    break;
                case "showserverid":
                    showServerId = attValue;
                    break;
                case "httpport":
                    httpPort = Integer.parseInt(attValue);
                    break;
                case "serverid":
                    serverId = Integer.parseInt(attValue);
                    break;
                case "platfrom":
                    serverPlatform = attValue;
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setPublicInfo(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            String var7 = attName.toLowerCase();
            switch (var7) {
                case "publicip":
                    publicIp = attValue;
                    break;
                case "publicport":
                    publicPort = Integer.parseInt(attValue);
                    break;
                default:
                    System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setBackgrandPort(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("port")) {
                backPort = Integer.parseInt(attValue);
            } else
                System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
        }
    }

    private void setErrorlogURL(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if ("http".equals(attName)) {
                errorLogUrl = attValue;
            } else {
                System.out.println("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    public static String GetHttpHeart() {
        return httpHeartUrl;
    }

    public static int getBackPort() {
        return backPort;
    }

    public static boolean isTestServer() {
        return serverType == 0;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getServerName() {
        return serverName;
    }

    public static String getServerPlatform() {
        return serverPlatform;
    }

    public static int getServerId() {
        return serverId;
    }

    public static int getLsId() {
        return LsId;
    }

    public static String getLangType() {
        return langType;
    }

    public static String getErrorLogUrl() {
        return errorLogUrl;
    }

    public static void setVerifyUrl(String verifyUrl) {
        ServerConfig.verifyUrl = verifyUrl;
    }

    public static void setHttpPort(int httpPort) {
        ServerConfig.httpPort = httpPort;
    }

    public static void setServerPort(int serverPort) {
        ServerConfig.serverPort = serverPort;
    }

    public static void setServerName(String serverName) {
        ServerConfig.serverName = serverName;
    }

    public static void setServerPlatform(String serverPlatform) {
        ServerConfig.serverPlatform = serverPlatform;
    }

    public static void setServerId(int serverId) {
        ServerConfig.serverId = serverId;
    }

    public static void setLsId(int LsId) {
        ServerConfig.LsId = LsId;
    }

    public static void setLogdrivers(String logdrivers) {
        ServerConfig.logdrivers = logdrivers;
    }

    public static void setLogurl(String logurl) {
        ServerConfig.logurl = logurl;
    }

    public static void setLoguser(String loguser) {
        ServerConfig.loguser = loguser;
    }

    public static void setLogpassword(String logpassword) {
        ServerConfig.logpassword = logpassword;
    }

    public static void setLogvalidationquery(String logvalidationquery) {
        ServerConfig.logvalidationquery = logvalidationquery;
    }

    public static void setOpenTime(String openTime) {
        ServerConfig.openTime = openTime;
    }

    public static void setPrivateKey(String privateKey) {
        ServerConfig.privateKey = privateKey;
    }

    public static void setHttpHeartUrl(String httpHeartUrl) {
        ServerConfig.httpHeartUrl = httpHeartUrl;
    }

    public static void setBackPort(int backPort) {
        ServerConfig.backPort = backPort;
    }

    public static void setErrorLogUrl(String errorLogUrl) {
        ServerConfig.errorLogUrl = errorLogUrl;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public static void setServerType(int serverType) {
        ServerConfig.serverType = serverType;
    }

    public static int GetServerType() {
        return serverType;
    }

    public static void setLangType(String langType) {
        ServerConfig.langType = langType;
    }

    public static String getPublicIp() {
        return publicIp;
    }

    public static int getPublicPort() {
        return publicPort;
    }

    public static String getGameServerIp() {
        return gameServerIp;
    }

    public static String getShowServerId() {
        return showServerId;
    }

    public static int getRechargePort() {
        return rechargePort;
    }

    public static BIConfig getBiConfig() {
        return biConfig;
    }

    public static BI4399Config getBi4399Config() {
        return bi4399Config;
    }

    public static String getVersion() {
        return version;
    }

    public static void setVersion(String version) {
        ServerConfig.version = version;
    }

    public static int getIsShenHe() {return isShenHe;}

    public static void setIsShenHe(int isShenHe) {ServerConfig.isShenHe = isShenHe;}

    public static String getServerOrderUrl() {
        return serverOrderUrl;
    }

    public static void setServerOrderUrl(String serverOrderUrl) {
        ServerConfig.serverOrderUrl = serverOrderUrl;
    }

    public static String getRechargeNotifyUrl() {
        return rechargeNotifyUrl;
    }

    public static void setRechargeNotifyUrl(String rechargeNotifyUrl) {
        ServerConfig.rechargeNotifyUrl = rechargeNotifyUrl;
    }

    public static int getCheckLoginTime() {
        return checkLoginTime;
    }

    public static void setCheckLoginTime(int checkLoginTime) {
        ServerConfig.checkLoginTime = checkLoginTime;
    }

    public static int getCheckRechargeTime() {
        return checkRechargeTime;
    }

    public static void setCheckRechargeTime(int checkRechargeTime) {
        ServerConfig.checkRechargeTime = checkRechargeTime;
    }


    public static String getPeopleLevels() {
        return peopleLevels;
    }

    public static void setPeopleLevels(String peopleLevels) {
        ServerConfig.peopleLevels = peopleLevels;
    }

    public static int getIsrebate() {
        return isrebate;
    }

    public static void setIsrebate(int isrebate) {
        ServerConfig.isrebate = isrebate;
    }

    public static QQConfig getQqConfig() {
        return qqConfig;
    }
}