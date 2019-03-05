function showMDP()
{
	var x = document.getElementById("mdp");
	if (x.type === "password") {
		x.type = "text";
	} else {
		x.type = "password";
	}
}

function showMDP2()
{
	var x = document.getElementById("mdp2");
	if (x.type === "password") {
		x.type = "text";
	} else {
		x.type = "password";
	}
}

function resetFile () {
	  var input = $(":file");
	  input.trigger('fileselect', [0, ""]);
	  input.val("");
}

function showOrHideFileForm(input) {
	if($(input).is(":checked")){
		$('.update-file').show();
	}
	else {
		$('.update-file').hide();
	}
}

function sendData()
{
	$.ajax(
	{
		type: "POST",
		data: $("#addUserForm").serialize(),
		cache: false,
		url: "/administration/gestion-utilisateurs/create",
		success: function(errors) 
		{
			$("#nom_error").hide();
			$("#prenom_error").hide();
			$("#numTel_error").hide();
			$("#heurestravail_error").hide();
			$("#mdp_error").hide();
			$("#user_exists_error").hide();
			
			if($.isEmptyObject(errors)) {
				$("#formulaire").modal("hide"); 
				$('#success_msg').modal('show');
				location.reload();
			}
			else {
				$.each(errors, function(id, message) {
					$(id).html(message);
					$(id).show();
				});
			}
		},
		error: function()
		{
			$("#formulaire").modal("hide"); 
			$('#failure_msg').modal('show');
		}
	});
}

function sendService()
{
	$.ajax(
	{
		type: "POST",
		data: $("#addServiceForm").serialize(),
		cache: false,
		url: "/administration/gestion-services/create",
		success: function(errors) 
		{
			$("#nom_error").hide();
			$("#service_exists_error").hide();
			
			if($.isEmptyObject(errors)) {
				$("#formulaire").modal("hide"); 
				$('#success_msg').modal('show');
			}
			else {
				$.each(errors, function(id, message) {
					$(id).html(message);
					$(id).show();
				});
			}
		},
		error: function()
		{
			$("#formulaire").modal("hide"); 
			$('#failure_msg').modal('show');
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
		url: "/administration/gestion-utilisateurs/delete?id="+idSuppr,
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

function deleteService() {
	$.ajax(
	{
		url: "/administration/gestion-services/delete?id="+idSuppr,
		success: function() 
		{ 
			$("#warning_msg").modal("hide");
			$("#success_delete").modal("show");
		},
		error: function()
		{
			$("#warning_msg").modal("hide");
			$("#failure_msg").modal("show");
		}
	});
}

function deleteRemboursement() {
	$.ajax(
	{
		url: "/remboursements/delete?id="+idSuppr,
		success: function() 
		{ 
			$("#formulaire").modal("hide");
			location.reload();
		},
		error: function()
		{
			alert("Echec de la suppression")
		}
	});
}

function deleteMission() {
	$.ajax(
	{
		url: "/missions/delete?id="+idSuppr,
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
  
  $("#success_msg").on('hidden.bs.modal', function(){
	    location.reload();
  });
});
