<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>超级邮件发送</title>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/validationEngine.jquery.css">
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.boxy.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine-zh_CN.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.validationEngine.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript"
            src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
    <script>
        var base = "${base}";
        var items = new Map();
        var addItemIndex=0;//初始化添加物品的id
        var itemIndexAll='';
        $(function () {
            loadAllItem();
            reloadNoHeFuServerGroups(false);
            queryMailList();
            // $("#itemDiv").hide();
        });

        //公共取值部分
        var roleIds;
        var serverId;
        function coom() {
            roleIds = $("#roleIds").val().trim();
            if ((roleIds == undefined || roleIds == "") && roleIds != "all") {
                alert("${msg['mail.query.roleidsError']}");
                return;
            }
            if ($("input[name='serverId']:checked").length > 1 || $("input[name='serverId']:checked").length < 0) {
                alert("必须选择一个服务器");
                return;
            }

            serverId = $("#select_server").val();
        }
        //手动检测角色ID是否有效
        function checkRoleIds() {
            coom();
            $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
            $.post(base + "/mail/queryRoleIds",
                {roleIds: roleIds, serverId: serverId},
                function (data) {
                    $("#loadingmodal").modal('hide');
                    $("#checkResult").html("<font color='red'>" + data.msg + "</font>");
                    if (data.ok) {
                        $("#roleid").val("1");
                    }
                });
        } //异步自动检测角色ID是否有效
        function autocheckRoleIds() {
            coom();
            $.ajax({
                url : base + '/mail/queryRoleIds',
                type :'POST',
                dataType : 'json',
                data:{
                    "roleIds":roleIds,
                    "serverId":serverId
                },
                async : true,
                success : function(data){
                    $("#autoCheckResult").html("<font color='red'>" + data.msg + "</font>");
                    if (data.ok) {
                        $("#roleid").val("1");
                    }
                }
            });
        }

        function saveMail() {
            var roleId = $("#roleIds").val().trim();
            if (roleId == "" || roleId === undefined) {
                alert("${msg['mail.query.roleidsError']}");
                return;
            }
            $("#roleIds").val(roleId);
            if ($('#sendMailForm').validationEngine('validate')) {
                $("#loadingmodal").modal({backdrop: 'static', keyboard: false});
                $.post(base + "/mail/saveMail", $("#sendMailForm").serialize(),
                    function (data) {
                        $("#loadingmodal").modal('hide');
                        $("#msg").html("<span>" + data.msg + "</span>");
                        queryMailList();
                    });
            } else {
                $("#alert").html("${msg['mail.save.paramError']}");
            }
        }

        function queryMailList() {
            paging(1);
        }
        function paging(page){
            var pageSize = $("#pageSize").val().trim();
            $.post(base + "/mail/queryMailList", {pageNumber: page, pageSize: pageSize},
                function (data) {
                    $("#pageUl").empty();
                    $("#sendSuperMail_count").html("共" + data.pager.recordCount + "个邮件, 总计" + data.pager.pageCount + "页");
                    var content = "<table class=\"table table-bordered table-striped\">\
                    <tr>\
                      <th>${msg['announce.jsp.createUser']}</th>\
                      <th>${msg['announce.jsp.createtime']}</th>\
                      <th>${msg['activity.login.mailTitle']}</th>\
                      <th>${msg['kick.player']}</th>\
                      <th>${msg['mail.jsp.reason']}</th>\
                      <th>${msg['jsp.server']}</th>\
                      <th>${msg['jsp.server.deal']}</th>\
                    </tr>";
                    for (var i = 0; i < data.list.length; ++i) {
                        var mailData = data.list[i];
                        var tmp = "<tr><td>" + mailData.createUser + "</td>\
                        <td>" + mailData.createDate + "</td>\
                        <td>" + mailData.title + "</td>\
                        <td style='WORD-BREAK: break-all; WORD-WRAP: break-word'><input type='button' name='dataBtn' value='∨' onclick='showRoleIds(this)'><label style='display:none'>" + mailData.roleIds + "</label></td>\
                        <td>" + mailData.reason + "</td>\
                        <td>" + mailData.serverId + "</td>\
                        <td><input type='button' class='btn btn-info' onclick='parse(" + mailData.id + ");' value='${msg['jsp.parse.content']}'/></td></tr>";
                        content += tmp;
                    }
                    content += "</table>";
                    $("#content").html(content);
                    pageQuery(data,page);
                });
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
        function parse(id) {
            $.post(base + "/mail/queryById", {id: id},
                function (data) {
                    if (data.ok) {
                        $("#items").empty();
                        $("#reward").val('');
                        $("#items").hide();
                        $("#show").attr("checked", false);
                        $("#itemDiv").hide();
                        var mailData = data.data;
                        $("#mailTitle").val(mailData.title);
                        $("#mailContent").val(mailData.content);
                        $("#reason").val(mailData.reason);

                        if (mailData.items.length > 0) {//列如1,6,0,0}3,6,1,9
                            $("#items").empty();
                            $("#reward").val('');
                            $("#items").show();
                            $("#show").attr("checked", true);
                            $("#itemDiv").show();
                            var itemsArr=mailData.items.split("}");
                            $.each(itemsArr,function (i) {//列如1,6,0,0
                                var item=itemsArr[i].split(",");
                                var itemId=item[0];
                                var itemName=items.get(item[0]);
                                var itemNum=item[1];
                                var isBind=item[2];
                                var picker=item[3];
                                var itemIndex='parseItem'+i;
                                $("#items").append("<span id=\'"+itemIndex+"\' class=\"label\"><a style='display: inline-block' href='#' onclick='showUpdateItem("+itemId+","+itemNum+","+isBind+","+picker+",\""+itemIndex+"\");'>" + itemName + "*" + itemNum +
                                    "</a><i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId +", " + itemNum + ")\"></i></span>");
                            });
                            $("#reward").val(mailData.items.toString());
                        }
                        $("#roleId").val("");
                    }
                }
            );
        }

        function fileSelected(files) {
            var filePath = $("#roleFile").val();
            var subLength = filePath.lastIndexOf(".");
            var sub = filePath.substring(subLength + 1, filePath.length);
            if (sub != "txt") {
                alert("请上传txt格式文件。如有多个数据，请换行区分。");
                return;
            }
            var roleContent = "";
            var file = files[0];
            var reader = new FileReader();
            reader.readAsText(file);
            reader.onload = function () {
                var fileContent = this.result;
                if (fileContent == "") {
                    alert("${msg['jsp.deductitem.syspromt.uploadcontent']}");
                    return;
                }
                var roleIds = fileContent.split("\n");
                for (var i = 0; i < roleIds.length - 1; i++) {
                    roleContent += roleIds[i].trim() + ",";
                }
                var result = roleContent.substring(0, roleContent.length - 2);
                $("#roleIds").val(result);
            };
        }

        function showItemDiv() {
            var isShow = $("#show").prop("checked");
            $("#itemDiv").toggle(isShow);
            if (!isShow) {
                $("#items").empty();
                $("#reward").val('');
            }
        }
        //显示添加物品界面
        function showAddItemModel() {
            $("#addItemModal").modal('toggle');
            $("#addItemForm")[0].reset();
            $("#autoCheckItemId").html("");
            $("h3").text("添加物品");
            $("#updateItem").css("display", "none");
            $("#addItem").css("display", "inline-block");
        }

        function addItem() {
            var itemId = $("#itemId").val();
            var itemName = items.get(itemId);
            var itemNum = $("#itemNum").val();
            var isBind = $("#isBind").val();
            var picker = $("#selectpicker").val();
            var itemValue = itemId + "," + itemNum + "," + isBind + "," + picker;
            console.log(itemName + ":" + itemName);
            var lastValue = $("#reward").val();
            var itemArray = lastValue.split('}');
            if (lastValue == "") {
                itemArray = new Array();
            }
            itemArray.push(itemValue);
            $("#reward").val(itemArray.join("}"));
            console.log(itemArray.join("}"));
            var itemIndex='addItem'+addItemIndex;
            $("#items").append("<span id=\'"+itemIndex+"\' class=\"label\"><a style='display: inline-block' href='#' onclick='showUpdateItem("+itemId+","+itemNum+","+isBind+","+picker+",\""+itemIndex+"\")'>" + itemName + "*" + itemNum +
                "</a><i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId +", " + itemNum + ")\"></i></span>");
            $("#addItemModal").modal('hide');
            addItemIndex+=1;
        }
        //显示修改物品界面
        var itemIdAll;
        var itemNumAll;
        var isBindAll;
        var pickerAll;
        function showUpdateItem(itemId,itemNum,isBind,picker,itemIndex) {
            $("#addItemModal").modal('toggle');
            $("h3").text("修改物品");
            $("#autoCheckItemId").html("");
            $("#updateItem").css("display", "inline-block");
            $("#addItem").css("display", "none");
            $("#itemId").val(itemId);
            $("#itemNum").val(itemNum);
            $("#isBind").val(isBind);
            $("#selectpicker").val(picker);

            itemIndexAll=itemIndex;
            itemIdAll=itemId;
            itemNumAll=itemNum;
            isBindAll=isBind;
            pickerAll=picker;
            console.log("itemIndexAll:"+itemIndexAll);
            event.stopPropagation();//防止冒泡事件的触发，只触发当前的点击事件
        }
        var count=0;
        //确认修改物品
        function updateItem() {
            var itemId = $("#itemId").val();
            var itemName = items.get(itemId);
            var itemNum = $("#itemNum").val();
            var isBind = $("#isBind").val();
            var picker = $("#selectpicker").val();
            var itemValue = itemId + "," + itemNum + "," + isBind + "," + picker;
            var itemIndexUpdate="updateItem"+count;
            $("#"+itemIndexAll).before("<span id=\'"+itemIndexUpdate+"\' class=\"label\"><a style='display: inline-block' href='#' onclick='showUpdateItem("+itemId+","+itemNum+","+isBind+","+picker+",\""+itemIndexUpdate+"\")'>" + itemName + "*" + itemNum +
                "</a><i class=\"icon-remove item-remove\" onclick=\"removeItem(this, " + itemId +", " + itemNum + ")\"></i></span>");
            count+=1;
            addUpdateItem($("#"+itemIndexAll),itemIdAll,itemNumAll,isBindAll,pickerAll);
            $("#addItemModal").modal('hide');
        }
        function addUpdateItem(obj,itemIdAll,itemNumAll,isBindAll,pickerAll) {
            var itemId = $("#itemId").val();
            var itemNum = $("#itemNum").val();
            var isBind = $("#isBind").val();
            var picker = $("#selectpicker").val();
            var value = $(obj).parent().prev(".rewardInput").val();
            var pos=0;
            var itemArray = value.split('}');
            $.each(itemArray, function (i, n) {
                var item = n.split(",");
                if (item[0] == itemIdAll && item[1] == itemNumAll && item[2] == isBindAll && item[3] == pickerAll) {
                    pos = i;
                }
            });
            itemArray.splice(pos, 1);
            console.log("pos："+pos);
            var updateItem=itemId+","+itemNum+","+isBind+","+picker;
            itemArray.splice(pos,0,updateItem);
            $(obj).parent().prev(".rewardInput").val(itemArray.join("}"));
            $(obj).parent().find($("#"+itemIndexAll)).remove();
        }
        function removeItem(obj, itemId, itemNum) {
            var pos;
            var value = $(obj).parent().parent().prev(".rewardInput").val();
            var itemArray = value.split('}');
            $.each(itemArray, function (i, n) {
                var item = n.split(",");
                if (item[0] == itemId && item[1] == itemNum) {
                    pos = i;
                }
            });
            itemArray.splice(pos, 1);
            $(obj).parent().parent().prev(".rewardInput").val(itemArray.join("}"));
            console.log(itemArray.join(";"));
            $(obj).parent().remove();
            event.stopPropagation();
        }
        //异步自动检测物品ID有效性
        function autoCheckItemId(obj) {
            var itemName=items.get($(obj).val());
            if (itemName == undefined){
                $("#autoCheckItemId").html("<font color='red'>物品ID不存在 </font>");
            } else {
                $("#autoCheckItemId").html("<font color='#90ee90'>物品ID可用</font>");
            }

        }
    </script>
