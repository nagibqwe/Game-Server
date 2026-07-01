package com.backend.module.admin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.backend.utils.Toolkit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.pager.Pager;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.backend.bean.Menu;
import com.backend.filter.MenuFilter;
import com.backend.utils.BackendLogUtil;

@IocBean
@Ok("json")
@At("/menu")
@Fail("http:500")
public class MenuModule {

    @Inject
    protected Dao dao;

    @At("/")
    @Ok("jsp:jsp.admin.menu.menulist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index() {}

    @At
    public Object singleMenu(int menuId) {
        Cnd cnd = Cnd.where("menuId", "=", menuId);
        Menu menu = dao.fetch(Menu.class, cnd);
        menu.setMenuName(Mvcs.getMessage(Mvcs.getReq(), menu.getMenuName()));
        return menu;
    }

    @At
    public Object list() {
        return dao.query(Menu.class, null);
    }

    @At
    public Object add(@Param("..") Menu menu, HttpServletRequest request) {
        if (Strings.isBlank(menu.getMenuName())) {
            return Toolkit.outResult(false, "菜单名不能为空");
        }
        menu = dao.insert(menu);
        BackendLogUtil.getInstance().log(request, "添加菜单" + menu.getMenuId());
        return Toolkit.outResult(true, menu);
    }

    @At
    public Object update(@Param("..") Menu menu, HttpServletRequest request) {
        dao.updateIgnoreNull(menu);
        BackendLogUtil.getInstance().log(request, "修改菜单" + menu.getMenuId());
        return Toolkit.outResult(true);
    }

    @At
    public Object delete(int menuId, HttpServletRequest request) {
        dao.delete(Menu.class, menuId);
        BackendLogUtil.getInstance().log(request, "删除菜单" + menuId);
        return Toolkit.outResult(true);

    }

    @At
    public Object query(String menuName,@Param("..") Pager pager) {
        Map<String, String> langMap = Mvcs.getMessages(Mvcs.getReq());
        Cnd cnd = Cnd.NEW();
//        if (!Strings.isBlank(menuName)) {
//            StringBuilder idStr = new StringBuilder();
//            for (Entry<String, String> entry : langMap.entrySet()) {
//                if (entry.getKey().contains("menu_") && entry.getValue().contains(menuName)) {
//                    idStr.append(entry.getKey().split("_")[1]).append(",");
//                }
//            }
//            idStr.setLength(idStr.length() - 1);
//            if (!Strings.isBlank(idStr)) {
//                cnd = Cnd.where("menuId", "in", idStr.toString());
//            }
//        }
        cnd = cnd.or("menuName", "like", "%" + menuName + "%");
        List<Menu> menuList = dao.query(Menu.class, cnd, pager);
        for (Menu menu : menuList) {
            menu.setMenuName(langMap.get(menu.getMenuName()));
        }
        QueryResult qr = new QueryResult();
        qr.setList(menuList);
        pager.setRecordCount(dao.count(Menu.class, cnd));
        qr.setPager(pager);
        return Toolkit.outResult(true).setv("qr", qr);
    }

    @At
    public Object parentMenu(int level) {
        Cnd cnd = Cnd.where("level", "=", level - 1).and("isDeleted", "=", 0);
        List<Menu> menus = dao.query(Menu.class, cnd);
        for (Menu menu : menus) {
            menu.setMenuName(Mvcs.getMessage(Mvcs.getReq(), menu.getMenuName()));
        }
        return menus;
    }

    @At
    public Object queryPermisson(int available, HttpServletRequest request) {
        List<Integer> userMenus = (List<Integer>) request.getSession().getAttribute("USERMENUS");
        if (userMenus.size() <= 0) {
            return Toolkit.outResult(false, Mvcs.getMessage(request, "jsp.login.noPermission"));
        } else {
            StringBuilder ids = new StringBuilder();
            for (Integer integer : userMenus) {
                ids.append(integer).append(",");
            }
            ids = new StringBuilder(ids.substring(0, ids.length() - 1));
            Cnd menuCnd;
            if (available == 1) {
                menuCnd = Cnd.where("menuId", "in", ids.toString()).and("isDeleted", "=", 0);
            } else {
                menuCnd = Cnd.where("menuId", "in", ids.toString());
            }
            List<Menu> menus = dao.query(Menu.class, menuCnd);
            for (Menu menu : menus) {
                menu.setMenuName(Mvcs.getMessage(request, menu.getMenuName()));
            }
            return Toolkit.outResult(true, menus);
        }
    }

}
