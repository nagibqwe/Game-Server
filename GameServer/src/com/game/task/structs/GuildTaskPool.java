package com.game.task.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * 公会（仙盟）任务池对象
 */
public class GuildTaskPool {
    /**
     * 刷新次数
     */
    private int refreshCount;
    /**
     * 任务池ID列表
     */
    private List<Integer> taskIds = new ArrayList<>();

    public int getRefreshCount() {
        return refreshCount;
    }

    public void setRefreshCount(int refreshCount) {
        this.refreshCount = refreshCount;
    }

    public List<Integer> getTaskIds() {
        return taskIds;
    }

    public void setTaskIds(List<Integer> taskIds) {
        this.taskIds = taskIds;
    }
}
