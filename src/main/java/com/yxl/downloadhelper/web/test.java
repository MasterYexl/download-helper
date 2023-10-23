package com.yxl.downloadhelper.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yxl.downloadhelper.model.Book;
import com.yxl.downloadhelper.model.Chapter;
import com.yxl.downloadhelper.test.MaxProxy;
import com.yxl.downloadhelper.test.ProxyTest;
import com.yxl.downloadhelper.tool.CBY;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class test {
    public static void main(String[] args) {
        String jsonStr = "{'cname': '高一三班', "
                + "'students':[{'no':'001','name':'张三'},{'no': '002', 'name': '李四'}]}";
        JSONObject jsonObject = JSON.parseObject(jsonStr);

        JSONArray jsonArrayStudent = jsonObject.getJSONArray("students");
        for (Object item : jsonArrayStudent) {
            JSONObject jsonItem = JSON.parseObject(item.toString());
            System.out.println(jsonItem.get("name"));
        }
    }
    @Test
    public void bookTest(){
        List<String > list = new LinkedList<>();
        list.add("123");
        list.add("456");
        list.add("789");
        WorkBook<String, String> workBook = new WorkBook<>(list,"test");
        int i = 0;
        while (true) {
            String s = workBook.get();
            if (s==null) break;
            workBook.put(i+"",i);

        }
        List<String> result = workBook.getResult();
        for (String rs : result){
            System.out.println(rs);
        }
    }
    @Test
    public void listTest(){
        List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        WorkBook<Integer, Integer> workBook = new WorkBook<>(list);
        workBook.put(3,2);
        workBook.put(4,0);
        workBook.put(5,1);
        workBook.put(100,10);
        WorkBook<Integer, String> workBook1 = new WorkBook<>(list);
        System.out.println(workBook.getName()+" "+workBook1.getName());
        for (Integer list1 : workBook.getResult()){
            System.out.println(workBook.getProgress() +" "+list1);
        }
    }

    @Test
    public void mapTest(){
        Map<String , String > map = new HashMap<>();
        map.put("12","34");
        map.put("5 6","78");
        String s = map.toString();
        s = s.replaceAll(", ","=\n").replace("{","").replace("}","=");
        System.out.println(s);
        System.out.println(map.get("5 6"));
    }

    @Test
    public void serializableTest(){
        Book book = new Book("my");
        Chapter chapter = new Chapter();
        chapter.setNumber(1);
        chapter.setContent("123");
        chapter.setTitle("第一章");
        Chapter chapter2 = new Chapter();
        chapter2.setNumber(2);
        chapter2.setContent("123");
        chapter2.setTitle("第二章");
        book.addChapters(chapter);
        book.addChapters(chapter2);
        FileOutputStream fileOutputStream;
        ObjectOutputStream outputStream = null;
        try {
            fileOutputStream = new FileOutputStream("./book.obj");
            outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(book);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (outputStream!=null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    @Test
    public void objReadTest(){
        FileInputStream inputStream = null;
        ObjectInputStream ois = null;
        Book book = null;
        try {
            inputStream = new FileInputStream("D:\\YXL\\WorkSpace\\java\\springboot\\download-helper\\books\\bin\\https[,,www'23txt'com,files,article,html,0,88,.ser");
            ois = new ObjectInputStream(inputStream);
            book = (Book) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }  finally {
            if (ois!=null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println(book);
        System.out.println(book.getChapter(0));
        System.out.println(book.getChapter(1));
        System.out.println(book.getReadingChapter()+" "+book.getReadingPos());
    }

    @Test
    public void codeTest(){
        CBY cby = new CBY();
        cby.useSimpleKey();
        String org = "阿斯顿房管局欧体进入1igk【pskdgf243895u59042rjewfdlkxcvnm.,.;'.][;'./[];'.aesdfhgjl';.qaw]r[estd';.'=-9`12345;y't.,";
        int suc = 0, fai = 0;
        for (int i = 0; i < 1; i++) {
            String code = cby.code(org);
            if (cby.decode(code).equals(org)) suc++;
            else fai++;
        }
        System.out.println("成功: "+suc+" 失败: "+fai);
    }

    @Test
    public void pt(){
        ProxyTest proxyTest = new MaxProxy(new com.yxl.downloadhelper.test.Test());
        proxyTest.max(123,455);
    }
}
