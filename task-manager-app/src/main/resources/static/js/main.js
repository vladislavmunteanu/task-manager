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

function openTaskDetails(taskMap) {

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

function openGroupDetails(groupName) {

    var ctx = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

    $.ajax({

        url: ctx + "/groups/" + groupName,
        method: 'get',
        cache: false,
        success: function (data) {
            $('#side-modules').html(data);
            $('.selected').removeClass('selected');
            $('#'+groupName).addClass('selected');
        }
    });

}
