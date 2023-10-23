package com.yxl.downloadhelper.tool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 当前版本：2.4
 * 更新内容：简易密钥
 */
public class CBY {
    private char[] loc = new char[]{'0', '1', '2', '3', '4', 'T', 'U', '5', 'P', 'Q', 'R', 'S', '6', '7', '8', '\"', ':', '\'', '.', '/', ',', '9', '[', ']', '-', '_', 'L', 't', 'u', 'v', 'w', 'M', 'N', '|', '{', '}', 'h', 'i', 'j', 'k', '!', 'O', 'A', 'B', 'd', 'e', 'f', 'g', 'l', 'C', 'D', 'E', 'F', 'G', '&', 'a', 'b', 'c', 'm', 'n', 'o', 'p', 'q', 'r', 's', 'x', 'y', 'z', '*', '(', ')', 'H', 'I', ' ', 'J', 'K', 'V', 'W', 'X', 'Y', 'Z', '+', '=', '\\', '?', '>', '<', '@', '#', '$', '%', '^', '`', '~', '，', '。', '？', '’', '【', '】'};
    public String key = "umk)|ed】1.&VNy%cW`IJ_=@]/TwP :vB7~'K？^?Cs{ZS+2Q>O9#nR(t$E，H4Arh\\z【3!*Xi’p,FU<gfaxqo-5j。LG\"8[YDbl60}Mrt`jq3xTD$LewiYHfUco%#】O-G2nQ>bay+(5=m/l_RBZXN{FvM?9[I]~@*< 0J\\|k？【.d7AS8sWC，!)V&EK,。'}\"z^hPp1’4g:6u";
    private List<String> prvkey = new ArrayList<>();
    private String orgkey = key;
    private String completeKey = key;
    private String judgeKey = this.code("~`#3");
    private String ascSignH = "~";
    private String ascSignT = "~";
    private boolean custom = false;
    String VALID_OF_YEAR = "year";
    String VALID_OF_MONTH = "month";
    String VALID_OF_DAY = "day";
    String VALID_OF_HOUR = "hour";

    public String createKey() {
        StringBuilder anskey = new StringBuilder();
        for (int j = 0; j < 2; j++) {
            char[] tmp = new char[loc.length];
            for (int i = 0; i < loc.length; i++) {
                tmp[i] = loc[i];
            }
            for (int i = 0; i < 10000; i++) {
                int a = (int) Math.floor(Math.random() * loc.length);
                int b = (int) Math.floor(Math.random() * loc.length);
                char tmpc = tmp[a];
                tmp[a] = tmp[b];
                tmp[b] = tmpc;
            }
            for (char i : tmp) anskey.append(i);
        }
        return anskey.toString();
    }


    public boolean loadKey(String key) {
        if (!checkKey(key)) return false;
        prvkey.add(this.key);
        this.key = key;
        judgeKey = code("~`#3");
        completeKey=key;
        return true;
    }

    private String c2e(String org) {
        char[] co = org.toCharArray();
        String org2 = "";
        for (char c : co) {
            if (!key.contains(c + "")) {
                org2 += ascSignH + (int) c+ascSignT;
            } else org2 += c;
        }
        return org2;
    }

    private String checkC(String dco) {
        Pattern p = Pattern.compile(ascSignH+"(\\d+)"+ascSignT);
        Matcher m = p.matcher(dco);
        while (m.find()){
            dco = dco.replace(m.group(0), (char)Integer.parseInt(m.group(1))+"");
        }
        return dco;
    }
    //更强的随机密码加密
    public String codeVS(String org){
        orgkey=key;
        if (prvkey.size()>16) prvkey = new ArrayList<>();
        StringBuilder miniPSWD = new StringBuilder();
        char[] words = key.substring(0,key.length()/2).toCharArray();
        for (int i=0;i<4;i++){
            miniPSWD.append(words[(int) Math.floor(Math.random() * words.length)]);
        }
        doShuffle(miniPSWD.toString());
        org = codeV(org);
        setOrgkey();
        return miniPSWD+org;
    }

    //密码时间随机数
    public String codePT(String org, String value, String pswd){
        org = codeT(org, value);
        return code(org, pswd);
    }
    //密码随机数
    public String codePV(String org, String pswd){
        org = codeVS(org);
        return code(org, pswd);
    }
    //时间
    public String codeT(String org, String value){
        String time = getDate(value);
        org = code(org, time);
        return codeVS("T~@"+value+"T~@"+org);
    }
    //此方法会生成随机数加密
    public String codeV(String org){
        int rdm = (int) Math.floor(Math.random()*10+8);
        StringBuilder CRC = new StringBuilder();
        char[] words = key.substring(key.length()/2).toCharArray();
        for (int i=0;i<rdm;i++){
            CRC.append(words[(int) Math.floor(Math.random() * words.length)]);
        }
        if (CRC.toString().contains(judgeKey)) codeV(org);
        return CRC+judgeKey+code(org,CRC.toString());
    }
    //重写规则加密
    public String code(String org, String crc){
        doShuffle(crc);
        String nc = code(org);
        loadKey(prvkey.get(prvkey.size()-1));
        return nc;
    }
    //原始核心加密
    public String code(String org) {
        org = c2e(org);
        while (org.length()%2!=0){
            org+=" ";
            org=c2e(org);
        }
        return deciphering(org.toCharArray());
    }

