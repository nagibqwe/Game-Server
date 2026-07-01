package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.StringUtils;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class LoginShow extends Activity implements IActivityCustom {

    public LoginShow(){

    }

    public LoginShow(Activity activity){
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
        String xuanJianPicStr = paramMap.get("xuanJianPic")[0];
        String tianYingPicStr = paramMap.get("tianYingPic")[0];
        String diZangPicStr = paramMap.get("diZangPic")[0];
        String luoChaPicStr = paramMap.get("luoChaPic")[0];
        String toFunctionStr = paramMap.get("toFunction")[0];

        HashMap<String, Object> loginShowData = new HashMap<>();
        loginShowData.put("0",xuanJianPicStr);
        loginShowData.put("1",tianYingPicStr);
        loginShowData.put("2",diZangPicStr);
        loginShowData.put("3",luoChaPicStr);
        loginShowData.put("toFunction", StringUtils.parseInt(toFunctionStr,0));

        HashMap<String, Object> resultMap = new HashMap<>();
//        resultMap.put("loginShowData", loginShowData);
        resultMap.put("client", JsonUtils.toJSONString(loginShowData));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