</head>
<body>
<div class="container-fluid">
    <form id="sendMailForm" method="post" class="form-horizontal well">

        <div class="control-group">
            <label for="select_group" class="control-label">${msg['jsp.server']}</label>
            <div class="controls">
                <select id="select_group" name="groupName" onchange="reloadNoHefuServerBox(this.value, false)"
                        class="span2"></select>
                <select id="select_server" name="serverId" class="span2"></select>
            </div>
        </div>

        <div class="control-group">
            <label for="roleIds" class="control-label">${msg['kick.player']}</label>
            <div class="controls">
                <input id="roleIds" type="text" onblur="autocheckRoleIds()" name="roleIds" class="validate[required] span6" width="30px"
                       placeholder="${msg['jsp.roleId.Info']}"/>
                <span id="autoCheckResult"></span>
                <input id="roleFile" type="file" onchange="fileSelected(this.files);"/>
                <br/>
                <input type="button" class="btn btn-warning" value="${msg['jsp.roleId.check']}"
                       onclick="checkRoleIds()"/>
                <label id="checkResult"></label>
            </div>
        </div>

        <div class="control-group">
            <label for="mailTitle" class="control-label">${msg['activity.login.mailTitle']}</label>
            <div class="controls">
                <input id="mailTitle" type="text" name="title" value="货币大礼包" class="validate[required],maxSize[60],minSize[4] span8"
                       placeholder="${msg['mail.jsp.titlemax']}"/>
            </div>
        </div>

        <div class="control-group">
            <label for="mailContent" class="control-label">${msg['activity.login.mailcontent']}</label>
            <div class="controls">
                <textarea id="mailContent" name="content" rows="5"
                          class="validate[required],maxSize[250],minSize[6] span8"
                          placeholder="${msg['mail.jsp.contentmax']}">基础货币大礼包</textarea>
            </div>
        </div>

        <div class="control-group">
            <label for="reason" class="control-label">${msg['mail.jsp.reason']}</label>
            <div class="controls">
                <input type="text" id="reason" name="reason" value="测试时一键发放货币" class="validate[required],minSize[6],maxSize[150] span8"
                       placeholder="${msg['forbid.needwrite']}"/>
            </div>
        </div>

        <div class="controls">
            <label class="checkbox inline"><input type="checkbox" checked id="show"
                                                  onclick="showItemDiv()"/>${msg['mail.jsp.sitemInfo']}</label>
        </div>
        <br/>

        <div class="control-group" id="itemDiv">
            <label class="control-label">${msg['activity.login.fujianItem']}</label>
            <div class="span8" style="border: 1px solid rgb(204, 204, 204); border-radius: 2px; height: 25px"
                 onclick="showAddItemModel()">
                <input type="hidden" id="reward" name="items" class="rewardInput"
                       value="1,99999,1,9}2,99999,1,9}3,99999999,1,9}12,999999,1,9}9,99999,1,9}11,99999,1,9}14,999999,1,9">
                <div id="items" class="items" style="margin-top: 3px">
                    <span id="defaultItem0" class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(1,99999,1,9,"defaultItem0")'>元宝*99999</a><i
                        class="icon-remove item-remove" onclick="removeItem(this, 1, 99999)"></i>
                    </span>
                    <span id="defaultItem1"
                          class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(2,99999,1,9,"defaultItem1")'>绑定元宝*99999</a><i class="icon-remove item-remove"
                                                   onclick="removeItem(this, 2, 99999)"></i>
                    </span>
                    <span id="defaultItem2" class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(3,99999999,1,9,"defaultItem2")'>铜钱*99999999</a><i
                        class="icon-remove item-remove" onclick="removeItem(this, 3, 99999999)"></i>
                    </span>
                    <span id="defaultItem3"
                          class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(12,999999,1,9,"defaultItem3")'>灵石*999999</a><i class="icon-remove item-remove"
                                                  onclick="removeItem(this, 12, 999999)"></i>
                    </span>
                    <span id="defaultItem4" class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(9,99999,1,9,"defaultItem4")'>声望*99999</a><i
                        class="icon-remove item-remove" onclick="removeItem(this, 9, 99999)"></i>
                    </span>
                    <span id="defaultItem5"
                          class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(11,99999,1,9,"defaultItem5")'>宗派贡献*99999</a><i class="icon-remove item-remove"
                                                   onclick="removeItem(this, 11, 99999)"></i>
                    </span>
                    <span id="defaultItem6" class="label"><a style='display: inline-block' href='#' onclick='showUpdateItem(14,999999,1,9,"defaultItem6")'>法宝精粹*999999</a><i
                        class="icon-remove item-remove" onclick="removeItem(this, 14, 999999)"></i>
                    </span>
                </div>
            </div>
        </div>

        <div class="controls">
            <input type="button" class="btn btn-primary" value="${msg['jsp.server.sumbit']}" onclick="saveMail()"/>
        </div>
    </form>
