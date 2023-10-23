

function save(key){
    let value = document.getElementById(key).value;
    if (key==="read-auto-save") {
        if (document.getElementById(key).checked){
            value=1;
        }
        else value=0;
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
            console.log(xmlhttp.responseText);
        }
    }
    xmlhttp.open("POST","/setting/save/",true);
    xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    if (key === "ras") key="read-auto-save";
    if (key === "folder") key="book-path";
    if (key === "fc") key="default-fc";
    if (key === "bc") key="default-bc";
    key = key.replace(/([A-Z])/g,"-$1").toLowerCase();
    xmlhttp.send("key="+key+"&value="+value);
}