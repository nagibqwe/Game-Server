package com.gm.project.gmtool.selectgroup.service.impl;

import com.gm.project.gmtool.selectgroup.service.ISelectGroupService;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.mapper.TServerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 服务器列Service业务层处理
 */
@Service
public class SelectGroupServiceImpl implements ISelectGroupService {

    @Autowired
    private TServerMapper tServerMapper;

    @Override
    public List<String> selectServerGroup() {
        return tServerMapper.selectServerGroup();
    }

    private List<TServer> getIgnoreMergeServer(List<TServer> serverList) {
        List<TServer> mergeServers = new ArrayList<>();
        for (TServer server : serverList) {
            if (server.getIsHeFu() == 1) {
                continue;
            }
            mergeServers.add(server);
        }
        return mergeServers;
    }

    /**
     * 通用类型筛选
     * @param groupName
     * @param ignoreMerge
     * @param serverTypeList
     * @return
     */
    public List<TServer> selectServerList(String groupName, int ignoreMerge,String serverTypeList){
        List<TServer> serverList = tServerMapper.selectServerList(groupName,serverTypeList);
        return ignoreMerge == 1 ? getIgnoreMergeServer(serverList) : serverList;
    }
}
