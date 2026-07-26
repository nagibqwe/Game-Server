package com.gm.project.gmtool.white.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.white.mapper.WhiteMapper;
import com.gm.project.gmtool.white.domain.White;
import com.gm.project.gmtool.white.service.IWhiteService;
import com.gm.common.utils.text.Convert;

/**
 * Белый списокService业务层处理
 * 
 * @author gm
 * @date 2021-11-22
 */
@Service
public class WhiteServiceImpl implements IWhiteService 
{
    @Autowired
    private WhiteMapper whiteMapper;

    /**
     * 查询Белый список
     * 
     * @param id Белый списокID
     * @return Белый список
     */
    @Override
    public White selectWhiteById(Long id)
    {
        return whiteMapper.selectWhiteById(id);
    }

    /**
     * 查询Белый список列表
     * 
     * @param white Белый список
     * @return Белый список
     */
    @Override
    public List<White> selectWhiteList(White white)
    {
        return whiteMapper.selectWhiteList(white);
    }

    /**
     * ДобавитьБелый список
     * 
     * @param white Белый список
     * @return Результат
     */
    @Override
    public int insertWhite(White white)
    {
        return whiteMapper.insertWhite(white);
    }

    /**
     * ИзменитьБелый список
     * 
     * @param white Белый список
     * @return Результат
     */
    @Override
    public int updateWhite(White white)
    {
        return whiteMapper.updateWhite(white);
    }

    /**
     * УдалитьБелый список对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteWhiteByIds(String ids)
    {
        return whiteMapper.deleteWhiteByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьБелый списокИнформация
     * 
     * @param id Белый списокID
     * @return Результат
     */
    @Override
    public int deleteWhiteById(Long id)
    {
        return whiteMapper.deleteWhiteById(id);
    }
}
