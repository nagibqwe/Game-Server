package com.gm.project.stat.stat_role.service;


import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_role.domain.RoleInfoBean;

import java.util.List;

public interface IStatRoleService {
    public List<RoleInfoBean> stat_role(TableDataInfo tableDataInfo,String groupName, Integer selectServerId, String channelNames, String sortType, int pageIndex, int pageSize);
}
