
let content = document.getElementById("read-block");
let settingPane = document.getElementById("setting-pane")
let fs = document.getElementById("fs");
let catalog = document.getElementById("catalog-block");
let OUT = " out";
let CATALOG_OUT = " catalog-out";
let bcColor = document.getElementById("bc-color");
let fcColor = document.getElementById("fc-color");

let settingPaneState = false;
let catalogState = false;



content.onclick = function (){
    if (!catalogState) {
        showSettingPane();
        if ((!settingPaneState)&&(document.getElementById("ras").checked)) saveAll();
    }
    showCatalog(true);
}

function changeFontSize(size){
    content.style.fontSize = size+"px";
    fs.value = size;
}

function changeBackgroundColor(color){
    document.body.style.backgroundColor = color;
    bc.value = color;
    addable(bcColor, color, changeBackgroundColor);
}

function changeFontColor(color){
    content.style.color = color;
    fc.value = color;
    addable(fcColor, color, changeFontColor);
}

function addable(element, color, func){
    let divs = element.getElementsByTagName("div");
    let isAddable = true;
    for (let i = 0; i < divs.length; i++) {
        if (divs[i].getAttribute("data-color")===color){
            isAddable = false;
            break;
        }
    }
    if (isAddable) element.insertBefore(createColorBall(color, func), element.firstChild);
}

function showSettingPane(out){
    if (out===undefined) out = false;
    if (settingPaneState) {
        settingPane.className += OUT;
        settingPaneState = false;
    }
    else if (!out) {
        settingPane.className = settingPane.className.replace(OUT, "");
        settingPaneState = true;
    }
}

function showCatalog(out){
    if (out===undefined){
        showSettingPane(true);
        out = false;
    }
    if (catalogState) {
        catalog.className += CATALOG_OUT;
        catalogState = false;
    }
    else if (!out) {
        catalog.className = catalog.className.replace(CATALOG_OUT, "");
        catalogState = true;
    }
}

function createColorBall(color, func){
    // <li>
    //     <div class="color-display" style="background-color: #5a6268" data-color="#5a6268" onclick="changeBackgroundColor(this.getAttribute('data-color'))"></div>
    // </li>
    let li = document.createElement("li");
    let div = document.createElement("div");
    div.className = "color-display";
    div.style.backgroundColor = color;
    div.setAttribute("data-color", color);
    div.onclick = function (){
        func(color);
    }
    div.ondblclick = function (){
        deleteColorDisplay(this);
    }
    li.appendChild(div);
    return li;
}

function deleteColorDisplay(element){
    swal({
        title: "确定要删除这个颜色吗?",
        icon: "warning",
        buttons: true,
        dangerMode: true,
    }).then((willDelete) => {
        if (willDelete) {
            let parent = element.parentElement.parentElement;
            parent.removeChild(element.parentElement);
            if (parent.id === "bc-color"&&bc.value === element.getAttribute("data-color")) {
                changeBackgroundColor(parent.getElementsByClassName("color-display")[0].getAttribute("data-color"));
            }
            if (parent.id === "fc-color"&&fc.value === element.getAttribute("data-color")) {
                changeFontColor(parent.getElementsByClassName("color-display")[0].getAttribute("data-color"));
            }
        }
    });
}

function createChapterElement(id, title, content){
    let chapter = document.createElement("div");
    chapter.id = "chapter-"+id;
    let br1 = document.createElement("br");
    let tit = document.createElement("h2");
    tit.innerText = title;
    let br2 = document.createElement("br");
    let br3 = document.createElement("br");
    let br4 = document.createElement("br");
    let br5 = document.createElement("br");
    let br6 = document.createElement("br");
    let hr = document.createElement("hr");
    let cont = document.createElement("div");
    cont.innerHTML = content;
    cont.className = "content";
    chapter.appendChild(br6);
    chapter.appendChild(br5);
    chapter.appendChild(br4);
    chapter.appendChild(br3);
    chapter.appendChild(br1);
    chapter.appendChild(tit);
    chapter.appendChild(br2);
    chapter.appendChild(hr);
    chapter.appendChild(cont);
    return chapter;
}



/**     文章-目录绑定       **/


