package com.gm.project.gmtool.cyAnnounce.service.impl;

import java.util.List;
import com.gm.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.cyAnnounce.mapper.CyAnnounceMapper;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;
import com.gm.project.gmtool.cyAnnounce.service.ICyAnnounceService;
import com.gm.common.utils.text.Convert;

/**
 * Циклическое объявлениеService业务层处理
 * 
 * @author gm
 * @date 2021-10-27
 */
@Service
public class CyAnnounceServiceImpl implements ICyAnnounceService 
{
    @Autowired
    private CyAnnounceMapper cyAnnounceMapper;

    /**
     * 查询Циклическое объявление
     * 
     * @param id Циклическое объявлениеID
     * @return Циклическое объявление
     */
    @Override
    public CyAnnounce selectCyAnnounceById(Integer id)
    {
        return cyAnnounceMapper.selectCyAnnounceById(id);
    }

    /**
     * 查询Циклическое объявление列表
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Циклическое объявление
     */
    @Override
    public List<CyAnnounce> selectCyAnnounceList(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.selectCyAnnounceList(cyAnnounce);
    }

    /**
     * ДобавитьЦиклическое объявление
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Результат
     */
    @Override
    public int insertCyAnnounce(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.insertCyAnnounce(cyAnnounce);
    }

    /**
     * ИзменитьЦиклическое объявление
     * 
     * @param cyAnnounce Циклическое объявление
     * @return Результат
     */
    @Override
    public int updateCyAnnounce(CyAnnounce cyAnnounce)
    {
        return cyAnnounceMapper.updateCyAnnounce(cyAnnounce);
    }

    /**
     * УдалитьЦиклическое объявление对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteCyAnnounceByIds(String ids)
    {
        return cyAnnounceMapper.deleteCyAnnounceByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьЦиклическое объявлениеИнформация
     * 
     * @param id Циклическое объявлениеID
     * @return Результат
     */
    @Override
    public int deleteCyAnnounceById(Integer id)
    {
        return cyAnnounceMapper.deleteCyAnnounceById(id);
    }

    /**
     * 查询Циклическое объявление列表(禁用列表)
     * @param cyAnnounce
     * @return
     */
    @Override
    public List<CyAnnounce> selectCyAnnounceDisableList(CyAnnounce cyAnnounce) {
        return cyAnnounceMapper.selectCyAnnounceDisableList(cyAnnounce);
    }
}
