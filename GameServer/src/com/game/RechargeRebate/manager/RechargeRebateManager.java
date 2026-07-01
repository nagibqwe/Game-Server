package com.game.RechargeRebate.manager;

import com.game.RechargeRebate.script.IRechargeRebate;
import com.game.db.bean.RechargeBean;
import com.game.db.bean.RechargeReturnBean;
import com.game.db.bean.RechargeTotalMoneyBean;
import com.game.db.dao.RechargeReturnDao;
import com.game.db.dao.RechargeTotalMoneyDao;
import com.game.manager.Manager;
import com.game.recharge.manager.RechargeManager;
import com.game.recharge.script.IRechargeScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

import java.util.HashMap;
import java.util.List;

public class RechargeRebateManager {


    private RechargeTotalMoneyDao rechargeTotalMoneyDao = new RechargeTotalMoneyDao();

    private HashMap<Long, RechargeTotalMoneyBean> rechargeTotalMoneyBeans = new HashMap<>();

    //充值返利
    private final RechargeReturnDao rechargeReturnDao  = new RechargeReturnDao();

    private  HashMap<String, RechargeReturnBean> returnBeanHashMap = new HashMap<>();


    public RechargeTotalMoneyDao getRechargeTotalMoneyDao() {
        return rechargeTotalMoneyDao;
    }

    public void setRechargeTotalMoneyDao(RechargeTotalMoneyDao rechargeTotalMoneyDao) {
        this.rechargeTotalMoneyDao = rechargeTotalMoneyDao;
    }

    public HashMap<Long, RechargeTotalMoneyBean> getRechargeTotalMoneyBeans() {
        return rechargeTotalMoneyBeans;
    }

    public void setRechargeTotalMoneyBeans(HashMap<Long, RechargeTotalMoneyBean> rechargeTotalMoneyBeans) {
        this.rechargeTotalMoneyBeans = rechargeTotalMoneyBeans;
    }

    public RechargeReturnDao getRechargeReturnDao() {
        return rechargeReturnDao;
    }


    public HashMap<String, RechargeReturnBean> getReturnBeanHashMap() {
        return returnBeanHashMap;
    }

    public void setReturnBeanHashMap(HashMap<String, RechargeReturnBean> returnBeanHashMap) {
        this.returnBeanHashMap = returnBeanHashMap;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;
        RechargeRebateManager manager;

        Singleton() {
            this.manager = new RechargeRebateManager();
        }

        RechargeRebateManager getProcess() {
            return manager;
        }
    }


    public static RechargeRebateManager getInstance() {
        return RechargeRebateManager.Singleton.INSTANCE.getProcess();
    }


    /**
     * 脚本接口
     *
     * @return Script
     */
    public IRechargeRebate deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.RechargeRebateScript);
        if (is  == null) {
            return  null;
        }
        return (IRechargeRebate)is;
    }

    public void load() {
        List<RechargeTotalMoneyBean> beans = rechargeTotalMoneyDao.selectAll();
        if (beans != null){
            for (RechargeTotalMoneyBean bean : beans){
                rechargeTotalMoneyBeans.put(bean.getUserId(),bean);
            }
        }
        List<RechargeReturnBean> beans1 =  rechargeReturnDao.selectAll();
        if (beans1 != null){
            for (RechargeReturnBean bean :beans1){
                returnBeanHashMap.put(bean.getUserName(),bean);
            }
        }
    }
}
