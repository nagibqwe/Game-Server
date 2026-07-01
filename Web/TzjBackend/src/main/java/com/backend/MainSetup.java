package com.backend;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.backend.bean.GameInfo;
import com.backend.manager.ActivityManager;
import com.backend.manager.*;
import org.apache.log4j.Logger;
import org.nutz.dao.Dao;
import org.nutz.dao.util.Daos;
import org.nutz.ioc.Ioc;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.backend.service.TaskTimerService;
import com.backend.utils.APILogUtil;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.QueryUtil;

public class MainSetup implements Setup{

    private final static Logger log = Logger.getLogger(MainSetup.class);

	@Override
	public void init(NutConfig conf) {
		Ioc ioc = conf.getIoc();
        Dao dao = ioc.get(Dao.class,"dao");
        Dao loginDao = ioc.get(Dao.class, "loginDao");
        Dao loginLogDao = ioc.get(Dao.class, "loginLogDao");

        ServletContext servletContext = Mvcs.getServletContext();
        String defaultLang = servletContext.getInitParameter("defaultLang");
        Mvcs.setDefaultLocalizationKey(defaultLang);
        Daos.createTablesInPackage(dao, "com.backend", false);

        //后台服务器相关连接信息初始化
        LoginServerManager.getInstance().init(dao, loginDao, loginLogDao);//初始化LS长连接
        DbLogListManager.getInstance().init(dao);//初始化平台服务器信息
        ServerListManager.getInstance().init(dao);//游戏服列表信息
        CrossManager.getInstance().init(dao);//公共服信息

        //后台相关数据初始化
        ItemManager.getInstance().init(dao);
        ItemManager.getInstance().loadServerLanguageConfig();
        BackendLogUtil.getInstance().init(dao);//初始化后台操作日志
        APILogUtil.getInstance().init(dao);//初始化API操作日志
        CyAnnounceManager.getInstance().initData(dao);
        QueryUtil.getInstance().init(dao);
        BlackListManager.getInstance().init(dao);
        ReasonManager.getInstance().init(dao);
        CurrencyRateManager.getInstance().init();
        ActivityManager.getInstance().init(dao);
        GameInfoManager.getInstance().init(dao);
        RechargeItemManager.getInstance().init(dao);

        //计时任务处理
        TaskTimerService taskTimer = ioc.get(TaskTimerService.class);
        List<Integer> list = new ArrayList<>(CyAnnounceManager.getInstance().getAnnounceList().keySet());
        for(Integer interval : list){
        	taskTimer.StartAnnounceTask(interval);
        }


        log.error("--------------------TzjBackend启动完成！--------------------");
	}

    @Override
    public void destroy(NutConfig conf) {

    }

}
