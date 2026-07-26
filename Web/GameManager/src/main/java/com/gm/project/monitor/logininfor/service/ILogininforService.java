package com.gm.project.monitor.logininfor.service;

import java.util.List;
import com.gm.project.monitor.logininfor.domain.Logininfor;

/**
 * 系统访问Журнал情况Информация 服务层
 * 
 * @author ruoyi
 */
public interface ILogininforService
{
    /**
     * Добавить系统Журнал входов
     * 
     * @param logininfor 访问Журнал对象
     */
    public void insertLogininfor(Logininfor logininfor);

    /**
     * 查询系统Журнал входов集合
     * 
     * @param logininfor 访问Журнал对象
     * @return 登录记录集合
     */
    public List<Logininfor> selectLogininforList(Logininfor logininfor);

    /**
     * 批量Удалить系统Журнал входов
     * 
     * @param ids 需要Удалить的Данные
     * @return
     */
    public int deleteLogininforByIds(String ids);
    
    /**
     * 清空系统Журнал входов
     */
    public void cleanLogininfor();
}
