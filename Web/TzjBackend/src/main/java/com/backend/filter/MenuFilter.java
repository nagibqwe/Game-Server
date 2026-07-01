package com.backend.filter;

import java.util.List;

import javax.servlet.http.HttpSession;


import com.backend.bean.User;
import com.backend.module.backend.UserModule;
import com.backend.utils.AESUtil;
import com.backend.utils.ServerKeyUtil;
import org.nutz.dao.Cnd;
import org.nutz.lang.Strings;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.HttpStatusView;
import org.nutz.mvc.view.ServerRedirectView;

public class MenuFilter implements ActionFilter {

    private String name;
    private String path;

    public MenuFilter(String name, String path) {
        this.name = name;
        this.path = path;
    }

    @SuppressWarnings("unchecked")
	public View match(ActionContext context) {

        HttpSession session = context.getRequest().getSession();
//        String env = ServerKeyUtil.getKey("env");
//        if ("pro".equals(env)) {
//            String param = context.getRequest().getQueryString();
//            if (Strings.isBlank(param)) {
//                return new ServerRedirectView(path);
//            }
//            try {
//                String data = param.split("=")[param.split("=").length - 1];
//                String gmpPrivacyKey = ServerKeyUtil.getKey("GmpPrivacyKey");
//                String accessInfo = AESUtil.decrypt(AESUtil.decodeURIComponent(data), gmpPrivacyKey);
//                JSONObject object = JSON.parseObject(accessInfo);
//                String reqUri = object.get("url").toString();
//                if (reqUri.contains("?")) {
//                    reqUri = reqUri.split("\\?")[0];
//                }
//                boolean checkUrl = context.getRequest().getRequestURL().toString().endsWith(reqUri);
//                if (checkUrl) {
//                    if (session == null || null == session.getAttribute("USER")) {
//                        User user = new User();
//                        user.setId(Integer.parseInt(object.get("userID").toString()));
//                        user.setName(object.get("userName").toString());
//                        user.setLanguage(object.get("lang").toString());
//                        session.setAttribute("USER", user);
//                    }
//                    return null;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return new ServerRedirectView(path);
//        }
    	if(session == null|| null == session.getAttribute("USER")){
    		return new ServerRedirectView("/user/tologin");
        }
       	String str = context.getRequest().getParameter("menuId");
    	if (Strings.isBlank(str)) {
			return new HttpStatusView(403); 
		}
        
        if(session.getAttribute(name) == null) {
            return new ServerRedirectView(path);
        }

        int menuId=Integer.parseInt(str);
        List<Integer> userMenus= (List<Integer>) session.getAttribute(name);
        if(!userMenus.contains(menuId)){
            return new ServerRedirectView(path);
        }
        return null;
    }
}