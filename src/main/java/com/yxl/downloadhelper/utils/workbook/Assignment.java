package com.yxl.downloadhelper.utils.workbook;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Assignment<D, R> {

    protected final List<Task<D, R>> tasks = new LinkedList<>();

    public int getSize() {
        return tasks.size();
    }

    public Task<D, R> addTask(D detail, Function<R, Boolean> checkFunction) {
        Task<D, R> task = new Task<>();
        task.setDetail(detail);
        task.setCheckFunction(checkFunction);
        tasks.add(task);
        return task;
    }

    public List<Task<D, R>> addTasks(List<D> details) {
        return addTasks(details, null);
    }

    public List<Task<D, R>> addTasks(List<D> details, Function<R, Boolean> checkFunction) {
        List<Task<D, R>> result = new LinkedList<>();
        details.forEach(task -> result.add(addTask(task, checkFunction)));
        return result;
    }

    public Task<D, R> addTask(Task<D, R> task) {
        tasks.add(task);
        return task;
    }

    public List<Task<D, R>> addTask(List<Task<D, R>> tasks) {
        this.tasks.addAll(tasks);
        return tasks;
    }

    public synchronized Task<D, R> get(){
        if (tasks.size()>0) {
            return tasks.remove(0);
        }
        else return null;
    }
}