let list = new List();
let nowChapter;
let nowChapterPos;
let nextChapterPos;
let nowPos;
let bufferChapter = document.getElementById("buffer-chapter");
let nowListPos=0;
let bufferNumber = 0;//未浏览的缓存数
let fc = document.getElementById("fc");
let bc = document.getElementById("bc");
//初始化数值
window.onload = function (){
    // changeBackgroundColor(bc.value);
    // changeFontColor(fc.value);
    // changeFontSize(document.getElementById("fs").value);
    let catalogElement = document.getElementById("catalog").getElementsByClassName("active")[0];
    catalogElement.focus();
    let id = parseInt(catalogElement.id.split("-")[1]);
    let chapter = createChapter(id);
    chapter.isSaw = true;
    nowChapter = chapter;
    list.add(chapter);
    nowChapterPos = chapter.chapterElement.offsetTop;
    nextChapterPos = chapter.chapterElement.offsetHeight;
    if (document.getElementById("preChapter").value===nowChapter.id+""){
        let px = parseFloat(document.getElementById("prePos").value)*nextChapterPos;
        window.scrollBy(0,px);
    }
    getNextChapter();
}
//滚动监听
window.onscroll = function (){
    nowPos = Math.ceil(document.documentElement.scrollTop||document.body.scrollTop);
    if (nowPos>nextChapterPos) nowChapter.catalogElement.className = nowChapter.catalogElement.className.replace("list-group-item-success", "list-group-item-secondary")
    while (nowPos > nextChapterPos){
        changeChapter(1);
    }
    while (nowPos < nowChapterPos){
        changeChapter(-1);
    }
    nowChapter.catalogElement.focus();
    let rate = (nowPos-nowChapterPos)/nowChapter.chapterElement.offsetHeight;
    sendRequest("/bookshelf/update-info?id="+document.getElementById("id").innerText+"&pos="+rate+"&chapter="+nowChapter.id,"GET");
}
//文章-目录绑定
function changeChapter(next){
    nowChapter.catalogElement.className = nowChapter.catalogElement.className.replace(" active", "");
    nowListPos = nowListPos+next;
    let tmpChapter = list.get(nowListPos);
    if (tmpChapter==null) {
        tmpChapter = createChapter(nowChapter.id+next);
        list.add(tmpChapter);
    }
    nowChapter = tmpChapter;
    if (!nowChapter.isSaw) getNextChapter();
    nowChapter.isSaw = true;
    bufferNumber--;
    nowChapter.catalogElement.className += " active";
    nowChapterPos = nowChapter.chapterElement.offsetTop;
    nextChapterPos = nowChapter.chapterElement.offsetHeight+nowChapterPos;
}
function createChapter(id){
    let chapter = new Chapter();
    chapter.catalogElement = document.getElementById("catalog-"+id);
    if (chapter.catalogElement===null) throw "未找到该章节";
    chapter.id = id;
    chapter.chapterElement = document.getElementById("chapter-"+id);
    return chapter;
}
function active(element){

}
//缓存下一章
function getNextChapter(){
    let buffer = parseInt(bufferChapter.value);
    let last = list.getLast();
    for (let i=list.size, id=last.id+1;i<=buffer+nowListPos;i++,id++){
        setTimeout(function (){
            getContent(id, document.getElementById("catalog-"+(id)).innerText);
        },1000);
    }

}
function getContent(id, title){
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
            let newContent = xmlhttp.responseText;
            content.appendChild(createChapterElement(id, title, newContent));
            list.add(createChapter(id));
            bufferNumber++;
            let ct = document.getElementById("catalog-"+id);
            ct.setAttribute("href","#chapter-"+id);
            ct.setAttribute("target","_self");
            ct.className += " list-group-item-success";
        }
    }
    xmlhttp.open("POST","/parse/content?id="+document.getElementById("id").innerText+"&num="+id,true);
    xmlhttp.send();
}
function gotoPage(next){
    let tmp = document.getElementById("catalog-"+(nowChapter.id+next));
    tmp.click();
}

/**     文章-目录绑定结束       **/


/**     保存设置        **/


let fcDisplays = fcColor.getElementsByClassName("color-display");
let bcDisplays = bcColor.getElementsByClassName("color-display");

function saveAll(){
    let fcs = fcDisplays[0].getAttribute("data-color");
    let bcs = bcDisplays[0].getAttribute("data-color");
    for (let i=1;i<fcDisplays.length;i++){
        fcs += (" "+fcDisplays[i].getAttribute("data-color"));
    }
    for (let i=1;i<bcDisplays.length;i++){
        bcs += (" " + bcDisplays[i].getAttribute("data-color"));
    }
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
            console.log(xmlhttp.responseText==="OK"? "已经自动保存设置":xmlhttp.responseText);
        }
    }
    xmlhttp.open("POST","/setting/save-all/",true);
    xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xmlhttp.send("keys="+['fc-color','bc-color','font-size','default-fc', 'default-bc']+"&values="+[fcs, bcs, fs.value, fc.value, bc.value]);
}


/**     保存设置结束        **/