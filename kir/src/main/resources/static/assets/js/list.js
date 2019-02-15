function swapSelectedLine(left) {
	  var selected = $("#" + (left ? "left" : "right")).val();
	  for (var i = 0; i < selected.length; i++) {
	    addLine(left, selected[i]);
	    removeLine(left, selected[i]);
	    
		}
	}

	function removeLine(left, id) {
	  $("#" + (left ? "left" : "right") + " option[value=" + id + "]").remove();
	}

	function addLine(left, id) {
	  var text = $("#" + (left ? "left" : "right") + " option[value=" + id + "]").text();
	  $("#" + (left ? "right" : "left")).append("<option value=" + id + ">" + text + "</option>");
	}

	function sub() {
	  $("#right option").each(function()
	  {
	      $(this).attr("selected", true);
	  });
	}
