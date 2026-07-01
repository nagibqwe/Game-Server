package com.kits.project.monitor.job.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kits.common.dbclient.DBClient;
import com.kits.common.dbclient.DBServerMgr;
import com.kits.common.utils.DateUtils;
import com.kits.common.utils.LogUtil;
import com.kits.common.utils.http.HttpUtils;
import com.kits.framework.aspectj.lang.annotation.DataSource;
import com.kits.framework.aspectj.lang.enums.DataSourceType;
import com.kits.framework.config.ProjectConfig;
import com.kits.project.photocheck.photodata.domain.TPhotodata;
import com.kits.project.photocheck.photodata.service.ITPhotodataService;
import com.kits.project.photocheck.photodata.utils.PhotoFileUtils;
import com.kits.project.photocheck.photoglobal.domain.TPhotoglobal;
import com.kits.project.photocheck.photoglobal.service.ITPhotoglobalService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kits.common.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static com.kits.project.photocheck.photodata.controller.TPhotodataController.dayLimit;

/**
 * 定时任务调度测试
 * 
 * @author ruoyi
 */
@Component("ryTask")
public class RyTask
{
    @Autowired
    private ProjectConfig ProjectConfig;

    @Autowired
    private ITPhotoglobalService tPhotoglobalService;

    @Autowired
    private ITPhotodataService tPhotodataService;

    public void ryMultipleParams(String s, Boolean b, Long l, Double d, Integer i)
    {
        System.out.println(StringUtils.format("执行多参方法： 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", s, b, l, d, i));
    }

    public void ryParams(String params)
    {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams()
    {
        System.out.println("执行无参方法");
    }

    /**
     * 定时检测回收站的数据是否需要删除(文件和数据都删除)
     */
    public void checkIsDelete(){
        TPhotoglobal tPhotoglobal = new TPhotoglobal();
        int dayLimit1 = dayLimit;
        List<TPhotoglobal> tPhotoglobals = tPhotoglobalService.selectTPhotoglobalList(tPhotoglobal);
        if (null != tPhotoglobals && tPhotoglobals.size() > 0){
            for (TPhotoglobal tPhotoglobal1:tPhotoglobals){
                if (tPhotoglobal1.getKeyStr().equals("dayLimit")){
                    dayLimit1 = Integer.parseInt(tPhotoglobal1.getValueStr());
                }
            }
        }
        long expiredTime = dayLimit1 * 24 * 60 * 60;
        TPhotodata tPhotodata = new TPhotodata();
        tPhotodata.setIsDelete(1);
        List<TPhotodata> list = tPhotodataService.selectTPhotodataList(tPhotodata);
        Date dateNow = DateUtils.getNowDate();
        if (null != list && list.size() > 0){
            for (TPhotodata tPhotodata1:list){
                String deleteTimeStr = tPhotodata1.getDeleteTime();
                Date deleteTime = DateUtils.stringToDateTime(deleteTimeStr);
                long diff = DateUtils.dateDiff(deleteTime,dateNow)/1000;
                if (diff >= expiredTime){
                    String bigPhotoPath = tPhotodata1.getBigPhotoPath();
                    String smallPhotoPath = tPhotodata1.getSmallPhotoPath();
                    PhotoFileUtils.delete(bigPhotoPath,smallPhotoPath);
                    long id = tPhotodata1.getId();
                    LogUtil.info("删除id="+id+"的图片文件成功");
                    int row = tPhotodataService.deleteTPhotodataById(tPhotodata1.getId());
                    if (row > 0){
                        LogUtil.info("删除id="+id+"的图片数据库数据成功");
                    }
                }
            }
        }
    }

}
