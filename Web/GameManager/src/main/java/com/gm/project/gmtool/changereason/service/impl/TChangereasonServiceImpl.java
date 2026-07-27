package com.gm.project.gmtool.changereason.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.project.gmtool.item.domain.Item;
import com.gm.project.gmtool.manager.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.changereason.mapper.TChangereasonMapper;
import com.gm.project.gmtool.changereason.domain.TChangereason;
import com.gm.project.gmtool.changereason.service.ITChangereasonService;
import com.gm.common.utils.text.Convert;

import javax.annotation.PostConstruct;

/**
 * 原因码Service业务层处理
 * 
 * @author gm
 * @date 2021-12-21
 */
@Service
public class TChangereasonServiceImpl implements ITChangereasonService 
{

    public static TChangereasonServiceImpl getInstance() {
        return   (TChangereasonServiceImpl) SpringUtils.getBean("TChangereasonServiceImpl");
    }
    @PostConstruct
    public void init() {
        //启动时从数据库加载一次道具物品信息
        loadData();
    }
    private Map<String, String> changereasonMap = new HashMap<>();
    public Map<String, String> getReasonMap() {
        return changereasonMap;
    }

    public String getReason(String reason){

        if(changereasonMap.containsKey(reason)){
            return changereasonMap.get(reason) + "[" + reason + "]";
        }
        else {
            return changereasonMap.get(reason) + "[" + reason + "]";
        }
    }

    public void loadData() {
        changereasonMap.clear();
        List<TChangereason> items = this.selectTChangereasonList(new TChangereason());
        items.forEach(n -> changereasonMap.put(n.getId()+"", n.getName()));
    }

    @Autowired
    private TChangereasonMapper tChangereasonMapper;

    /**
     * 查询原因码
     * 
     * @param id 原因码ID
     * @return 原因码
     */
    @Override
    public TChangereason selectTChangereasonById(Long id)
    {
        return tChangereasonMapper.selectTChangereasonById(id);
    }

    /**
     * 查询原因码列表
     * 
     * @param tChangereason 原因码
     * @return 原因码
     */
    @Override
    public List<TChangereason> selectTChangereasonList(TChangereason tChangereason)
    {
        return tChangereasonMapper.selectTChangereasonList(tChangereason);
    }

    /**
     * 新增原因码
     * 
     * @param tChangereason 原因码
     * @return 结果
     */
    @Override
    public int insertTChangereason(TChangereason tChangereason)
    {
        return tChangereasonMapper.insertTChangereason(tChangereason);
    }

    /**
     * 修改原因码
     * 
     * @param tChangereason 原因码
     * @return 结果
     */
    @Override
    public int updateTChangereason(TChangereason tChangereason)
    {
        return tChangereasonMapper.updateTChangereason(tChangereason);
    }

    /**
     * 删除原因码对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTChangereasonByIds(String ids)
    {
        return tChangereasonMapper.deleteTChangereasonByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除原因码信息
     * 
     * @param id 原因码ID
     * @return 结果
     */
    @Override
    public int deleteTChangereasonById(Long id)
    {
        return tChangereasonMapper.deleteTChangereasonById(id);
    }


    public int deleteAllTChangereason(){
        return tChangereasonMapper.deleteAllTChangereason();
    }
}