</div>

<div id="addItemModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>添加物品</h3>
    </div>
    <div class="modal-body">
        <form id="addItemForm" class="row-fluid">
            <div class="offset1 span10">
                <input id="curObj" type="hidden"/>
                <label for="itemId">物品ID</label><input type="text" id="itemId" onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')" onblur="autoCheckItemId(this)" list="itemList" class="span8"/>
                <datalist id="itemList"></datalist>
                <span id="autoCheckItemId"></span>
                <label for="itemNum">物品数量</label><input id="itemNum" type="text" onkeyup="this.value=this.value.replace(/^(0+)|[^\d]+/g,'')" class="span8"/>
                <label for="isBind">是否绑定</label>
                <select id="isBind" class="span8">
                    <option value="0" selected>非绑</option>
                    <option value="1">绑定</option>
                </select>
                <label for="selectpicker">职业</label>
                <select id="selectpicker" class="span8">
                    <option value="0">玄剑</option>
                    <option value="1">天英</option>
                    <option value="2">地藏</option>
                    <option value="3">罗刹</option>
                    <option value="9" selected>通用</option>
                </select>
            </div>
        </form>
    </div>
    <div class="modal-footer">
        <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
        <input type="button" value="添加" id="addItem" onclick="addItem()" class="btn btn-primary"/>
        <input type="button" value="修改" id="updateItem" onclick="updateItem()" style="display: inline-block" class="btn btn-primary"/>
    </div>
</div>

<div id="msg" class="container-fluid"></div>
<span for="pageSize">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;每页</span>
<input type="text" name="pageSize" id="pageSize" value="10" class="span1"/>
<input type="button" id="query_btn" value="查询" class="btn btn-primary" onclick="queryMailList()">
<p id="sendSuperMail_count"></p>
<div class="container-fluid" id="content"></div>
<div class="pagination" id="pageUl"></div>
<jsp:include page="../commonmodal.jsp"/>
</body>
</html>