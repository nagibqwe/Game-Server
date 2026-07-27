package com.gm.project.gmtool.hefu.mapper;

import java.util.List;
import com.gm.project.gmtool.hefu.domain.Hefu;

/**
 * 合服Mapper接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface HefuMapper 
{
    /**
     * 查询合服
     * 
     * @param id 合服ID
     * @return 合服
     */
    public Hefu selectHefuById(Long id);

    /**
     * 查询合服详细信息
     *
     * @param id 合服ID
     * @return 合服
     */
    public Hefu selectHefuRecord(Long id);

    /**
     * 查询合服列表
     * 
     * @param hefu 合服
     * @return 合服集合
     */
    public List<Hefu> selectHefuList(Hefu hefu);

    /**
     * 新增合服
     * 
     * @param hefu 合服
     * @return 结果
     */
    public int insertHefu(Hefu hefu);

    /**
     * 修改合服
     * 
     * @param hefu 合服
     * @return 结果
     */
    public int updateHefu(Hefu hefu);

    /**
     * 删除合服
     * 
     * @param id 合服ID
     * @return 结果
     */
    public int deleteHefuById(Long id);

    /**
     * 批量删除合服
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHefuByIds(String[] ids);
}
