package com.yxl.downloadhelper.utils.workbook;

import java.util.function.Consumer;
import java.util.function.Function;

public class Worker<D, R> extends Thread {
    protected Assignment<D, R> assignment;
    protected Consumer<Worker<D, R>> taskStartEvent;
    protected Function<D, R> workflow;
    protected Consumer<Worker<D, R>> taskEndEvent;
    protected Consumer<Worker<D, R>> taskFailEvent;
    protected Consumer<Worker<D, R>> taskSuccessEvent;
    protected Consumer<Worker<D, R>> startEvent;
    protected Consumer<Worker<D, R>> endEvent;
    protected Task<D, R> currentTask;
    protected boolean isWorking;

    Worker(Function<D, R> workflow) {
        this.workflow = workflow;
    }
    Worker() {
    }

    protected Function<D, R> workflow() {
        return workflow;
    }

    protected Consumer<Worker<D, R>> onStart() {
        return startEvent;
    }

    protected Consumer<Worker<D, R>> onEnd() {
        return endEvent;
    }

    protected Consumer<Worker<D, R>> onTaskSuccess() {
        return taskSuccessEvent;
    }

    protected Consumer<Worker<D, R>> onTaskFail() {
        return taskFailEvent;
    }

    protected Consumer<Worker<D, R>> onTaskStart() {
        return taskStartEvent;
    }

    protected Consumer<Worker<D, R>> onTaskEnd() {
        return taskEndEvent;
    }

    @Override
    public void run() {
        work();
    }

    public void work() {
        try {
            executeConsumer(startEvent);
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
            executeConsumer(onEnd());
        }
    }

    public void work(Task<D, R> task) {
        try {
            executeConsumer(onStart());
            work(task, true);
            finishWork();
        } finally {
            executeConsumer(onEnd());
        }
    }

    protected void work(Task<D, R> task, boolean endWorking) {
        currentTask = task;
        isWorking = true;
        try {
            executeConsumer(onTaskStart());
            if (task != null) {
                R result = workflow().apply(task.getDetail());
                task.finishTask(result);
            }
            executeConsumer(onTaskSuccess());
        } catch (Exception e) {
            executeConsumer(onTaskFail());
        } finally {
            executeConsumer(onTaskEnd());
        }
        isWorking = endWorking;
    }

    protected void finishWork() {
        isWorking = false;
        currentTask = null;
    }

    protected void executeConsumer(Consumer<Worker<D, R>> consumer) {
        if (consumer != null) {
            try {
                consumer.accept(this);
            } catch (Exception e) {
                e.printStackTrace();
                isWorking = false;
            }
        }
    }

    public Assignment<D, R> getAssignment() {
        return assignment;
    }

    public Consumer<Worker<D, R>> getTaskStartEvent() {
        return taskStartEvent;
    }

    public Function<D, R> getWorkflow() {
        return workflow;
    }

    public Consumer<Worker<D, R>> getTaskEndEvent() {
        return taskEndEvent;
    }

    public Consumer<Worker<D, R>> getTaskFailEvent() {
        return taskFailEvent;
    }

    public Consumer<Worker<D, R>> getTaskSuccessEvent() {
        return taskSuccessEvent;
    }

    public Consumer<Worker<D, R>> getStartEvent() {
        return startEvent;
    }

    public Consumer<Worker<D, R>> getEndEvent() {
        return endEvent;
    }

    public void setAssignment(Assignment<D, R> assignment) {
        this.assignment = assignment;
    }

    public void setTaskStartEvent(Consumer<Worker<D, R>> taskStartEvent) {
        this.taskStartEvent = taskStartEvent;
    }

    public void setTaskEndEvent(Consumer<Worker<D, R>> taskEndEvent) {
        this.taskEndEvent = taskEndEvent;
    }

    public void setTaskFailEvent(Consumer<Worker<D, R>> taskFailEvent) {
        this.taskFailEvent = taskFailEvent;
    }

    public void setStartEvent(Consumer<Worker<D, R>> startEvent) {
        this.startEvent = startEvent;
    }

    public void setEndEvent(Consumer<Worker<D, R>> endEvent) {
        this.endEvent = endEvent;
    }

    public void setTaskSuccessEvent(Consumer<Worker<D, R>> taskSuccessEvent) {
        this.taskSuccessEvent = taskSuccessEvent;
    }

    public void setWorkflow(Function<D, R> workflow) {
        this.workflow = workflow;
    }

    public Task<D, R> getCurrentTask() {
        return currentTask;
    }

    public boolean isWorking() {
        return isWorking;
    }

}
