package com.gm.project.monitor.logininfor.mapper;

import java.util.List;
import com.gm.project.monitor.logininfor.domain.Logininfor;

/**
 * 系统访问Журнал情况Информация Данные层
 * 
 * @author ruoyi
 */
public interface LogininforMapper
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
     * @return Результат
     */
    public int deleteLogininforByIds(String[] ids);

    /**
     * 清空系统Журнал входов
     * 
     * @return Результат
     */
    public int cleanLogininfor();
}
