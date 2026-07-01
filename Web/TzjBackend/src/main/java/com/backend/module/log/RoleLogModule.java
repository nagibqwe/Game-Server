package com.backend.module.log;

import com.backend.filter.MenuFilter;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@IocBean
@Ok("json")
@At("/rolelog")
@Fail("http:500")
public class RoleLogModule {

    private DateFormat sdfhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.role.roledetail")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        request.setAttribute("nowDate", sdfhms.format(new Date()));
    }

    @At
    @Ok("jsp:jsp.role.roledetail")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void roledetail(String groupName, int serverId, String roleId, HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("groupName", groupName);
        map.put("serverId", serverId);
        map.put("queryType", 3);
        map.put("queryString", roleId);
        String role = Json.toJson(map);
        request.setAttribute("ROLEINFO", role);
        request.setAttribute("nowDate", sdfhms.format(new Date()));
    }


}
