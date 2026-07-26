package com.gm.project.monitor.logininfor.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.common.utils.text.Convert;
import com.gm.project.monitor.logininfor.domain.Logininfor;
import com.gm.project.monitor.logininfor.mapper.LogininforMapper;

/**
 * 系统访问Журнал情况Информация 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class LogininforServiceImpl implements ILogininforService
{
    @Autowired
    private LogininforMapper logininforMapper;

    /**
     * Добавить系统Журнал входов
     * 
     * @param logininfor 访问Журнал对象
     */
    @Override
    public void insertLogininfor(Logininfor logininfor)
    {
        logininforMapper.insertLogininfor(logininfor);
    }

    /**
     * 查询系统Журнал входов集合
     * 
     * @param logininfor 访问Журнал对象
     * @return 登录记录集合
     */
    @Override
    public List<Logininfor> selectLogininforList(Logininfor logininfor)
    {
        return logininforMapper.selectLogininforList(logininfor);
    }

    /**
     * 批量Удалить系统Журнал входов
     * 
     * @param ids 需要Удалить的Данные
     * @return
     */
    @Override
    public int deleteLogininforByIds(String ids)
    {
        return logininforMapper.deleteLogininforByIds(Convert.toStrArray(ids));
    }
    
    /**
     * 清空系统Журнал входов
     */
    @Override
    public void cleanLogininfor()
    {
        logininforMapper.cleanLogininfor();
    }
}
