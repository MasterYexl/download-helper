package com.yxl.downloadhelper.controller;

import com.yxl.downloadhelper.conponent.SettingsIO;
import com.yxl.downloadhelper.model.Book;
import com.yxl.downloadhelper.model.Chapter;
import com.yxl.downloadhelper.tool.Tool;
import com.yxl.downloadhelper.web.BookSearch;
import com.yxl.downloadhelper.web.Downloader;
import com.yxl.downloadhelper.web.TIO;
import com.yxl.downloadhelper.web.WorkBook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/download")
public class DownloadController {


    @GetMapping("/{id}")
    public String getDown(@PathVariable("id") int id, @RequestParam("downer") int downer, HttpSession session, Model model){
        List<WorkBook<Chapter, String>> workBooks = (List<WorkBook<Chapter, String>>) session.getAttribute("workBooks");
        if (workBooks==null) workBooks = new LinkedList<>();
        if (workBooks.size()>id) {
            if (workBooks.get(id).isWorking()) return "download";
        }
        Book book = Tool.getBookFromID(id, session);
        if (book==null) return "download";
        try {
            WorkBook<Chapter, String> workBook = down(book, downer);
            if (workBooks.size()<id){
                for (int i=workBooks.size();i<id;i++){
                    workBooks.add(new WorkBook<>(null,null));
                }
            }
            workBooks.add(workBook);
            model.addAttribute("id", id);
            session.setAttribute("workBooks", workBooks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "download";
    }

    @GetMapping("/progress")
    @ResponseBody
    public String getProgress(@RequestParam int id, HttpSession session){
        List<WorkBook<Chapter, String>> list = (List<WorkBook<Chapter, String>>) session.getAttribute("workBooks");
        WorkBook<Chapter, String> workBook = list.get(id);
        return "{\"progress\":" + workBook.getProgress() + ",\"tot\":" + workBook.getTot() + ",\"finish\":" + workBook.isFinish() + "}";
    }

    @GetMapping("/open")
    @ResponseBody
    public String open(@RequestParam int id, HttpSession session){
        List<WorkBook<Chapter, String>> list = (List<WorkBook<Chapter, String>>) session.getAttribute("workBooks");
        WorkBook<Chapter, String> workBook = list.get(id);
        try {
            System.out.println("地址："+SettingsIO.get("book-path")+File.separator+workBook.getName()+".txt");
            Runtime.getRuntime().exec("explorer /e,/select, "+ SettingsIO.get("book-path")+File.separator+workBook.getName()+".txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    @ResponseBody
    @GetMapping("/copy")
    public String copy(@RequestParam int id, HttpSession session, HttpServletResponse response){
        List<WorkBook<Chapter, String>> list = (List<WorkBook<Chapter, String>>) session.getAttribute("workBooks");
        WorkBook<Chapter, String> workBook = list.get(id);
        File file = new File(SettingsIO.get("book-path")+File.separator+workBook.getName()+".txt");
        byte[] buffer = new byte[1024];
        FileInputStream is = null;
        BufferedInputStream bis = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(workBook.getName()+".txt","UTF-8"));
            is = new FileInputStream(file);
            bis = new BufferedInputStream(is);
            ServletOutputStream outputStream = response.getOutputStream();
            for (int i = bis.read(buffer); i!=-1; i = bis.read(buffer)){
                outputStream.write(buffer,0,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (bis!=null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String deDuplicate(String name, String filePath, String fileType){
        for (int i=0;true;i++){
            File file = new File(filePath+File.separator+name+(i==0? "."+fileType:" ("+i+")."+fileType));
            if (!file.exists()) return name+(i==0? "":" ("+i+")");
        }
    }

    @Test
    public void deTest(){
        System.out.println(deDuplicate("鲁x孙漂流记", "D:\\YXL\\WorkSpace\\java\\springboot\\download-helper\\books", "txt"));
    }

    public WorkBook<Chapter, String> down(Book book, int downer) throws InterruptedException {
        List<Chapter> chapters = book.getChapters();
        String name = book.getName();
        for (int i=0;true;i++){
            File file = new File(SettingsIO.get("book-path")+File.separator+name+(i==0? ".txt":i+".txt"));
            if (!file.exists()) {
                book.setName(name+(i==0? "":"("+i+")"));
                break;
            }
        }
        book.setName(deDuplicate(book.getName(), SettingsIO.get("book-path"),"txt"));
        WorkBook<Chapter, String> workBook = new WorkBook<>(chapters, book.getName());
        List<Downloader> list = new LinkedList<>();
        for (int i = 0; i < downer; i++) {
            list.add(new Downloader(workBook));
        }
        for (int i = 0; i < downer; i++) {
            Downloader downloader = list.get(i);
            downloader.start();
        }
        return workBook;
    }

    @Test
    public void openTest(){
        try {
            Runtime.getRuntime().exec("explorer /e,/select,"+"D:\\YXL\\WorkSpace\\java\\springboot\\download-helper\\src\\main\\java\\com\\yxl\\downloadhelper\\web\\asa.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
//    @Test
//    public void downTest(){
//        BookSearch bookSearch = new BookSearch();
//        try {
//            Book book = bookSearch.parseCatalog("https://www.uubiqu.com/read/151939/");
//            WorkBook<Chapter, String> down = down(book, 3);
//            while (true){
//                int now = down.getProgress();
////                System.out.println((now-down.getTot())+"/"+down.getTot());
//                if (now==0) break;
//                Thread.sleep(2000);
//            }
//            List<String> result = down.getResult();
//            for (String s: result){
//                TIO.write(s, "斗罗大陆.txt");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//}
//shshshfpa=a15ab290-6769-560e-7506-4224d7a2eaba-1593940851; shshshfpb=r0sNLPDyICb3ZIVAXdqmn2w==; pinId=ETK_8JSBIkKVqVw0apLAGg; 3AB9D23F7A4B3C9B=2CQ54JA46C3TDZSHRYCGTODNNKLTBIDT7UQZHKDKP4XBG5BUV433IH2GT6XANCXRF7476IAF5BJVF53OZUX4VLYWGY; __jdc=122270672; mba_muid=16186448302381403244641; jcap_dvzw_fp=zYz2HFVYFfwqxfbCbQxtsHxi6Dq4HBL-lk4RMQOhG-Hdg1hkL6vbej01vgHIoR_f5hTY1w==; TrackerID=hm9aT1sVbTFyuhqcBFom_8mA1Ebg5RbO1U8Bl8LlomVYpumtH4foCnalSPWewcqeVlYIzBrDxEjOhA_Brv3VEnYtsH-TBpbE8QzausdsqVWfLztZ8Mth8O3SR7LIFsOv-NQW2lp1vMjuJb1MSYov4Q; pt_key=AAJgeo98ADAhNgAMsytpD4wDKoC5rKuv-8TUngOM6AfaLuyYMVt8tRajVH3cfOfweUmaGIFu0tQ; pt_pin=1093151953_m; pt_token=lyropby3; pwdt_id=1093151953_m; sfstoken=tk01mbc4a1c5aa8sMXgyKzN4MnpFnf46lTVVoJEwrA0OpV0YqVJdp8ou3R3FRTIopCz8LJSHX3nZlCPMXYSO6RzUpyEY; whwswswws=; wxa_level=1; retina=1; cid=9; jxsid=16186448620961066055; webp=1; autoOpenApp_downCloseDate_auto=1618644864914_10800000; visitkey=22388145123737566; __jda=122270672.16186448302381403244641.1618644830.1618644830.1618645885.2; __jdv=122270672|baidu|-|organic|not set|1618645885108; PPRD_P=UUID.16186448302381403244641; sc_width=375; wqmnx1=MDEyNjM3NTouby9lbjg3bzVoUG4zaSBBYjUoIGVlMU0xUzZkLjJGN2YyMUVPSCg=; __wga=1618645907459.1618645900725.1618645900725.1618645900725.2.1; jxsid_s_t=1618645907614; jxsid_s_u=https://home.m.jd.com/myJd/newhome.action; shshshfp=db50327a155bf7b80d843e4451deb362
//shshshfpa=a15ab290-6769-560e-7506-4224d7a2eaba-1593940851; shshshfpb=r0sNLPDyICb3ZIVAXdqmn2w%3D%3D; pinId=ETK_8JSBIkKVqVw0apLAGg; 3AB9D23F7A4B3C9B=2CQ54JA46C3TDZSHRYCGTODNNKLTBIDT7UQZHKDKP4XBG5BUV433IH2GT6XANCXRF7476IAF5BJVF53OZUX4VLYWGY; __jdv=122270672%7Cdirect%7C-%7Cnone%7C-%7C1618644830239; __jdc=122270672; __jda=122270672.16186448302381403244641.1618644830.1618644830.1618644830.1; mba_muid=16186448302381403244641; shshshfp=856d89074f542212a7acc7bf33e1229a; shshshsID=fffbf9f5ab740542f95df9a86e100e73_1_1618644831386; jcap_dvzw_fp=zYz2HFVYFfwqxfbCbQxtsHxi6Dq4HBL-lk4RMQOhG-Hdg1hkL6vbej01vgHIoR_f5hTY1w==; pt_token=lyropby3; pwdt_id=1093151953_m; sfstoken=tk01mbc4a1c5aa8sMXgyKzN4MnpFnf46lTVVoJEwrA0OpV0YqVJdp8ou3R3FRTIopCz8LJSHX3nZlCPMXYSO6RzUpyEY; wxa_level=1; retina=1; cid=9; wqmnx1=MDEyNjM5NHNjaShDZV9hQWkxLGNpMzFmMTA4c24yUUQqSA%3D%3D; jxsid=16186448620961066055; webp=1; __jdb=122270672.2.16186448302381403244641|1.1618644830; mba_sid=16186448302415059512951663694.2; autoOpenApp_downCloseDate_auto=1618644864914_10800000; visitkey=22388145123737566; __jd_ref_cls=MHome_DGardenFloorExpo