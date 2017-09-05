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

// $(document).ready(function(){
//
// // Get the modal
var modal = document.getElementById('myModal');

//
// // Get the button that opens the modal
// var btn = document.getElementsByClassName("task_row");
//
// // Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];
//
// // When the user clicks the button, open the modal
// btn.onclick = function() {
//     modal.style.display = "block";
// };
//
// // When the user clicks on <span> (x), close the modal
 span.onclick = function() {
     modal.style.display = "none";
 };
//
// // When the user clicks anywhere outside of the modal, close it
 window.onclick = function(event) {
 if (event.target == modal) {
         modal.style.display = "none";
     }
 };



// $(function group_hover() {
//     $(".task_row").click(function (e) {
//         var modal = document.getElementById('myModal');
//         var text = $(".group_tasks_task_name");
//         console.log(text);
//         modal.style.display = "block";
//     })
// });




function openTask(taskPath) {

    var modal = document.getElementById('myModal');
    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/groups/" + taskPath,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#myModal').html(data);
            modal.style.display = "block";
        }
    });


}