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

    var modal = document.getElementById('modal');
     modal.style.display = "none";

}


function openTask(taskPath) {

    var modal = document.getElementById('modal');
    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/groups/" + taskPath,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#modal').html(data);
            modal.style.display = "block";
        }
    });
    
}

function openTask_tepm(taskMap) {

    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/tasks/" + taskMap,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#side-modules').html(data);
            $('.selected').removeClass('selected');
            $('#'+taskMap).addClass('selected');
        }
    });

}
