package game.core.net.Config;

import game.core.db.DataBaseConfig;
import game.core.db.ServerConfigException;
import game.core.db.bean.ConfigDbBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ServerConfig {
    //服务器名称
    private String name;
    //服务器ID
    private int id;

    private String platformGroupID;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the platformGroupID
     */
    public String getPlatformGroupID()
    {
        return platformGroupID;
    }

    /**
     * @param platformGroupID the platformGroupID to set
     */
    public void setPlatformGroupID(String platformGroupID)
    {
        this.platformGroupID = platformGroupID;
    }




    private static final Logger logger = LogManager.getLogger(ServerConfig.class);
    private DataBaseConfig loginDBConfig = null;
    private DataBaseConfig gameDBConfig = null;
    private DataBaseConfig publicDBConfig = null;
    private DataBaseConfig configDBConfig = null;
    private static String verifyUrl = "http://x8.iwgame.test/api/usr/vtk.do";
    private static int httpPort = 8999;
    private static int isNeedVerify = 1;
    private static int productId = 185;
    private static int serverPort = 8000;
    private static String serverName = "";
    private static String serverPlatform = "";
    private static int serverId = 0;
    private static int LsId = 0;
    private static String logdrivers = "com.mysql.jdbc.Driver";
    private static String logurl = "jdbc:mysql://192.168.10.146:3306/test?autoReconnect=true&characterEncoding=UTF-8";
    private static String loguser = "root";
    private static String logpassword = "xujian";
    private static String logvalidationquery = "select 1";
    private static String openTime = "2015-01-01 00:00:00";
    private static String privateKey = "r9h$t!1*63^2rjet6hdj2";
    private static String tokenSecretKey = "";
    private static String httpHeartUrl = "";
    private static String httpParams = "";
    private static int backPort = 7000;
    private static final List<Integer> serverIdList = new ArrayList();
    // FIXME 该字段目前无用
    private static String rechargeVerifyUrl = "http://10.0.0.52:8080/gmp";
    private static String errorLogUrl = "";
    private boolean loaded = false;
    private static final ServerConfig instance = new ServerConfig();
    private static int serverType = 0;
    private static String langType = "cn";
    private static String publicIp = "";
    private static int publicPort = 0;
    private static int isCrossCountry = 0;
    private static String gameServerIp = "127.0.0.1";
    private static String showServerId = "";
    private static boolean have843 = false;
    private static String funcellArea = "China";
    private static int funcellGameId = 185;
    private static String loginHttpUrl = "192.168.1.38:9999";
    /**
     * 充值回调端口
     */
    private static int rechargePort = 8888;

    private ServerConfig() {
    }

    public static ServerConfig getInstance() {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException {
        if (this.loaded) {
            logger.warn("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch (name) {
                    case "privatekey":
                    case "serverType":
                    case "login-data":
                    case "recharge":
                    case "openTime":
                    case "tokenSecretKey":
                    case "reyun":
                    case "serverId":
                    case "talkingtada":
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
                    default:
                        logger.error("unknow node [" + name + "]");
                }
            }
        }

        InetAddress addr = InetAddress.getLocalHost();
        gameServerIp = addr.getHostAddress();
        this.loaded = true;
    }

    public boolean loadConfigDB(String filePath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();

        for(int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child instanceof Element) {
                String name = child.getNodeName();
                switch(name) {
                    case "config-db":
                        this.configDBConfig = this.getDBConf(child);
                        break;
                    case "serverId":
                        if (!this.setData(child)) {
                            return false;
                        }
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                }
            }
        }

        return true;
    }

    public void setGameServerConfigInfo(ConfigDbBean bean) {
        if (this.loaded) {
            logger.warn("has loaded ServerConfig,setServerConfigInfo---->ConfigDbBean.getServerId()== [" + bean.getServerId() + "]");
        } else {
            this.setServerIdListByStr(bean.getServerIdList());
            openTime = bean.getOpenTime();
            privateKey = bean.getPrivatekey();
            serverType = bean.getServerType();
            serverPort = bean.getPort();
            serverId = bean.getServerId();
            LsId = bean.getLsId();
            this.loaded = true;
            publicIp = bean.getPublicIp();
            publicPort = bean.getPublicPort();
            backPort = bean.getBackgrand_port();
            rechargePort = bean.getRechargePort();
        }
    }

    private String url(String uu) {
        return "jdbc:mysql://" + uu + "?autoReconnect=true&characterEncoding=UTF-8";
    }

    private DataBaseConfig getDBConf(Node node) {
        String url = null;
        String username = null;
        String password = null;
        NamedNodeMap attributes = node.getAttributes();

        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch(attName) {
                case "username":
                    username = attValue;
                    break;
                case "url":
                    url = attValue;
                    break;
                case "password":
                    password = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
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
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            int id;
            switch(attName) {
                case "privatekey":
                    privateKey = attValue;
                    break;
                case "serverType":
                    serverType = Integer.parseInt(attValue);
                    break;
                case "rechargePort":
                    rechargePort = Integer.parseInt(attValue);
                    break;
                case "verifyUrl":
                    verifyUrl = attValue;
                    break;
                case "language":
                    langType = attValue;
                    break;
                case "productId":
                    productId = Integer.parseInt(attValue);
                    break;
                case "sectetKey":
                    tokenSecretKey = attValue;
                    break;
                case "openTime":
                    openTime = attValue;
                    break;
                case "isCrossCountry":
                    id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    isCrossCountry = id;
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
                case "isNeedVerify":
                    isNeedVerify = Integer.parseInt(attValue);
                    break;
                case "serverId":
                    id = Integer.parseInt(attValue);
                    if (id <= 0) {
                        return false;
                    }
                    serverId = id;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }

        return true;
    }

    private void setLogData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch(attName) {
                case "username":
                    loguser = attValue;
                    break;
                case "validationquery":
                    logvalidationquery = attValue;
                    break;
                case "url":
                    logurl = attValue;
                    break;
                case "password":
                    logpassword = attValue;
                    break;
                case "drivers":
                    logdrivers = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }

    }

    private void setServerIdList(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if ("serverIdList".equals(attName)) {
                this.setServerIdListByStr(attValue);
            } else {
                logger.warn("setServerIdList unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setServerIdListByStr(String str) {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        while(tokenizer.hasMoreTokens()) {
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

    public static boolean isNeedVerify() {
        return isNeedVerify == 1;
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

    public static String getRechargeVerifyUrl() {
        return rechargeVerifyUrl;
    }

    private void checkLoaded() {
        if (!this.loaded) {
            throw new ServerConfigException();
        }
    }

    private void setHttpHeartData(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch(attName) {
                case "params":
                    httpParams = attValue;
                    break;
                case "host":
                    httpHeartUrl = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setServerInfo(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            String var7 = attName.toLowerCase();
            switch(var7) {
                case "servername":
                    serverName = attValue;
                    break;
                case "lsid":
                    LsId = Integer.parseInt(attValue);
                    break;
                case "port":
                    serverPort = Integer.parseInt(attValue);
                    break;
                case "showServerId":
                    showServerId = attValue;
                    break;
                case "have843":
                    have843 = Integer.parseInt(attValue) > 0;
                    break;
                case "serverid":
                    serverId = Integer.parseInt(attValue);
                    break;
                case "platfrom":
                    serverPlatform = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setPublicInfo(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            String var7 = attName.toLowerCase();
            switch(var7) {
                case "publicip":
                    publicIp = attValue;
                    break;
                case "publicport":
                    publicPort = Integer.parseInt(attValue);
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    private void setBackgrandPort(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("port")) {
                backPort = Integer.parseInt(attValue);
            } else
                logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
        }
    }

    private void setErrorlogURL(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for(int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if ("http".equals(attName)) {
                errorLogUrl = attValue;
            } else {
                logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
            }
        }
    }

    public static String GetHttpHeart() {
        return httpHeartUrl;
    }

    public static String GetHttpParams() {
        return httpParams;
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

    public static void setIsNeedVerify(int isNeedVerify) {
        ServerConfig.isNeedVerify = isNeedVerify;
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

    public static String getTokenSecretKey() {
        return tokenSecretKey;
    }

    public static void setTokenSecretKey(String tokenSecretKey) {
        ServerConfig.tokenSecretKey = tokenSecretKey;
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

    public static void setHttpParams(String httpParams) {
        ServerConfig.httpParams = httpParams;
    }

    public static void setBackPort(int backPort) {
        ServerConfig.backPort = backPort;
    }

    public static void setRechargeVerifyUrl(String rechargeVerifyUrl) {
        ServerConfig.rechargeVerifyUrl = rechargeVerifyUrl;
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

    public static int getIsCrossCountry() {
        return isCrossCountry;
    }

    public static String getShowServerId() {
        return showServerId;
    }

    public static boolean isHave843() {
        return have843;
    }

    public static String getFuncellArea() {
        return funcellArea;
    }

    public static int getFuncellGameId() {
        return funcellGameId;
    }

    public static void setFuncellArea(String funcellArea) {
        ServerConfig.funcellArea = funcellArea;
    }

    public static void setFuncellGameId(int funcellGameId) {
        ServerConfig.funcellGameId = funcellGameId;
    }

    public static String getLoginHttpUrl() {
        return loginHttpUrl;
    }

    public static int getProductId() {
        return productId;
    }

    public static int getRechargePort() {
        return rechargePort;
    }
}