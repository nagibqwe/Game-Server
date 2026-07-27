/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gm.project.gmtool.hefu.tool;

import com.alibaba.fastjson.JSON;
import com.gm.project.gmtool.hefu.HefuTask;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author lanxiang@haowan123.com
 */
public class HeFuCheck {
    
    private static final Logger log = LoggerFactory.getLogger("HeFuTool");

    public static void check(HefuTask task) throws Exception{
        String str = task.getHefu().getRecord();
        log.info(str);
        String[] strArray = str.split("#");
        if (strArray.length < 2) {
            throw new RuntimeException("合服检验 record 中数据记录错误！");
        }
        Map<String, Integer> toTableMap = JSON.parseObject(strArray[0], HashMap.class); //目标合服上数据表的数据量
        List<Map<String, Integer>> fromList = new ArrayList<>();
        //被合服上数据表的数据量
        for(int i = 1;i < strArray.length;i++){
            fromList.add(JSON.parseObject(strArray[i], HashMap.class));
        }

        HefuServer toServerInfo = task.getToServer();
        Connection toConn = toServerInfo.getDb().getConn();
        Statement toStat = toConn.createStatement();
        Connection toLogConn = toServerInfo.getDblog().getConn();
        Statement toLogStat = toLogConn.createStatement();
        ResultSet rs;

        task.writeLog("开始合服数据完整性检验");
        boolean isOk = true;
        StringBuilder logsb = new StringBuilder();
        StringBuilder sqlSe = new StringBuilder();
        for (String table : toTableMap.keySet()) {
            sqlSe.setLength(0);
            sqlSe.append("select count(*) from ").append(table);
            if (table.equals("rolestate") || table.equals("roleitems")) {
                rs = toLogStat.executeQuery(sqlSe.toString());
            } else {
                rs = toStat.executeQuery(sqlSe.toString());
            }
            while (rs.next()) {
                int totalNum = toTableMap.get(table);
                for(Map<String,Integer> v:fromList){
                    totalNum += v.get(table);
                }
                int realNum = rs.getInt(1);
                if (totalNum != realNum) {
                    isOk = false;
                    logsb.setLength(0);
                    logsb.append(table).append("表合服数据完整性检验失败");
                    for(int i = 0; i < fromList.size(); i++){
                        logsb.append(", fromNum[").append(i).append("]=").append(fromList.get(i).get(table));
                    }
                    logsb.append(", toNum=").append(toTableMap.get(table)).append(", totalNum=" ).append(totalNum).append(",realTotalNum =").append(realNum);
                    log.error(logsb.toString());
                    System.out.println(logsb.toString());
                }
            }
        }
        task.writeLog("合服数据完整性检验完毕！");
        if (isOk) {
            task.writeLog("检验结果：合服成功！");
        } else {
            throw new RuntimeException("检验结果：合服失败！");
        }
    }
    
}

