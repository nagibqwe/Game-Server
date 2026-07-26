package com.gm.project.gmtool.db.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.db.mapper.TDbMapper;
import com.gm.project.gmtool.db.domain.TDb;
import com.gm.project.gmtool.db.service.ITDbService;
import com.gm.common.utils.text.Convert;

/**
 * Журнал库列Service业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class TDbServiceImpl implements ITDbService 
{
    @Autowired
    private TDbMapper tDbMapper;

    /**
     * 查询Журнал库列
     * 
     * @param id Журнал库列ID
     * @return Журнал库列
     */
    @Override
    public TDb selectTDbById(Integer id)
    {
        return tDbMapper.selectTDbById(id);
    }

    /**
     * 查询Журнал库列
     *
     * @param serverId ID сервера
     * @return Журнал库列
     */
    @Override
    public TDb selectTDbByServerId(Integer serverId)
    {
        return tDbMapper.selectTDbByServerId(serverId);
    }

    /**
     * 查询Журнал库列列表
     * 
     * @param tDb Журнал库列
     * @return Журнал库列
     */
    @Override
    public List<TDb> selectTDbList(TDb tDb)
    {
        return tDbMapper.selectTDbList(tDb);
    }

    /**
     * ДобавитьЖурнал库列
     * 
     * @param tDb Журнал库列
     * @return Результат
     */
    @Override
    public int insertTDb(TDb tDb)
    {
        tDb.setUpdateDate(new Date());
        return tDbMapper.insertTDb(tDb);
    }

    /**
     * ИзменитьЖурнал库列
     * 
     * @param tDb Журнал库列
     * @return Результат
     */
    @Override
    public int updateTDb(TDb tDb)
    {
        tDb.setUpdateDate(new Date());
        return tDbMapper.updateTDb(tDb);
    }

    /**
     * УдалитьЖурнал库列对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteTDbByIds(String ids)
    {
        return tDbMapper.deleteTDbByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЖурнал库列Информация
     * 
     * @param id Журнал库列ID
     * @return Результат
     */
    @Override
    public int deleteTDbById(Integer id)
    {
        return tDbMapper.deleteTDbById(id);
    }
}
