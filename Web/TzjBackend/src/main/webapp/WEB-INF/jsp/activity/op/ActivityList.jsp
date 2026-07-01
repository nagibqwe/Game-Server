<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html>
<head>
    <title>活动总览</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${base}/css/activity.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/common.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/boxy.css" type="text/css"/>
    <link rel="stylesheet" href="${base}/css/alertify.css">
    <link rel="stylesheet" href="${base}/css/alertify.default.css">
    <link rel="stylesheet" href="${base}/css/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${base}/css/jquery.shCircleLoader.css">
    <script type="text/javascript" src="${base}/js/jquery/jquery-2.1.4.min.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-paginator.js"></script>
    <script type="text/javascript" src="${base}/css/bootstrap/js/bootstrap-confirmation.js"></script>
    <script type="text/javascript" src="${base}/js/dateFormat.js"></script>
    <script type="text/javascript" src="${base}/js/global.js"></script>
    <script type="text/javascript" src="${base}/js/jquery/jquery.shCircleLoader-min.js"></script>
    <script type="text/javascript" src="${base}/js/activity.js"></script>
    <script type="text/javascript" src="${base}/js/excel.js"></script>
    <script type="text/javascript">
        var base = '${base}';
        var pageNum =1;
        $(function () {
            $("#loading").shCircleLoader();
            loadAllServerList();
            getBaseUrl(base);
            loadActivityFestivalType();
            festivals.set(-1, "所有");//设置一个所有条件
            festivals.forEach(function(value, key) {
                if (key != -1){
                    $("#subType2").append("<option value='" + key +"'>" + value +"(" + key +")</option>");
                }else {
                    $("#subType2").append("<option value='" + key +"' selected>" + value +"</option>");
                }
            });
            $("#publishActivity").modal('hide');
            doSearch($("#type").val(),$("#subType2").val(),$("#tag").val(),$("#queryString").val(),pageNum);
        });

        function doSearch(type,subtype2,tag,activityName,pageNumber) {
            isActivityList=true;
            $("#loadingmodal").modal({
                backdrop: 'static',
                keyboard: false
            });
            $.ajax({
                url: base + "/activity/queryActivityByServer",
                data: {
                    "type":type,
                    "subtype2":subtype2,
                    "tag":tag,
                    "activityName":activityName,
                    "pageNumber": pageNumber,
                    "pageSize": pageSize
                },
                method: "post",
                dataType: "json",
                success: function (data) {
                    $("#loadingmodal").modal('hide');
                    if (!data.ok) {
                        alert(data.msg);
                        $("#activity_list").html("");
                        return;
                    }
                    var activitydata = data.data.list;
                    var html = "";
                    html += "<tbody>";
                    html += "<tr>";
                    html += "<th>活动ID</th>";
                    html += "<th>活动名称</th>";
                    html += "<th>活动类型</th>";
                    html += "<th>节日类型</th>";
                    html += "<th>最小等级</th>";
                    html += "<th>最大等级</th>";
                    html += "<th>活动标签</th>";
                    html += "<th>标签排序</th>";
                    html += "<th>时间类型</th>";
                    html += "<th>开服天数</th>";
                    html += "<th>持续天数</th>";
                    html += "<th>开始时间</th>";
                    html += "<th>结束时间</th>";
                    html += "<th>数据</th>";
                    html += "<th>活动状态</th>";
                    html += "<th>发布列表</th>";
                    html += "<th>成功列表</th>";
                    html += "<th>测试<input type='checkbox' id='testAll'/></th>";
                    html += "<th>验证<input type='checkbox' id='verifyAll'/></th>";
                    html += "<th>发布<input type='checkbox' id='publishAll'/></th>";
                    html += "<th>删除<input type='checkbox' id='deleteAll'/></th>";
                    html += "<th>Excel</th>";
                    html += "</tr>";

                    for (var i = 0; i < activitydata.length; i++) {
                        html += "<tr>";
                        html += "<td class='id'>" + activitydata[i].id + "</td>";
                        html += "<td>" + activitydata[i].name + "</td>";
                        html += "<td class='type'>" + activitydata[i].type + "</td>";
                        html += "<td class='subType'>" + activitydata[i].subType + "</td>";
                        html += "<td>" + activitydata[i].minLv + "</td>";
                        html += "<td>" + activitydata[i].maxLv + "</td>";
                        html += "<td>" + activitydata[i].tag + "</td>";
                        html += "<td>" + activitydata[i].sort + "</td>";
                        html += "<td>" + activitydata[i].timeType + "</td>";
                        html += "<td>" + activitydata[i].openServerOffsetBegin + "</td>";
                        html += "<td>" + activitydata[i].openServerOffset + "</td>";
                        html += "<td>" + activitydata[i].beginTime + "</td>";
                        html += "<td>" + activitydata[i].endTime + "</td>";
                        html += "<td><label name='dataDetail' style='display: none'>" + activitydata[i].custom + "</label></br>" +
                            "<input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>";

                        html += "<td>" + getState(activitydata[i].state) + "</td>";
                        html += "<td><label name='dataDetail' style='display: none'>" + activitydata[i].toSidList + "</label></br>" +
                            "<input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>";
                        html += "<td><label name='dataDetail' style='display: none'>" + activitydata[i].okSidList + "</label></br>" +
                            "<input type='button' name='dataBtn' value='∨' onclick='showActData(this)' /></td>";

                        var testable = activitydata[i].state == 0;
                        var valiadable = activitydata[i].state == 1;
                        var publishable = activitydata[i].state == 2;
                        // "<td><input type='checkbox' class='test'" + (testable ? "" : "disabled='disabled'") + "></td>\n" +
                        html += "<td><input type='checkbox' class='test' ></td>";
                        html += "<td><input type='checkbox' class='valid'" + (valiadable ? "" : "disabled='disabled'") + "></td>";
                        html += "<td><input type='checkbox' class='publish'" + (publishable ? "" : "disabled='disabled'") + "></td>";
                        html += "<td><input type='checkbox' class='delete'></td>";
                        html += "<td><form name='actForm"+i+"' method='post' action='"+baseUrl+"/exportActivityData'><input name='actId' type='hidden' value='"+activitydata[i].id+"'><a href='#' style='color: #5bb75b' id='export' onclick='exportActivityData("+i+",this)'>导出</a></form></td>"
                        html += "</tr>";
                    }
                    html += "</tbody>";
                    $("#activity_list").html(html);

                    //全选处理
                    $("#testAll").click(function () {
                        var flag = $("#testAll").prop("checked");
                        $(".test:not(:disabled)").prop("checked", flag);
                    });
                    $("#verifyAll").click(function () {
                        var flag = $("#verifyAll").prop("checked");
                        $(".valid:not(:disabled)").prop("checked", flag);
                    });
                    $("#publishAll").click(function () {
                        var flag = $("#publishAll").prop("checked");
                        $(".publish:not(:disabled)").prop("checked", flag);
                    });
                    $("#deleteAll").click(function () {
                        var flag = $("#deleteAll").prop("checked");
                        $(".delete").prop("checked", flag);
                    });
                    $("#coverAll").click(function () {
                        var flag = $("#coverAll").prop("checked");
                        $(".cover").prop("checked", flag);
                    });

                    //批量全选处理
                    $("#testAll,#verifyAll,#publishAll,#deleteAll").prop("checked", false);
                    $(".test").click(function () {
                        var flag = $(".test:not(:disabled):checked").length == $(".test:not(:disabled)").length;
                        $("#testAll").prop("checked", flag);
                    });
                    $(".valid").click(function () {
                        var flag = $(".valid:not(:disabled):checked").length == $(".valid:not(:disabled)").length;
                        $("#verifyAll").prop("checked", flag);
                    });
                    $(".publish").click(function () {
                        var flag = $(".publish:not(:disabled):checked").length == $(".publish:not(:disabled)").length;
                        $("#publishAll").prop("checked", flag);
                    });
                    $(".delete").click(function () {
                        var flag = $(".delete:checked").length == $(".delete").length;
                        $("#deleteAll").prop("checked", flag);
                    });

                    //分页处理
                    $("#pager").empty();
                    $("#pageInfo").empty();
                    var pager = data.data.pager;
                    if (activitydata.length > 0) {
                        for (var i = pager.pageNumber - 2; i < pager.pageNumber + 3; i++) {
                            if (i < 1) {
                                continue;
                            }
                            if (i == 1) {
                                if (pager.pageNumber == 1) {
                                    $("#pager").append("<li class='disabled'><span>&laquo;</span></li>")
                                } else {
                                    $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + (pager.pageNumber - 1) + ")'>&laquo;</a></li>")
                                }
                            }
                            if (i == pager.pageNumber) {
                                $("#pager").append("<li class='disabled'><span>" + i + "</span></li>")
                            } else if (i <= pager.pageCount) {
                                $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + i + ")'>" + i + "</a></li>")
                            }
                            if (i == pager.pageCount) {
                                if (pager.pageNumber == pager.pageCount) {
                                    $("#pager").append("<li class='disabled'><span>&raquo;</span></li>")
                                } else {
                                    $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + (pager.pageNumber + 1) + ")'>&raquo;</a></li>")
                                }
                                break;
                            }
                        }
                        $("#pageInfo").html("共" + pager.recordCount + "条记录，当前第" + pager.pageNumber + "页， 共" + pager.pageCount + "页");
                    } else {
                        // console.log("no data");
                        // alert("no data");
                    }
                }
            });
        }

        function searchActivityList(type,subtype2,tag,activityName,pageNumber) {
            isActivityList=true;
            pageNum = pageNumber;
            $.ajax({
                url: baseUrl + "/queryActivityByServer",
                method: "post",
                datatype: "json",
                data: {
                    "type":type,
                    "subtype2":subtype2,
                    "tag":tag,
                    "activityName":activityName,
                    "pageNumber": pageNumber,
                    "pageSize": pageSize
                },
                success: function (data) {
                    if (!data.ok) {
                        alert("error");
                        return
                    }

                    //活动数据
                    $("#activity_data").empty();
                    var list = data.data.list;
                    doSearch(type,subtype2,tag,activityName,pageNumber);

                    //批量全选处理
                    $("#testAll,#verifyAll,#publishAll,#deleteAll").prop("checked", false);
                    $(".test").click(function () {
                        var flag = $(".test:not(:disabled):checked").length == $(".test:not(:disabled)").length;
                        $("#testAll").prop("checked", flag);
                    });
                    $(".valid").click(function () {
                        var flag = $(".valid:not(:disabled):checked").length == $(".valid:not(:disabled)").length;
                        $("#verifyAll").prop("checked", flag);
                    });
                    $(".publish").click(function () {
                        var flag = $(".publish:not(:disabled):checked").length == $(".publish:not(:disabled)").length;
                        $("#publishAll").prop("checked", flag);
                    });
                    $(".delete").click(function () {
                        var flag = $(".delete:checked").length == $(".delete").length;
                        $("#deleteAll").prop("checked", flag);
                    });

                    //分页处理
                    $("#pager").empty();
                    $("#pageInfo").empty();
                    var pager = data.data.pager;
                    if (list.length > 0) {
                        for (var i = pager.pageNumber - 2; i < pager.pageNumber + 3; i++) {
                            if (i < 1) {
                                continue;
                            }
                            if (i == 1) {
                                if (pager.pageNumber == 1) {
                                    $("#pager").append("<li class='disabled'><span>&laquo;</span></li>")
                                } else {
                                    $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + (pager.pageNumber - 1) + ")'>&laquo;</a></li>")
                                }
                            }
                            if (i == pager.pageNumber) {
                                $("#pager").append("<li class='disabled'><span>" + i + "</span></li>")
                            } else if (i <= pager.pageCount) {
                                $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + i + ")'>" + i + "</a></li>")
                            }
                            if (i == pager.pageCount) {
                                if (pager.pageNumber == pager.pageCount) {
                                    $("#pager").append("<li class='disabled'><span>&raquo;</span></li>")
                                } else {
                                    $("#pager").append("<li><a href=\"javascript:void(0)\" onclick='searchActivityList(" + type + "," + subtype2 + "," + tag + ",\""+ activityName +"\"," + (pager.pageNumber + 1) + ")'>&raquo;</a></li>")
                                }
                                break;
                            }
                        }
                        $("#pageInfo").html("共" + pager.recordCount + "条记录，当前第" + pager.pageNumber + "页， 共" + pager.pageCount + "页");
                    } else {
                        // console.log("no data");
                        // alert("no data");
                    }
                }
            });
        }
    </script>
    <script>
        function validate() {
            return true;
        }
    </script>
