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
 * Код причиныService业务层处理
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
        //启动时从Данные库加载一次道具物品Информация
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
     * 查询Код причины
     * 
     * @param id Код причиныID
     * @return Код причины
     */
    @Override
    public TChangereason selectTChangereasonById(Long id)
    {
        return tChangereasonMapper.selectTChangereasonById(id);
    }

    /**
     * 查询Код причины列表
     * 
     * @param tChangereason Код причины
     * @return Код причины
     */
    @Override
    public List<TChangereason> selectTChangereasonList(TChangereason tChangereason)
    {
        return tChangereasonMapper.selectTChangereasonList(tChangereason);
    }

    /**
     * ДобавитьКод причины
     * 
     * @param tChangereason Код причины
     * @return Результат
     */
    @Override
    public int insertTChangereason(TChangereason tChangereason)
    {
        return tChangereasonMapper.insertTChangereason(tChangereason);
    }

    /**
     * ИзменитьКод причины
     * 
     * @param tChangereason Код причины
     * @return Результат
     */
    @Override
    public int updateTChangereason(TChangereason tChangereason)
    {
        return tChangereasonMapper.updateTChangereason(tChangereason);
    }

    /**
     * УдалитьКод причины对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteTChangereasonByIds(String ids)
    {
        return tChangereasonMapper.deleteTChangereasonByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьКод причиныИнформация
     * 
     * @param id Код причиныID
     * @return Результат
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
