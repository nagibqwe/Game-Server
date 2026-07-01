package com.gm.project.stat.common.service;

import com.game.util.TwoTuple;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.stat.common.dao.IStatLoginDao;
import com.gm.project.stat.common.dao.IStatRechargeDao;
import com.gm.project.stat.common.dao.IStatRoleStateDao;
import com.gm.project.stat.stat_ltv.service.impl.StatLtvServiceImpl;
import com.gm.project.stat.stat_remain.service.StatRemainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * @author ruoyi
 */
@Service
public class StatCommonServiceImpl
{

    @Autowired
    public IStatLoginDao statLoginDao;


    @Autowired
    public IStatRechargeDao statRechargeDao;

    @Autowired
    public IStatRoleStateDao statRoleStateDao;




}
