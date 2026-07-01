function get_project() {
    var url = "/api/project/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            var selectHtml = '';
            var buttonHtml = '<div class="btn-group  btn-group-xs">' +
                '<button type="button" data-toggle="modal"  data-target="#detailmodel" name="btn-database-link" onclick="asset_detail(this)" value="' + "查看" + '" class="btn btn-default"  aria-label="Justify"><span class="glyphicon glyphicon glyphicon-zoom-in" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
                '</button>' +
                '</div>'
            for (var i = 0; i < response.length; i++) {
                // selectHtml += '<tr> <name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</tr>'
                selectHtml += '<tr> <td >' + response[i]["id"] + '</td>' + '<td >' + response[i]["project_name"] + '</td>' + '<td >' + response[i]["project_short_name"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
            }
            ;
            binlogHtml = selectHtml + '';
            $("#projecttabletbody").html(binlogHtml)
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
            console.log(response)
            var selectHtml = '';
            var buttonHtml = '<div class="btn-group  btn-group-xs">' +
                '<button type="button" data-toggle="modal"  data-target="#detailmodel" name="btn-database-link" onclick="asset_detail(this)" value="' + "查看" + '" class="btn btn-default"  aria-label="Justify"><span class="glyphicon glyphicon glyphicon-zoom-in" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
                '</button>' +
                '</div>'
            for (var i = 0; i < response.length; i++) {
                // selectHtml += '<tr> <name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</tr>'
                selectHtml += '<tr> <td >' + response[i]["id"] + '</td>' + '<td >' + response[i]["region_name"] + '</td>' + '<td >' + response[i]["region_short_name"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
            }
            ;
            binlogHtml = selectHtml + '';
            $("#regiontablebody").html(binlogHtml)
        }
    })
}

function get_status() {
    var url = "/api/status/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            var selectHtml = '';
            var buttonHtml = '<div class="btn-group  btn-group-xs">' +
                '<button type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
                '</button>' +
                '</div>'
            for (var i = 0; i < response.length; i++) {
                // selectHtml += '<tr> <name="machine_room" value="' + response[i]["id"] + '">' + response[i]["machine_name"] + '</tr>'
                selectHtml += '<tr> <td>' + response[i]["id"] + '</td>' + '<td>' + response[i]["status_name"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
            }
            ;
            binlogHtml = selectHtml + '';
            $("#statustabletbody").html(binlogHtml)
        }
    })
}

function get_machine() {
    var url = "/api/machine/"
    $.ajax({
        dataType: "JSON",
        url: url,
        type: "GET",
        success: function (response) {
            var selectHtml = '';
            var buttonHtml = '<div class="btn-group  btn-group-xs">' +
                '<button type="button" data-toggle="modal"  data-target="#detailmodel" name="btn-database-link" onclick="asset_detail(this)" value="' + "查看" + '" class="btn btn-default"  aria-label="Justify"><span class="glyphicon glyphicon glyphicon-zoom-in" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#updatemodel" name="btn-database-edit" onclick="asset_detail(this)" value="' + "修改" + '" class="btn btn-default"  aria-label="Justify"><span class="fa fa-edit" aria-hidden="true"></span>' +
                '</button>' +
                '<button type="button" data-toggle="modal" data-target="#deletemodel" name="btn-database-delete" onclick="delete_pk(this)" value="' + "删除" + '" class="btn btn-default" aria-label="Justify"><span class="glyphicon glyphicon-trash" aria-hidden="true"></span>' +
                '</button>' +
                '</div>'
            for (var i = 0; i < response.length; i++) {
                selectHtml += '<tr> <td>' + response[i]["id"] + '</td>' + '<td>' + response[i]["machine_name"] + '</td>' + '<td>' + response[i]["machine_address"] + '</td>' + '<td>' + response[i]["machine_user"] + '</td>' + '<td>' + response[i]["machine_mail"] + '</td>' + '<td class="text-center">' + buttonHtml + '</td>' + '</tr>'
            }
            ;
            binlogHtml = selectHtml + '';
            $("#machinetabletbody").html(binlogHtml)
        }
    })
}


$(document).ready(function () {
    console.log("I'ready")
    get_project();
    get_machine();
    get_status();
    get_region();
})