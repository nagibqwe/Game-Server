<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/3/11
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<div class="col-md-12 col-sm-12 col-lg-12" style="height: 30px"></div>
<div class="col-sm-5 col-sm-5"></div>
<div class="row-fluid col-sm-7 col-md-7">
    <%--<input type="button" value="自定义数据验证" onclick="validateAll()"--%>
           <%--class="btn btn-sm btn-danger"--%>
           <%--style="margin-right: 30px"/>--%>
    <input type="button" value="${msg['jsp.giftbag.button.submit']}" onclick="submitActivity()"
           class="btn btn-sm btn-danger"
           style="margin-right: 30px"/>
    <a href="javascript:;" class="file">${msg['jsp.giftbag.button.import']}
        <input type="file" name="" id="" onchange="importf(this)">
    </a>
</div>


