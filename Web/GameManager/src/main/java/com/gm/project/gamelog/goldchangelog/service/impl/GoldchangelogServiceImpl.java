package com.gm.project.gamelog.goldchangelog.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import com.gm.project.gamelog.itemchangelog.domain.Itemchangelog;
import com.gm.project.gmtool.changereason.service.impl.TChangereasonServiceImpl;
import com.gm.project.gmtool.manager.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.goldchangelog.domain.Goldchangelog;
import com.gm.project.gamelog.goldchangelog.service.IGoldchangelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 元宝变化日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-11
 */
@Service
public class GoldchangelogServiceImpl implements IGoldchangelogService 
{


    /**
     * 查询元宝变化日志列表
     * 
     * @param goldchangelog 元宝变化日志
     * @return 元宝变化日志
     */
    @Override
    public List<Goldchangelog> selectGoldchangelogList(Goldchangelog goldchangelog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(goldchangelog.getRoleId() != null){
            wheresql.append(" and roleId = " + goldchangelog.getRoleId());
        }
        if(!StringUtils.isEmpty(goldchangelog.getReason())){
            wheresql.append(" and reason = " + goldchangelog.getReason());
        }

        //自定义查询条件
        param.put("tableName","goldchangelog");
        param.put("where",wheresql);

        List<Goldchangelog> goldchangelogList =  GameLogUtil.getLogDataList(Goldchangelog.class,param);
        if(goldchangelogList!=null && goldchangelogList.size()>0){
            for(int i = 0;i<goldchangelogList.size();i++){
                goldchangelogList.get(i).setReason(TChangereasonServiceImpl.getInstance().getReason(goldchangelogList.get(i).getReason()+""));
            }
        }
        return goldchangelogList;

    }
}
