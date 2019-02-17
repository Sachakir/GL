$(document).ready(function()
{
	$("#notificationLink").click(function()
	{
		$("#notificationContainer").fadeToggle(300);
		$("#notification_count").fadeOut("slow");
		return false;
	});
	
	//Document Click hiding the popup 
	$(document).click(function()
	{
		$("#notificationContainer").hide();
	});

	//Popup on click
	$("#notificationContainer").click(function()
	{
		return false;
	});
	
	$("#notificationLink1").click(function()
	{
		window.location.href="https://www.google.fr/"
	});
});

function myFunction()
{
	var x = document.getElementById("mdp");
	if (x.type === "password") {
		x.type = "text";
	} else {
		x.type = "password";
	}
}


function sendData()
{
	$.ajax(
	{
		type: "POST",
		data: $("#addUserForm").serialize(),
		cache: false,
		url: "/adminAdd",
		success: function() 
		{
			$("#formulaire").modal("hide"); 
			$('#success_msg').modal('show');
		},
		error: function()
		{
			alert("Error - Data not saved");
		}
	});
}

var idSuppr = "31";

$("button[value='suppr']").click(function() {
	idSuppr = this.id;
});

function deleteUser() {
	$.ajax(
	{
		url: "/deleteUser/"+idSuppr,
		success: function() 
		{ 
			$("#formulaire").modal("hide");
			location.reload();
		},
		error: function()
		{
			alert("Echec de la suppression");
		}
	});
}

/*
function successShow()
{
	
}

$('#warning_msg').on('show.bs.modal', function (e) {
  $(this).find('.btn-ok').attr('href', $(e.relatedTarget).data('href'));
  $('#deleteVersionHiddenId').val($(this).find('.btn-ok').attr('href'));
});

$('#deleteVersionHiddenbtn').click(function (e){
  e.preventDefault();
  var Id = $('#deleteVersionHiddenId').val();
  $.ajax({
        type: "DELETE",
        data: Id,
        cache: false,
        url: "/deleteUser/",
        success: function() {
        	$("#warning_msg").modal("hide"); 
        	alert("User deleted");
        }
		error: function()
		{
			alert("Error - Data not saved");
		}
    });
});


function deleteUser() 
{
    $.ajax({
        type: "POST",
        cache: false,
        url: "/deleteUser/"+$('#deleteVersionHiddenId').val(),
        success: function() {
        	$("#warning_msg").modal("hide"); 
        	alert("User deleted");
        }
		error: function()
		{
			alert("Error - Data not saved");
		}
    });
}*/

$(function() {
  $(document).on('change', ':file', function() {
    var input = $(this),
        numFiles = input.get(0).files ? input.get(0).files.length : 1,
        label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
  });

  $(document).ready( function() {
      $(':file').on('fileselect', function(event, numFiles, label) {

          var input = $(this).parents('.input-group').find(':text'),
              log = numFiles > 1 ? numFiles + ' files selected' : label;

          if( input.length ) {
              input.val(log);
          } else {
              if( log ) alert(log);
          }

      });
  });
});