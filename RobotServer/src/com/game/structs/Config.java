/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import game.core.db.DataBaseConfig;
import game.core.db.DbServerConfig;
import game.core.db.ServerConfigException;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hewei@haowan123.com
 */
public class Config {

    private final static Logger logger = LogManager.getLogger(DbServerConfig.class);

    private static DataBaseConfig gameDBConfig = null; // game数据库配置
    private static DataBaseConfig gameDataDBConfig = null; // game_data数据库配置
    private static DataBaseConfig publicDataDBConfig = null; // db-public-data激活码数据库配置
    private static String serverIp = "127.0.0.1";//服务器ip
    private static int serverPort = 8586;//服务器端口
    private static int serverId = 2;//服务器id
    private static int connectNum = 1;//机器人连接数量
    private static int userIdBegin = 1;//机器人userId开始数字
    private static int eventType = 1;//机器人初始事件
    private static int toMapid = 2;//进入指定地图
    private static String loginServerIp = "10.7.17.130";//登录服务器ip
    private static int loginServerPort = 8000;//登录服务器端口
    private static String privateKey = "";
    private static String publicIp = "";//跨服Ip
    private static int publicPort = 1913;//跨服端口

    public static String aifilename = "robotmain";

    public static DataBaseConfig getGameDBConfig() {
        return gameDBConfig;
    }

    public static DataBaseConfig getGameDataDBConfig() {
        return gameDataDBConfig;
    }

    public static DataBaseConfig getPublicDataDBConfig() {
        return publicDataDBConfig;
    }

    public static String getServerIp() {
        return serverIp;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static String getLoginServerIp() {
        return loginServerIp;
    }

    public static int getLoginServerPort() {
        return loginServerPort;
    }

    public static int getServerId() {
        return serverId;
    }

    public static int getConnectNum() {
        return connectNum;
    }

    public static int getUserIdBegin() {
        return userIdBegin;
    }

    public static void setUserIdBegin(int id) {
        userIdBegin = id;
    }

    public static int getEventType() {
        return eventType;
    }

    public static void setEventType(int type) {
        eventType = type;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public static void load(String filePath) throws ParserConfigurationException, SAXException, IOException {
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
                    case "db-game":
                        gameDBConfig = getDBConf(child);
                        break;
                    case "db-game-data":
                        gameDataDBConfig = getDBConf(child);
                        break;
                    case "db-public-data":
                        publicDataDBConfig = getDBConf(child);
                        break;
                    case "robotCfg":
                        setData(child);
                        break;
                    case "privatekey":
                        setprivateKey(child);
                        break;
                    default:
                        logger.error("unknow node [" + name + "]");
                        break;
                }
            }
        }
    }

    private static void setprivateKey(Node child) {
        NamedNodeMap attributes = child.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "privatekey":
                    privateKey = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

    private static DataBaseConfig getDBConf(Node node) {
        String url = null, username = null, password = null;
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
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
        if (url == null || url.isEmpty() || username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new ServerConfigException();
        }
        return new DataBaseConfig(url, username, password);
    }

    public static int getToMapid() {
        return toMapid;
    }

    public static void setToMapid(int toMapid) {
        Config.toMapid = toMapid;
    }

    private static void setData(Node node) {
        NamedNodeMap attributes = node.getAttributes();
        for (int j = 0; j < attributes.getLength(); ++j) {
            Node attribute = attributes.item(j);
            String attName = attribute.getNodeName();
            String attValue = attribute.getNodeValue();
            switch (attName) {
                case "serverIp":
                    serverIp = attValue;
                    break;
                case "serverPort":
                    serverPort = Integer.parseInt(attValue);
                    break;
                case "loginServerIp":
                    loginServerIp = attValue;
                    break;
                case "loginServerPort":
                    loginServerPort = Integer.parseInt(attValue);
                    break;
                case "serverId":
                    serverId = Integer.parseInt(attValue);
                    break;
                case "connectNum":
                    connectNum = Integer.parseInt(attValue);
                    break;
                case "userIdBegin":
                    userIdBegin = Integer.parseInt(attValue);
                    break;
                case "eventType":
                    eventType = Integer.parseInt(attValue);
                    break;
                case "toMapId":
                    toMapid = Integer.parseInt(attValue);
                    break;
                case "aifilename":
                    aifilename = attValue;
                    break;
                default:
                    logger.warn("ServerConfig unknow attribute name: [" + attName + "], value: [" + attValue + "]");
                    break;
            }
        }
    }

}
