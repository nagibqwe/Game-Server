package com.backend.module.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.Ok;

@IocBean
@Ok("json")
@At("/home")
@Fail("http:500")
public class HomeModule {

    @At("/")
    @Ok("->:/main.jsp")
    public void index() {}

    @At
    @Ok("forward:${obj}")
    public String page(int index, HttpServletRequest request) {
        switch (index) {
            case 1:
                Map<String, String> langMap = new HashMap<>();
                for (String lang : Mvcs.getLocalizationKeySet()) {
                    langMap.put(lang, Mvcs.getMessage(request, "jsp.language." + lang));
                }
                langMap.remove("$default");
                Mvcs.getReq().setAttribute("langMap", Json.toJson(langMap));
                return "/WEB-INF/jsp/home/frametop.jsp";
            case 2:
                return "/WEB-INF/jsp/home/framemenu.jsp";
            case 3:
                return "/WEB-INF/jsp/home/mainframe.jsp";
            default:
                return "/WEB-INF/jsp/404.jsp";
        }
    }

    @At
    @Ok("forward:${obj}")
    public String top() {
        return "/WEB-INF/jsp/home/frametop.jsp";
    }

    @At
    @Ok("forward:${obj}")
    public String menu() {
        return "/WEB-INF/jsp/home/framemenu.jsp";
    }

    @At
    @Ok("forward:${obj}")
    public String main() {
        return "/WEB-INF/jsp/home/mainframe.jsp";
    }
}
