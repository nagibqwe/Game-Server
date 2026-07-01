package com.gm.project.gmtool.white.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.white.mapper.WhiteMapper;
import com.gm.project.gmtool.white.domain.White;
import com.gm.project.gmtool.white.service.IWhiteService;
import com.gm.common.utils.text.Convert;

/**
 * 白名单管理Service业务层处理
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
     * 查询白名单管理
     * 
     * @param id 白名单管理ID
     * @return 白名单管理
     */
    @Override
    public White selectWhiteById(Long id)
    {
        return whiteMapper.selectWhiteById(id);
    }

    /**
     * 查询白名单管理列表
     * 
     * @param white 白名单管理
     * @return 白名单管理
     */
    @Override
    public List<White> selectWhiteList(White white)
    {
        return whiteMapper.selectWhiteList(white);
    }

    /**
     * 新增白名单管理
     * 
     * @param white 白名单管理
     * @return 结果
     */
    @Override
    public int insertWhite(White white)
    {
        return whiteMapper.insertWhite(white);
    }

    /**
     * 修改白名单管理
     * 
     * @param white 白名单管理
     * @return 结果
     */
    @Override
    public int updateWhite(White white)
    {
        return whiteMapper.updateWhite(white);
    }

    /**
     * 删除白名单管理对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteWhiteByIds(String ids)
    {
        return whiteMapper.deleteWhiteByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除白名单管理信息
     * 
     * @param id 白名单管理ID
     * @return 结果
     */
    @Override
    public int deleteWhiteById(Long id)
    {
        return whiteMapper.deleteWhiteById(id);
    }
}
