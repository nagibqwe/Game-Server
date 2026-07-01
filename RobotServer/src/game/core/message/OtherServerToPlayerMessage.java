/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.message;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class OtherServerToPlayerMessage
{
    private int id;

    private long sendId;

    private List<Long> roleIds = new ArrayList<>();

    private byte[] bytes;
    //发送时间
    private int sendTime;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public long getSendId()
    {
        return sendId;
    }

    public void setSendId(long sendId)
    {
        this.sendId = sendId;
    }

    public List<Long> getRoleIds()
    {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds)
    {
        this.roleIds = roleIds;
    }

    public byte[] getBytes()
    {
        return bytes;
    }

    public void setBytes(byte[] bytes)
    {
        this.bytes = bytes;
    }

    public int getSendTime()
    {
        return sendTime;
    }

    public void setSendTime(int sendTime)
    {
        this.sendTime = sendTime;
    }

    public int getLength()
    {
        return bytes.length + Integer.SIZE / Byte.SIZE;
    }

    public int getLengthWithRole()
    {
        return bytes.length + Integer.SIZE / Byte.SIZE + Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE + roleIds.size() * Long.SIZE / Byte.SIZE + Integer.SIZE / Byte.SIZE;
    }
}
