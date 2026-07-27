package com.gm.project.gmtool.manager;

import com.gm.common.utils.spring.SpringUtils;
import com.gm.project.gmtool.cyAnnounce.domain.CyAnnounce;
import com.gm.project.gmtool.cyAnnounce.service.ICyAnnounceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 循环公告管理
 */
@Service
public class CyAnnounceManager {

    @Autowired
    private ICyAnnounceService cyAnnounceService;

    public static CyAnnounceManager getInstance(){
        return SpringUtils.getBean("cyAnnounceManager");
    }

    private ConcurrentHashMap<Integer, List<CyAnnounce>> announceList = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, List<CyAnnounce>> getAnnounceList() {
        return announceList;
    }

    public List<CyAnnounce> getList(Integer key) {
        return announceList.getOrDefault(key, null);
    }

    @PostConstruct
    public void initData() {
        //启动时加载
        load();
    }

    public void load() {
        announceList.clear();
        CyAnnounce cyAnnounce = new CyAnnounce();
        cyAnnounce.setState(0);
        List<CyAnnounce> list = cyAnnounceService.selectCyAnnounceList(cyAnnounce);
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
        cyAnnounceService.updateCyAnnounce(an);
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
        return cyAnnounceService.updateCyAnnounce(an);
    }
}
