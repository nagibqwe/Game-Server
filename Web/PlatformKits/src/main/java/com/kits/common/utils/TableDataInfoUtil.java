package com.kits.common.utils;

import com.github.pagehelper.PageInfo;
import com.kits.framework.web.page.TableDataInfo;

import java.util.List;

/**
 * 查询返回 封装
 */
public class TableDataInfoUtil {
    public static TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }
    public static TableDataInfo getDataTableErrorMsg(String msg)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(-1);
        rspData.setMsg(msg);
        return rspData;
    }
}
