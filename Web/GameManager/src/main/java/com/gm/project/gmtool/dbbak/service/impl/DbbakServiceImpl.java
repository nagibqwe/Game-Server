package com.gm.project.gmtool.dbbak.service.impl;

import java.io.File;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.dbbak.mapper.DbbakMapper;
import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.dbbak.service.IDbbakService;
import com.gm.common.utils.text.Convert;

/**
 * Резервные копии БДService业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class DbbakServiceImpl implements IDbbakService 
{
    @Autowired
    private DbbakMapper dbbakMapper;

    /**
     * 查询Резервные копии БД
     * 
     * @param id Резервные копии БДID
     * @return Резервные копии БД
     */
    @Override
    public Dbbak selectDbbakById(Long id)
    {
        return dbbakMapper.selectDbbakById(id);
    }

    @Override
    public Dbbak selectLatestDbbak(Dbbak dbbak) {
        return dbbakMapper.selectLatestDbbak(dbbak);
    }

    /**
     * 查询Список резервных копий БД
     * 
     * @param dbbak Резервные копии БД
     * @return Резервные копии БД
     */
    @Override
    public List<Dbbak> selectDbbakList(Dbbak dbbak)
    {
        return dbbakMapper.selectDbbakList(dbbak);
    }

    /**
     * ДобавитьРезервные копии БД
     * 
     * @param dbbak Резервные копии БД
     * @return Результат
     */
    @Override
    public int insertDbbak(Dbbak dbbak)
    {
        return dbbakMapper.insertDbbak(dbbak);
    }

    /**
     * ИзменитьРезервные копии БД
     * 
     * @param dbbak Резервные копии БД
     * @return Результат
     */
    @Override
    public int updateDbbak(Dbbak dbbak)
    {
        return dbbakMapper.updateDbbak(dbbak);
    }

    /**
     * УдалитьРезервные копии БД对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteDbbakByIds(String ids)
    {
        String[] arr = Convert.toStrArray(ids);
        for(String id : arr){
            Long v = Long.valueOf(id);
            deleteDbbakById(v);
        }
        return arr.length;
    }

    /**
     * УдалитьРезервные копии БДИнформация
     * 
     * @param id Резервные копии БДID
     * @return Результат
     */
    @Override
    public int deleteDbbakById(Long id)
    {
        Dbbak dbbak = selectDbbakById(id);
        if(dbbak != null){
            String url = dbbak.getUrl();
            if(url != null && url.length() > 0){
                File file = new File(url);
                file.deleteOnExit();
            }
        }
        return dbbakMapper.deleteDbbakById(id);
    }
}
