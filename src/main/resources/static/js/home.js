



function get(){
    let questions;
    let answer=[];
    let title=[];
    let size;
    document.getElementsByClassName("mhncHIIMqFiV5fw53_ZL9")[0].click();
    let bt = document.getElementsByClassName("s5aL2vkpTvwZL2k3p8VtR")[0].getElementsByTagName("button");
    bt[bt.length-1].click();
    setTimeout(function (){
        questions = document.getElementsByClassName("_3r37v-3apQa42bIQ_raL7M")[0].children;
        for (size=0;size<questions.length;size++){
            title[size] = questions[size].getElementsByClassName("_1tVWqI4-1Dj8SXnD7oQ9WP")[0].children[1].innerText;
            let as = questions[size].getElementsByClassName("_2IQQQia-dlMQD2yUaAcBBe");
            let tmp = [];
            if (as.length === 0) {
                as = questions[size].getElementsByClassName("dhiccbzLAlvLFEBjU5Y5R");
                for (let j=0;j<as.length;j++){
                    tmp[j] = as[j].children[1].innerHTML;
                }
            }
            else {
                for (let j=0;j<as.length;j++){
                    tmp[j] = as[j].parentElement.innerText;
                }
            }
            answer[size] = JSON.stringify(tmp);
        }
        let newProgram = "let _title="+JSON.stringify(title)+";\nlet _ans="+JSON.stringify(answer)+";\nfunction set(){\n" +
            "    let title = _title;\n" +
            "    let ans = _ans;\n" +
            "    document.getElementsByClassName(\"mhncHIIMqFiV5fw53_ZL9\")[0].click();\n" +
            "    let bt = document.getElementsByClassName(\"s5aL2vkpTvwZL2k3p8VtR\")[0].getElementsByTagName(\"button\");\n" +
            "    bt[bt.length-1].click();\n" +
            "    setTimeout(function (){\n" +
            "        let questions = document.getElementsByClassName(\"_3r37v-3apQa42bIQ_raL7M\")[0].children;\n" +
            "        for (let i = 0;i<questions.length;i++){\n" +
            "            let nowTitle = questions[i].getElementsByClassName(\"_1tVWqI4-1Dj8SXnD7oQ9WP\")[0].children[1].innerText;\n" +
            "            for (let j=0;j<title.length;j++){\n" +
            "                if (title[j] === nowTitle) {\n" +
            "                    let getAns = questions[i].getElementsByTagName(\"button\");\n" +
            "                    let nowAns = JSON.parse(ans[j]);\n" +
            "                    if (getAns.length===0){\n" +
            "                        getAns = questions[i].getElementsByTagName(\"input\");\n" +
            "                        for (let k = 0;k<getAns.length;k++){\n" +
            "                            getAns[k].value =  nowAns[k];\n" +
            "                        }\n" +
            "                    }else {\n" +
            "                        for (let k=0;k<nowAns.length;k++){\n" +
            "                            for (let p=0;p<getAns.length;p++){\n" +
            "                                if (nowAns[k]===getAns[p].innerText) {\n" +
            "                                    getAns[p].click();\n" +
            "                                    break;\n" +
            "                                }\n" +
            "                            }\n" +
            "                        }\n" +
            "                    }\n" +
            "                    break;\n" +
            "                }\n" +
            "            }\n" +
            "        }\n" +
            "        console.log(\"OK!\");\n" +
            "    }, 2000);\n" +
            "    return \"2秒后开始执行\";\n" +
            "}\n" +
            "set()";
        let tmpCopy = document.createElement("textarea");
        tmpCopy.value = newProgram;
        document.body.appendChild(tmpCopy);
        tmpCopy.select();
        document.execCommand("Copy");
        tmpCopy.style.display = "none";
        console.log("OK!");
    },2000);
    return "2秒后开始执行";
}
get()
let _title=["在线索二叉树中，一个结点是叶子结点的充要条件为（    ）","树最适合用来表示（  ）。","树的后序遍历与对应二叉树的（     ）一致。","在按层次遍历二叉树的算法中，需要借助的辅助数据结构是（ ）","设哈夫曼编码的长度不超过4，若已经对两个字符编码为1和01，则最多还可以为（   ）个字符编码。","假定在一棵二叉树中，度为2的结点数为15，度为1的结点数为30，则叶子结点数为（  ）。","将一棵有100个结点的完全二叉树进行层序编号，编号为49的结点的左孩子编号为（  ）。","二叉树的深度为k，则二叉树最多有（  ）个结点。","为5个使用频率不等的字段设计哈夫曼编码，不可能的设计方案是（   ）。","设二叉树有 8 个结点，则其深度至少为（    ）。","存在这样的二叉树，对它采用任何次序的遍历，结果相同。","编码集 “0，1，00，11” 是前缀码","具有12个结点的完全二叉树有5个度为2的结点。","前序遍历结果和中序遍历结果相同的二叉树有（    ）","中序遍历结果和后序遍历结果相同的二叉树有（    ）","二叉树是非线性数据结构，所以( )。","按照二叉树的定义，具有3个结点的二叉树有（  ）种。","有一棵二叉树，其终端结点的个数为 7，度为 2 的结点个数应为（   ）。","已知二叉树的前序和中序分别为：ABCDEGFIH 和 CBEDAFIGH，则该二叉树后序遍历序列为（    ）。","设哈夫曼树中的叶子结点总数为m，若用二叉链表作为存储结构，则该哈夫曼树中总共有（  ）个空指针域。","完全二叉树的某结点若无左孩子，则它必须是叶子结点。","在线索二叉树中，任一结点均有指向前趋和后继的线索。","由树转换成的二叉树，其根结点的右子树总是空的。","在二叉树的前序遍历中，任意一个结点均处在其子女的前面。","对于一棵非空二叉树，它的根结点作为第一层，则它的第i层上最多能有2i—1个结点。\n\n\n","对于完全二叉树中的任一结点，若其右分支下的子孙的最大层次为 h，则其左分支下子孙的最大层次一定为 h + 1。","已知一棵二叉树的前序和中序，则一定可以确定这棵二叉树。","编码集“00, 100, 101, 110, 111”是哈夫曼编码。","二叉树是度为 2 的树。","一棵满二叉树中共有 n 个结点，有 m 个叶子结点，树的深度为 h，则有 n = 2h - 1。\n\n\n","假定一棵度为 3 的树中结点数为 37，则其最小高度应为____，结点的最大度数为____。","用5个权值{3, 2, 4, 5, 1}构造的哈夫曼（Huffman）树的带权路径长度是____。","深度为 5 的二叉树最多有____个结点，最少有____个结点。","函数InOrder实现中序遍历二叉树，请在空格处将算法补充完整。\n\nvoid InOrder(Bitree *root)\n\n{\n\n    if(root==NULL) return 0;\n\n    else\n\n       {\n\n           ____;\n\n            cout<<root->data;\n\n            ____;\n\n       }\n\n}\n\n注：不要增加多余的分号","对于一棵具有 18 个结点的树，其所有结点的度之和为____。","前序遍历结果为 ABC 的二叉树共有____种可能，中序遍历为 ABC 的二叉树共有____种可能。","一棵有 n 个结点满二叉树，其中有 m 个叶子结点，则有 n = 2m - 1 成立。","二叉链表表示的二叉树中，空指针域比非空指针域多 2 个。","由 n 个权值构造的哈夫曼树一定有 n - 1 个分支结点。","设哈夫曼编码的长度不超过 4，若已对两个字符编码为 1 和 01，则最多还可以为 4 个字符编码。","由权值为 {3，4，9，2，5} 的叶子结点生成一棵哈夫曼树，其带权路径长度为____。","写出如图所示的二叉树的前序遍历、中序遍历、后序和层序遍历序列。\n\n注意：全部连续填写大写字母\n\n前序：____\n\n中序：____\n\n后序：____\n\n层序：____","对于一棵具有 6 个结点的树，其所有结点的度数之和为____，该树的度数最多为____。","已知二叉树如图所示，该二叉树转换为森林后有____棵树，森林中，最高的树的深度为____，最大度数的树的度为____。","5.设一棵完全二叉树的顺序存储结构中存储数据元素为ABCDEF，则该二叉树的前序遍历序列为____，中序遍历序列为____，后序遍历序列为____。","已知一二叉链表的结点个数为 13，则该二叉链表中有____个非空指针域，有____个空指针域。","用顺序存储的方法将完全二叉树中的所有结点逐层存放到数组 A[1]~A[20] 中，对于层序编号为 9 的结点其根结点编号为____，若其有左子树，则左子树的根结点编号是____，若有右子树，则右子树的根结点编号是____。","函数depth实现返回二叉树的高度，请在空格处将算法补充完整。\n\nint depth(Bitree *t)\n\n{\n\n    if(t==NULL)\n\n        return 0;\n\n    else\n\n    {\n\n        hl=depth(t->lchild);\n\n        hr=____;\n\n        if(____）\n\n            return hl+1;\n\n        else\n\n            return hr+1;\n\n      }\n\n}","完全二叉树的高度为 5，则该二叉树最多有____个结点，至少有____个结点。"];
let _ans=["[\"左右线索标志均为 0\"]","[\"元素之间具有分支层次关系的数据\"]","[\"中序遍历\"]","[\"队列\"]","[\"4\"]","[\"16\"]","[\"98\"]","[\"2k-1  \"]","[\"00，100，101，110，111\"]","[\"4\"]","[\"是\"]","[\"否\"]","[\"是\"]","[\"空二叉树\",\"只有一个根结点的二叉树\"]","[\"空二叉树\",\"只有一个根结点的二叉树\"]","[\"顺序存储结构和链式存储结构都能存储\"]","[\"5\"]","[\"6\"]","[\"CEDBIFHGA\"]","[\"2m\"]","[\"是\"]","[\"否\"]","[\"是\"]","[\"是\"]","[\"否\"]","[\"否\"]","[\"是\"]","[\"否\"]","[\"否\"]","[\"是\"]","[\"4\",\"3\"]","[\"33\"]","[\"31\",\"5\"]","[\"InOrder(root-&gt;lchild)\",\"InOrder(root-&gt;rchild)\"]","[\"17\"]","[\"5\",\"5\"]","[\"是\"]","[\"是\"]","[\"是\"]","[\"是\"]","[\"51\"]","[\"EIJGBKACFD\",\"IGJEKBFCDA\",\"GJIKFDCABE\",\"EIBJKAGCFD\"]","[\"5\",\"6\"]","[\"2\",\"3\",\"4\"]","[\"ABDECF\",\"DBEAFC\",\"DEBFCA\"]","[\"12\",\"14\"]","[\"4\",\"18\",\"19\"]","[\"depth(t-&gt;rchild)\",\"hl&gt;hr\"]","[\"31\",\"16\"]"];
function set(){
    let title = _title;
    let ans = _ans;
    document.getElementsByClassName("mhncHIIMqFiV5fw53_ZL9")[0].click();
    let bt = document.getElementsByClassName("s5aL2vkpTvwZL2k3p8VtR")[0].getElementsByTagName("button");
    bt[bt.length-1].click();
    setTimeout(function (){
        let questions = document.getElementsByClassName("_3r37v-3apQa42bIQ_raL7M")[0].children;
        for (let i = 0;i<questions.length;i++){
            let nowTitle = questions[i].getElementsByClassName("_1tVWqI4-1Dj8SXnD7oQ9WP")[0].children[1].innerText;
            for (let j=0;j<title.length;j++){
                if (title[j] === nowTitle) {
                    let getAns = questions[i].getElementsByTagName("button");
                    let nowAns = JSON.parse(ans[j]);
                    if (getAns.length===0){
                        getAns = questions[i].getElementsByTagName("input");
                        for (let k = 0;k<getAns.length;k++){
                            getAns[k].value =  nowAns[k];
                        }
                    }else {
                        for (let k=0;k<nowAns.length;k++){
                            for (let p=0;p<getAns.length;p++){
                                if (nowAns[k]===getAns[p].innerText) {
                                    getAns[p].click();
                                    break;
                                }
                            }
                        }
                    }
                    break;
                }
            }
        }
        console.log("OK!");
    }, 2000);
    return "2秒后开始执行";
}
set()