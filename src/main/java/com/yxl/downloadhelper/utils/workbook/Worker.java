package com.yxl.downloadhelper.utils.workbook;

import java.util.function.Consumer;
import java.util.function.Function;

public class Worker<D, R> extends Thread {
    private Assignment<D, R> assignment;
    private Consumer<Worker<D, R>> taskStartFunction;
    private Function<D, R> taskFunction;
    private Consumer<Worker<D, R>> taskEndFunction;
    private Consumer<Worker<D, R>> failFunction;
    private Consumer<Worker<D, R>> successFunction;
    private Consumer<Worker<D, R>> startFunction;
    private Consumer<Worker<D, R>> endFunction;
    private Task<D, R> currentTask;
    private boolean isWorking;

    Worker(Function<D, R> taskFunction) {
        this.taskFunction = taskFunction;
    }

    @Override
    public void run() {
        work();
    }

    public void work() {
        try {
            executeFunction(startFunction);
            while (true) {
                if (assignment == null) {
                    return;
                }
                Task<D, R> task = assignment.get();
                if (task == null) {
                    break;
                }
                work(task, false);
            }
            finishWork();
        } finally {
            executeFunction(endFunction);
        }
    }

    public void work(Task<D, R> task) {
        try {
            executeFunction(startFunction);
            work(task, true);
            finishWork();
        } finally {
            executeFunction(endFunction);
        }
    }

    private void work(Task<D, R> task, boolean endWorking) {
        currentTask = task;
        isWorking = true;
        try {
            executeFunction(taskStartFunction);
            R result = taskFunction.apply(task.getDetail());
            task.finishTask(result);
            executeFunction(successFunction);
        } catch (Exception e) {
            executeFunction(failFunction);
        } finally {
            executeFunction(taskEndFunction);
        }
        isWorking = endWorking;
    }

    private void finishWork() {
        isWorking = false;
        currentTask = null;
    }

    private void executeFunction(Consumer<Worker<D, R>> function) {
        if (function != null) {
            try {
                function.accept(this);
            } catch (Exception e) {
                e.printStackTrace();
                isWorking = false;
            }
        }
    }

    public Assignment<D, R> getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment<D, R> assignment) {
        this.assignment = assignment;
    }

    public void setTaskStartFunction(Consumer<Worker<D, R>> taskStartFunction) {
        this.taskStartFunction = taskStartFunction;
    }

    public void setTaskEndFunction(Consumer<Worker<D, R>> taskEndFunction) {
        this.taskEndFunction = taskEndFunction;
    }

    public void setFailFunction(Consumer<Worker<D, R>> failFunction) {
        this.failFunction = failFunction;
    }

    public void setStartFunction(Consumer<Worker<D, R>> startFunction) {
        this.startFunction = startFunction;
    }

    public void setEndFunction(Consumer<Worker<D, R>> endFunction) {
        this.endFunction = endFunction;
    }

    public void setSuccessFunction(Consumer<Worker<D, R>> successFunction) {
        this.successFunction = successFunction;
    }

    public void setTaskFunction(Function<D, R> taskFunction) {
        this.taskFunction = taskFunction;
    }

    public Task<D, R> getCurrentTask() {
        return currentTask;
    }

    public boolean isWorking() {
        return isWorking;
    }

}
