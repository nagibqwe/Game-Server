<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script>
        var base = "${base}";
        var items = new Map();
        $(function () {
            formatItems("");
            loadAllItem2();
            loadAllMailList();
            $(".datetimepicker").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                weekStart: 1,
                todayBtn: 1,
                autoclose: 1,
                minView: 3
            });
        });
        //格式化附件Items显示
        function formatItems(itemData) {
            console.log("itemData:"+itemData);
            if(itemData==""||itemData==undefined||itemData==null){
                return "";
            }
            var itemArr=itemData.split("}");
            var itemShow="";
            $.each(itemArr,function (i) {
                var itemArr_=itemArr[i].toString().split(",");
                var itemId=itemArr_[0];
                var itemName=items.get(itemId);
                var itemNumber=itemArr_[1];
                var isBind=itemArr_[2]==0?"非绑":"绑定";
                itemShow+=itemName+"*"+itemNumber+"("+isBind+")"+"}";
            });
            return itemShow;
        }
        function loadAllMailList() {
            waitDeal(1);
            mine(1);
            history(1);
        }
        function waitDeal(page) {
            $.post(base + "/mail/query",
                {
                    pageNumber: page,
                    pageSize: 100,
                    type: 1
                },
                function (data) {
                    var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                        <th>${msg['jsp.deductitem.td.number']}</th>\
                        <th>${msg['jsp.server']}</th>\
                        <th>${msg['kick.player']}</th>\
                        <th>${msg['activity.login.mailTitle']}</th>\
                        <th>${msg['activity.login.mailcontent']}</th>\
                        <th>${msg['mail.jsp.reason']}</th>\
                        <th>${msg['mail.jsp.fujian']}</th>\
                        <th>${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>\
                        <th>${msg['announce.jsp.createUser']}</th>\
                        <th>${msg['announce.jsp.createtime']}</th>\
                        <th>${msg['mail.jsp.mailState']}</th>\
                        <th>${msg['mail.jsp.operate']}</th>\
                        <th><input type=\"button\" class=\"btn btn-warning pull-right sendall\" value=\"${msg['mail.list.operate1']}\" onclick=\"oneKeySend();\"/></th>\
                    </tr>";
                    for (var i = 0; i < data.list.length; ++i) {
                        var mailData = data.list[i];
                        var tmp = "<tr><td>" +mailData.id+"</td>\
                            <td>" + mailData.serverId + "</td>\
                    <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'><input type='button' name='dataBtn' value='∨' onclick='showRoleIds(this)'><label style='display:none'>" + mailData.roleIds + "</label></td>\
                    <td>" + mailData.title + "</td>\
                    <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + mailData.content + "</td>\
                    <td>" + mailData.reason + "</td>\
                    <td style='WORD-BREAK: break-all; WORD-WRAP: break-word' >" + mailData.items + "</td>\
                    <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + formatItems(mailData.items) + "</td>\
                    <td>" + mailData.createUser + "</td>\
                    <td>" + mailData.createDate + "</td>\
                    <td>" + mailData.stateStr + "</td>\
                    <td><input type=\"button\" class=\"btn btn-primary pull-right sendall\" value=\"${msg['mail.list.operate2']}\" onclick=\"operateMail(" + mailData.id + ", 1);\"/></td>\
                    <td><input type=\"button\" class=\"btn btn-primary pull-right sendall\" value=\"${msg['mail.list.operate3']}\" onclick=\"operateMail(" + mailData.id + ", 2);\"/></td>\
                    </tr>";
                        content += tmp;
                    }
                    content += "</table>";
                    $("#waitMailList").html(content);
                });
        }

        function mine(page) {
            $.post(
                base + "/mail/query",
                {
                    pageNumber: page,
                    pageSize: 10,
                    type: 2,
                    queryDate: $("#mydate").val()
                },
                function (data) {
                    var content = "<table class=\"table table-bordered table-striped\">\
                    <tr><th>${msg['jsp.deductitem.td.number']}</th>\
                    <th>${msg['jsp.server']}</th>\
                    <th>${msg['kick.player']}</th>\
                    <th>${msg['activity.login.mailTitle']}</th>\
                    <th>${msg['activity.login.mailcontent']}</th>\
                    <th>${msg['mail.jsp.reason']}</th>\
                    <th>${msg['mail.jsp.fujian']}</th>\
                    <th>${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>\
                    <th>${msg['announce.jsp.createUser']}</th>\
                    <th>${msg['announce.jsp.createtime']}</th>\
                    <th>${msg['mail.jsp.adminUser']}</th>\
                    <th>${msg['mail.jsp.adminTime']}</th>\
                    <th>${msg['mail.jsp.demo']}</th>\
                    <th>${msg['mail.jsp.mailState']}</th>\
                    <th>${msg['mail.jsp.operate']}</th>\
                    </tr>";

                    for (var i = 0; i < data.list.length; ++i) {
                        var mailData = data.list[i];
                        var tmp = "<tr><td>"+mailData.id+"</td>\
                            <td>" + mailData.serverId + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'><input type='button' name='dataBtn' value='∨' onclick='showRoleIds(this)'><label style='display:none'>" + mailData.roleIds + "</label></td>\
                        <td>" + mailData.title + "</td>\
                        <td>" + mailData.content + "</td>\
                        <td>" + mailData.reason + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + mailData.items + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + formatItems(mailData.items) + "</td>\
                        <td>" + mailData.createUser + "</td>\
                        <td>" + mailData.createDate + "</td>\
                        <td>" + checkStr(mailData.adminUser) + "</td>\
                        <td>" + checkStr(mailData.adminDate) + "</td>\
                        <td>" + checkStr(mailData.SendErrorMess) + "</td>\
                        <td>" + mailData.stateStr + "</td>\
                        <td><input type=\"button\" class=\"btn btn-primary pull-right sendall\" value=\"${msg['mail.list.operate4']}\" onclick=\"operateMail(" + mailData.id + ", 3);\"/></td>\
                        </tr>";
                        content += tmp;
                    }
                    if (data.pager.pageCount > 1) {
                        var pageList = "<tr><td aligen='center' colspan=13>\
                            ${msg['jsp.server.gong']}"
                            + data.pager.recordCount
                            + "${msg['forbid.chat.tiao']}"
                            + data.pager.pageCount
                            + "${msg['jsp.server.page']},${msg['forbid.chat.currpage']}"
                            + data.pager.pageNumber
                            + "${msg['forbid.chat.currpageend']}"
                            + data.pager.pageSize
                            + "${msg['forbid.chat.tiao']}&nbsp;&nbsp;";

                        for (var k = 1; k <= data.pager.pageCount; ++k) {
                            if (k != data.pager.pageNumber) {
                                pageList += "&nbsp;<a href='#' onclick='mine(" + k + ");'>" + k + "</a>";
                            } else {
                                pageList += "&nbsp;" + k;
                            }
                        }
                        pageList += "</td></tr>";
                        content += pageList;
                    }
                    content += "</table>";
                    $("#myMailList").html(content);
                }
            );
        }
        function showRoleIds(obj) {
            if($(obj).parent().find("label")[0].style.display=='none'){
                $(obj).parent().find("label")[0].style.display='block';
                $(obj).val("∧");
            }else{
                $(obj).parent().find("label").get(0).style.display='none';
                $(obj).val("∨");
            }
        }

        function history(page) {
            var pageSize = $("#pageSize").val().trim();
            $.post(base + "/mail/query",
                {
                    pageNumber: page,
                    pageSize: pageSize,
                    type: 3,
                    queryDate: $("#otherdate").val()
                },
                function (data) {
                    var content = "<table class=\"table table-bordered table-striped\">\
                    <tr><th>${msg['jsp.deductitem.td.number']}</th>\
                    <th>${msg['jsp.server']}</th>\
                    <th>${msg['kick.player']}</th>\
                    <th>${msg['activity.login.mailTitle']}</th>\
                    <th>${msg['activity.login.mailcontent']}</th>\
                    <th>${msg['mail.jsp.reason']}</th>\
                    <th>${msg['mail.jsp.fujian']}</th>\
                    <th>${msg['mail.jsp.fujian']}${msg['activity.login.mailcontent']}</th>\
                    <th>${msg['announce.jsp.createUser']}</th>\
                    <th>${msg['announce.jsp.createtime']}</th>\
                    <th>${msg['mail.jsp.adminUser']}</th>\
                    <th>${msg['mail.jsp.adminTime']}</th>\
                    <th>${msg['mail.jsp.demo']}</th>\
                    <th>${msg['mail.jsp.mailState']}</th>\
                    </tr>";

                    for (var i = 0; i < data.list.length; ++i) {
                        var mailData = data.list[i];
                        var tmp = "<tr><td>"+mailData.id+"</td>\
                            <td>" + mailData.serverId + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'><input type='button' name='dataBtn' value='∨' onclick='showRoleIds(this)'><label style='display:none'>" + mailData.roleIds + "</label></td>\
                        <td>" + mailData.title + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + mailData.content + "</td>\
                        <td>" + mailData.reason + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + mailData.items + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + formatItems(mailData.items) + "</td>\
                        <td>" + mailData.createUser + "</td>\
                        <td>" + mailData.createDate + "</td>\
                        <td>" + checkStr(mailData.adminUser) + "</td>\
                        <td>" + checkStr(mailData.adminDate) + "</td>\
                        <td>" + checkStr(mailData.SendErrorMess) + "</td>\
                        <td>" + mailData.stateStr + "</td></tr>";
                        content += tmp;
                    }

                    if (data.pager.pageCount > 1) {
                        var pageList = "<tr><td aligen='center' colspan=12>\
                        ${msg['jsp.server.gong']}"
                            + data.pager.recordCount
                            + "${msg['forbid.chat.tiao']}"
                            + data.pager.pageCount
                            + "${msg['jsp.server.page']},${msg['forbid.chat.currpage']}"
                            + data.pager.pageNumber
                            + "${msg['forbid.chat.currpageend']}"
                            + data.pager.pageSize
                            + "${msg['forbid.chat.tiao']}&nbsp;&nbsp;";

                        for (var k = 1; k <= data.pager.pageCount; ++k) {
                            if (k != data.pager.pageNumber) {
                                pageList += "&nbsp;<a href='#' onclick='history(" + k + ");'>" + k + "</a>";
                            } else {
                                pageList += "&nbsp;" + k;
                            }
                        }
                        pageList += "</td></tr>";
                        content += pageList;
                    }
                    content += "</table>";
                    $("#history").html(content);
                }
            );
        }

        //一键发送
        function oneKeySend() {
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/mail/onekeySend", {}, function (data) {
                $("#loadingmodal").modal('hide');
                if (data.ok) {
                    loadAllMailList()
                } else {
                    alert(data.msg);
                }
            });
        }

        //邮件操作，1：发送，2不予发送，3：删除
        function operateMail(id, type){
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/mail/adminMail", {id: id, type: type}, function (data) {
                $("#loadingmodal").modal('hide');
                if (data.ok) {
                    loadAllMailList()
                } else {
                    alert(data.msg);
                }
            });
        }

        function checkStr(data) {
            if (data == undefined) {
                return "";
            }
            return data;
        }

        function toggleTable(obj) {
            $(obj).toggle();
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="container-fluid">
        <label id="mail1" class="label label-info" onclick="toggleTable('${"#waitMailList"}')">${msg['mail.waitlist']}</label>
        <div id="waitMailList"></div>
    </div>

    <div class="container-fluid">
        <label id="mail2" class="label label-info" onclick="toggleTable('${"#myMailList"}')">${msg['mail.minelist']}</label>
        <div id="myMailList"></div>
    </div>

    <div class="container-fluid">
        <label id="mail3" class="label label-info" onclick="toggleTable('${"#history"}')">${msg['mail.historylist']}</label>
        <span for="pageSize">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页</span>
        <input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
        <input type="button" id="query_btn" value="查询" class="btn btn-primary" onclick="history()">
        <div id="history">

        </div>
    </div>
</div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>