
let bookshelf = document.getElementById("bookshelf");
let inShelf = document.getElementById("inShelf");
function addToBookshelf(){
    if(inShelf.value === "true") sendRequest("/bookshelf/delete?id="+document.getElementById("id").innerText,"GET",alterStatus)
    else sendRequest("/bookshelf/add?id="+document.getElementById("id").innerText,"GET",alterStatus);
}
function alterStatus(response){
    if (response==="OK"){
        if (inShelf.value==="true") {
            bookshelf.innerText = "加入书架";
            inShelf.value="false";
        }
        else if (inShelf.value==="false") {
            bookshelf.innerText = "移出书架";
            inShelf.value="true";
        }
    }
    else {
        alert(response);
    }
}