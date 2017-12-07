function closeTask() {

    var modal = document.getElementById('modal');
     modal.style.display = "none";

}


function openGroupTaskDetails(taskPath) {

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

function openTaskDetails(taskMap,taskId) {

    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/tasks/" + taskMap,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#side-modules').html(data);
            $('.selected').removeClass('selected');
            $('#'+taskId).addClass('selected');
        }
    });

}

function openGroupDetails(groupName,groupId) {

    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/groups/" + groupName,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#side-modules').html(data);
            $('.selected').removeClass('selected');
            $('#'+groupId).addClass('selected');
        }
    });

}

