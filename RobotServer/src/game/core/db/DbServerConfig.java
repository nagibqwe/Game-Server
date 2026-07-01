/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * ServerConfig配置类
 */
public class DbServerConfig
{
    private final static Logger logger = LogManager.getLogger(DbServerConfig.class);

    private DataBaseConfig loginDBConfig = null; // 登录数据库配置
    private DataBaseConfig gameDBConfig = null; // game数据库配置
    private DataBaseConfig gameDataDBConfig = null; // game_data数据库配置
    private DataBaseConfig publicDBConfig = null; // 激活码数据库配置

    private static String verifyUrl = "https://auth.funcell123.com/verify"; //登录验证地址
    private static int httpPort = 8999;                   //登录服务器http端口
    private static int isNeedVerify = 1;                   //是否需要导平台验证,0不需要,1需要

    private static String logdrivers = "com.mysql.jdbc.Driver";    //数据库连接驱动
    private static String logurl = "jdbc:mysql://192.168.10.146:3306/test?autoReconnect=true";    //登陆地址
    private static String loguser = "root";   //用户名
    private static String logpassword = "xujian";  //密码
    private static String logvalidationquery = "select 1"; //验证连接是否有效

    private static String openTime = "2015-01-01 00:00:00";//开服时间
    private static String privateKey = "r9h$t!1*63^2rjet6hdj2";

    private static String httpHeartUrl = "";
    private static String httpParams = "";

    private static int backPort = 7000;

    private static final List<Integer> serverIdList = new ArrayList<>();
    
    private static String rechargeVerifyUrl = "https://payment.funcell123.com/confirm"; //充值验证地址

    private boolean loaded = false;
    private final static DbServerConfig instance;
    
    private static int serverType = 0;//服务器类型：0表示测试服，1表示正式游戏服，2表示跨服 

    static
    {
        instance = new DbServerConfig();
    }

    private DbServerConfig()
    {
    }

    public static DbServerConfig getInstance()
    {
        return instance;
    }

    public void load(String filePath) throws ParserConfigurationException, SAXException, IOException
    {
        if (loaded)
        {
            logger.warn("has loaded ServerConfig, [" + filePath + "]");
            return;
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File xmlFile = new File(filePath);
        Document doc = builder.parse(xmlFile);
        Element configure = doc.getDocumentElement();
        NodeList children = configure.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i)
        {
            Node child = children.item(i);
            if (child instanceof Element)
            {
                String name = child.getNodeName();
                switch (name)
                {
                    case "db-login-data":
                        loginDBConfig = getDBConf(child);
                        break;
                    case "db-game":
                        gameDBConfig = getDBConf(child);
                        break;
                    case "db-game-data":
                        gameDataDBConfig = getDBConf(child);
                        break;
                    case "db-public-data":
                        publicDBConfig = getDBConf(child);
                        break;
                    case "login-data":
                        setData(child);
                        break;
                    case "db-log-info":
                        setLogData(child);
                        break;
                    case "serverIdList":
                        setServerIdList(child);
                        break;
                    case "openTime":
                        setData(child);
                        break;
                    case "heartHttp":
                        setHttpHeartData(child);
                        break;
                    case "backgrand":
                        setBackgrandPort(child);
                        break;
                    case "privatekey":
                        setData(child);
                        break;
                    case "recharge":
                        setData(child);
                        break;
                    case "serverType":
                        setData(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
        loaded = true;
    }

    private long getLongValueConf(Node node)
    {
        String longVal = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                longVal = attValue;
                break;
            }
        }
        return Long.parseLong(longVal);
    }

    private int getIntValueConf(Node node)
    {
        String intValue = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                intValue = attValue;
                break;
            }
        }
        return Integer.parseInt(intValue);
    }

