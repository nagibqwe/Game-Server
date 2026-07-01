<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>活动查询</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/bootstrap-datetimepicker.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/datetimepicker/language/bootstrap-datetimepicker.zh-CN.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript">
        var base = '${base}';
        var initPageNumber = 1;
        var initPageSize = 20;

        $(function () {
            $("#pageSize").val(initPageSize);

            $(".date").datetimepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                weekStart: 1,
                todayBtn: 1,
                autoclose: 1,
                todayHighlight: 1,
                startView: 2,
                minView: 2,
                showMeridian: 1
            });

            $("#loading").shCircleLoader();
        });

        function search() {
            paging(initPageNumber);
        }

        function paging(page) {
            var id = $("#id").val().trim();
            var actType = $("#actType").val().trim();
            var activeName = $("#activeName").val().trim();
            var pageSize = $("#pageSize").val().trim();
            var startDate = $("#start input").val();
            var endDate = $("#end input").val();
            if (!checkDate(startDate, endDate)) {
                return;
            }
            var html = "";
            $("#loadingmodal").modal({
                backdrop: 'static',
                keyboard: false
            });
            $.ajax({
                url: base + "/activity/queryAllActivity",
                data: {
                    "id": id,
                    "actType": actType,
                    "activeName": activeName,
                    "beginTime": startDate,
                    "endTime": endDate,
                    "pageNumber": page,
                    "pageSize": pageSize
                },
                method: "post",
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    html += "<tbody>";
                    html += "<tr>";
                    html += "<th>活动ID</th>";
                    html += "<th>活动类型</th>";
                    html += "<th>活动名称</th>";
                    html += "<th>活动时间</th>";
                    html += "<th>活动条件列表</th>";
                    html += "<th>活动奖励列表</th>";
                    html += "<th>活动封顶(重复)次数</th>";
                    html += "<th>活动面板显示文字</th>";
                    html += "<th>活动面板图片资源ID</th>";
                    html += "<th>活动标签位置</th>";
                    html += "<th>活动在标签中的排序(标签内活动排序)</th>";
                    html += "<th>活动状态</th>";
                    html += "<th>活动发布平台标识</th>";
                    html += "<th>活动要发布到的区服列表</th>";
                    html += "<th>活动发布成功的区服列表</th>";
                    html += "<th>活动是否被删除</th>";
                    html += "</tr>";

                    var activitydata = data.list;
                    for (var i = 0; i < activitydata.length; i++) {
                        html += "<tr>";
                        html += "<td>" + activitydata[i].id + "</td>";
                        html += "<td>" + getType(activitydata[i].type) + "</td>";
                        html += "<td>" + activitydata[i].name + "</td>";
                        html += "<td>" + activitydata[i].beginTime + "~" + activitydata[i].endTime + "</td>";
                        html += "<td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + activitydata[i].conditionList + "</td>";
                        html += "<td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + activitydata[i].rewardList + "</td>";
                        html += "<td>" + activitydata[i].numLimit + "</td>";
                        html += "<td style='WORD-BREAK: break-all; WORD-WRAP: break-word'>" + activitydata[i].panelText + "</td>";
                        html += "<td>" + activitydata[i].panelImageId + "</td>";
                        html += "<td>" + activitydata[i].labelPosition + "</td>";
                        html += "<td>" + activitydata[i].labelOrder + "</td>";
                        html += "<td>" + getstate(activitydata[i].state) + "</td>";
                        html += "<td>" + activitydata[i].platform + "</td>";
                        html += "<td>" + activitydata[i].toSidList + "</td>";
                        html += "<td>" + activitydata[i].okSidList + "</td>";
                        html += "<td>" + (activitydata[i].isDeleted == 0 ? "否" : "是") + "</td>";
                        html += "</tr>";
                    }
                    html += "</tbody>";
                    $("#activity_list").html(html);

                    var pages = data.pager.pageCount;
                    var options = {
                        bootstrapMajorVersion: 2,
                        currentPage: page,//当前页面
                        numberOfPages: 5,//一页显示几个按钮（在ul里面生成5个li）
                        totalPages: pages, //总页数
                        itemTexts: function (type, page) {
                            switch (type) {
                                case "first":
                                    return "首页";
                                case "prev":
                                    return "上一页";
                                case "next":
                                    return "下一页";
                                case "last":
                                    return "末页";
                                case "page":
                                    return page;
                            }
                        }
                    };
                    $("#pageUl").bootstrapPaginator(options);
                }
            });
        }

        function getstate(state) {
            if (state === "0") {
                return "未验证";
            } else if (state === "1") {
                return "已验证";
            } else if (state === "2") {
                return "已发布";
            }
        }

        function checkDate(startDate, endDate) {
            if (startDate != null && endDate != null) {
                var start = new Date(startDate.replace("-", "/").replace("-", "/"));
                var end = new Date(endDate.replace("-", "/").replace("-", "/"));
                if (end < start) {
                    alert("${msg['jsp.log.sdatelessthanedate']}");
                    return false;
                }
                return true;
            }
        };
    </script>
