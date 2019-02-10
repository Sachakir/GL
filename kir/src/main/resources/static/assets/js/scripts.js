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

			});
			$("#notificationLink1").click(function()
			{
				window.location.href="https://www.google.fr/"
			});