    private String getStringValueConf(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            if (attName.equals("value"))
            {
                return attValue;
            }
        }
        return null;
    }

    private DataBaseConfig getDBConf(Node node)
    {
        String url = null, username = null, password = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "url":
                    url = attValue;
                    break;
                case "username":
                    username = attValue;
                    break;
                case "password":
                    password = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
        if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty())
            throw new ServerConfigException();
        return new DataBaseConfig(url, username, password);
    }

    private void setData(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "verifyUrl":
                    verifyUrl = attValue;
                    break;
                case "httpPort":
                    httpPort = Integer.parseInt(attValue);
                    break;
                case "isNeedVerify"://是否需要导平台验证,0不需要，1需要
                    isNeedVerify = Integer.parseInt(attValue);
                    break;
                case "openTime":
                    openTime = attValue;
                    break;
                case "privatekey":
                    privateKey = attValue;
                    break;
                case "rechargeVerifyUrl":
                    rechargeVerifyUrl = attValue;
                    break;
                case "serverType"://0表示测试服，1表示正式游戏服，2表示跨服
                    serverType = Integer.parseInt(attValue);
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setLogData(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "drivers":
                    logdrivers = attValue;
                    break;
                case "url":
                    logurl = attValue;
                    break;
                case "username":
                    loguser = attValue;
                    break;
                case "password":
                    logpassword = attValue;
                    break;
                case "validationquery":
                    logvalidationquery = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    //设置合过的服务器id列表
    private void setServerIdList(Node node)
    {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "serverIdList":
                    setServerIdListByStr(attValue);
                    break;
                default:
                    logger.warn("setServerIdList unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setServerIdListByStr(String str)
    {
        StringTokenizer tokenizer = new StringTokenizer(str, ",");
        int i = 0;
        while (tokenizer.hasMoreTokens())
        {
            String d = tokenizer.nextToken();
            i = Integer.valueOf(d);//将字符串转换为整型
            serverIdList.add(i);
        }
    }

    //判断是否是正确的服务器id
    public static boolean isRightServerId(int serverId)
    {
        return serverIdList.contains(serverId);
    }

    public DataBaseConfig getLoginDBConfig()
    {
        checkLoaded();
        return loginDBConfig;
    }

    public DataBaseConfig getGameDBConfig()
    {
        checkLoaded();
        return gameDBConfig;
    }

    public DataBaseConfig getGameDataDBConfig()
    {
        checkLoaded();
        return gameDataDBConfig;
    }

    public DataBaseConfig getPublicDBConfig()
    {
        checkLoaded();
        return publicDBConfig;
    }

    //获取登录验证地址
    public static String getLoginVerifyUrl()
    {
        return verifyUrl;
    }

    //获取登录http端口
    public static int getLoginHtttpPort()
    {
        return httpPort;
    }

    //是否需要登录验证
    public static boolean isNeedVerify()
    {
        return isNeedVerify == 1;
    }

    //获取驱动
    public static String getLogDrivers()
    {
        return logdrivers;
    }

    //获取URL
    public static String getLogUrl()
    {
        return logurl;
    }

    //获取用户名
    public static String getLogUser()
    {
        return loguser;
    }

    //获取LOG密码
    public static String getLogPassword()
    {
        return logpassword;
    }

    //获取验证信息
    public static String getLogValidationquery()
    {
        return logvalidationquery;
    }

    //获取合服过的服务器id列表
    public static List<Integer> getServerIdList()
    {
        return serverIdList;
    }

    //获取开服时间
    public static String getServerOpenTime()
    {
        return openTime;
    }
    
    //获取私密key
    public static String getPrivateKey()
    {
        return privateKey;
    }
    
    //获取充值验证地址
    public static String getRechargeVerifyUrl()
    {
        return rechargeVerifyUrl;
    }
    

    private void checkLoaded()
    {
        if (!loaded)
            throw new ServerConfigException();
    }

    private void setHttpHeartData(Node child)
    {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "host":
                    httpHeartUrl = attValue;
                    break;
                case "params":
                    httpParams = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private void setBackgrandPort(Node child)
    {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j)
        {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName)
            {
                case "port":
                    backPort = Integer.parseInt(attValue);
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    public static String GetHttpHeart()
    {
        return httpHeartUrl;
    }

    public static String GetHttpParams()
    {
        return httpParams;
    }

    public static int getBackPort()
    {
        return backPort;
    }
    
    public static boolean isTestServer()
    {
        return serverType == 0; 
    }

}
