
function groupChanged(groupKey) {
    //遍历groupServer,获取指定的serverID
    var serverIDList = null;

    var allSrverIDList = [];
    var allValue=[];

    if (groupKey == 0){
        for (var i = 0; i < gs.length; i++){
            var idList = gs[i].serverIDList;
            for (var j = 0; j < idList.length; j++){
                var json = {};
                json.value = idList[j].value;
                json.text = idList[j].text;
                allValue.push(json.value);
                allSrverIDList.push(json);
                // console.log(json);
            }
        }
        //添加所有服务器
        var json = {};
//                json.value = 0;
        json.value = JSON.stringify(allValue);
        json.text = '所有服务器';
        allSrverIDList.push(json);

        $("#select_server").combobox("loadData", allSrverIDList);
        $('#select_server').combobox('select', allSrverIDList[0].value);

//                console.log(allSrverIDList);
        return;
    }

    for (var i = 0; i < gs.length; i++) {
        console.log(gs[i].groupKey==(groupKey));
        if (gs[i].groupKey==(groupKey)) {
            serverIDList = gs[i].serverIDList;
            for (var j = 0; j < serverIDList.length; j++){
                var json = {};
                json.value = serverIDList[j].value;
                json.text = serverIDList[j].text;
                allValue.push(json.value);
                allSrverIDList.push(json);
                // console.log(json);
            }

            break;
        }
    }

    //添加所有服务器
    var json = {};
//            json.value = 0;
    json.value = JSON.stringify(allValue);
    json.text = '所有服务器';
    allSrverIDList.push(json);

//            console.log(serverIDList);

    if (null == serverIDList) {
        $('#select_server').combobox("clear");
        return;
    }
    // console.log("allSrverIDList:"+allSrverIDList);
    //将获取的serverID列表加载到server下拉框中(combobox."loadData"),并默认选中第一个元素(combobox.select)
    $("#select_server").combobox("loadData", allSrverIDList);
    $('#select_server').combobox("clear");
    $('#select_server').combobox('select', allSrverIDList[0].value);
}