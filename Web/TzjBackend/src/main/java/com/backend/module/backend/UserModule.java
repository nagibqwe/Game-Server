package com.backend.module.backend;

import com.backend.bean.BlackIP;
import com.backend.bean.User;
import com.backend.filter.MenuFilter;
import com.backend.struct.StaticField;
import com.backend.utils.BackendLogUtil;
import com.backend.utils.PasswordUtil;
import com.backend.utils.ServerKeyUtil;
import com.backend.utils.Toolkit;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.random.R;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.*;

@IocBean
@At("/user")
@Ok("json:{locked:'password|salt',ignoreNull:true}")
@Fail("http:500")
public class UserModule {

    private static final Logger log = Logger.getLogger(UserModule.class);

    @Inject
    protected Dao dao;

    @At
    public int count() {
        return dao.count(User.class);
    }

    @At
    @Filters
    public Object login(String username, String password) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        HttpServletRequest request = Mvcs.getReq();
        HttpSession session = request.getSession();
        int loginCounts = 0;
        if (session.getAttribute("loginCounts") != null) {
            loginCounts = Integer.parseInt(session.getAttribute("loginCounts").toString());
        }
        String loginIp = Toolkit.getIp(request);
        String whileIpStr = ServerKeyUtil.GetIP();
        List<String> loginIpList = Arrays.asList(whileIpStr.split(";"));
        if (!Strings.isBlank(whileIpStr) && !loginIpList.contains(loginIp)) {
            BackendLogUtil.getInstance().log(request, "用户" + username + "登录失败,ip不允许访问");
            return Toolkit.outResult(false, msg.get("user.useripillegal"));
        }
        User user = dao.fetch(User.class, Cnd.where("name", "=", username));
//        List<BlackIP> blackIps = dao.query(BlackIP.class, null);
//        List<String> blackIpStr = new ArrayList<>();
//        for (BlackIP ip : blackIps) {
//            blackIpStr.add(ip.getIp());
//        }
        if (loginCounts >= 10 && !loginIpList.contains(loginIp) && user.getIsDeleted() != 1) {
            user.setUpdateTime(new Date());
            user.setIp(loginIp);
            user.setIsDeleted(1);
            session.setAttribute("USER", user);
            int result = dao.update(user);
            if (result < 1) {
                log.error("用户" + user.getName() + "更新登录信息失败");
            }
//            BlackIP blackIp = new BlackIP();
//            blackIp.setIp(loginIp);
//            dao.insert(blackIp);
        }
        if (Strings.isBlank(username) || Strings.isBlank(password)) {
            loginCounts = loginCounts + 1;
            session.setAttribute("loginCounts", loginCounts);
            return Toolkit.outResult(false, msg.get("user.userorpwdnull"));
        }
        if (user == null) {
            BackendLogUtil.getInstance().log(request, "用户" + username + "登录失败,没有找到该用户");
            loginCounts = loginCounts + 1;
            session.setAttribute("loginCounts", loginCounts);
            return Toolkit.outResult(false, msg.get("user.usernoexist"));
        }
        if (user.getIsDeleted() == 1) {
            BackendLogUtil.getInstance().log(request, "用户" + username + "登录失败,用户被禁用");
            return Toolkit.outResult(false, msg.get("user.userisdeleted"));
        }
//        if (!Strings.isBlank(loginIp) && blackIpStr.contains(loginIp)) {
//            BackendLogUtil.getInstance().log(request, "用户" + username + "登录失败,IP为" + loginIp + "被禁用");
//            return Toolkit.outResult(false, msg.get("user.ipforbid"));
//        }
        if (!PasswordUtil.toHex(password, user.getSalt()).equals(user.getPassword())) {
            BackendLogUtil.getInstance().log(request, "用户" + username + "登录失败,密码错误");
            loginCounts = loginCounts + 1;
            session.setAttribute("loginCounts", loginCounts);
            return Toolkit.outResult(false, msg.get("user.pwderror"));
        }

