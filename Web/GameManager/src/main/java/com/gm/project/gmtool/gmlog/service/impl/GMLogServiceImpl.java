package com.gm.project.gmtool.gmlog.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.gmlog.mapper.GMLogMapper;
import com.gm.project.gmtool.gmlog.domain.GMLog;
import com.gm.project.gmtool.gmlog.service.IGMLogService;
import com.gm.common.utils.text.Convert;

/**
 * Журнал GM-панелиService业务层处理
 * 
 * @author gm
 * @date 2021-09-01
 */
@Service
public class GMLogServiceImpl implements IGMLogService 
{
    @Autowired
    private GMLogMapper gMLogMapper;

    /**
     * 查询Журнал GM-панели
     * 
     * @param id Журнал GM-панелиID
     * @return Журнал GM-панели
     */
    @Override
    public GMLog selectGMLogById(Long id)
    {
        return gMLogMapper.selectGMLogById(id);
    }

    /**
     * 查询Журнал GM-панели列表
     * 
     * @param gMLog Журнал GM-панели
     * @return Журнал GM-панели
     */
    @Override
    public List<GMLog> selectGMLogList(GMLog gMLog)
    {
        return gMLogMapper.selectGMLogList(gMLog);
    }

    /**
     * ДобавитьЖурнал GM-панели
     * 
     * @param gMLog Журнал GM-панели
     * @return Результат
     */
    @Override
    public int insertGMLog(GMLog gMLog)
    {
        return gMLogMapper.insertGMLog(gMLog);
    }

    /**
     * ИзменитьЖурнал GM-панели
     * 
     * @param gMLog Журнал GM-панели
     * @return Результат
     */
    @Override
    public int updateGMLog(GMLog gMLog)
    {
        return gMLogMapper.updateGMLog(gMLog);
    }

    /**
     * УдалитьЖурнал GM-панели对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteGMLogByIds(String ids)
    {
        return gMLogMapper.deleteGMLogByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЖурнал GM-панелиИнформация
     * 
     * @param id Журнал GM-панелиID
     * @return Результат
     */
    @Override
    public int deleteGMLogById(Long id)
    {
        return gMLogMapper.deleteGMLogById(id);
    }
}
