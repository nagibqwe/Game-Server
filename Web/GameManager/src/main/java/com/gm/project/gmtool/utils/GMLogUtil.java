package com.gm.project.gmtool.utils;

import com.gm.common.utils.StringUtils;
import com.gm.common.utils.security.ShiroUtils;
import com.gm.framework.manager.AsyncManager;
import com.gm.framework.manager.factory.AsyncFactory;
import com.gm.project.gmtool.gmlog.domain.GMLog;
import com.gm.project.system.user.domain.User;

import java.util.Date;

/**
 * GM后台自定义日志
 */
public class GMLogUtil {

    public static void log(String content) {
        GMLog gmLog = new GMLog();
        gmLog.setContent(content);
        gmLog.setIp(ShiroUtils.getIp());
        User currentUser = ShiroUtils.getSysUser();
        if (currentUser != null)
        {
            gmLog.setName(currentUser.getLoginName());
            if (StringUtils.isNotNull(currentUser.getDept())
                    && StringUtils.isNotEmpty(currentUser.getDept().getDeptName()))
            {
                gmLog.setDeptName(currentUser.getDept().getDeptName());
            }
        }
        gmLog.setTime(new Date(System.currentTimeMillis()));

        // 保存数据库
        AsyncManager.me().execute(AsyncFactory.recordGMLog(gmLog));
    }

}
