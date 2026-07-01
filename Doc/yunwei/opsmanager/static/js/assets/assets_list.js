function get_assets_body() {
    var url = "/api/assets_list/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            if (response.length % 10 != 0) {
                var v = Math.ceil(response.length / 10)
            } else {
                var v = parseInt(response.length / 10)
            }
            f(v, 5, 1, response)
        }
    })
}

function f(total_pages, visible_pages, current_page, data) {
    var if_firstime = false;
    if (!visible_pages) {
        visible_pages = 5;
    }
    if (!current_page) {
        current_page = 1;
    }
    console.log(11111111)
    console.log(total_pages)
    $.jqPaginator('#pagination0', {
        totalPages: total_pages,
        visiblePages: visible_pages,
        currentPage: current_page,
        prev: '<li class="prev"><a href="javascript:;">上一页</a></li>',
        next: '<li class="next"><a href="javascript:;">下一页</a></li>',
        page: '<li class="page"><a href="javascript:;">{{page}}</a></li>',

        onPageChange: function (page, type) {
            if (if_firstime) {
                if_firstime = false;
            } else if (!if_firstime) {
                changepage(data, page);
            }
        }

    })
}

function changepage(response, page) {
    var assets_html = '';
    var checkbox_html_head = '<td id="choiceid" class="text-center">\n' +
        '                        <label>\n' +
        '                            <input type="checkbox" name="saltchoice" value="'
    var checkbox_html_end = '"></label></td>'
    var buttonHtml = '<div class="btn-group  btn-group-xs">' +
        '<button title="查看详情" type="button" data-toggle="modal"  data-target="#detailmodel" name="btn-database-link" onclick="asset_detail(this)"  value="' + "查看" + '" class="btn btn-default"  aria-label="Justify"><span class="glyphicon glyphicon glyphicon-zoom-in" aria-hidden="true"></span>' +
        '</button>' +
        '<button title="修改资产" type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
        '</button>' +
        '<button title="删除资产" type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" disabled onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
        '</button>' +
        '<button title="远程连接资产" type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-sshweb" onclick="websocket(this)" value="' + "SSHweb" + '" class="btn btn-default" aria-label="Justify"><span class="fa fa-desktop" aria-hidden="true"></span>' +
        '</button>' +
        '</div>'
    var start = 10 * (page - 1)
    var end = start + 9
    console.log(response)
    for (var i = start; i <= end; i++) {
        if (response[i] == undefined) {
            assets_html += ''
        } else {
            assets_html += '<tr>' + checkbox_html_head + +response[i]["id"] + checkbox_html_end + '<td class="text-center" id="pk">' + response[i]["id"] + '</td>' + '<td class="text-center">' + response[i]["assets_type"] + '</td>' + '<td class="text-center">' + response[i]["hostname"] + '</td>' + '<td class="text-center">' + response[i]["wip"] + '</td>' + '<td class="text-center">' + response[i]["lip"] + '</td>' + '<td class="text-center">' + response[i]["status"] + '</td>' + '<td class="text-center">' + response[i]["os"] + '</td>' + '<td class="text-center">' + response[i]["buy_time"] + '</td>' + '<td class="text-center">' + response[i]["project"] + '</td>' + '<td class="text-center">' + response[i]["machineroom"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
        }
    }
    ;
    $("#assets_body").html(assets_html)
}

function asset_detail(p) {
    var i = $(p).parent().parent().parent().children("#pk").html()
    $.ajax({
        dataType: "JSON",
        url: "/api/assets/" + i + "/",
        type: "GET",
        success: function (response) {
            console.log(response)
            $("#asset_detail").modal("show");
            asset_detail_html = '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">机房</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" id="update_machine" name="system"\n' +
                '                                                       placeholder="机房" class="input-xlarge" value=' + response["machine_room"]["name"] + '>' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">服务器类型</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" id="update_servertype" name="system"\n' +
                '                                                       class="input-xlarge" value=' + response["assets_type"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">初始化完成</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" id="is_init" name="system"\n' +
                '                                                       class="input-xlarge" value=' + response["is_init"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">供货商</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text" class="form-control" id="update_provider" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["provider"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">购买时间</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text" class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["buy_time"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">过期时间</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" id="update_expire_date" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["expire_date"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">登陆用户名</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text" class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["username"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">登陆密码</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="password"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["passwd"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">外网IP</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["wip"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">内网IP</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["lip"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">端口号</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["port"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">状态</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["status"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">CPU个数</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["cpu_count"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">内存</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text" class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["mem_total"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="row">\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">磁盘</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["disk_total"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                    <div class="col-md-6">\n' +
                '                                        <div class="form-group">\n' +
                '                                            <label class="control-label col-md-3 col-sm-3 col-xs-12">系统</label>\n' +
                '                                            <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                                <input type="text"  class="form-control" name="system"\n' +
                '                                                       placeholder="System" class="input-xlarge" value=' + response["system"] + '>\n' +
                '                                            </div>\n' +
                '                                        </div>\n' +
                '                                    </div>\n' +
                '                                </div>\n<br>' +
                '                                <div class="form-group">\n' +
                '                                    <label class="control-label col-md-3 col-sm-3 col-xs-12" >备注</label>\n' +
                '                                    <div class="col-md-6 col-sm-6 col-xs-12">\n' +
                '                                        <textarea type="text" class="form-control" id="asset_mark" name="asset_mark"\n' +
                '                                                  placeholder="备注" value="" class="input-xlarge" style="margin-top: 10px"></textarea>\n' +
                '                                    </div>\n' +
                '                                </div>'

            $("#modal_asset_detail").html(asset_detail_html);
        }
    })
}

$("#assets_add").on("click", function () {
    $("#asset_add").modal('show');
})

function d(p) {
    var i = p
    $.ajax({
        dataType: "JSON",
        url: "/api/assets/" + i + "/",
        type: "DELETE",
        success: function (data) {
            console.log("delete success")
        }
    })
}


$("#asset_update").on("click", function () {
    console.log("开始更新资产信息")
    var update_machine = $("#update_machine").val()
    var update_servertype = $("#update_servertype").val()
    var is_init = $("#is_init").val()
    var update_provider = $("#update_provider").val()
    var update_expire_date = $("#update_expire_date").val()




})


$("#assetsdelete").on("click", function () {
    var value_list = []
    $("[name=saltchoice]:checked").each(function () {
        value_list.push($(this).val())
    })
    console.log(value_list)
    for (var i = 0; i < value_list.length; i++) {
        d(value_list[i])
    }
    window.location.reload();
})

function machine_room_js() {
    $.ajax({
        dataType: "JSON",
        url: '/api/machine/',
        type: "GET",
        success: function (response) {
            // var binlogHtml = '<select class="form-control" name="asset_put_zone" id="asset_put_zone" required="required">'
            var selectHtml = '<option name="machine_room_select" selected="selected" value="">请选择一个机房</option>';
            for (var i = 0; i < response.length; i++) {
                selectHtml += '<option name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</option>'
            }
            ;
            binlogHtml = selectHtml + '</select>';
            $("#machine_room").html(binlogHtml)
            // var obj =document.getElementById("asset_put_zone").innerHTML = binlogHtml;
        }
    })
}

function asset_list() {

    $("#addAssets").on("submit", function () {    //表单提交时监听提交事件
        $(this).ajaxSubmit(options);    //当前表单执行异步提交，optons 是配置提交时、提交后的相关选项
        return false;   //  必须返回false，才能跳到想要的页面
    })
    //配置 options 选项
    var options = {
        url: "/api/assets/",       //提交地址：默认是form的action,如果申明,则会覆盖
        type: "post",           //默认是form的method（get or post），如果申明，则会覆盖
        success: successFun,    //提交成功后的回调函数，即成功后可以定页面跳到哪里
        dataType: "json",       //接受服务端返回的类型
        clearForm: true,        //成功提交后，清除所有表单元素的值
        resetForm: true,        //成功提交后，重置所有表单元素的值
        timeout: 3000           //设置请求时间，超过该时间后，自动退出请求，单位(毫秒)
    }

    //设置提交成功后返回的页面
    function successFun(data, status) {
        alert("添加成功")
        // $("#showForm").html(data.msg);      //提交表单后从后台接收到的返回来的数据，我保存到了msg里
        // // $("#showForm").html("或者这里可以直接写想要显示的内容")
    }
}

function submit_asset() {
    var select_ip = $("input[name='wip']").val();
    console.log(111112222)
    console.log(select_ip)
    $.ajax({
        type: "POST",
        dataType: "JSON",
        url: "/api/assets/",
        data: $("#assetadd").serialize(),
        success: function (result) {
            if (result["id"]) {
                alert("添加成功");
                $("#asset_add").modal("hide")
                window.location.reload();
            }
            ;
        },
        error: function () {
            alert("提交数据异常");
        }
    })
}


function delete_pk(p) {
    var i = $(p).parent().parent().parent().children("#pk").html()
    $.ajax({
        dataType: "JSON",
        url: "/api/assets/" + i + "/",
        type: "DELETE",
        success: function (data) {
            alert("删除成功")
            window.location.reload();
        }
    })
}

function select_assets(p) {
    // var project = $(p).parent().parent().children("#div_project").children("#project_select").find("option:selected").val();
    var app = $(p).parent().parent().children("#div_app").children("#app_select").find("option:selected").val();
    var group = $(p).parent().parent().children("#div_group").children("#group_select").find("option:selected").val();
    // var region = $(p).parent().parent().children("#div_region").children("#region_select").find("option:selected").val();
    var status = $(p).parent().parent().children("#div_type").children("#type_select").find("option:selected").val();
    var ip = $(p).parent().parent().children("#div_ip").children("#ip").val();
    $.ajax({
        dataType: "JSON",
        url: "/assets/assets_select/",
        type: "POST",
        data: {"app": app, "group": group, "status": status, "ip": ip},
        success: function (response) {
            var assets_html = '';
            var buttonHtml = '<div class="btn-group  btn-group-xs">' +
                '<button type="button" data-toggle="modal"  data-target="#detailmodel" name="btn-database-link" onclick="asset_detail(this)" value="' + "查看" + '" class="btn btn-default"  aria-label="Justify"><span class="glyphicon glyphicon glyphicon-zoom-in" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
                '</button>' +
                '</div>'
            for (i = 0; i < response.length; i++) {
                assets_html += '<tr></tr><td class="text-center">' + 1 + '</td>' + '<td class="text-center" hidden>' + response[i]["id"] + '</td>' + '<td class="text-center" id="pk">' + response[i]["id"] + '</td>' + '<td class="text-center">' + response[i]["assets_type"] + '</td>' + '<td class="text-center">' + response[i]["wip"] + '</td>' + '<td class="text-center">' + response[i]["system"] + '</td>' + '<td class="text-center">' + response[i]["kernel"] + '</td>' + '<td class="text-center">' + response[i]["cpu_count"] + '</td>' + '<td class="text-center">' + response[i]["mem_total"] + '</td>' + '<td class="text-center">' + response[i]["disk_total"] + '</td>' + '<td class="text-center">' + response[i]["machine_room"]["name"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
            }
            ;
            $("#assets_body").html(assets_html)
            if (response.length % 10 != 0) {
                var v = Math.ceil(response.length / 10)
            } else {
                var v = parseInt(response.length / 10)
            }
            f(v, 5, 1, response)
        }
    })
}

function selectcheck() {
    var allcheck = document.getElementById("allcheck");
    var is_check = allcheck.checked;
    var check_boxs = document.getElementsByName("saltchoice")
    console.log(check_boxs.length)
    if (is_check) {
        for (var i = 0; i < check_boxs.length; i++) {
            check_boxs[i].checked = true;
        }
        document.getElementById("keychoice").innerHTML = "取消全选";
    } else {
        for (var i = 0; i < check_boxs.length; i++) {
            check_boxs[i].checked = false;
        }
        document.getElementById("keychoice").innerHTML = "全选"
    }
}


function test() {
    $.ajax({
        dataType: "JSON",
        url: "/api/databases/",
        type: "GET",
        success: function (database_data) {
            $.ajax({
                dataType: "JSON",
                url: "/api/assets/",
                type: "GET",
                success: function (assets_data) {
                }
            })
        }
    })
}

function get_term_size() {
    var init_width = 9;
    var init_height = 17;

    var windows_width = $(window).width();
    var windows_height = $(window).height();

    return {
        cols: Math.floor(windows_width / init_width),
        rows: Math.floor(windows_height / init_height),
    }
}

function websocket(p) {
    var i = $(p).parent().parent().parent().children("#pk").html()
    console.log(i)
    openTerminal(i, 'tzj 127.0.0.1')
}

function openTerminal(obj, parentsName) {
    var aid = obj
    $("#myWebsshModalLabel").html('<p class="text-blank"><code><i class="fa fa fa-terminal"></i></code>' + parentsName.replace(" ", "_") + '' + obj["text"] + '</p>')
    $("#websshConnect").val(aid)
    $('#webssh_tt').empty()
    $('.bs-example-modal-webssh-info').modal({backdrop: "static", show: true});

}

var webssh = false
function make_terminal(element, size, ws_url) {
    var term = new Terminal({
        cols: size.cols,
        rows: size.rows,
        screenKeys: true,
        useStyle: true,
        cursorBlink: true,  // Blink the terminal's cursor
    });
    if (webssh) {
        return;
    }
    webssh = true;
    term.open(element, false);
    term.write('正在连接...')
/*             term.fit(); */
    var ws = new WebSocket(ws_url);
    ws.onopen = function (event) {
        term.resize(term.cols, term.rows);
/*                 ws.send(JSON.stringify(["id", id,term.cols, term.rows]));  */
        term.on('data', function (data) {
            <!--console.log(data);-->
             ws.send(data);
        });

        term.on('title', function (title) {
            document.title = title;
        });
        ws.onmessage = function (event) {
        	term.write(event.data);
        };
    };
    ws.onerror = function (e) {
    	term.write('\r\n连接失败')
    	ws = false
    };
/*    ws.onclose = function () {
        term.destroy();
    }; */
    return {socket: ws, term: term};
}

function makeRandomId() {
  var text = "";
  var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
  for (var i = 0; i < 8; i++)
    text += possible.charAt(Math.floor(Math.random() * possible.length));
  return text;
}

$("#add_machine").on("click", function () {
    console.log("添加机房信息")
    $("#machine_add").modal("show");
})

function submit_machine() {
    console.log(111112222)
    $.ajax({
        type: "POST",
        dataType: "JSON",
        url: "/api/machine/",
        data: $("#machineadd").serialize(),
        success: function (result) {
            if (result["id"]) {
                alert("添加成功");
                $("#asset_add").modal("hide")
                window.location.reload();
            }
            ;
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            console.log(XMLHttpRequest.status)
            console.log(XMLHttpRequest.readyState)
            console.log(textStatus)
            console.log(errorThrown)
            alert("提交数据异常");
        }
    })
}

$(document).ready(function () {
    // prject_selectd();
    // app_selectd();
    // region_selectd();
    get_assets_body();
    machine_room_js();
    // project_js();
    asset_list();
    // test();

    $("#websshConnect").on("click", function () {
        var vIds = $(this).val();
        var randromChat = makeRandomId()
        var ws_scheme = window.location.protocol == "https:" ? "wss" : "ws";
        var ws_path = ws_scheme + '://' + window.location.host + '/ws/websocket/webssh/' + vIds + '/' + randromChat + '/';
//        console.log(randromChat)
        websocket = make_terminal(document.getElementById('webssh_tt'), {rows: 30, cols: 140}, ws_path);
        $(this).attr("disabled", true);
        /*             $(".xterm-screen").css("width", "800px").css("height", "510px"); */
    });

    $('.bs-example-modal-webssh-info').on('hidden.bs.modal', function () {
        try {
            websocket["socket"].close()
        } catch (err) {
            console.log(err)
        } finally {
            webssh = false
        }
        $("#websshConnect").attr("disabled", false);
    });

})