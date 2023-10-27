package com.yxl.downloadhelper.utils.workbook;

import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 通过WorkBook来管理多线程资源同步，同步资源通过get获取，使用write来标记完成该资源的处理，并可以存放处理完成后的一些数据
 *
 * @param <D> 输入类型
 * @param <R> 输出类型
 */
@Slf4j
public class Workbook<D, R> {
    protected final List<Task<D, R>> taskList = new LinkedList<>();
    protected final List<Task<D, R>> failTasks = new LinkedList<>();
    protected final List<Task<D, R>> successTasks = new LinkedList<>();
    protected final Assignment<D, R> assignment = new Assignment<>();
    protected final List<Worker<D, R>> workers = new LinkedList<>();
    protected Function<D, R> workflow;
    protected Consumer<Worker<D, R>> taskStartEvent;
    protected Consumer<Worker<D, R>> taskEndEvent;
    protected Consumer<Worker<D, R>> taskFailEvent;
    protected Consumer<Worker<D, R>> taskSuccessEvent;
    protected Consumer<Worker<D, R>> workerStartEvent;
    protected Consumer<Worker<D, R>> workerEndEvent;
    protected Consumer<Workbook<D, R>> endEvent;
    protected Consumer<Workbook<D, R>> startEvent;
    protected boolean isFinish = false;
    protected int workerNumber = 3;
    protected int taskMaxRetryTimes = 3;
    protected static int sequence = 0;
    protected int workingTasksNumber = 0;
    protected int doneTasksNumber = 0;
    protected int workingWorkerNumber = 0;
    String name;

    public Workbook() {
        List<D> list = new LinkedList<>();
        init(list, "WorkBook-" + (sequence++));
    }

    public Workbook(List<D> taskList) {
        init(taskList, "WorkBook-" + (sequence++));
    }

    public Workbook(List<D> taskList, String name) {
        init(taskList, name);
    }

    protected Function<D, R> workflow() {
        return workflow;
    }

    protected Consumer<Workbook<D, R>> onStart() {
        return startEvent;
    }

    protected Consumer<Workbook<D, R>> onEnd() {
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

    protected Consumer<Worker<D, R>> onWorkerStart() {
        return workerStartEvent;
    }

    protected Consumer<Worker<D, R>> onWorkerEnd() {
        return workerEndEvent;
    }

    protected void init(List<D> list, String name) {
        this.name = name;
        this.taskList.clear();
        List<Task<D, R>> tasks = this.assignment.addTasks(list);
        taskList.addAll(tasks);
    }

    public void workStart() {
        executeConsumer(onStart(), this);
        int currentWorkerNumber = workers.size();
        for (int i = 0; i < workerNumber - currentWorkerNumber; i++) {
            workers.add(createWorker());
        }
        int minWorkerNumber = Math.min(workerNumber, assignment.getSize());
        for (int i = 0; i < minWorkerNumber; i++) {
            workers.get(i).start();
        }
    }

    public Worker<D, R> createWorker() {
        Worker<D, R> worker = new Worker<>(workflow());
        worker.setAssignment(assignment);
        worker.setTaskSuccessEvent(this::taskSuccess);
        worker.setTaskFailEvent(this::taskFail);
        worker.setTaskStartEvent(this::startTask);
        worker.setTaskEndEvent(this::endTask);
        worker.setStartEvent(this::workerStart);
        worker.setEndEvent(this::workerEnd);
        return worker;
    }

    protected void taskFail(Worker<D, R> worker) {
        taskFail(worker, onTaskFail());
    }

    protected void taskFail(Worker<D, R> failWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, failWorker);
        Task<D, R> currentTask = failWorker.getCurrentTask();
        int retryTimes = currentTask.getRetryTimes();
        if (retryTimes < taskMaxRetryTimes) {
            currentTask.setRetryTimes(retryTimes + 1);
            assignment.addTask(currentTask);
        }
        failTasks.add(currentTask);
    }

    protected void taskSuccess(Worker<D, R> worker) {
        taskSuccess(worker, onTaskSuccess());
    }

