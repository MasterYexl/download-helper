
let slct = -1, slcted=0;
let tipsLen = 0;
let loading = document.getElementById("loading");
let actionButton = document.getElementById("action-button");
let searchBlock = document.getElementById("search-block");
let actionView = document.getElementById("action-view");
let searchPane = document.getElementById('search');
let replace = false;
let resizeSearch = true;

window.onload = function (){
    resize();
}
window.onresize = function (){
    resize();
}
function reset(){
    searchBlock.id = "search-block";
    resize();
}
function resize(){
    let tips = document.getElementById("tips-panel");
    tips.style.left = (actionView.offsetLeft + actionView.offsetWidth)+"px";
    tips.style.width = searchPane.offsetWidth+"px";
}

function getQuestions() {
    let key = event.key;
    let down = 0;
    if (key==="Enter") {
        if (!replace) {
            actionButton.click();
            hiddenTipsPanel();
        }
        else {
            document.getElementById("search").value=document.getElementById("tips"+slcted).innerHTML;
            replace = false;
        }
        return;
    }
    if (key==="ArrowUp") {
        replace = true;
        down=-1;
    }
    else if (key==="ArrowDown") {
        replace = true;
        down=1;
    }
    else {
        replace = false;
        let v = document.getElementById("search").value;
        let srp = document.createElement("script");
        srp.src = "https://www.baidu.com/sugrec?pre=1&p=3&ie=utf-8&json=1&prod=pc&from=pc_web&wd="+v+"&cb=cbc";
        document.body.appendChild(srp);
        return;
    }
    if (replace){
        slct+=down;
        if (slct<0) slct = tipsLen-1;
        if (slct >=tipsLen) slct=0;
        document.getElementById("tips"+slcted).style.backgroundColor="unset";
        document.getElementById("tips"+slct).style.backgroundColor="#5bc0de";
        slcted=slct;
    }
}
function selectTips(tips){
    document.getElementById("tips"+slcted).style.backgroundColor="unset";
    document.getElementById(tips).style.backgroundColor="#5bc0de";
    slct = tips.substring(4);
    slcted = slct;
}
function hiddenTipsPanel() {
    document.getElementById("tips-panel").style.display="none";
    slct=-1;
    replace = false;
}
function cbc(res) {
    var panel = document.getElementById("tips-panel");
    if (res===null||res==={}||res.g===undefined) {
        panel.style.display="none";
        return;
    }
    panel.style.display="block";
    panel.innerHTML="";
    tipsLen = res.g.length;
    for(var i=0;i<tipsLen;i++){
        var tips = document.createElement("div");
        tips.className = "auto-tips";
        tips.innerHTML = res.g[i].q;
        tips.id="tips"+i;
        tips.onmouseover = function (){
            selectTips(this.id);
        }
        tips.addEventListener("click",function (ev) { document.getElementById("search").value=this.innerHTML; });
        panel.appendChild(tips);
    }
}
function createResult(title, url){
    let result = document.createElement("h4");
    let span = document.createElement("span");
    let aTitle = document.createElement("a");
    let aUrl = document.createElement("a");
    aTitle.innerText = title;
    aTitle.className = "rs-name";
    aTitle.href = "#";
    aUrl.target = "view_window";
    aTitle.onclick = function (){
        document.getElementById("post-url").value = url;
        document.getElementById("post-url-form").submit();
    }
    aUrl.target = "view_window";
    aUrl.href = url;
    span.innerText = "源地址";
    span.className = "badge badge-info";
    aUrl.appendChild(span);
    result.appendChild(aTitle);
    result.appendChild(aUrl);
    return result;
}

function createTxtResult(title, url){
    let result = document.createElement("h3");
    let aTitle = document.createElement("a");
    aTitle.innerText = title;
    aTitle.href = url;
    aTitle.target = "view_window";
    result.appendChild(aTitle);
    return result;
}

function search(name, next, txt){
    if (!next&&(name==null||name===""||name===undefined)) return ;
    if (resizeSearch){
        searchBlock.id = "search-block-search";
        resizeSearch = false;
    }
    loading.style.display = "block";
    let xmlhttp;
    if (window.XMLHttpRequest)
    {
        //  IE7+, Firefox, Chrome, Opera, Safari 浏览器执行代码
        xmlhttp=new XMLHttpRequest();
    }
    else
    {
        // IE6, IE5 浏览器执行代码
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState===4 && xmlhttp.status===200)
        {
            loading.style.display = "none";
            let st = xmlhttp.responseText;
            console.log(st);
            if (st===null||st==="null"){
                reset();
                alert("网络状态不佳，请稍后再试");
                return ;
            }
            let rsBlock = document.getElementById("result-block");
            let urls = JSON.parse(st);
            if (!next) rsBlock.innerHTML = "";
            for (let i = 0; i < urls.length; i++) {
                if (txt) rsBlock.appendChild(createTxtResult(urls[i].name, urls[i].url));
                else rsBlock.appendChild(createResult(urls[i].name, urls[i].url));
            }
            document.getElementById("continue-search").style.display = "block";
        }
    }
    if (txt){
        if (next) xmlhttp.open("GET","/search/txt_next",true);
        else xmlhttp.open("GET","/search/downloadable?book_name="+name,true);
    }
    else {
        if (next) xmlhttp.open("GET","/search/next",true);
        else xmlhttp.open("GET","/search/book?book_name="+name,true);
    }
    xmlhttp.send();
}

function changeMode(mode){
    //阅读模式
    if (mode===0){
        actionButton.innerText = "搜索";
        actionView.innerText = "阅读模式";
        actionButton.onclick = function (){
            search(document.getElementById('search').value, false, false);
        }
        document.getElementById("continue-search").onclick = function (){
            search(searchPane.value, true, false);
        }
    }
    //txt下载模式
    if (mode===1){
        actionButton.innerText = "搜索";
        actionView.innerText = "txt下载模式";
        actionButton.onclick = function (){
            search(searchPane.value, false, true);
        }
        document.getElementById("continue-search").onclick = function (){
            search(searchPane.value, true, true);
        }
    }
    //指定网站模式
    if (mode===2){
        actionButton.innerText = "解析";
        actionView.innerText = "指定网站模式";
        actionButton.onclick = function (){
            document.getElementById("post-url").value = searchPane.value;
            document.getElementById("post-url-form").submit();
        }
    }
}