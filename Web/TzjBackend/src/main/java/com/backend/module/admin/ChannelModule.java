package com.backend.module.admin;

import com.backend.manager.LoginServerManager;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

import java.util.List;

@IocBean
@Ok("json")
@At("/channel")
@Fail("http:500")
public class ChannelModule {

    @Inject
    private Dao loginDao;

    @At
    public List<String> getChannelName() {

        return LoginServerManager.getInstance().getChannel();
    }

}
