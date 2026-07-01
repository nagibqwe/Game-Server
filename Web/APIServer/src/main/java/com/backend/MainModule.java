package com.backend;

import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.filter.CheckSession;

@SetupBy(MainSetup.class)
@Modules
@IocBy(args={"*js", "ioc/",
    "*anno", "com.backend",
    "*tx"})
@Fail("jsp:500")
@Filters(@By(type = CheckSession.class,args={"USER","/user/tologin"}))
@Localization(value="language/", defaultLocalizationKey="zh-CN")
@ChainBy(args={"${app.root}/WEB-INF/classes/ioc/mvc-chain.js"})
public class MainModule {

}