    public String decode(String org) {
        while (org.length()%2!=0){
            org+=" ";
            org=c2e(org);
        }
        return checkC(deciphering(org.toCharArray()));
    }

    public String decode(String org, String crc){
        org = checkC(code(org,crc));
        if (org.substring(0,1).equals(" ")) org = " "+org.trim();
        else org = org.trim();
        return org;
    }

    public String decodeV(String org){
        if (!org.contains(judgeKey)) return code(org);
        String key = judgeKey.replaceAll("\\\\", "\\\\\\\\");
        key = key.replaceAll("\\?", "\\\\?");
        key = key.replaceAll("\\^", "\\\\^");
        key = key.replaceAll("\\$", "\\\\"+ Matcher.quoteReplacement("$"));
        key = key.replaceAll("\\*", "\\\\*");
        key = key.replaceAll("\\+", "\\\\+");
        key = key.replaceAll("\\(", "\\\\(");
        key = key.replaceAll("\\)", "\\\\)");
        key = key.replaceAll("\\[", "\\\\[");
        key = key.replaceAll("\\{", "\\\\{");
        key = key.replaceAll("\\|", "\\\\|");
        String[] dv = org.split(key) ;
        String tmp = decode(dv[1], dv[0]);

        Pattern pattern = Pattern.compile("T~@(.+)T~@");
        Matcher matcher = pattern.matcher(tmp);
        if (matcher.find()){
            String timeAns = tmp.replace(matcher.group(0), "");
            String time = getDate(matcher.group(1));
            return decode(timeAns, time);
        }
        return tmp;
    }

    public String decodeVS(String org){
        orgkey=key;
        String miniPSWD = org.substring(0,4);
        doShuffle(miniPSWD);
        org = decodeV(org.substring(4));
        setOrgkey();
        return org;
    }

    public String decodeP(String org, String pswd){
        org = decode(org, pswd);
        org = decodeVS(org);
        return org;
    }
//核心算法
    private String deciphering(char[] pswd) {
        char[] key = this.key.toCharArray();
        int halfLen = key.length/2;
        StringBuilder ans = new StringBuilder();
        int col;
        for (col=halfLen;col%2==0;col/=2);
        for (int i = 0; i < pswd.length; i += 2) {
            char a = pswd[i];
            char b = pswd[i + 1];
            int loc_a = this.key.indexOf(a);
            int loc_b = this.key.substring(halfLen).indexOf(b);
            int tmpa = loc_a, tmpb = loc_b;
            loc_a = tmpa % col;
            loc_b = tmpb % col;
            int cha = Math.abs(loc_a - loc_b);
            if (loc_a > loc_b) ans.append(key[tmpa - cha]).append(key[tmpb + cha + halfLen]);
            else ans.append(key[tmpa + cha]).append(key[tmpb - cha + halfLen]);
        }
        return ans.toString();
    }
//根据code得出随机交换规则
    private String  getShuffleRule(String code) {
        StringBuilder tmp = new StringBuilder();
        for (char c : code.toCharArray()) {
            tmp.append((int) c * 793);
        }
        while (tmp.length() < 200) {
            if (tmp.length() < 10) {
                String tmp2 = tmp.toString();
                tmp = new StringBuilder();
                for (char c : tmp2.toCharArray()) {
                    tmp.append(Integer.parseInt(c + "") * 7553);
                }
            }
            if (tmp.length() < 100) {
                String tmp2 = tmp.toString();
                tmp = new StringBuilder();
                for (char c : tmp2.toCharArray()) {
                    tmp.append(Integer.parseInt(c + "") * 573);
                }
            }
            if (tmp.length() < 200) {
                String tmp2 = tmp.toString();
                tmp = new StringBuilder();
                for (char c : tmp2.toCharArray()) {
                    tmp.append(Integer.parseInt(c + "") * 91);
                }
            }
        }
        return tmp.toString();
    }

    private void doShuffle(String code){
        char[] newKey = key.toCharArray();
        int halfLen = newKey.length/2;
        String rule = getShuffleRule(code);
        for (int i=0;i<rule.length();i+=4){
            int a = Integer.parseInt(rule.substring(i,i+2>rule.length()?rule.length() : i+2));
            if (a>=halfLen) a-=halfLen;
            int b = i+2>=rule.length() || i+4>=rule.length()? halfLen-1 : Integer.parseInt(rule.substring(i+2, i+4));
            if (b>=halfLen) b-=halfLen;
            char tmp = newKey[a];
            char tmp2 = newKey[halfLen+a];
            newKey[a]=newKey[b];
            newKey[b]=tmp;
            newKey[halfLen+a] = newKey[halfLen+b];
            newKey[halfLen+b] = tmp2;
        }
        StringBuilder tmpKey = new StringBuilder();
        for (char c : newKey){
            tmpKey.append(c);
        }
        loadKey(tmpKey.toString());
    }

