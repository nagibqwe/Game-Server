package com.game.yed.scripts;

import com.game.yed.YedAI;

public interface YedMethodScript {
    /**
     * 执行AI
     * @param ai
     * @param methodName
     * @param args
     * @return
     */
     boolean execAiMethod(YedAI ai, String methodName, Object[] args);
}
