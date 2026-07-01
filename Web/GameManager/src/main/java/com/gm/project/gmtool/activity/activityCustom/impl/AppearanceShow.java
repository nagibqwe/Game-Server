package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppearanceShow extends Activity implements IActivityCustom {

    public AppearanceShow(){

    }

    public AppearanceShow(Activity activity){
        super(activity);
    }

    /**
     * 解析自定义参数
     * @param paramMap
     * @return
     * @throws ParseException
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) throws ParseException {
        String[] shows = paramMap.get("show");
        String[] toFunctions = paramMap.get("toFunction");

        if (shows.length != toFunctions.length) {
            throw new RuntimeException("===配置外观展示数据错误" );
        }

        List<Object> showData = new ArrayList<>();
        for (int i = 0; i < shows.length; i++) {
            String show= shows[i];
            String toFunction = toFunctions[i];

            HashMap<String, Object> data = new HashMap<>();
            data.put("toFunction", StringUtils.parseInt(toFunction,0));
            data.put("showList", ItemBean.split(show));
            showData.add(data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
//        resultMap.put("showData", showData);
        resultMap.put("client", JsonUtils.toJSONString(showData));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
