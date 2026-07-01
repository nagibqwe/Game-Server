function get_project() {
    var url = "/api/project/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            var selectHtml = '<select class="form-control" id="project_select">';
            selectHtml += '<option selected="selected" value="None">请选择一个项目</option>'
            for (var i = 0; i < response.length; i++) {
                // selectHtml += '<tr> <name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</tr>'
                selectHtml += '<option ' + ' value="' + response[i]["id"] + '"' + '>' + response[i]["project_name"] + '</option>'
            }
            ;
            var orderhtml = selectHtml += '</select>'
            $("#project_select").html(orderhtml)
        }
    })
}


function get_region() {
    var url = "/api/region/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            var selectHtml = '<select class="form-control" id="region_select">';
            selectHtml += '<option selected="selected" value="None">请选择一个渠道</option>'
            for (var i = 0; i < response.length; i++) {
                // selectHtml += '<tr> <name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</tr>'
                selectHtml += '<option ' + ' value="' + response[i]["id"] + '"' + '>' + response[i]["region_name"] + '</option>'
            }
            ;
            var orderhtml = selectHtml += '</select>'
            $("#region_select").html(orderhtml)
        }
    })
}

function reset_type() {
    var types = ["gameserver", "loginserver", "fightserver", "publicserver", "GM"]
    var selectHtml = '<select class="form-control" id="type_select">';
    selectHtml += '<option selected="selected" value="None">请选择一个类型</option>'
    for (var i = 0; i < types.length; i++) {
        selectHtml += '<option ' + ' value="' + types[i] + '"' + '>' + types[i] + '</option>'
    }
    ;
    var orderhtml = selectHtml += '</select>'
    $("#type_select").html(orderhtml)
}


function select_assets(p) {
    var project = $("#project_select").select().val()
    var region = $("#region_select").select().val()
    var servertype = $("#type_select").select().val()
    if (project == "None") {
        alert("请选择一个项目")
        return
    }
    if (region == "None") {
        alert('请选择一个渠道')
        return
    }
    if (servertype == "None") {
        alert("请选择一个服务器类型")
        return
    }
    $("#servercheck").css("display", "block")
    $("#package").css("display", "block")
    $("#descripe").css("display", "block")
    $("#startupdate").css("display", "block")

    $.ajax({
        dataType: "JSON",
        url: '/api/select_server',
        type: "POST",
        data: {"project": project, "region": region, "servertype": servertype},
        success(response) {
            console.log(response)
            $("#servercheckbox").css("display", "block")
            var body_html = '<div class="col-sm-12 col-md-12 col-xs-12 col-lg-12 " id="servercheckboxbody">'
            for (var key in response) {
                body_html += '<label class="col-md-1"><input name="selectd_server" type="checkbox" value="' + key + '" />&nbsp&nbsp' + response[key] + '</label>'
            }
            var order_html = body_html + '</div>'
            $("#servercheckboxbody").html(order_html)
        }
    })
}

function set_select(p) {
    console.log(p)

}


function WebSocketTest() {
    alert(1)
    if ("WebSocket" in window) {
        alert("您的浏览器支持 WebSocket!");

        // 打开一个 web socket
        ws = new WebSocket("ws://127.0.0.1:9000/assets/test/");

        ws.onopen = function () {
            // Web Socket 已连接上，使用 send() 方法发送数据
            ws.send("发送数据");
            alert("数据发送中...");
        };

        ws.onmessage = function (evt) {
            var received_msg = evt.data;
            alert("数据已接收...");
            alert("数据:" + received_msg)
        };

        ws.onclose = function () {
            // 关闭 websocket
            alert("连接已关闭...");
        };
    } else {
        // 浏览器不支持 WebSocket
        alert("您的浏览器不支持 WebSocket!");
    }
}

function start_update() {
    console.log("开始热更新服务器")
    var server_names = [];
    var tmp = ''
    var servet_type =$("#type_select option:selected").val()
    // var server_names = {}
    $('input[name="selectd_server"]:checked').each(function () {
        tmp += $(this).val()
        tmp += ','
        // server_names.push($(this).val())
        // server_names[$(this).val()] = $(this).val()
    })
    console.log(server_names)
    var package_name = $('input[name="packagename"]').val()
    console.log(package_name)
    var descr_text = $('#descripetext').val()
    console.log(descr_text)

    $.ajax({
        dataType: JSON,
        url: '/api/excuse_cmd',
        type: "POST",
        async: false,
        traditional :true,
        data: {
            "server_names":tmp,
            "package_name":package_name,
            "descr_text":descr_text,
            "servet_type":servet_type,
        },
        success(response) {
            console.log("执行命令")
            console.log(response)
        },
        error(XMLHttpRequest, textStatus, errorThrown) {
            // console.log("执行命令失败")
            // console.log(XMLHttpRequest.responseText )
            // console.log(textStatus )
            $("#myModal").modal("show")
            for (var i in XMLHttpRequest['results']) {
                console.log(i)
            }

        }
    })
    // $("#myModal").modal("show")
}

$(document).ready(function () {
    console.log("I'm ready")
    get_project();
    get_region();
    reset_type();
})