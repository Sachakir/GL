$(document).ready(function() { 
	$("#leftbtn").click(function() {
		swapSelectedLine("#servicesPeople", "#allServices", "#service", "#allSelected", "#selected");
		var arr = [];
		$("#allSelected > option").each(function(){
		 		arr.push(this.value);
		});
		$("#missionMembers").val(arr);
	});
	
	$("#rightbtn").click(function() {
		swapSelectedLine("#selectedPeople", "#allSelected", "#selected", "#allServices", "#service");
		var arr = [];
		$("#allSelected > option").each(function(){
	 		arr.push(this.value);
		});
		$("#missionMembers").val(arr);
	});
	
	$("#servicesPeople").on("change", function() {
		showHide("#servicesPeople", "#allServices", "#service");
	});
	
	$("#selectedPeople").on("change", function() {
		showHide("#selectedPeople", "#allSelected", "#selected");
	});
});

function swapSelectedLine(startListId, startAllId, startElemId, endAllId, endElemId){
	
	// Il faut voir quel service est selectionne d'abord
	var service_id = $(startListId + " option:selected").val();
	var service_values;

	// Cas ou le panel choisi est all
	if(service_id == "all"){
		service_values = $(startAllId).val();
		for(var i=0; i<service_values.length; i++) {
			// Il faut pour chacune des valeurs chercher le service associé
			var actual_value = service_values[i];
			for(var search_id=1; search_id<$(startListId + " option").length; search_id++){
				// Service associé trouvé
				var arr = [];
				$(startElemId + search_id + " > option").each(function(){
				 		arr.push(this.value);
				});
				if(arr.length > 0 && arr.includes(actual_value)) {
					var text = deleteLine(startAllId, startElemId, search_id, service_values[i]);
					addLine(endAllId, endElemId, search_id, service_values[i], text);
					break;
				}
			}
		}
	}
	// Cas ou on selectionne un service
	else {
		service_values = $(startElemId + service_id).val();
		for(var i=0; i<service_values.length; i++) {
			var text = deleteLine(startAllId, startElemId, service_id, service_values[i]);
			addLine(endAllId, endElemId, service_id, service_values[i], text);
		}
	}

	// Partie tri
	var selectedAll = $(endAllId + " option");
	selectedAll.sort(function(a,b){
		return compare(a,b);
	});
	$(endAllId).html(selectedAll);

	if(service_id != "all") {
		var selected = $(endElemId + service_id + " option");
		selected.sort(function(a,b){
    		return compare(a,b);
		});
		$(endElemId + service_id).html(selected);
	}
	/////////////
}

function showHide(listId, allId, elemId) {
	console.log("Ola");
	var id = $(listId + " option:selected").val();
	var listLength = $(listId + " option").length;
	for (var i = 0; i < listLength; i++) {
		if(i == 0){
			if(id == "all"){
				$(allId).show();
			}
			else {
				$(allId).hide();
			}
		}
		else {
			if(i == id){
				$(elemId + i).show();
			}
			else {
				$(elemId + i).hide();
			}
		}
	}
}

function addLine(allId, elemId, elemNum, value, text) {
	$(allId).append("<option value=" + value + ">" + text + "</option>");
	$(elemId + elemNum).append("<option value=" + value + ">" + text + "</option>");
}

function deleteLine(allId, elemId, elemNum, value) {
	var text = $(elemId + elemNum + " option[value=" + value + "]").text();
	$(allId + " option[value=" + value + "]").remove();
	$(elemId + elemNum + " option[value=" + value + "]").remove();
	return text;
}

function compare(a, b) {
	a = a.value;
	b = b.value;
	return a-b;
}
