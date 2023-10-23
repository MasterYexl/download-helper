package com.yxl.downloadhelper.test;

public class Test implements ProxyTest{

    @Override
    public long max(long x, long y) {
        return Math.max(x, y);
    }
}
