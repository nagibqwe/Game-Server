function checkInput(name) {
    var prefix = ctx + "gmtool/server";
    var serverId = $("input[name='serverId']").val();
    $.ajax({
        url : prefix + "/checkInput",
        type :'POST',
        dataType : 'json',
        data:{
            "serverId":serverId,
        },
        async : true,
        success : function(data){
            if (name == 'serverId'){
                if (data.code > 0){
                    $("#checkServerId").html("<font color='red'>" + data.msg + "</font>");
                }else {
                    $("#checkServerId").html("<font color='#90ee90'>" + data.msg + "</font>");
                }
            }
        }
    });
}