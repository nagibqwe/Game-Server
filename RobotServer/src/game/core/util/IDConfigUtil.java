/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

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

    private static final Object obj = new Object();

    public static int m_ServerId = 0;

    public static int platfromID = 1;

    /**
     * 获得唯一id
     *
     * @return id
     */
    public static long getId()
    {
        synchronized (obj)
        {
            id = id + 1;
            return (((long) (m_ServerId & 0xFFFF)) << 48) | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16) | (id & 0x0000FFFF);
        }
    }
    
    public static int GetServerIdById(long id)
    {
        return (int)((0xFFFF0000L & id) >> 48);
    }
}
