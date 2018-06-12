// $(function scrollMore() {
//
//     /*初始化*/
//     var counter = 0; /*计数器*/
//     var pageStart = 0; /*offset*/
//     var pageSize = 7; /*size*/
//     var isEnd = false;/*结束标志*/
//
//     /*首次加载*/
//     getData(pageStart, pageSize);
//
//     /*监听加载更多*/
//     $(window).scroll(function(){
//         if(isEnd == true){
//             return;
//         }
//
//         // 当滚动到最底部以上100像素时， 加载新内容
//         // 核心代码
//         if ($(document).height() - $(this).scrollTop() - $(this).height()<100){
//             counter ++;
//             pageStart = counter * pageSize;
//
//             getData(pageStart, pageSize);
//         }
//     });
// });