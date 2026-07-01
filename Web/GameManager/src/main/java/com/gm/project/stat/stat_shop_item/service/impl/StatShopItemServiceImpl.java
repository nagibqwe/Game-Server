package com.gm.project.stat.stat_shop_item.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;

import com.gm.project.gmtool.item.service.IItemService;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.gmtool.manager.ItemManager;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.stat.stat_shop_item.dao.IStatShopItemDao;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import com.gm.project.stat.stat_shop_item.service.IStatShopItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商店统计 服务
 * @author ruoyi
 */
@Service
public class StatShopItemServiceImpl implements IStatShopItemService
{

    @Autowired
    private ItemManager itemManager;

    @Autowired
    public IStatShopItemDao statShopItemDao;
    public  List<ShopItemBean> statShopItem(String groupName, Integer selectServerId, String channelNames, Integer FromSrc, Integer moneyType,String startDate, String endDate, Boolean isBlack){
        //渠道筛选
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单
        String blackUsers = "";
        if (isBlack!=null && isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(groupName);
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        String tableName = "shopbuylog";
        //时间通用 判断
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        start.setTime(DateUtils.parseDate(startDate));
        end.setTime(DateUtils.parseDate(endDate));
        long startTime = start.getTimeInMillis() / 1000;
        long endTime = end.getTimeInMillis() / 1000;
        //获取合服相关数据库列表
        List<TServer> tServerList = DBServerMgr.getInstance().checkHeFu(selectServerId, DateUtils.parseDate(startDate),DateUtils.parseDate(endDate));
        //得到按日期分表的 列表
        List<String> realTables = DBServerMgr.getInstance().getQueryTables(tableName, TableType.Month ,startDate,endDate);
        Map<String,ShopItemBean> shopItemBeanMap = new HashMap<>();
        for (TServer db : tServerList) {
            List<String> tableList = DBServerMgr.getInstance().queryTables(db,tableName);
            if(tableList != null){
                realTables.retainAll(tableList);
            }
            if (realTables.isEmpty()) {
                continue;
            }
            Collections.sort(realTables);
            DBClient dbClient = DBServerMgr.getInstance().getLogDBClient(db);
            if(dbClient == null){
                continue;
            }
            //查询数据
            List<ShopItemBean> dataList = statShopItemDao.statShopItem(dbClient,channelNames,realTables,FromSrc,moneyType,blackUsers,selectServerId,startTime,endTime);
            if(dataList!=null && dataList.size()>0){
               //合并多个数据库的数据 按照物品id 相同的累加
                for(int i = 0;i<dataList.size();i++){
                    if(shopItemBeanMap.containsKey(dataList.get(i).getItemmodelid()+"_"+dataList.get(i).getMoneyType())){
                        ShopItemBean shopItemBean = shopItemBeanMap.get(dataList.get(i).getItemmodelid()+"_"+dataList.get(i).getMoneyType());
                        shopItemBean.setUsers(shopItemBean.getUsers()+dataList.get(i).getUsers());
                        shopItemBean.setRoles(shopItemBean.getRoles()+dataList.get(i).getRoles());
                        shopItemBean.setTotalgold(shopItemBean.getTotalgold()+dataList.get(i).getTotalgold());
                        shopItemBean.setTotalnum(shopItemBean.getTotalnum()+dataList.get(i).getTotalnum());
                        shopItemBean.setTotaltimes(shopItemBean.getTotaltimes()+dataList.get(i).getTotaltimes());
                    }else {
                        shopItemBeanMap.put(dataList.get(i).getItemmodelid()+"_"+dataList.get(i).getMoneyType(),dataList.get(i));
                    }
                }
            }
        }
        List<ShopItemBean> finalDataList = new ArrayList<>();
        for(ShopItemBean shopItemBean : shopItemBeanMap.values()){
            shopItemBean.setItemmodelidShow(ItemManager.getInstance().getItemName(shopItemBean.getItemmodelid()));
            shopItemBean.setMoneyTypeShow(ItemManager.getInstance().getItemName(shopItemBean.getMoneyType()));
            finalDataList.add(shopItemBean);
        }
        return finalDataList;
    }
}
