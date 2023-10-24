package com.yxl.downloadhelper.utils.workbook;

import java.util.function.Function;

public class Task<D, R> {
    private D detail;
    private R result;
    private int retryTimes = 0;
    private boolean isFinish = false;
    private Function<R, Boolean> checkFunction;

    public D getDetail() {
        return detail;
    }

    public void setDetail(D detail) {
        this.detail = detail;
    }

    public R getResult() {
        return result;
    }

    public void setResult(R result) {
        this.result = result;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public Function<R, Boolean> getCheckFunction() {
        return checkFunction;
    }

    public void setCheckFunction(Function<R, Boolean> checkFunction) {
        this.checkFunction = checkFunction;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public void finishTask(R result) {
        this.result = result;
        if (checkFunction != null) {
            isFinish = checkFunction.apply(result);
        } else {
            isFinish = true;
        }
    }
}
