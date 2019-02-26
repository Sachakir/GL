$(document).ready(function () {
    var counter = 0;

    $("#addrow").on("click", function () {
        var newRow = $("<tr>");
        var cols = "";

        cols += '<td><input type="text"/></td>';
        cols += '<td><input type="date"/></td>';
        cols += '<td><input type="number"/></td>';
	
		cols += '<td><input type="file"/></td>';

        cols += '<td><input type="button" class="ibtnDel btn btn-md btn-danger "  value="Delete"></td>';
        newRow.append(cols);
        $("table.order-list").append(newRow);
        counter++;
    });



    $("table.order-list").on("click", ".ibtnDel", function (event) {
        $(this).closest("tr").remove();       
        counter -= 1
    });
    
    $('#myTable').on('click', 'tbody tr', function(event) {
        
      $(this).addClass('highlight').siblings().removeClass('highlight');
      $("#nom").val($(this).find("td:nth-child(1)").text());
      $("#date").text("Date: "+$(this).find("td:nth-child(3)").text());
    });
    $('#ListNDFValidation').on('click', 'tbody tr', function(event) {
        
        $(this).addClass('highlight').siblings().removeClass('highlight');
        $("#mission").text("Mission: "+$(this).find("td:nth-child(2)").text());
        $("#employe").text("Nom de l'employé: "+$(this).find("td:nth-child(1)").text());
        $("#date").text("Date: "+$(this).find("td:nth-child(3)").text());
        $("#montant").text("Montant  total: "+$(this).find("td:nth-child(4)").text()+"€");
        $("#validationfinances").text($(this).find("td:nth-child(5)").text());
        $("#validationcds").text($(this).find("td:nth-child(6)").text());
        $("#validationf").text($(this).find("td:nth-child(7)").text());
        $("#demandeid").text($(this).find("td:nth-child(8)").text());
        
        var fic = "/ValidationRemb/";
        fic=fic+$(this).find("td:nth-child(8)").text();
		document.getElementById('accept').href = fic;
		
		var fic2 = "/RefusRemb/";
        fic2=fic2+$(this).find("td:nth-child(8)").text();
		document.getElementById('refuse').href = fic2;
		
		var fic3 = "/remboursements/files?file_id=";
        fic3=fic3+$(this).find("td:nth-child(9)").text();
		document.getElementById('download').href = fic3;
		if ($(this).find("td:nth-child(9)").text() === "")
		{
			document.getElementById('download').style.display = "none";
		}

        
      });
	  $('#demandes').on('click', 'tbody tr', function(event) {
        if($(this).find("td:nth-child(1)").text() != "Il n'y a pas de demandes de congé!")
		{
			$(this).addClass('highlight').siblings().removeClass('highlight');
			$("#dateD").text("Date de début: "+$(this).find("td:nth-child(2)").text());
			var res = $(this).find("td:nth-child(1)").text().split(" ");
			$("#nom").text("Nom : "+ res[0]);
			$("#prenom").text("Prénom : "+ res[1]);
			
			$("#dateF").text("Date de fin: "+$(this).find("td:nth-child(3)").text());
			$("#vRH").text("Validation RH: "+$(this).find("td:nth-child(4)").text());
			$("#vC").text("Validation Chef de Service: "+$(this).find("td:nth-child(5)").text());
			
			$("#accept").attr("href","/ValidationConges/"+$(this).find("td:nth-child(7)").text());
			$("#refuse").attr("href","/RefusConges/"+$(this).find("td:nth-child(7)").text());
			document.getElementById("bouttons").hidden=false;
		}
        
      });
	  $('#demandesGestion').on('click', 'tbody tr', function(event) {
        
        $(this).addClass('highlight').siblings().removeClass('highlight');
		var debut = $(this).find("td:nth-child(2)").text();
		var d = debut.substring(6,10)+"-"+debut.substring(3,5)+"-"+debut.substring(0,2)+"T"+debut.substring(11,16);
		
		$("#dateD").val(d);
		var fin = $(this).find("td:nth-child(3)").text();
		var f = fin.substring(6,10)+"-"+fin.substring(3,5)+"-"+fin.substring(0,2)+"T"+fin.substring(11,16);
		
		$("#dateF").val(f);
		
		$("#cid").val( $(this).find("td:nth-child(7)").text());
        $("#vRH").text("Validation RH: "+$(this).find("td:nth-child(4)").text());
        $("#vC").text("Validation Chef de Service: "+$(this).find("td:nth-child(5)").text());
        if($(this).find("td:nth-child(4)").text() == "En attente" && $(this).find("td:nth-child(5)").text() == "En attente"){
			/*$("#edit").show();
			$("#delete").show();*/
			document.getElementById("edit").hidden=false;
			document.getElementById("delete").hidden=false;
		}
		else{
			/*$("#edit").hide();
			$("#delete").hide();*/
			document.getElementById("edit").hidden=true;
			document.getElementById("delete").hidden=true;
		}
        
      });
	  
	var today = new Date();
	var d = $.fullCalendar.moment(today);
	$('#calendarC').fullCalendar({
		defaultDate: d,
		editable: true,
		eventLimit: true,
		selectable: true,
		
		
		select: function(startDate, endDate) {
			if(today.getTime()<startDate && today.getTime()<endDate){
				
				$("#dateD").val(startDate.format()+"T08:00");
				$("#dateF").val(endDate.format()+"T08:00");
				$('#calendarC').fullCalendar('removeEvents');
				$('#calendarC').fullCalendar('renderEvent', {
					title: 'Congé',
					start: startDate,
					end: endDate
				});
			}
		}
	});
	
	$('#calendarCGestion').fullCalendar({
		defaultDate: d,
		editable: true,
		eventLimit: true,
		selectable: true,
		
		
		
	});
	$("#demandesGestion tbody").find('tr').each(function(i,el){
		var $tds = $(this).find('td');
		
		var d1 = $tds.eq(1).text().split(" ");
		var f1 = $tds.eq(2).text().split(" ");
		var d = d1[0].split("/");
		var f = f1[0].split("/");
		var titre = d[0]+'/'+d[1]+ ' --> ' + +f[0]+'/'+f[1];
		var debut =  moment(d[2]+"-"+d[1]+"-"+d[0]+"T"+d1[1]);
		var fin = moment(f[2]+"-"+f[1]+"-"+f[0]+"T"+f1[1]);
		if($tds.eq(3).text()=="En attente" || $tds.eq(4).text()=="En attente"){
			var c = '#595959';
		}
		else if($tds.eq(3).text()=="Refusé" || $tds.eq(4).text()=="Refusé"){
			var c = '#ff0000';
		}
		else{
			var c = '#11af00';
		}
		$('#calendarCGestion').fullCalendar('renderEvent', {
              title: titre,
              start: debut,
              end: fin,
			  color: c
            },true);
	});
    document.getElementById("editer").onclick = function() {bouttonDeRow()};
	document.getElementById("edit").onclick = function() {editer()};
    

});



