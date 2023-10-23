
const h = 2*Math.PI/360;

function start(elmt,e){
    let noX = elmt.offsetLeft;
    let noY = elmt.offsetTop;
    let v = document.getElementById("test-v").value;
    let radius = h * parseFloat(document.getElementById("test-radius").value);
    let time = document.getElementById("test-time").value;
    run(noX, noY,time, v, radius, elmt);
    let i = createProjectile("50%", "30px");
    i.id = "ball-1";
    document.body.appendChild(i);

}

function run(x,y,t, v, radius, elmt){
    let fps = 15;
    let time = parseInt(t);
    let a = setInterval(function (){
        let xp = v*Math.cos(radius)*time/10000+x;
        let yp = -(v*Math.sin(radius)*time-(9.8*Math.pow(time,2)/2))/10000+y;
        if (yp>window.innerHeight) {
            document.body.removeChild(elmt);
            clearInterval(a);
            return ;
        }
        //console.log(xp+" "+yp);
        elmt.style.left = xp+"px";
        elmt.style.top = yp+"px";
        time+=fps;
    },fps);
}

