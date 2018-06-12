/*for 智阅卷 练习管理 0328*/
window.onload = function(e) {
    e = e || window.event;
    // startX, startY 为鼠标点击时初始坐标
    // diffX, diffY 为鼠标初始坐标与 box 左上角坐标之差，用于拖动
    var startX, startY, diffX, diffY;
    // 是否拖动，初始为 false
    var dragging = false;

    // 鼠标按下
    document.onmousedown = function(e) {
        function delare(id) {
            var target = document.getElementById(id).parentNode;
            delete  target;
        }
        startX = e.pageX;
        startY = e.pageY;

        // 如果鼠标在 box 上被按下
        if(e.target.className.match(/area/)) {
            // 允许拖动
            dragging = true;

            // 设置当前 box 的 id 为 moving_box
            if(document.getElementById("moving_box") !== null) {
                document.getElementById("moving_box").removeAttribute("id");
            }
            e.target.id = "moving_box";

            // 计算坐标差值
            diffX = startX - e.target.offsetLeft;
            diffY = startY - e.target.offsetTop;
        }
        else {
            // 在页面创建 box
            var active_box = document.createElement("div");
            active_box.id = "active_box";
            active_box.className = "area";
            active_box.innerHTML ="<p class='titN'>题目</p><a class='delBtn' href='javascript:;' onclick='delare(this)'><i class='qky-icon'>&#xe674;</i></a>";
            active_box.style.top = startY + 'px';
            active_box.style.left = startX + 'px';
            document.body.appendChild(active_box);
           // document.getElementById("#paper").appendChild(active_box);
            active_box = null;
        }
    };

    // 鼠标移动
    document.onmousemove = function(e) {
        // 更新 box 尺寸
        if(document.getElementById("active_box") !== null) {
            var ab = document.getElementById("active_box");
            ab.style.width = e.pageX - startX + 'px';
            ab.style.height = e.pageY - startY + 'px';
        }

        // 移动，更新 box 坐标
        if(document.getElementById("moving_box") !== null && dragging) {
            var mb = document.getElementById("moving_box");
            mb.style.top = e.pageY - diffY + 'px';
            mb.style.left = e.pageX - diffX + 'px';
        }
    };

    // 鼠标抬起
    document.onmouseup = function(e) {
        // 禁止拖动
        dragging = false;
        if(document.getElementById("active_box") !== null) {
            var ab = document.getElementById("active_box");
            ab.removeAttribute("id");
            // 如果长宽均小于 3px，移除 box
            if(ab.offsetWidth < 3 || ab.offsetHeight < 3) {
                document.body.removeChild(ab);
            }
        }
    };
};







