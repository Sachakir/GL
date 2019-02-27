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
			alert("Echec de la suppression");
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
});
