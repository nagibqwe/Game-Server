package com.gm.project.gmtool.hefu.service;

import java.util.List;
import java.util.Map;

import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.hefu.domain.Hefu;

/**
 * 合服Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface IHefuService 
{
    /**
     * 查询合服
     * 
     * @param id 合服ID
     * @return 合服
     */
    public Hefu selectHefuById(Long id);

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
     * 批量删除合服
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteHefuByIds(String ids);

    /**
     * 删除合服信息
     * 
     * @param id 合服ID
     * @return 结果
     */
    public int deleteHefuById(Long id);

    /**
     * 开始合服
     * @param id
     * @return
     */
    public boolean start(Long id);

    /**
     * 停止合服
     * @param id
     * @return
     */
    public boolean stop(Long id);

    /**
     * 合服日志
     * @param id
     * @param index
     * @return
     */
    Map<String, Object> getLog(Long id, Integer index);

    /**
     * 数据库备份
     * @param id
     * @param type
     */
    void dbbak(Long id, Integer serverId, Integer type);

    /**
     * 数据备份列表
     * @param id
     */
    List<Dbbak> bakList(Long id);

    /**
     * 数据库还原
     * @param id
     * @param serverId
     * @param type
     */
    void dbrestore(Long id, Integer serverId, Integer type);

    /**
     * 检测配置
     * @param id
     * @return
     */
    boolean check(Long id) throws Exception;

    /**
     * 检测是否有已合并的服务器
     * @param id
     * @return
     */
    Map<Integer, Integer> checkIsHefu(Long id);

    /**
     * 查询日志记录
     * @param id
     * @return
     */
    List<String> logRecord(Long id);
}
