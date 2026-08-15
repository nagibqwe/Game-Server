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
            return Toolkit.outResult(false, "Не указан получатель письма");
        }

        if (!to.contains("@")) {
            return Toolkit.outResult(false, "Неверный формат email!");
        }

        if (Strings.isBlank(title) || Strings.isBlank(content)) {
            return Toolkit.outResult(false, "Содержимое письма пусто");
        }

        if (!Toolkit.checkSign(request)) {
            return Toolkit.outResult(false, "Ошибка отправки");
        }

        boolean ok = emailService.send(to, title, content);
        if (!ok) {
            return Toolkit.outResult(false, "Ошибка отправки");
        }
        return Toolkit.outResult(true, "Отправлено успешно");
    }
}