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
 * 数据库备份Service业务层处理
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
     * 查询数据库备份
     * 
     * @param id 数据库备份ID
     * @return 数据库备份
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
     * 查询数据库备份列表
     * 
     * @param dbbak 数据库备份
     * @return 数据库备份
     */
    @Override
    public List<Dbbak> selectDbbakList(Dbbak dbbak)
    {
        return dbbakMapper.selectDbbakList(dbbak);
    }

    /**
     * 新增数据库备份
     * 
     * @param dbbak 数据库备份
     * @return 结果
     */
    @Override
    public int insertDbbak(Dbbak dbbak)
    {
        return dbbakMapper.insertDbbak(dbbak);
    }

    /**
     * 修改数据库备份
     * 
     * @param dbbak 数据库备份
     * @return 结果
     */
    @Override
    public int updateDbbak(Dbbak dbbak)
    {
        return dbbakMapper.updateDbbak(dbbak);
    }

    /**
     * 删除数据库备份对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
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
     * 删除数据库备份信息
     * 
     * @param id 数据库备份ID
     * @return 结果
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
