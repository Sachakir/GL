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

function myFunction() {
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
		success: function(data) 
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