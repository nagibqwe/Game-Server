package com.backend.module.admin;

import com.backend.bean.Role;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

@IocBean
@At("/backrole")
@Ok("json")
@Fail("http:500")
public class BackRoleModule {

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.admin.role.backrolelist")
    public void index() {}

    @At
    public Object roleList(@Param("..") Pager pager) {
        QueryResult qr = new QueryResult();
        List<Role> roleList = dao.query(Role.class, null,pager);
        qr.setList(roleList);
        pager.setRecordCount(dao.count(Role.class, null));
        qr.setPager(pager);
        return Toolkit.outResult(true).setv("qr", qr);
    }

    @At
    public Object addRole(String description, String roleName, HttpServletRequest request) {
        Role role = dao.fetch(Role.class, Cnd.where("roleName", "=", roleName));
        int flag = 0;
        if (role != null) {
            flag = 1;
        } else {
            String msg;
            role = new Role();
            role.setRoleName(roleName);
            role.setCreateTime(new Date());
            role.setDescription(description);
            role.setIsDeleted(0);
            role = dao.insert(role);
            if (role != null) {
                flag = 2;
                msg = "添加角色:" + roleName;
            } else {
                msg = "角色添加失败";
            }
            BackendLogUtil.getInstance().log(request, msg);
        }
        return Toolkit.outResult(true).setv("flag", flag);
    }

    @At
    public Object getRoleInfoById(String roleId) {
        Role role = dao.fetch(Role.class, Cnd.where("roleId", "=", roleId));
        return Toolkit.outResult(true, role);
    }

    @At
    public Object updateRoleInfoById(@Param("..") Role role, HttpServletRequest request) {
        role.setDescription(role.getDescription());
        role.setRoleName(role.getRoleName());
        int flag = dao.updateIgnoreNull(role);
        BackendLogUtil.getInstance().log(request, "修改角色基本信息");
        return Toolkit.outResult(flag > 0);
    }

    @At
    public Object getRoleListData(String userId) {
        String sqlAllStr = "SELECT roleId as id,roleName as name FROM t_role";
        List<Map<String, Object>> roleAllData = selectRoleList(sqlAllStr);
        String sqlPersonStr = "SELECT TR.roleId as id,TR.roleName as name FROM t_role TR,t_user_role TUR "
                + "WHERE TR.roleId = TUR.roleId AND TUR.userId = " + userId;
        List<Map<String, Object>> userRoleData = selectRoleList(sqlPersonStr);
        for (Map<String, Object> roleData : roleAllData) {
            for (Map<String, Object> userRoleDatum : userRoleData) {
                if (userRoleDatum.get("id").toString().equals(roleData.get("id").toString())) {
                    roleData.put("checked", true);
                }
            }
        }
        return Toolkit.outResult(true, roleAllData);
    }

    /**
     * 从数据库中查找角色的列表
     */
    private List<Map<String, Object>> selectRoleList(String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List<Map<String, Object>> dataList = new ArrayList<>();
            int count = rs.getMetaData().getColumnCount();
            int num = 0;
            Map<String, Object> map;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 1; i <= count; i++) {
                    map.put(rs.getMetaData().getColumnLabel(i), rs.getObject(i));
                }
                map.put("pId", 0);
                map.put("open", true);
                dataList.add(map);
                num++;
            }
            if (num > 0) {
                map = new HashMap<>();
                map.put("id", "0");
                map.put("name", "用户角色");
                map.put("pId", -1);
                map.put("open", true);
                dataList.add(map);
            }
            return dataList;
        });
        dao.execute(sql);
        return (List<Map<String, Object>>) sql.getResult();
    }

    /**
     * 重新分配角色
     */
    @At
    public Object reAssignRole(String userId, String roleIds, HttpServletRequest request) {
        Sql sql = Sqls.create("DELETE FROM t_user_role WHERE userId = " + userId);
        dao.execute(sql);

        String[] roleIdArr = roleIds.split(",");
        for (String s : roleIdArr) {
            sql = Sqls.create("INSERT INTO t_user_role values(@userId,@roleId)");
            sql.params().set("userId", userId);
            sql.params().set("roleId", s);
            dao.execute(sql);
        }
        BackendLogUtil.getInstance().log(request, "为用户重新分配角色：" + roleIds.substring(0, roleIds.length() - 1));
        return Toolkit.outResult(true);
    }

    @At
    public Object getMenuListData(String roleId, HttpServletRequest request) {
        String sqlAllStr = "SELECT tm.menuId,tm.parentId,tm.menuName FROM t_MENU TM ";
        List<Map<String, Object>> menuAllData = selectMenuList(sqlAllStr);
        String sqlPersonStr = "SELECT TM.menuId,TM.parentId,TM.menuName FROM t_menu TM,t_role TR,t_role_menu TRM "
                + "WHERE TM.menuId = TRM.menuId AND TR.roleId= TRM.roleId AND TRM.roleId = " + roleId + " "
                + "AND TM.isDeleted = 0 ";
        List<Map<String, Object>> menuRoleData = selectMenuList(sqlPersonStr);
        for (Map<String, Object> menuAllDatum : menuAllData) {
            menuAllDatum.put("name", Mvcs.getMessage(request, menuAllDatum.get("name").toString()));
            for (Map<String, Object> menuRoleDatum : menuRoleData) {
                if (menuRoleDatum.get("id").toString().equals(menuAllDatum.get("id").toString())) {
                    menuAllDatum.put("checked", true);
                }
            }
        }
        return Toolkit.outResult(true, menuAllData);
    }

    /**
     * 从数据库中查找菜单的列表
     */
    private List<Map<String, Object>> selectMenuList(String sqlStr) {
        Sql sql = Sqls.create(sqlStr);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List<Map<String, Object>> dataList = new ArrayList<>();
            String[] columnName = {"id", "pId", "name"};
            Map<String, Object> map;
            while (rs.next()) {
                map = new HashMap<>();
                for (int i = 0; i < columnName.length; i++) {
                    map.put(columnName[i], rs.getObject(i + 1));
                }
                map.put("open", true);
                dataList.add(map);
            }
            return dataList;
        });
        dao.execute(sql);
        return (List<Map<String, Object>>) sql.getResult();
    }

    /**
     * 重新分配菜单
     */
    @At
    public Object reAssignMenu(String roleId, String menuIds, HttpServletRequest request) {
        Sql sql = Sqls.create("DELETE FROM t_role_menu WHERE roleId = " + roleId);
        dao.execute(sql);
        String[] menuIdArr = menuIds.split(",");
        for (String s : menuIdArr) {
            sql = Sqls.create("INSERT INTO T_ROLE_MENU values(@roleId,@menuId)");
            sql.params().set("roleId", roleId);
            sql.params().set("menuId", s);
            dao.execute(sql);
        }
        BackendLogUtil.getInstance().log(request, "为角色重新分配权限：" + menuIds.substring(0, menuIds.length() - 1));
        return Toolkit.outResult(true);
    }
}







