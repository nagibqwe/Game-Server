package com.kits.project.clientlog.clientlogdata.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 客户端日志对象 t_clientlog
 * 
 * @author gzg
 * @date 2021-06-18
 */
public class Clientlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 自增ID */
    private Long id;

    /** 设备ID */
    @Excel(name = "设备ID")
    private String uuid;

    /** MIEI */
    @Excel(name = "MIEI")
    private String miei;

    /** IDFA */
    @Excel(name = "IDFA")
    private String idfa;

    /** 游戏标记 */
    @Excel(name = "游戏标记")
    private String game;

    /** 玩家信息 */
    @Excel(name = "玩家信息")
    private String playerinfo;

    /** 版本号 */
    @Excel(name = "版本号")
    private String version;

    /** 内存使用 */
    @Excel(name = "内存使用")
    private String memUsed;

    /** 内存未使用 */
    @Excel(name = "内存未使用")
    private String memFree;

    /** 文件md5 */
    @Excel(name = "文件md5")
    private String filemd5;

    /** 时间 */
    @Excel(name = "时间")
    private String time;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public String getUuid()
    {
        return uuid;
    }
    public void setMiei(String miei)
    {
        this.miei = miei;
    }

    public String getMiei()
    {
        return miei;
    }
    public void setIdfa(String idfa)
    {
        this.idfa = idfa;
    }

    public String getIdfa()
    {
        return idfa;
    }
    public void setGame(String game)
    {
        this.game = game;
    }

    public String getGame()
    {
        return game;
    }
    public void setPlayerinfo(String playerinfo)
    {
        this.playerinfo = playerinfo;
    }

    public String getPlayerinfo()
    {
        return playerinfo;
    }
    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return version;
    }
    public void setMemUsed(String memUsed)
    {
        this.memUsed = memUsed;
    }

    public String getMemUsed()
    {
        return memUsed;
    }
    public void setMemFree(String memFree)
    {
        this.memFree = memFree;
    }

    public String getMemFree()
    {
        return memFree;
    }
    public void setFilemd5(String filemd5)
    {
        this.filemd5 = filemd5;
    }

    public String getFilemd5()
    {
        return filemd5;
    }
    public void setTime(String time)
    {
        this.time = time;
    }

    public String getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("uuid", getUuid())
            .append("miei", getMiei())
            .append("idfa", getIdfa())
            .append("game", getGame())
            .append("playerinfo", getPlayerinfo())
            .append("version", getVersion())
            .append("memUsed", getMemUsed())
            .append("memFree", getMemFree())
            .append("filemd5", getFilemd5())
            .append("time", getTime())
            .toString();
    }
}
