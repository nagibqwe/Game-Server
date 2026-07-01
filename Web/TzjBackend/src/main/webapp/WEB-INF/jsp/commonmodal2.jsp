<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/6/2
  Time: 10:37
  To change this template use File | Settings | File Templates.
--%>
<%--这是配套使用bootstrap3版本对应的 --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="modal fade" id="loadingmodal" tabindex="-1" role="dialog" aria-labelledby="_ModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-body">
                <div style="margin-left: 20px; margin-top:18px;" id="spintarget"></div>
                <div style="padding-left: 50px;"><p style="font-size: 14px;">${msg['jsp.loading']}</p></div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>

<div class="modal fade" id="progressmodal" tabindex="-1" role="dialog" aria-labelledby="_ModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h3 id="progressdetailhead">${msg['jsp.dealing']}</h3>
            </div>
            <div class="modal-body">
                <div class="progress">
                    <div class="bar" id="bar" style="width: 1%;"></div>
                </div>
                <div id="progressdetail"></div>
            </div>
        </div><!-- /.modal-content -->
    </div><!-- /.modal-dialog -->
</div>
