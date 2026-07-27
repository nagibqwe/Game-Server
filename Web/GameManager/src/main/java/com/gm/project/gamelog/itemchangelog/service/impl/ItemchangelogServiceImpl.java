package com.gm.project.gamelog.itemchangelog.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.changereason.service.impl.TChangereasonServiceImpl;
import com.gm.project.gmtool.manager.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.itemchangelog.domain.Itemchangelog;
import com.gm.project.gamelog.itemchangelog.service.IItemchangelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 物品变化日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class ItemchangelogServiceImpl implements IItemchangelogService 
{


    /**
     * 查询物品变化日志列表
     * 
     * @param itemchangelog 物品变化日志
     * @return 物品变化日志
     */
    @Override
    public List<Itemchangelog> selectItemchangelogList(Itemchangelog itemchangelog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(itemchangelog.getRoleId() != null){
            wheresql.append(" and roleId = " + itemchangelog.getRoleId());
        }
        if(itemchangelog.getItemId() != null){
            wheresql.append(" and itemId = " + itemchangelog.getItemId());
        }
        if(!StringUtils.isEmpty(itemchangelog.getModelId())){
            wheresql.append(" and modelId = " + itemchangelog.getModelId());
        }
        if(!StringUtils.isEmpty(itemchangelog.getReason())){
            wheresql.append(" and reason = " + itemchangelog.getReason());
        }

        //自定义查询条件
        param.put("tableName","itemchangelog");
        param.put("where",wheresql);
        List<Itemchangelog> itemchangelogList =  GameLogUtil.getLogDataList(Itemchangelog.class,param);
        if(itemchangelogList!=null && itemchangelogList.size()>0){
            for(int i = 0;i<itemchangelogList.size();i++){
                itemchangelogList.get(i).setReason(TChangereasonServiceImpl.getInstance().getReason(itemchangelogList.get(i).getReason()+""));

                itemchangelogList.get(i).setModelId(ItemManager.getInstance().getItemName(Integer.parseInt(itemchangelogList.get(i).getModelId())));

            }
        }
        return itemchangelogList;
    }
}
