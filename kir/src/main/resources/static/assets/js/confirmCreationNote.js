function confirmCreationNote(urlNote, urlRedirect, message) {
	var isConfirmed = confirm(message);
	if(isConfirmed)
		window.location.href = urlNote;
	else
		window.location.href = urlRedirect;
}