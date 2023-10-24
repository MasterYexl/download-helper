package com.yxl.downloadhelper.utils.workbook;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 通过WorkBook来管理多线程资源同步，同步资源通过get获取，使用write来标记完成该资源的处理，并可以存放处理完成后的一些数据
 *
 * @param <D> 输入类型
 * @param <R> 输出类型
 */
public class Workbook<D, R> {
    protected final List<Task<D, R>> taskList = new LinkedList<>();
    protected final List<Task<D, R>> failTasks = new LinkedList<>();
    protected final List<Task<D, R>> successTasks = new LinkedList<>();
    protected final Assignment<D, R> assignment = new Assignment<>();
    protected final List<Worker<D, R>> workers = new LinkedList<>();
    protected Consumer<Worker<D, R>> taskStartFunction;
    protected Function<D, R> taskFunction;
    protected Consumer<Worker<D, R>> taskEndFunction;
    protected Consumer<Worker<D, R>> workerFailFunction;
    protected Consumer<Worker<D, R>> workerSuccessFunction;
    protected Consumer<Worker<D, R>> workerStartFunction;
    protected Consumer<Worker<D, R>> workerEndFunction;
    protected Consumer<Workbook<D, R>> endFunction;
    protected Consumer<Workbook<D, R>> startFunction;
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

    protected void init(List<D> list, String name) {
        this.name = name;
        this.taskList.clear();
        List<Task<D, R>> tasks = this.assignment.addTasks(list);
        taskList.addAll(tasks);
    }

    public void workStart() {
        try {
            if (startFunction != null) {
                startFunction.accept(this);
            }
            int currentWorkerNumber = workers.size();
            for (int i = 0; i < workerNumber - currentWorkerNumber; i++) {
                workers.add(createWorker());
            }
            int minWorkerNumber = Math.min(workerNumber, assignment.getSize());
            for (int i = 0; i < minWorkerNumber; i++) {
                workers.get(i).start();
            }
        } finally {
            if (endFunction != null) {
                endFunction.accept(this);
            }
        }
        
    }

    public Worker<D, R> createWorker() {
        Worker<D, R> worker = new Worker<>(taskFunction);
        worker.setAssignment(assignment);
        worker.setSuccessFunction(successWorker -> {
            successTasks.add(successWorker.getCurrentTask());
            if (workerSuccessFunction != null) {
                workerSuccessFunction.accept(successWorker);
            }
        });
        worker.setFailFunction(failWorker -> {
            Task<D, R> currentTask = failWorker.getCurrentTask();
            int retryTimes = currentTask.getRetryTimes();
            if (retryTimes < taskMaxRetryTimes) {
                currentTask.setRetryTimes(retryTimes + 1);
                assignment.addTask(currentTask);
            }
            failTasks.add(currentTask);
            if (workerFailFunction != null) {
                workerFailFunction.accept(failWorker);
            }
        });
        worker.setTaskStartFunction(startWorker -> {
            startTask();
            if (taskStartFunction != null) {
                taskStartFunction.accept(startWorker);
            }
        });
        worker.setTaskEndFunction(endWorker -> {
            finishTask();
            if (taskEndFunction != null) {
                taskEndFunction.accept(endWorker);
            }
        });
        worker.setStartFunction(startWorker -> {
            workerStart();
            if (workerStartFunction != null) {
                workerStartFunction.accept(startWorker);
            }
        });
        worker.setEndFunction(endWorker -> {
            workerEnd();
            if (workerStartFunction != null) {
                workerStartFunction.accept(endWorker);
            }
        });
        return worker;
    }

    protected void workerStart() {
        workingWorkerNumber++;
    }
    
    protected void workerEnd() {
        workingWorkerNumber--;
        if (workingWorkerNumber == 0) {
            if (assignment.getSize() > 0) {
                this.workStart();
            } else {
                isFinish = true;
            }
        }
    }
    
    protected void finishTask() {
        workingTasksNumber--;
        doneTasksNumber++;
    }

    protected void startTask() {
        workingTasksNumber++;
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

    public void setTaskStartFunction(Consumer<Worker<D, R>> taskStartFunction) {
        this.taskStartFunction = taskStartFunction;
    }

    public void setTaskFunction(Function<D, R> taskFunction) {
        this.taskFunction = taskFunction;
    }

    public void setTaskEndFunction(Consumer<Worker<D, R>> taskEndFunction) {
        this.taskEndFunction = taskEndFunction;
    }

    public void setWorkerFailFunction(Consumer<Worker<D, R>> workerFailFunction) {
        this.workerFailFunction = workerFailFunction;
    }

    public void setWorkerSuccessFunction(Consumer<Worker<D, R>> workerSuccessFunction) {
        this.workerSuccessFunction = workerSuccessFunction;
    }

    public void setWorkerStartFunction(Consumer<Worker<D, R>> workerStartFunction) {
        this.workerStartFunction = workerStartFunction;
    }

    public void setWorkerEndFunction(Consumer<Worker<D, R>> workerEndFunction) {
        this.workerEndFunction = workerEndFunction;
    }

    public void setEndFunction(Consumer<Workbook<D, R>> endFunction) {
        this.endFunction = endFunction;
    }

    public void setStartFunction(Consumer<Workbook<D, R>> startFunction) {
        this.startFunction = startFunction;
    }

    @Override
    public String toString() {
        return "当前状况:" + (taskList.size() == assignment.getSize() ? "未开始" : isFinish() ? "已完成" : "进行中") + "\n总任务数:" + taskList.size() + "\n当前完成:" + doneTasksNumber + "\n失败数量:" + failTasks.size() + "\n启动的线程数:" + workingWorkerNumber;
    }

    public double getProgress() {
        return (double)doneTasksNumber / taskList.size();
    }

    public int getTot() {
        return taskList.size();
    }

    public boolean isWorking() {
        return workingWorkerNumber != 0;
    }
}