package com.gm.project.stat.stat_role.service.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_role.dao.IStatRoleDao;
import com.gm.project.stat.stat_role.domain.RoleInfoBean;
import com.gm.project.stat.stat_role.service.IStatRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商店统计 服务
 * @author ruoyi
 */
@Service
public class StatRoleServiceImpl implements IStatRoleService
{
    @Autowired
    public IStatRoleDao statRoleDao;
    public List<RoleInfoBean> stat_role(TableDataInfo tableDataInfo,String groupName, Integer selectServerId, String channelNames, String sortType, int pageIndex,int pageSize)
    {
         int sid = DBServerMgr.getInstance().getHeFuId(selectServerId);
         String table = "rolestate";
         if (StringUtils.isBlank(sortType)) sortType = "rechargeGold";//设置默认

        DBClient dbClient  = DBServerMgr.getInstance().getLogDBClient(sid);
        if(dbClient == null){
            return null;
        }
        int count = this.statRoleDao.getRoleStateCount(dbClient,table,channelNames);

        tableDataInfo.setTotal(count);

        List<RoleInfoBean> dataList =  statRoleDao.getRoleStateList(dbClient,table,channelNames,sortType,pageIndex,pageSize);

        tableDataInfo.setRows(dataList);
        return dataList;
    }

}
