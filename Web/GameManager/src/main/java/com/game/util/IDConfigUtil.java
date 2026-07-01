/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.util;

/**
 *
 * @author Administrator
 */
public class IDConfigUtil
{
    //停止服务器命令
    public static final String CLOSE_COMMAND = "stop server";
    //计数
    private static int id = 0;
    //日志计数
    private static int logid = 0;
    //public及战斗服的使用id值
    private static int pubid = 0;

    private static final Object obj = new Object();
    private static final Object logobj = new Object();
    private static final Object pubObj = new Object();

    public static int m_ServerId = 0;

    public static int platfromID = 0;

    public static int ServerType = 0;// 0游戏服， 1为世界服

    /**
     * 获得世界唯一id
     *
     * @return id
     */
    public static long getId()
    {
        synchronized (obj)
        {
            id = id + 1;
            return (((long) (platfromID & 0x000F)) << 59) | (((long) (m_ServerId & 0x07FF)) << 48) | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16) | ((ServerType & 0x00000001) << 15) | (id & 0x0000FFFF);
        }
    }

    /**
     * 用于使用在游戏中不保存的实例唯一ID值
     *
     * @return
     */
    public static long getLogId()
    {
        synchronized (logobj)
        {
            logid = logid + 1;
            return (((long) (platfromID & 0x000F)) << 59) | (((long) (m_ServerId & 0x07FF)) << 48) | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16) | (logid & 0x0000FFFF);
        }
    }

    /**
     * 获得唯一id
     *
     * @return id
     */
    public static long getPubId()
    {
        synchronized (pubObj)
        {
            pubid = pubid + 1;
            return (((long) (m_ServerId & 0xFFFF)) << 48) | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16) | (pubid & 0x0000FFFF);
        }
    }

    public static int GetServerIdById(long id)
    {
        return (int) ((0x07FF000000000000L & id) >> 48);
    }

    public static int GetLsIdById(long id)
    {
        return (int) (id >> 59);
    }
}