</head>

<body>
    <div class="container-fluid">
        <form id="role_query_form" class="well form-inline">
            <select id="type" name="type" onchange="" class="span2">
                <option value="1">活跃活动(1)</option>
                <option value="2">每日充值(2)</option>
                <option value="3">每日登陆(3)</option>
                <option value="4">限购礼包(4)</option>
                <option value="5">天帝宝库(5)</option>
                <option value="6">累计充值(6)</option>
                <option value="7">累计消耗(7)</option>
                <option value="8">集物兑换(8)</option>
                <option value="9">团购活动(9)</option>
                <option value="10">招财猫(10)</option>
                <option value="11">首领狂欢(11)</option>
                <option value="12">庆典任务(12)</option>
                <option value="13">节日集字(13)</option>
                <option value="14">节日特惠(14)</option>
                <option value="15">连续累充(15)</option>
                <option value="16">限时商城(16)</option>
                <option value="17">节日礼包(17)</option>
                <option value="18">积分排名(18)</option>
                <option value="19">节日许愿(19)</option>
                <option value="20">FB分享(20)</option>
                <option value="21">连续累充2(21)</option>
                <option value="22">节日祝福(22)</option>
                <option value="23">掷骰子(23)</option>
                <option value="-1" selected>所有</option>
            </select>
            <select id="subType2" name="subtype2" onchange="" class="span2">

            </select>
            <select id="tag" name="tag" onchange="" class="span2">
                <option value="1">限时活动(1)</option>
                <option value="2">定制活动(2)</option>
                <option value="3">圣诞活动(3)</option>
                <option value="4">元旦活动(4)</option>
                <option value="5">春节活动(5)</option>
                <option value="6">情人节活动(6)</option>
                <option value="7">泼水节活动(7)</option>
                <option value="-1" selected>所有</option>
            </select>
            <label>活动名称</label>
            <input id="queryString" name="queryString" type="text" class="validate[required] forbidSubmit span2">
            <input type="button" value="${msg['jsp.role.search']}" class="btn btn-primary" onclick="doSearch($('#type').val(),$('#subType2').val(),$('#tag').val(),$('#queryString').val())">
        </form>

        <table class="table table-bordered table-striped" id="activity_list">
        </table>
        <div class="row-fluid" style="height: 28px">
            <div class="col-sm-8 col-md-8"></div>
            <div class="span5 col-sm-4 col-md-4" style="margin-left: 1200px">
                <input type="button" id="batchTest" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">
                <%--<input type="button" id="batchValid" value="批量验证" onclick="verifyEvent()" class="btn btn-primary">--%>
                <%--<input type="button" id="batchPublish" value="批量发布" onclick="publishEvent(3)" class="btn btn-primary">--%>
                <input type="button" id="batchDelete" value="批量删除" onclick="deleteActivity()" class="btn btn-primary">
                <%--                <input type="button" id="oneKeyDelete" value="一键删除过期活动" onclick="oneKeyDelete()" class="btn btn-primary">--%>
            </div>
            <div class="pagination pagination-large offset1 span4" style="margin: 0">
                <ul id="pager">
                </ul>
            </div>
            <div id="pageInfo" class="span3" style="text-align: right"></div>
        </div>
    </div>

    <jsp:include page="../../commonmodal.jsp"/>
    <div id="publishActivity" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3>发布活动</h3>
        </div>
        <div class="modal-body">
            <div class="row-fluid">
                <form id="publish_form" action="#" class="well span12">
                    <input id="operationType" type="hidden" name="operationType"/>
                    <input id="actIds" type="hidden" name="actIds"/>
                    <input id="cover" type="hidden" name="cover"/>
                    <select id="platform" name="platform" onchange="changePlatformEvent()" class="span4"></select>
                    <br/><br/>
                    <div id="servers" class="row-fluid"></div>
                </form>
            </div>
        </div>
        <div class="modal-footer">
            <input type="button" value="关闭" class="btn" data-dismiss="modal" aria-hidden="true"/>
            <input type="button" value="发布" onclick="publishActivity()" class="btn btn-primary"/>
        </div>
    </div>
</body>
</html>
