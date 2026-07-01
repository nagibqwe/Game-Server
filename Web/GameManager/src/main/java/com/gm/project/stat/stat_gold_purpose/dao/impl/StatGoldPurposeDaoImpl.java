package com.gm.project.stat.stat_gold_purpose.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_gold_purpose.dao.IStatGoldPurposeDao;
import com.gm.project.stat.stat_gold_purpose.domain.GoldPurposeBean;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * 二次付费统计dao
 * 
 * @author gm
 * @date 2021-08-06
 */

@Service
public class StatGoldPurposeDaoImpl extends BaseDao implements IStatGoldPurposeDao
{
    private static final long serialVersionUID = 1L;


    public List<GoldPurposeBean> getGoldConsumeList(DBClient dbClient, String table, String serverId, String channelNames, String startDate, String endDate, String blackUsers){
        StringBuilder str = new StringBuilder();
        str.append("select reason,count(distinct(userid)) users,sum(changenum) totalConsume,sid from ");
        str.append(table);
        str.append(" where changenum<0 and sid in(");
        str.append(serverId);
        str.append(") and time between UNIX_TIMESTAMP('");
        str.append(startDate);
        str.append(" 00:00:00') and UNIX_TIMESTAMP('");
        str.append(endDate);
        str.append(" 23:59:59') ");
        if (!StringUtils.isBlank(channelNames)) {
            str.append(" and platformName in (").append(channelNames).append(") ");
        }
        if (blackUsers != null && !"".equals(blackUsers)) {
            str.append("and userid not in(");
            str.append(blackUsers);
            str.append(") ");
        }
        str.append("group by reason");
        if(dbClient == null){
            return null;
        }
        List<GoldPurposeBean> mapList = dbClient.selectList(str.toString(), GoldPurposeBean.class);
        return mapList;
    }


}
