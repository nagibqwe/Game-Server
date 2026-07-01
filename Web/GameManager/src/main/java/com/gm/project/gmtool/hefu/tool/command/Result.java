package com.gm.project.gmtool.hefu.tool.command;

import java.util.List;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/14 15:56
 */
public class Result {

    private boolean success;

    private List<String> result;

    private List<String> error;

    public Result(boolean success, List<String> result, List<String> error) {
        this.success = success;
        this.result = result;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<String> getResult() {
        return result;
    }

    public void setResult(List<String> result) {
        this.result = result;
    }

    public List<String> getError() {
        return error;
    }

    public void setError(List<String> error) {
        this.error = error;
    }
}
