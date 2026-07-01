package com.backend.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.backend.bean.CyAnnounce;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;

public class CyAnnounceManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        CyAnnounceManager manager;

        Singleton() {
            this.manager = new CyAnnounceManager();
        }

        CyAnnounceManager getProcessor() {
            return manager;
        }
    }

    public static CyAnnounceManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private Dao dao;

    private ConcurrentHashMap<Integer, List<CyAnnounce>> announceList = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, List<CyAnnounce>> getAnnounceList() {
        return announceList;
    }

    public List<CyAnnounce> getList(Integer key) {
        return announceList.getOrDefault(key, null);
    }

    public void initData(Dao dao) {
        this.dao = dao;
        load();
    }

    public void load() {
        announceList.clear();
        Cnd cnd = Cnd.where("state", "=", 0);
        cnd.orderBy("createTime", "desc");
        List<CyAnnounce> list = dao.query(CyAnnounce.class, cnd);
        for (CyAnnounce cy : list) {

            if (cy.getTotalTimes() > 0 && cy.getTotalTimes() <= cy.getNowTimes()) {
                continue;
            }

            int cl = cy.getCycleInterval();
            if (!announceList.containsKey(cl)) {
                announceList.put(cl, new ArrayList<>());
            }
            List<CyAnnounce> ll = announceList.get(cl);
            ll.add(cy);
        }
    }

    public boolean addCyAnnounce(CyAnnounce announce) {
        if (announce == null || announce.getState() > 0) {
            return false;
        }

        int cl = announce.getCycleInterval();
        List<CyAnnounce> list;
        if (announceList.containsKey(cl)) {
            list = announceList.get(cl);
            for (CyAnnounce ca : list) {
                if (ca.getId() == announce.getId()) {
                    list.remove(ca);
                    break;
                }
            }
            list.add(announce);
        } else {
            list = new ArrayList<>();
            list.add(announce);
            announceList.put(cl, list);
        }
        return true;
    }

    public void remove(CyAnnounce an) {
        an.setState(4);
        dao.update(an);
        List<CyAnnounce> list = announceList.get(an.getCycleInterval());
        if (list == null) {
            return;
        }
        for (CyAnnounce al : list) {
            if (al.getId() == an.getId()) {
                list.remove(al);
                break;
            }
        }
    }

    public int updateSave(CyAnnounce an) {
        List<CyAnnounce> list = announceList.get(an.getCycleInterval());
        if (list != null) {
            for (CyAnnounce al : list) {
                if (al.getId() == an.getId()) {
                    al.setState(an.getState());
                    break;
                }
            }
        }
        return dao.update(an);
    }
}
