function getMap(){//初始化map，给map对象增加方法，使map像个Map
    var map=new Object();
    map.put=function(key,value){
        map[key]=value;
    }
    map.get=function(key){
        return map[key];
    }
    return map;
}