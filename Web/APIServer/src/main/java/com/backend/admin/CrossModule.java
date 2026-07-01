package com.backend.admin;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Ok;

import com.backend.manager.CrossManager;
import org.nutz.mvc.annotation.Param;

@IocBean
@At("/cross")
@Ok("json")
public class CrossModule {

    @Inject
    protected Dao dao;

    @At
    public Object getServerGroupNames(int serverType) {
        return CrossManager.getInstance().getServerGroupNames(serverType);
    }

    @At
    public Object getDbGroupNames(int serverType) {
        return CrossManager.getInstance().getDbGroupNames(serverType);
    }

    @At
    public Object getServers(String groupName, int serverType) {
        return CrossManager.getInstance().getServers(groupName, serverType);
    }

    @At
    public Object getDBs(String groupName, int serverType) {
        return CrossManager.getInstance().getDBs(groupName, serverType);
    }
}