    protected void taskSuccess(Worker<D, R> successWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, successWorker);
        successTasks.add(successWorker.getCurrentTask());
    }

    protected void workerStart(Worker<D, R> worker) {
        workerStart(worker, onWorkerStart());
    }

    protected void workerStart(Worker<D, R> startWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, startWorker);
        workingWorkerNumber++;
    }

    protected void workerEnd(Worker<D, R> worker) {
        workerEnd(worker, onWorkerEnd());
    }

    protected void workerEnd(Worker<D, R> endWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, endWorker);
        workingWorkerNumber--;
        if (workingWorkerNumber == 0) {
            if (assignment.getSize() > 0) {
                this.workStart();
            } else {
                isFinish = true;
                executeConsumer(onEnd(), this);
            }
        }
    }

    protected void endTask(Worker<D, R> worker) {
        endTask(worker, onTaskEnd());
    }

    protected void endTask(Worker<D, R> endWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, endWorker);
        workingTasksNumber--;
        doneTasksNumber++;
    }

    protected void startTask(Worker<D, R> worker) {
        startTask(worker, onTaskStart());
    }

    protected void startTask(Worker<D, R> startWorker, Consumer<Worker<D, R>> extractConsumer) {
        executeConsumer(extractConsumer, startWorker);
        workingTasksNumber++;
    }

    private <T> void executeConsumer(Consumer<T> consumer, T arg) {
        if (consumer != null) {
            try {
                consumer.accept(arg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addTasks(List<Task<D, R>> tasks) {
        taskList.addAll(assignment.addTask(tasks));
    }

    public void addTask(Task<D, R> task) {
        taskList.add(assignment.addTask(task));
    }

    public void addTask(D task) {
        taskList.add(assignment.addTask(task, null));
    }

    public void addTask(D task, Function<R, Boolean> checkFunction) {
        taskList.add(assignment.addTask(task, null));
    }

    public void addTask(List<D> tasks) {
        taskList.addAll(assignment.addTasks(tasks, null));
    }

    public void addTask(List<D> tasks, Function<R, Boolean> checkFunction) {
        taskList.addAll(assignment.addTasks(tasks, checkFunction));
    }

    public void addWorker(Worker<D, R> worker) {
        worker.setStartEvent(onWorkerStart());
    }

    public boolean isFinish() {
        return isFinish;
    }

    public int getTaskMaxRetryTimes() {
        return taskMaxRetryTimes;
    }

    public void setTaskMaxRetryTimes(int taskMaxRetryTimes) {
        this.taskMaxRetryTimes = taskMaxRetryTimes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWorkerNumber(int workerNumber) {
        this.workerNumber = workerNumber;
    }

    public List<R> getResult() {
        List<R> resultList = new LinkedList<>();
        taskList.forEach(task -> resultList.add(task.getResult()));
        return resultList;
    }

    public List<Task<D, R>> getFailTasks() {
        return failTasks;
    }

    public List<Task<D, R>> getSuccessTasks() {
        return successTasks;
    }

    public void setTaskStartEvent(Consumer<Worker<D, R>> taskStartEvent) {
        this.taskStartEvent = taskStartEvent;
    }

    public void setWorkflow(Function<D, R> workflow) {
        this.workflow = workflow;
    }

    public void setTaskEndEvent(Consumer<Worker<D, R>> taskEndEvent) {
        this.taskEndEvent = taskEndEvent;
    }

    public void setTaskFailEvent(Consumer<Worker<D, R>> taskFailEvent) {
        this.taskFailEvent = taskFailEvent;
    }

    public void setTaskSuccessEvent(Consumer<Worker<D, R>> taskSuccessEvent) {
        this.taskSuccessEvent = taskSuccessEvent;
    }

    public void setWorkerStartEvent(Consumer<Worker<D, R>> workerStartEvent) {
        this.workerStartEvent = workerStartEvent;
    }

    public void setWorkerEndEvent(Consumer<Worker<D, R>> workerEndEvent) {
        this.workerEndEvent = workerEndEvent;
    }

    public void setEndEvent(Consumer<Workbook<D, R>> endEvent) {
        this.endEvent = endEvent;
    }

    public void setStartEvent(Consumer<Workbook<D, R>> startEvent) {
        this.startEvent = startEvent;
    }

    @Override
    public String toString() {
        return "当前状况:" + (taskList.size() == assignment.getSize() ? "未开始" : isFinish() ? "已完成" : "进行中") + "\n总任务数:" + taskList.size() + "\n当前完成:" + doneTasksNumber + "\n失败数量:" + failTasks.size() + "\n启动的线程数:" + workingWorkerNumber;
    }

    public double getProgress() {
        return (double) doneTasksNumber / taskList.size();
    }

    public int getTot() {
        return taskList.size();
    }

    public boolean isWorking() {
        return workingWorkerNumber != 0;
    }
}