        user.setIp(loginIp);
        user.setUpdateTime(new Date());
        if (Strings.isBlank(user.getLanguage())) {
            user.setLanguage(Mvcs.getDefaultLocalizationKey());
        }
        session.setAttribute("USER", user);
        int result = dao.update(user);
        if (result < 1) {
            log.error("用户" + user.getName() + "更新登录信息失败");
        }
        Mvcs.setLocalizationKey(user.getLanguage());
        List<Integer> userMenus = getUserMenu(user.getId());
        session.setMaxInactiveInterval(24 * 60 * 60);
        session.setAttribute("USERMENUS", userMenus);
        BackendLogUtil.getInstance().log(request, "用户登录成功:" + username);
        return Toolkit.outResult(true);
    }

    private List<Integer> getUserMenu(int userId) {
        String sqlMenuStr = "SELECT DISTINCT TRM.MENUID FROM t_user_role TUR,T_ROLE_MENU TRM WHERE TUR.ROLEID = TRM.ROLEID AND TUR.USERID =" + userId;
        Sql sql = Sqls.create(sqlMenuStr);
        sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
            List<Integer> dataList = new ArrayList<>();
            while (rs.next()) {
                dataList.add(rs.getInt(1));
            }
            return dataList;
        });
        dao.execute(sql);
        return (List<Integer>) sql.getResult();
    }

    @At
    public Object add(@Param("..") User addUserInfo, HttpServletRequest request) {
        String msg = checkUser(addUserInfo, true);
        if (msg != null) {
            return Toolkit.outResult(false, msg);
        }
        String salt = R.sg(5).next();
        addUserInfo.setSalt(salt);
        addUserInfo.setPassword(PasswordUtil.toHex(addUserInfo.getPassword(), salt));
        addUserInfo.setCreateTime(new Date());
        addUserInfo.setUpdateTime(new Date());
        addUserInfo = dao.insert(addUserInfo);
        BackendLogUtil.getInstance().log(request, "添加用户");
        return Toolkit.outResult(true, addUserInfo);
    }

    @At
    public Object update(@Param("..") User updateUserInfo, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        String check = checkUser(updateUserInfo, false);
        if (check != null) {
            return Toolkit.outResult(false, check);
        }
        String salt = R.sg(5).next();
        updateUserInfo.setSalt(salt);
        updateUserInfo.setPassword(PasswordUtil.toHex(updateUserInfo.getPassword(), salt));
        updateUserInfo.setName(null);// 不允许更新用户名
        updateUserInfo.setCreateTime(null);//也不允许更新创建时间
        updateUserInfo.setUpdateTime(new Date());// 设置正确的更新时间
        updateUserInfo.setIp(null);
        updateUserInfo.setLanguage(null);
        dao.updateIgnoreNull(updateUserInfo);// 真正更新的其实只有password和salt
        BackendLogUtil.getInstance().log(request, "修改用户密码");
        return Toolkit.outResult(true, msg.get("user.pwdupdatesuccess"));
    }

    @At
    public Object delete(@Param("id") int id, int isDeleted, HttpServletRequest request) {
        User user = new User();
        user.setId(id);
        user.setIsDeleted(isDeleted);
        dao.update(user, "isDeleted");
        BackendLogUtil.getInstance().log(request, "删除用户");
        return Toolkit.outResult(true);
    }

    @At
    public Object query(@Param("name") String name, @Param("..") Pager pager) {
        Cnd cnd = Strings.isBlank(name) ? null : Cnd.where("name", "like", "%" + name + "%");
        QueryResult qr = new QueryResult();
        List<User> userList = dao.query(User.class, cnd, pager);
        List<String> roleNameList = new ArrayList<>();
        //得到每个用户拥有的角色
        for (User user : userList) {
            String getRoleSql = "";
            getRoleSql += "SELECT TR.ROLENAME FROM T_ROLE TR,T_USER_ROLE TUR ";
            getRoleSql += "WHERE TR.ROLEID = TUR.ROLEID AND TUR.USERID = " + user.getId();
            Sql sql = Sqls.create(getRoleSql);
            sql.setCallback((Connection conn, ResultSet rs, Sql sql1) -> {
                List<String> dataList = new ArrayList<>();
                while (rs.next()) {
                    dataList.add(rs.getString(1));
                }
                return dataList;
            });
            dao.execute(sql);
            StringBuilder roleNames = new StringBuilder();
            List<String> roles = (List<String>) sql.getResult();
            for (String role : roles) {
                roleNames.append(role).append(";");
            }
            roleNameList.add(roleNames.toString());
        }
        qr.setList(userList);
        pager.setRecordCount(dao.count(User.class, cnd));
        qr.setPager(pager);
        return Toolkit.outResult(true).setv("qr", qr).setv("roles", roleNameList);
    }

    @At
    public Object changepwd(@Param("password") String password, String cpassword, HttpServletRequest request) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (Strings.isBlank(password) || Strings.isBlank(cpassword)) {
            return Toolkit.outResult(false, msg.get("user.pwdcannotempty"));
        }
        password = password.trim().intern();
        cpassword = cpassword.trim().intern();
        if (!password.equals(cpassword)) {
            return Toolkit.outResult(false, msg.get("user.pwdisnotsame"));
        }
        User user = (User) request.getSession().getAttribute("USER");

        String salt = R.sg(5).next();
        user.setSalt(salt);
        user.setPassword(PasswordUtil.toHex(password, salt));
        dao.update(user);
        BackendLogUtil.getInstance().log(request, "用户修改密码");
        return Toolkit.outResult(true, msg.get("user.pwdupdatesuccess"));
    }

    private String checkUser(User user, boolean create) {
        Map<String, String> msg = Mvcs.getMessages(Mvcs.getReq());
        if (user == null) {
            return msg.get("user.emptyobj");
        }
        if (create) {
            if (Strings.isBlank(user.getName()) || Strings.isBlank(user.getPassword()))
                return msg.get("user.userorpwdnull");
        } else {
            if (Strings.isBlank(user.getPassword()))
                return msg.get("user.pwdcannotempty");
        }
        if (create) {
            if (Strings.isBlank(user.getLanguage())) {
                return "请选择语言";
            }
        }
        String password = user.getPassword().trim();
        if (2 > password.length()) {
            return msg.get("user.pwdlengtherror");
        }
        user.setPassword(password);
        if (create) {
            int count = dao.count(User.class, Cnd.where("name", "=", user.getName()));
            if (count != 0) {
                return msg.get("user.userisexist");
            }
        } else {
            if (user.getId() < 1) {
                return msg.get("user.useridillegal");
            }
        }
        if (user.getName() != null) {
            user.setName(user.getName().trim());
        }
        return null;
    }

    @At("/")
    @Ok("jsp:jsp.admin.user.userlist")
    @Filters(@By(type = MenuFilter.class, args = {"USERMENUS", "/noauthority.jsp"}))
    public void index(HttpServletRequest request) {
        Map<String, String> langMap = new HashMap<>();
        for (String lang : Mvcs.getLocalizationKeySet()) {
            langMap.put(lang, Mvcs.getMessage(request, "jsp.language." + lang));
        }
        langMap.remove("$default");
        Mvcs.getReq().setAttribute("langMap", Json.toJson(langMap));
    }

    @Filters
    @At
    @Ok(">>:/logout.jsp")
    public void tologin() {
    }

    @At
    @Ok(">>:/")
    public void logout(HttpServletRequest request) {
        BackendLogUtil.getInstance().log(request, "用户注销");
        request.getSession().invalidate();
    }

    @At
    @Ok(">>:/home")
    public void langSwitch(String lang, HttpServletRequest request) {
        Mvcs.setLocalizationKey(lang);
        User user = (User) request.getSession().getAttribute("USER");
        if (!Strings.isBlank(lang)) {
            user.setLanguage(lang);
            user.setUpdateTime(new Date());
            user.setIp(request.getHeader(StaticField.USER_REAL_IP));
            request.getSession().setAttribute("USER", user);
            int result = dao.update(user);
            if (result < 1) {
                log.error("用户" + user.getName() + "更新登录信息失败");
            }
            request.getSession().setAttribute("USER", user);
            BackendLogUtil.getInstance().log(request, "用户切换语言：" + user.getLanguage() + ">>>" + lang);
        }
    }

    @At
    @Filters
    @Ok(">>:/noauthority.jsp")
    public void noauthority() {
    }

}
