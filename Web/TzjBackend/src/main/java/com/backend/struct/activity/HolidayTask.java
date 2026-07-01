package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 庆典任务
 */
public class HolidayTask extends Activity implements IActivityCustom {

    public HolidayTask() {
    }

    public HolidayTask(Activity activity) {
        super(activity);
    }

    /**
     * 解析自定义参数
     *
     * @param paramMap
     * @return
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) {
        //任务类型
        String[] taskTypes = paramMap.get("taskTypes");
        //进度
        String[] reachs = paramMap.get("reachs");
        //奖励
        String[] rewardData = paramMap.get("rewards");
        //客户端展示货币
        String coinId = paramMap.get("coinId")[0];

        if (taskTypes.length != reachs.length || taskTypes.length != rewardData.length) {
            throw new RuntimeException("===数据错误");
        }

        HashMap<Integer, Object> taskMap = new HashMap<>();
        for (int i = 0; i < taskTypes.length; i++) {
            int id = i+1;
            int taskType = Integer.parseInt(taskTypes[i]);
            int reach = Integer.parseInt(reachs[i]);
            List<ItemBean> rewards = ItemBean.split(rewardData[i]);

            HashMap<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("taskType", taskType);
            data.put("reach", reach);
            data.put("item", rewards);
            if (taskMap.containsKey(id)) {
                throw new RuntimeException("===重复的任务id" + id);
            }
            taskMap.put(id, data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("tasks", taskMap);

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("tasks",taskMap.values());
        clientMap.put("coinId", Integer.parseInt(coinId));

        resultMap.put("client", JsonUtils.toJSONString(clientMap));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
