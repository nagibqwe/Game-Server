package com.gm.project.gmtool.changereason.service;

import java.util.List;
import com.gm.project.gmtool.changereason.domain.TChangereason;

/**
 * 原因码Service接口
 * 
 * @author gm
 * @date 2021-12-21
 */
public interface ITChangereasonService 
{

    public void loadData();
    /**
     * 查询原因码
     * 
     * @param id 原因码ID
     * @return 原因码
     */
    public TChangereason selectTChangereasonById(Long id);

    /**
     * 查询原因码列表
     * 
     * @param tChangereason 原因码
     * @return 原因码集合
     */
    public List<TChangereason> selectTChangereasonList(TChangereason tChangereason);

    /**
     * 新增原因码
     * 
     * @param tChangereason 原因码
     * @return 结果
     */
    public int insertTChangereason(TChangereason tChangereason);

    /**
     * 修改原因码
     * 
     * @param tChangereason 原因码
     * @return 结果
     */
    public int updateTChangereason(TChangereason tChangereason);

    /**
     * 批量删除原因码
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteTChangereasonByIds(String ids);

    /**
     * 删除原因码信息
     * 
     * @param id 原因码ID
     * @return 结果
     */
    public int deleteTChangereasonById(Long id);

    /**
     * 删除全部
     * @return
     */
    public int deleteAllTChangereason();

}
