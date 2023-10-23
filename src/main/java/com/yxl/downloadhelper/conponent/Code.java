package com.yxl.downloadhelper.conponent;

import com.yxl.downloadhelper.tool.CBY;
import org.springframework.stereotype.Component;

@Component
public class Code {
    CBY cby;
    Code(){
        cby = new CBY();
        cby.useSimpleKey();
    }
    public String code(String org){
        return cby.codeVS(org);
    }
    public String decode(String code){
        return cby.decodeVS(code);
    }
}
