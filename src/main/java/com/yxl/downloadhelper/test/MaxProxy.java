package com.yxl.downloadhelper.test;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MaxProxy implements ProxyTest{
    Logger logger = Logger.getLogger(this.getClass().getName());
    ProxyTest proxyTest;
    List list = new ArrayList();
    public MaxProxy(ProxyTest proxyTest){
        this.proxyTest = proxyTest;
    }
    @Override
    public long max(long x, long y) {
        log("x="+x+", y="+y);
        long rs = proxyTest.max(x,y);
        log(rs+"");
        return rs;
    }
    public void log(String msg){
        logger.log(Level.INFO, msg);
    }


}