    private String getDate(String value){
        String ans = "的23如果fgd";
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        ans+=year*13+"gf3sop,m1";
        if (value.equals("year")) return ans;
        int month = calendar.get(Calendar.MONTH);
        ans+=month*79+"nmqwesd[12";
        if (value.equals("month")) return ans;
        int day = calendar.get(Calendar.DAY_OF_YEAR);
        ans+=day*17+",Mvfe";
        if (value.equals("day")) return ans;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        ans+=hour*97+"12jadbFSD]";
        if (value.equals("hour")) return ans;
        int minute = calendar.get(Calendar.MINUTE);
        ans+=minute*57+"3FDG4HGNdH;";
        return ans;
    }

    public boolean checkKey(String key){
        if (key.length()!=orgkey.length()) return false;
        int halfLen = key.length()/2;
        for (int i=0;i<=halfLen;i+=halfLen){
            String tmpKey = key.substring(i, i+halfLen);
            for (char c : orgkey.substring(0,halfLen).toCharArray()){
                if (!tmpKey.contains(c+"")) return false;
            }
        }
        return true;
    }

    public void setOrgkey() {
        this.loadKey(orgkey);
    }

    public void setPrvkey() {
        this.loadKey(prvkey.get(prvkey.size()-1));
    }

    public int getKeyLength(){
        return key.length();
    }

    public void useCompleteKey(){
        key=completeKey;
        ascSignH="~";
        ascSignT="~";
    }
    public void useSimpleKey(){
        key=key.replaceAll("[^A-Za-z0-9]","");
        ascSignH="v";
        ascSignT="qg";
    }
    public void useCustomKey(){

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        CBY bk = new CBY();

        String code = bk.codeVS("叶向林");
        System.out.println(code);
        //code = "!g+soMG(we?mw,=#KR{|rZ？D)r|Cz-~6QQzZ？5B.R4m";
        System.out.println(bk.decodeVS(code));


        System.out.println(">>>>>>>>    加密系统V2.20   <<<<<<<<");
        while (true) {
            System.out.print(">>");
            String cmd = sc.nextLine();
            if (cmd.equals(" ")){
                System.out.println("输入解码密码>>");
                String ps = sc.nextLine();
                System.out.println(bk.decodeP(code,ps));
            }
            if (cmd.equals("help")) System.out.println("1.加密(字母+字符)\n2.解密\n3.更换口令\n4.生成口令\n5.生成密钥\n6.翻译密钥\nexit:退出");
            if (cmd.equals("1") || cmd.equals("加密")) {
                System.out.println(">>>>  加密  <<<<");
                StringBuilder all = new StringBuilder();
                while (true) {
                    String org = sc.nextLine();
                    if (org.equals("")) break;
                    all.append(bk.codeVS(org)).append("\n");
                }
                System.out.println(">>>>  密文  <<<<\n" + all);
            }
            if (cmd.equals("2") || cmd.equals("解密")) {
                System.out.println(">>>>  输入原文  <<<<");
                StringBuilder ans = new StringBuilder();
                while (true) {
                    String org = sc.nextLine();
                    if (org.equals("")) break;
                    ans.append(bk.decodeVS(org)).append("\n");
                }

                System.out.println(">>>>  解密文  <<<<\n" + ans);
            }
            if (cmd.equals("3") || cmd.equals("加载密钥")) {
                System.out.println(">>>>  输入密钥  <<<<");
                String org = sc.nextLine();
                if (bk.loadKey(org)) System.out.println(">>>>  加载成功  <<<<");
                else System.out.println(">>>>  请输入正确的密钥  <<<<");
            }
            if (cmd.equals("4") || cmd.equals("生成密钥")) {
                System.out.println(">>>>  密钥  <<<<");
                System.out.println(bk.createKey());
                System.out.println(">>>>        <<<<");
            }
            if (cmd.equals("5") || cmd.equals("生成口令")) {
                String tmpkey = bk.createKey();
                bk.loadKey(tmpkey);
                System.out.println(">>>>  输入待加密文  <<<<");
                String tmptext = sc.nextLine();
                System.out.println(tmpkey + bk.code(tmptext));
                bk.setPrvkey();
                System.out.println(">>>>        <<<<");
            }
            if (cmd.equals("6") || cmd.equals("翻译口令")) {
                int len = bk.createKey().length();
                System.out.println(">>>>  输入密文  <<<<");
                String tmptext = sc.nextLine();
                bk.loadKey(tmptext.substring(0, len));
                System.out.println(bk.decodeV(tmptext.substring(len)));
                System.out.println(">>>>        <<<<");
            }
            if (cmd.equals("7")){
                bk.useSimpleKey();
                System.out.println("已切换到简易密钥");
            }
            if (cmd.equals("8")){
                bk.useCompleteKey();
                System.out.println("已切换到完整密钥");
            }
            if (cmd.equals("exit")) break;
        }
    }
}