function calculateRow(row) {
    var price = +row.find('input[name^="price"]').val();

}

function calculateGrandTotal() {
    var grandTotal = 0;
    $("table.order-list").find('input[name^="price"]').each(function () {
        grandTotal += +$(this).val();
    });
    $("#grandtotal").text(grandTotal.toFixed(2));
}
function bouttonDeRow()
{ 
    
    if(document.getElementById("ndf").value=="locked"){
        document.getElementById("addrow").disabled=true;
        document.getElementById("dropdownMenuButton").disabled=true;
        document.getElementById("nom").disabled=true;
        document.getElementById("date").disabled=true;
        $("#frais").find("input,button,textarea,select").prop("disabled", true);
        document.getElementById("ndf").value="unlocked"
        document.getElementById("editer").textContent="Enregistrer";
    }
    else{
        document.getElementById("addrow").disabled=false;
        document.getElementById("dropdownMenuButton").disabled=false;
        document.getElementById("nom").disabled=false;
        document.getElementById("date").disabled=false;
        $("#frais").find("input,button,textarea,select").prop("disabled", false);
        document.getElementById("ndf").value="locked";
        document.getElementById("editer").textContent="Editer";
    }
}
function editer(){
	if(document.getElementById("detailsConge").value=="locked"){
		document.getElementById("dateD").disabled=true;
		document.getElementById("dateF").disabled=true;
		document.getElementById("detailsConge").value="unlocked";
		document.getElementById("submit").hidden=true;
	}
	else{
		document.getElementById("dateD").disabled=false;
		document.getElementById("dateF").disabled=false;
		document.getElementById("detailsConge").value="locked";
		document.getElementById("submit").hidden=false;
	}
	
	
}

