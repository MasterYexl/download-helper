

let colors = ["#CCFF99","#99CCCC","#99CCFF","#CCCCFF","#FF9999","#99CC33","#666699","#FF0033"];

document.body.onclick = function (e){
    // let i = createProjectile(e.pageX+"px", e.pageY+"px");
    // let dir = Math.random() * 10 >= 5 ? 1 : -1;
    // let v = (Math.random() * 1000 + 3000) * dir;
    // let radius =  2*Math.PI/360 *  (Math.random() * 20 + 60) * dir;
    // console.log(v+" "+radius);
    // document.body.appendChild(i);
    // run(i.offsetLeft, i.offsetTop, 0, v, radius, i);
    let num = Math.random()*5+5;

    for (let i=0;i<num;i++){
        let i = createProjectile(e.pageX+"px", e.pageY+"px");
        let dir =  Math.random()*10 >= 5? 1:-1;
        let v = (Math.random()*1000+2500)*dir;
        let radius = h * (Math.random()*45+45)*dir;
        document.body.appendChild(i);
        run(i.offsetLeft, i.offsetTop, 0, v, radius, i);
    }

}


function createProjectile(x,y){
    let i = document.createElement("i");
    i.className = "bi-star-fill";
    i.style.fontSize = "0.1px";
    i.style.color = colors[Math.floor(Math.random()*colors.length)];
    i.style.position = "fixed";
    i.style.left = x;
    i.style.top = y;
    return i;
}