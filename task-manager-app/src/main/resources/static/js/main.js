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
// // When the user clicks anywhere outside of the modal, close it
// window.onclick = function(event) {
// if (event.target == modal) {
//         modal.style.display = "none";
//     }
// };

function closeTask() {

    var modal = document.getElementById('myModal');
     modal.style.display = "none";

}


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