package com.kits.project.photocheck.photodata.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 图片信息对象 t_photodata
 * 
 * @author gm
 * @date 2021-07-19
 */
public class TPhotodata extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private String desc1;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String desc2;

    /** 角色名称 */
    @Excel(name = "角色名称")
    private String desc3;

    /** 预留描述 */
    @Excel(name = "预留描述")
    private String desc4;

    /** 预留描述 */
    @Excel(name = "预留描述")
    private String desc5;

    /** 预留描述 */
    @Excel(name = "预留描述")
    private String desc6;

    /** 原图的MD5 */
    @Excel(name = "原图的MD5")
    private String photoMD5;

    /** 原始图片的MD5+数据自增ID */
    @Excel(name = "原始图片的MD5+数据自增ID")
    private String photoID;

    /** 图片大小 */
    @Excel(name = "图片大小")
    private String photoSize;

    /** 小图图片大小 */
    @Excel(name = "小图图片大小")
    private String smallPhotoSize;

    /** 小图的MD5 */
    @Excel(name = "小图的MD5")
    private String smallMD5;

    /** 大图文件相对路径:{上传时间YYYYMMDD}/big/{图片ID}.{扩展名} */
    @Excel(name = "大图文件相对路径:{上传时间YYYYMMDD}/big/{图片ID}.{扩展名}")
    private String bigPhotoPath;

    /** 小图文件相对路径:{上传时间YYYYMMDD}/small/{图片ID}.{扩展名} */
    @Excel(name = "小图文件相对路径:{上传时间YYYYMMDD}/small/{图片ID}.{扩展名}")
    private String smallPhotoPath;

    /** 图片扩展名:jpeg 或者 png */
    @Excel(name = "图片扩展名:jpeg 或者 png")
    private String photoExtName;

    /** 图片上传时间 */
    @Excel(name = "图片上传时间")
    private String uploadTime;

    /** 请求次数 */
    @Excel(name = "请求次数")
    private Integer requestNum;

    /** 最后一次请求时间 */
    @Excel(name = "最后一次请求时间")
    private String lastRquestTime;

    /** 最后一次下载时间 */
    @Excel(name = "最后一次下载时间")
    private String lastDownloadTime;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDelete;

    /** 删除时间 */
    @Excel(name = "删除时间")
    private String deleteTime;

    /** 审核状态:0未审核 1:审核中 2:审核通过 3:审核未通过 */
    @Excel(name = "审核状态:0未审核 1:审核中 2:审核通过 3:审核未通过")
    private Integer checkStatus;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDesc1(String desc1)
    {
        this.desc1 = desc1;
    }

    public String getDesc1()
    {
        return desc1;
    }
    public void setDesc2(String desc2)
    {
        this.desc2 = desc2;
    }

    public String getDesc2()
    {
        return desc2;
    }
    public void setDesc3(String desc3)
    {
        this.desc3 = desc3;
    }

    public String getDesc3()
    {
        return desc3;
    }
    public void setDesc4(String desc4)
    {
        this.desc4 = desc4;
    }

    public String getDesc4()
    {
        return desc4;
    }
    public void setDesc5(String desc5)
    {
        this.desc5 = desc5;
    }

    public String getDesc5()
    {
        return desc5;
    }
    public void setDesc6(String desc6)
    {
        this.desc6 = desc6;
    }

    public String getDesc6()
    {
        return desc6;
    }
    public void setPhotoMD5(String photoMD5)
    {
        this.photoMD5 = photoMD5;
    }

    public String getPhotoMD5()
    {
        return photoMD5;
    }
    public void setPhotoID(String photoID)
    {
        this.photoID = photoID;
    }

    public String getPhotoID()
    {
        return photoID;
    }
    public void setPhotoSize(String photoSize)
    {
        this.photoSize = photoSize;
    }

    public String getPhotoSize()
    {
        return photoSize;
    }
    public void setSmallPhotoSize(String smallPhotoSize)
    {
        this.smallPhotoSize = smallPhotoSize;
    }

    public String getSmallPhotoSize()
    {
        return smallPhotoSize;
    }
    public void setSmallMD5(String smallMD5)
    {
        this.smallMD5 = smallMD5;
    }

    public String getSmallMD5()
    {
        return smallMD5;
    }
    public void setBigPhotoPath(String bigPhotoPath)
    {
        this.bigPhotoPath = bigPhotoPath;
    }

    public String getBigPhotoPath()
    {
        return bigPhotoPath;
    }
    public void setSmallPhotoPath(String smallPhotoPath)
    {
        this.smallPhotoPath = smallPhotoPath;
    }

    public String getSmallPhotoPath()
    {
        return smallPhotoPath;
    }
    public void setPhotoExtName(String photoExtName)
    {
        this.photoExtName = photoExtName;
    }

    public String getPhotoExtName()
    {
        return photoExtName;
    }
    public void setUploadTime(String uploadTime)
    {
        this.uploadTime = uploadTime;
    }

    public String getUploadTime()
    {
        return uploadTime;
    }
    public void setRequestNum(Integer requestNum)
    {
        this.requestNum = requestNum;
    }

    public Integer getRequestNum()
    {
        return requestNum;
    }
    public void setLastRquestTime(String lastRquestTime)
    {
        this.lastRquestTime = lastRquestTime;
    }

    public String getLastRquestTime()
    {
        return lastRquestTime;
    }
    public void setLastDownloadTime(String lastDownloadTime)
    {
        this.lastDownloadTime = lastDownloadTime;
    }

    public String getLastDownloadTime()
    {
        return lastDownloadTime;
    }
    public void setIsDelete(Integer isDelete)
    {
        this.isDelete = isDelete;
    }

    public Integer getIsDelete()
    {
        return isDelete;
    }
    public void setDeleteTime(String deleteTime)
    {
        this.deleteTime = deleteTime;
    }

    public String getDeleteTime()
    {
        return deleteTime;
    }
    public void setCheckStatus(Integer checkStatus)
    {
        this.checkStatus = checkStatus;
    }

    public Integer getCheckStatus()
    {
        return checkStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("desc1", getDesc1())
            .append("desc2", getDesc2())
            .append("desc3", getDesc3())
            .append("desc4", getDesc4())
            .append("desc5", getDesc5())
            .append("desc6", getDesc6())
            .append("photoMD5", getPhotoMD5())
            .append("photoID", getPhotoID())
            .append("photoSize", getPhotoSize())
            .append("smallPhotoSize", getSmallPhotoSize())
            .append("smallMD5", getSmallMD5())
            .append("bigPhotoPath", getBigPhotoPath())
            .append("smallPhotoPath", getSmallPhotoPath())
            .append("photoExtName", getPhotoExtName())
            .append("uploadTime", getUploadTime())
            .append("requestNum", getRequestNum())
            .append("lastRquestTime", getLastRquestTime())
            .append("lastDownloadTime", getLastDownloadTime())
            .append("isDelete", getIsDelete())
            .append("deleteTime", getDeleteTime())
            .append("checkStatus", getCheckStatus())
            .toString();
    }
}
