/**     链表      **/


class Chapter{
    isSaw = false;
    chapterElement;
    catalogElement;
    id;
}
class Node{
    nextNode;
    preNode;
    chapter;
}
class List{
    size = 0;
    start = new Node();
    last = this.start;
    pointer = this.start;

    add(value){
        let node = new Node();
        node.chapter = value;
        node.preNode = this.last;
        this.last.nextNode = node;
        this.last = node;
        this.size++;
    }
    get(index){
        let a = this.goto(index);
        if (!a) return null;
        return this.pointer.chapter;
    }
    getLast(){
        return this.last.chapter;
    }
    goto(index){
        if (index >= this.size||index<0) return false;
        index++;
        let half = this.size>>1;
        if (index>=half){
            this.pointer = this.last;
            for (let i=this.size;i>index;i--){
                this.pointer = this.pointer.preNode;
            }
        }
        if (index<half) {
            this.pointer = this.start;
            for (let i = 0; i < index; i++) {
                this.pointer = this.pointer.nextNode;
            }
        }
        return true;
    }
}


/**     链表结束        **/

/**     ajax        **/
function sendRequest(url, method = "GET", success="", fail="", body=""){
    let xmlhttp;
    if (window.XMLHttpRequest){
        xmlhttp = new XMLHttpRequest();
    }
    else {
        xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange = function (){
        if (xmlhttp.readyState===4&&xmlhttp.status===200){
            if (success!==""){
                success(xmlhttp.responseText);
            }
        }
    }
    xmlhttp.open(method, url);
    if (method === "POST") xmlhttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xmlhttp.send(body);
}