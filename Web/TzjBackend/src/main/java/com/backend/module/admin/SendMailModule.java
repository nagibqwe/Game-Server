package com.backend.module.admin;

import com.backend.service.EmailService;
import com.backend.utils.Toolkit;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;

@IocBean
@At("/sendmail")
@Ok("json")
@Fail("jsp:500")
public class SendMailModule {

    @Inject
    private EmailService emailService;

    @At
    @GET
    public Object send(@Param("to") String to, @Param("title") String title, @Param("content") String content, HttpServletRequest request) {

        if (Strings.isBlank(to)) {
            return Toolkit.outResult(false, "当前没有指定发送的目标邮件");
        }

        if (!to.contains("@")) {
            return Toolkit.outResult(false, "邮件格式不正确！");
        }

        if (Strings.isBlank(title) || Strings.isBlank(content)) {
            return Toolkit.outResult(false, "邮件内容为空");
        }

        if (!Toolkit.checkSign(request)) {
            return Toolkit.outResult(false, "发送失败1");
        }

        boolean ok = emailService.send(to, title, content);
        if (!ok) {
            return Toolkit.outResult(false, "发送失败");
        }
        return Toolkit.outResult(true, "发送成功");
    }

}
