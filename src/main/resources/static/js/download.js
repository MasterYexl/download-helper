
let id = document.getElementById("number").innerText;
let progressData = document.getElementById("progress-data");
let progressInfo = document.getElementById("progress-info");
let finish = false;
let progress;
let tot;

window.onload = function (){
    let ft;
    let itv = setInterval(function (){
        getProgress();
        let data = (progress/tot)*100;
        let improve = -1;
        if (ft!==undefined) improve = data - ft;
        data = data.toFixed(2);
        progressData.style.width = data + '%';
        ft = data;
        progressInfo.innerHTML = data + "%<br>" + progress+"/"+ tot + '<br>'+"预计"+formatPerhapsTime((100.001-data)/improve)+"后完成";
        if (finish) {
            clearInterval(itv);
            progressData.style.width = '100%';
            document.getElementById("over-down").style.display = "block";
            progressData.innerHTML = "";
        }
    }, 1000);
}

function formatPerhapsTime(time){
    let hour = Math.floor(time/3600);
    time = (time % 3600);
    let min = Math.floor(time/60);
    time = time % 60;
    let ans = "";
    if (hour!==0) ans+=hour+"小时";
    if (min!==0) ans+=min+"分";
    if (time!==0) ans+=Math.floor(time)+"秒";
    return ans;
}

function getProgress(){
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
            let st = xmlhttp.responseText;
            console.log(st);
            let pros = JSON.parse(st);
            progress = pros.progress;
            tot = pros.tot;
            finish = pros.finish;
        }
    }
    xmlhttp.open("GET","/download/progress?id="+id,true);
    xmlhttp.send();
}