package com.gm.project.gamelog.coinchangelog.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import com.gm.project.gamelog.goldchangelog.domain.Goldchangelog;
import com.gm.project.gmtool.changereason.service.impl.TChangereasonServiceImpl;
import com.gm.project.gmtool.manager.ItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.coinchangelog.domain.Coinchangelog;
import com.gm.project.gamelog.coinchangelog.service.ICoinchangelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;

/**
 * 货币变化日志Service业务层处理
 *
 * @author gm
 * @date 2021-11-08
 */
@Service
public class CoinchangelogServiceImpl implements ICoinchangelogService {


    /**
     * 查询货币变化日志列表
     *
     * @param coinchangelog 货币变化日志
     * @return 货币变化日志
     */
    @Override
    public List<Coinchangelog> selectCoinchangelogList(Coinchangelog coinchangelog, Map<String, Object> param) {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件


        if (coinchangelog.getRoleId() != null) {
            wheresql.append(" and roleId = " + coinchangelog.getRoleId());
        }


        if (!StringUtils.isEmpty(coinchangelog.getMoneyType())) {
            wheresql.append(" and moneyType = " + coinchangelog.getMoneyType());
        }

        if (!StringUtils.isEmpty(coinchangelog.getReason())) {
            wheresql.append(" and reason = " + coinchangelog.getReason());
        }
        //自定义查询条件
        param.put("tableName", "coinchangelog");
        param.put("where", wheresql);
        List<Coinchangelog> coincchangelogList = GameLogUtil.getLogDataList(Coinchangelog.class, param);
        if (coincchangelogList != null && coincchangelogList.size() > 0) {
            for (int i = 0; i < coincchangelogList.size(); i++) {
                coincchangelogList.get(i).setReason(TChangereasonServiceImpl.getInstance().getReason(coincchangelogList.get(i).getReason() + ""));
                coincchangelogList.get(i).setMoneyType(ItemManager.getInstance().getItemName(Integer.parseInt(coincchangelogList.get(i).getMoneyType())));
            }
        }

        return coincchangelogList;
    }
}
