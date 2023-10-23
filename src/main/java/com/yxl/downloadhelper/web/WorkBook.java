package com.yxl.downloadhelper.web;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 通过WorkBook来管理多线程资源同步，同步资源通过get获取，使用write来标记完成该资源的处理，并可以存放处理完成后的一些数据
 * @param <T> 输入类型
 * @param <R> 输出类型
 */
public class WorkBook<T,R> {
    private final LinkedList<T> list = new LinkedList<>();
    private final List<R> result = new LinkedList<>();
    private Map<T, Integer> isFalse = new HashMap<>();
    private int surplus;//剩余任务
    private int tot;//总共任务
    private int process=0;//提交的反馈
    private int code;//结束代码
    private int worker = 0;
    private boolean finish = false;
    private static int number = 0;
    private int falseNumber = 0;
    String name;

    public WorkBook(){
        List<T> list = new LinkedList<>();
        init(list,"WorkBook-"+(number++));
    }

    public WorkBook(List<T> list){
        init(list,"WorkBook-"+(number++));
    }

    public WorkBook(List<T> list, String name){
        init(list, name);
    }

    private void init(List<T> list, String name){
        if (list==null||name==null){
            surplus=0;
            tot=0;
        }
        else {
            code = (int) (Math.random() * 100);
            this.name = name;
            this.list.addAll(list);
            surplus = list.size();
            tot = surplus;
        }
    }

    public void add(T newWork){
        add(newWork, false);
    }

    public void add(T work, boolean first){
        if (first) list.addFirst(work);
        else list.add(work);
        tot++;
        surplus++;
    }

    public synchronized T get(){
        if (list.size()>0) {
            surplus--;
            return list.remove(0);
        }
        else return null;
    }

    /**
     * 向结果表写入数据, 可指定位置，超出反馈表后自动填充null
     * @param value
     */
    public synchronized void put(R value){
        put(value, result.size());
    }
    public synchronized void put(R value, int index){
        if (index >= result.size()){
            for (int i = result.size(); i <= index; i++){
                result.add(null);
            }
        }
        result.set(index, value);
        process++;
    }
    public void fallDone(T work){
        if (isFalse.containsKey(work)){
            int falseTimes = isFalse.get(work);
            if (falseTimes < 3) isFalse.put(work, falseTimes+1);
            else {
                falseNumber++;
                process++;
                surplus--;
            }
        }
        else isFalse.put(work, 1);
        add(work, true);
    }

    /**
     * 只允许一个线程处理最后的整理工作
     * @return 结束代码
     */
    public synchronized int getFinalWork(){
        if (process==tot) {
            surplus--;
            process--;
            return code;
        }
        else return -1;
    }
    public synchronized void workFinish(int code){
        worker--;
        if (this.code == code) {
            process++;
            finish = true;
        }
        if (worker==0) finish=true;
    }

    public boolean isFinish(){
        return finish;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getProgress(){
        return process;
    }
    public int getTot(){
        return tot;
    }
    public List<R> getResult(){
        return result;
    }
    public boolean isWorking(){
        if (surplus<=0) return true;
        else return surplus!=tot;
    }
    public void workStart(){
        worker++;
    }

    @Override
    public String toString() {
        return "当前状况:"+(tot==surplus? "未开始":isFinish()? "已完成":"进行中")+"\n总任务数:"+tot+"\n当前完成:"+process+"\n失败数量:"+falseNumber+"\n启动的线程数:"+worker;
    }
}