</head>
<body>
<div class="container-fluid">
    <form action="#" class="well form-inline">
        <input type="text" id="id" placeholder="活动ID"/>
        <label class="label">活动类型</label>
        <select id="actType">
            <option value="0" selected="selected">${msg['activity.all']}</option>
            <option value="1">${msg['activity.recharge.common']}</option>
            <option value="2">${msg['activity.consume.common']}</option>
            <option value="3">${msg['activity.exchange']}</option>
            <option value="4">${msg['activity.drop']}</option>
            <option value="5">${msg['activity.monster']}</option>
            <option value="6">${msg['activity.recharge.loop']}</option>
            <option value="7">${msg['activity.consume.loop']}</option>
            <option value="8">${msg['activity.receive']}</option>
            <option value="9">${msg['activity.DaySend']}</option>
            <option value="10">${msg['activity.DayReceive']}</option>
            <option value="11">${msg['activity.RechargeTopReward']}</option>
            <option value="12">${msg['activity.XiaoHaoTopReward']}</option>
            <option value="13">${msg['activity.SendTopReward']}</option>
            <option value="14">${msg['activity.ReceiveTopReward']}</option>
            <option value="15">${msg['activity.LoginMail']}</option>
            <option value="17">${msg['activity.buylimit']}</option>
            <option value="18">${msg['activity.PaySendItem']}</option>
            <option value="19">${msg['activity.monsterexpinc']}</option>
            <option value="20">${msg['activity.tianqibaoku']}</option>
            <option value="22">${msg['activity.killboss']}</option>
            <option value="23">${msg['activity.numlimitbuy']}</option>
            <option value="24">${msg['activity.rechargeExtra']}</option>
            <option value="27">${msg['activity.ContinuousRecharge']}</option>
            <option value="28">${msg['activity.CrossServerRank']}</option>
            <option value="32">${msg['activity.firstkill']}</option>
            <option value="33">${msg['activity.timeLimitGift']}</option>
            <option value="34">${msg['activity.cloudBuy']}</option>
            <option value="35">${msg['activity.continueLogin']}</option>
            <option value="36">${msg['activity.ShareAction']}</option>
            <option value="101">${msg['activity.glories']}</option>
            <option value="102">${msg['activity.submitMaterial']}</option>
        </select>

        <input id="activeName" name="activeName" type="text" id="serverId" placeholder="活动名称"/>

        <!-- 时间段 -->
        <label class="label">活动时间</label>
        <div class="input-append date" id="start">
            <input style="width: 120px;" name="beginTime" size="20" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>
        -
        <div class="input-append date" id="end">
            <input style="width: 120px;" name="endTime" size="20" type="text" readonly>
            <span class="add-on"><i class="icon-th"></i></span>
        </div>

        ${msg['jsp.server.perPage']}:
        <input type="text" id="pageSize" name="pageSize" class="span1">
        <span style="margin-right: 10px;">
				<button id="server_query_btn" type="button" class="btn btn-primary" onclick="search();"><i
                        class="icon-search icon-white"></i></button>
			</span>
    </form>
</div>
<div class="container-fluid">
    <table class="table table-bordered table-striped" id="activity_list">
    </table>
    <span style="font-size:14px;">
		<div class="pagination" id="pageUl"></div>
	</span>
</div>

<jsp:include page="../commonmodal.jsp"/>
</body>
</html>
