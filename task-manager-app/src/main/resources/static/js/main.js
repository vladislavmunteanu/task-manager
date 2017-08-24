/*function auto_load(){
    $.ajax({
        url: "/task/time",
        cache: false,
        success: function(data){
            $("#time").html(data);
        }
    });
}

$(document).ready(function(){auto_load();});
setInterval(auto_load,1000);
*/

// $(function dash_click(){
//     const dash = $('#dash_widget');
//     dash.click(function (e) {
//         document.getElementById("title").innerHTML = "Dashboard";
//         dash.css("background-color", "#D9733B");
//         $('#group_widget').css("background-color", "#fff");
//         $('#task_widget').css("background-color", "#fff");
//     })
// });
//
// $(function group_click(){
//     const group = $('#group_widget');
//     group.click(function (e) {
//         document.getElementById("title").innerHTML = "Groups";
//         group.css("background-color", "#D9733B");
//         $('#dash_widget').css("background-color", "#fff");
//         $('#task_widget').css("background-color", "#fff");
//     })
// });
//
// $(function task_click(){
//     const task = $('#task_widget');
//     task.click(function (e) {
//         document.getElementById("title").innerHTML = "Tasks";
//         task.css("background-color", "#D9733B");
//         $('#group_widget').css("background-color", "#fff");
//         $('#dash_widget').css("background-color", "#fff");
//     })
// });
//
//
// $(function dash_hover() {
//     $("#dash_widget").hover(function () {
//         $(this).css("background-color", "aliceblue");
//     }, function () {
//         $(this).css("background-color", "#fff");
//     });
// });
//
// $(function group_hover() {
//     $("#group_widget").hover(function () {
//         $(this).css("background-color", "aliceblue");
//     }, function () {
//         $(this).css("background-color", "#fff");
//     });
// });
//
// $(function task_hover() {
//     $("#task_widget").hover(function () {
//         $(this).css("background-color", "aliceblue");
//     }, function () {
//         $(this).css("background-color", "#fff");
//     });
// });