function verifDateD(){
	
	var dateFinWtime = $("#dateF").val().split("T");
	var dateDebutWtime = $("#dateD").val().split("T");
	var dateFin = dateFinWtime[0].split("-");
	var dateDebut = dateDebutWtime[0].split("-");
	var now = new Date();
	var tomorrow = new Date(now.getTime() + 86400000);
	/*var fin = new Date(dateFin[0],dateFin[1]-1,dateFin[2]);
	var debut = new Date(dateDebut[0],dateDebut[1]-1,dateDebut[2]);*/
	var fin = new Date($("#dateF").val());
	var debut = new Date($("#dateD").val());
    var day = ("0" + tomorrow.getDate()).slice(-2);
    var month = ("0" + (tomorrow.getMonth() + 1)).slice(-2);
    var dateString = tomorrow.getFullYear()+"-"+(month)+"-"+(day)+"T"+dateDebutWtime[1] ;
	
	
	if(debut.getTime()<now.getTime()){
		alert("Entrez une date de début ultérieure à aujourd'hui");
		$("#dateD").val(dateString);
		if(!$("#dateF").val()){
			$("#dateF").val(dateString);
		}
	}
	else if(fin.getTime()<debut.getTime()){
		alert("Entrez une date de début antérieure à la date de fin de votre congé.");
		$("#dateF").val($("#dateD").val());
	}
	$('#calendarC').fullCalendar('removeEvents');
	$('#calendarC').fullCalendar('renderEvent', {
		title: 'congé',
		start: $("#dateD").val(),
		end: $("#dateF").val()
	},true);
	
}
function verifDateF(){
	var dateFinWtime = $("#dateF").val().split("T");
	var dateDebutWtime = $("#dateD").val().split("T");
	var dateFin = dateFinWtime[0].split("-");
	var dateDebut = dateDebutWtime[0].split("-");
	var now = new Date();
	var tomorrow = new Date(now.getTime() + 86400000);
	/*var fin = new Date(dateFin[0],dateFin[1]-1,dateFin[2]);
	var debut = new Date(dateDebut[0],dateDebut[1]-1,dateDebut[2]);*/
	var fin = new Date($("#dateF").val());
	var debut = new Date($("#dateD").val());
    var day = ("0" + tomorrow.getDate()).slice(-2);
    var month = ("0" + (tomorrow.getMonth() + 1)).slice(-2);
    var dateString = tomorrow.getFullYear()+"-"+(month)+"-"+(day)+"T"+dateFinWtime[1] ;
	if(fin.getTime()<now.getTime()){
		alert("Entrez une date de fin ultérieure à aujourd'hui");
		
		if(!$("#dateD").val()){
			$("#dateF").val(dateString);
			$("#dateD").val(dateString);
		}
		else{
			$("#dateF").val($("#dateD").val());
		}
	}
	else if(fin.getTime()<debut.getTime()){
		alert("Entrez une date de fin ultérieure à la date de début de votre congé.");
		$("#dateF").val($("#dateD").val());
	}
	$('#calendarC').fullCalendar('removeEvents');
	$('#calendarC').fullCalendar('renderEvent', {
		title: 'congé',
		start: $("#dateD").val(),
		end: $("#dateF").val()
	},true);
	
}
function validationAjoutUtilisateur(){
	
	var deb = new Date($("#dateD").val());
	var fi = new Date($("#dateF").val());
	var dureeConge = heureDiff(deb,fi);
	var days = $("#days").text().split(" ");
	var rtt = $("#daysrtt").text().split(" ");
	var msgBool =0;
	var msg="";
	alert("\ndureeConge :"+dureeConge+"  solde congé:"+days[4]+" solde rtt:"+rtt[4]);
	if(!$("#dateD").val()){
		msg+="Entrez une date de début!\n";
		msgBool=1;
	}
	if(!$("#dateF").val()){
		msg+="Entrez une date de fin!\n";
		msgBool=1;
	}
	if(dureeConge>parseInt(days[4])&&$('#0').prop('checked')){
		msg+="Votre solde de congés n'est pas suffisant pour prendre un aussi long congé.\nVeuillez entrer un congé plus court.\n";
		msgBool=1;
	}
	if(dureeConge>parseInt(rtt[4])&&$('#1').prop('checked')){
		msg+="Votre solde de RTT n'est pas suffisant pour prendre un aussi long congé.\nVeuillez entrer un congé plus court.\n";
		msgBool=1;
	}
	if(msgBool){
		alert(msg);
		return false;
	}
	return true;
}

function dayDiff(d1, d2)
{
  d1 = d1.getTime() / 86400000;
  d2 = d2.getTime() / 86400000;
  return new Number(d2 - d1).toFixed(0);
}
function heureDiff(d1, d2)
{
  
  var x1 = d1.getTime() / 86400000;
  var x2 = d2.getTime() / 86400000;
  var jours = new Number(x2 - x1).toFixed(0);
  d1 = d1.getTime() / 3600000;
  d2 = d2.getTime() / 3600000;
  var heures =d2-d1;
  alert("heure: "+ heures + " jours: " + jours);
  if(heures%24>4)
	  jours+=1.0;
  else if(heures%24>0 && heures%24<=4)
	  jours+=0.5;
  return jours;
}
