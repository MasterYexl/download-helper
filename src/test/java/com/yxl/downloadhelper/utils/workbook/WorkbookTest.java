package com.yxl.downloadhelper.utils.workbook;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class WorkbookTest {

    @Test
    public void testWorkbook() throws InterruptedException {
        Workbook<Integer, Integer> workbook = new Workbook<>();
        Integer[] numbers = new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        List<Integer> integerList = Arrays.asList(numbers);
        workbook.addTask(integerList);
        workbook.setWorkflow(num -> {
            try {
                System.out.println(Thread.currentThread().getName()+"工作中("+num+")...");
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return num + 1;
        });
//        workbook.setTaskStartEvent(worker -> {
//            System.out.println(worker.getName() + "开始任务");
//        });
//        workbook.setTaskEndEvent(worker -> {
//            System.out.println(worker.getName() + "完成任务");
//        });
//        workbook.setWorkerStartEvent(worker -> {
//            System.out.println(worker.getName() + "开始工作");
//        });
//        workbook.setWorkerEndEvent(worker -> {
//            System.out.println(worker.getName() + "结束工作");
//        });
//        workbook.setStartEvent(args -> {
//            System.out.println("开始工作");
//        });
//        workbook.setEndEvent(args -> {
//            System.out.println("结束工作");
//        });
        workbook.workStart();
        while (true) {
            System.out.println(workbook.getProgress());
            System.out.println(workbook);
            if (workbook.isFinish()) {
                System.out.println(workbook.getProgress());
                System.out.println(workbook);
                System.out.println(workbook.getResult());
                break;
            }
            Thread.sleep(10);
        }
    }

    @Test
    public void testURL() {
        String url = "https://www.52bqg.org/book_10049/5027452.html";
        System.out.println(url.replaceAll("^(.*//)?[^/]+", ""));
    }
}