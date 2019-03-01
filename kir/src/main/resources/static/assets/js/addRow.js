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
        if($(this).find("td:nth-child(1)").text()!="Vous n'avez pas de validations de remboursement")
		{
			$(this).addClass('highlight').siblings().removeClass('highlight');
			$("#mission").text("Mission: "+$(this).find("td:nth-child(2)").text());
			$("#employe").text("Nom de l'employé: "+$(this).find("td:nth-child(1)").text());
			$("#date").text("Date: "+$(this).find("td:nth-child(3)").text());
			$("#montant").text("Montant  total: "+$(this).find("td:nth-child(4)").text()+"€");
			$("#validationfinances").text($(this).find("td:nth-child(5)").text());
			$("#validationcds").text($(this).find("td:nth-child(6)").text());
			$("#validationf").text($(this).find("td:nth-child(7)").text());
			$("#demandeid").text($(this).find("td:nth-child(8)").text());
			document.getElementById("bouttons").hidden=false;
			document.getElementById("justif").hidden=false;
			var fic = "/ValidationRemb/";
			fic=fic+$(this).find("td:nth-child(8)").text();
			document.getElementById('accept').href = fic;
			
			var fic2 = "/RefusRemb/";
			fic2=fic2+$(this).find("td:nth-child(8)").text();
			document.getElementById('refuse').href = fic2;
			
			var fic3 = "/remboursements/files?file_id=";
			fic3=fic3+$(this).find("td:nth-child(9)").text();
			document.getElementById('download').href = fic3;
			/*if ($(this).find("td:nth-child(9)").text() === "")
			{
				document.getElementById('download').style.display = "none";
			}*/
			if ($(this).find("td:nth-child(9)").text() === "")
			{
				document.getElementById("justif").hidden= true;
			}
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
        if($(this).find("td:nth-child(1)").text() != "Pas de demandes de congés effectuées!")
		{
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
				document.getElementById("edit").hidden=false;
				document.getElementById("delete").hidden=false;
			}
			else{
				document.getElementById("edit").hidden=true;
				document.getElementById("delete").hidden=true;
			}
			$("#delete").attr("href","/DeleteConges/"+$(this).find("td:nth-child(7)").text());
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
				var debut = new Date(startDate);
				var fin = new Date(endDate);
				var d = debut.getTime();
				var f = fin.getTime()-86400000;
				var msg = "";
				
				if(debut.getDay()==6)
				{
					d += 2*86400000;
					msg+="Vous ne pouvez pas commencer un congé Samedi! Choisissez une date de début hors weekend!\n";
				}
				else if(debut.getDay()==0)
				{
					d += 86400000;
					msg+="Vous ne pouvez pas commencer un congé Dimanche! Choisissez une date de début hors weekend!\n";
				}
				var jour=fin.getDay()-1;
				if(jour==-1)
					jour=6;
				if(jour==6)
				{
					f += 2*86400000;
					msg+="Vous ne pouvez pas finir un congé Samedi! Choisissez une date de fin hors weekend!\n";
				}
				else if(jour==0)
				{
					f += 86400000;
					msg+="Vous ne pouvez pas fin un congé Dimanche! Choisissez une date de fin hors weekend!\n";
				}
				if(msg!="")
				{
					alert(msg);
				}
				debut = new Date(d);
				fin = new Date(f);
				var day = ("0" + debut.getDate()).slice(-2);
				var month = ("0" + (debut.getMonth() + 1)).slice(-2);
				var debutString = debut.getFullYear() + "-"+(month)+"-"+(day)+"T08:00";
				day = ("0" + fin.getDate()).slice(-2);
				month = ("0" + (fin.getMonth() + 1)).slice(-2);
				var finString = fin.getFullYear() + "-"+(month)+"-"+(day)+"T18:00";
				$("#dateD").val(debutString);
				$("#dateF").val(finString);
				$('#calendarC').fullCalendar('removeEvents');
				$('#calendarC').fullCalendar('renderEvent', {
					title: 'Congé',
					start: debutString,
					end: finString
				},true);
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
    $('#0').attr('checked',true);

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
	document.getElementById("dateD").disabled=false;
	document.getElementById("dateF").disabled=false;
	document.getElementById("submit").hidden=false;
	document.getElementById("cancel").hidden=false;
	document.getElementById("edit").hidden=true;
	document.getElementById("delete").hidden=true;
}
function annuler(){
	document.getElementById("dateD").disabled=true;
	document.getElementById("dateF").disabled=true;
	document.getElementById("submit").hidden=true;
	document.getElementById("cancel").hidden=true;
	document.getElementById("edit").hidden=false;
	document.getElementById("delete").hidden=false;
}
function verifDateD(){
	
	var dateFinWtime = $("#dateF").val().split("T");
	var dateDebutWtime = $("#dateD").val().split("T");
	var dateFin = dateFinWtime[0].split("-");
	var dateDebut = dateDebutWtime[0].split("-");
	var timeDebut = dateDebutWtime[1].split(":");
	var now = new Date();
	var tomorrow = new Date(now.getTime() + 86400000);
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
	else if(debut.getDay()==6 ){
		alert("Vous ne pouvez pas commencer un congé Samedi! Choisissez une date de début hors weekend!");
		var d = new Date($("#dateD").val());
		d = new Date(d.getTime() + 2*86400000);
		var da = ("0" + d.getDate()).slice(-2);
		var m = ("0" + (d.getMonth() + 1)).slice(-2);
		var dString = d.getFullYear()+"-"+(m)+"-"+(da)+"T"+dateDebutWtime[1] ;
		$("#dateD").val(dString);
	}
	else if(debut.getDay()==0 ){
		alert("Vous ne pouvez pas commencer un congé Dimanche! Choisissez une date de début hors weekend!");
		var d = new Date($("#dateD").val());
		d = new Date(d.getTime() + 86400000);
		var da = ("0" + d.getDate()).slice(-2);
		var m = ("0" + (d.getMonth() + 1)).slice(-2);
		var dString = d.getFullYear()+"-"+(m)+"-"+(da)+"T"+dateDebutWtime[1] ;
		$("#dateD").val(dString);
	}
	else if(fin.getTime()<debut.getTime()){
		alert("Entrez une date de début antérieure à la date de fin de votre congé.");
		$("#dateF").val($("#dateD").val());
	}
	else if(timeDebut[0]<8 || timeDebut[0]>18)
	{
		alert("Entrez un horaire de début de congé entre 8h et 18h." );
		$("#dateD").val(dateDebutWtime[0]+"T08:00");
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
	var timeFin = dateFinWtime[1].split(":");
	var now = new Date();
	var tomorrow = new Date(now.getTime() + 86400000);
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
	else if(fin.getDay()==6 ){
		alert("Vous ne pouvez pas finir un congé Samedi! Choisissez une date de fin hors weekend!");
		var d = new Date($("#dateF").val());
		d = new Date(d.getTime() + 2*86400000);
		var da = ("0" + d.getDate()).slice(-2);
		var m = ("0" + (d.getMonth() + 1)).slice(-2);
		var dString = d.getFullYear()+"-"+(m)+"-"+(da)+"T"+dateFinWtime[1] ;
		$("#dateF").val(dString);
	}
	else if(fin.getDay()==0 ){
		alert("Vous ne pouvez pas commencer un congé Dimanche! Choisissez une date de fin hors weekend!");
		var d = new Date($("#dateF").val());
		d = new Date(d.getTime() + 86400000);
		var da = ("0" + d.getDate()).slice(-2);
		var m = ("0" + (d.getMonth() + 1)).slice(-2);
		var dString = d.getFullYear()+"-"+(m)+"-"+(da)+"T"+dateFinWtime[1] ;
		$("#dateF").val(dString);
	}
	else if(fin.getTime()<debut.getTime()){
		alert("Entrez une date de fin ultérieure à la date de début de votre congé.");
		$("#dateF").val($("#dateD").val());
	}
	else if(timeFin[0]<8 || timeFin[0]>18)
	{
		alert("Entrez un horaire de fin de congé entre 8h et 18h." );
		$("#dateF").val(dateDebutWtime[0]+"T18:00");
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
	//var dureeConge = heureDiff(deb,fi);
	var dureeConge = workingDaysBetweenDates(deb,fi);
	var days = $("#days").text().split(" ");
	var rtt = $("#daysrtt").text().split(" ");
	var msg="";
	alert("\ndureeConge :"+dureeConge+"  solde congé:"+days[4]+" solde rtt:"+rtt[4]);
	if(!$("#dateD").val()){
		msg+="Entrez une date de début!\n";
	}
	if(!$("#dateF").val()){
		msg+="Entrez une date de fin!\n";
	}
	if(dureeConge>parseInt(days[4])&&$('#0').prop('checked')){
		msg+="Votre solde de congés n'est pas suffisant pour prendre un aussi long congé.\nVeuillez entrer un congé plus court.\n";
	}
	if(dureeConge>parseInt(rtt[4])&&$('#1').prop('checked')){
		msg+="Votre solde de RTT n'est pas suffisant pour prendre un aussi long congé.\nVeuillez entrer un congé plus court.\n";
	}
	if(!$("#type1").is("checked") && !$("#type2").is("checked"))
	{
		msg+="Sélectionnez un type de congé. \n";
	}
	if(msg!=""){
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
  var jours = Math.trunc(x2 - x1);
  d1 = d1.getTime() / 3600000;
  d2 = d2.getTime() / 3600000;
  var heures =d2-d1;
  if(heures%24>4)
	  jours+=1.0;
  else if(heures%24>0 && heures%24<=4)
	  jours+=0.5;
  return jours;
}
function workingDaysBetweenDates(d0, d1) {
	var holidays = ['2016-05-03','2016-05-05'];
    var startDate = d0;
    var endDate = d1;  
    // Validate input
    if (endDate < startDate) {
        return 0;
    }
    // Calculate days between dates  
    var days = heureDiff(d0,d1);
    
    // Subtract two weekend days for every week in between
    var weeks = Math.floor(days / 7);
    days -= weeks * 2;

    // Handle special cases
    var startDay = startDate.getDay();
    var endDay = endDate.getDay();
    
    // Remove weekend not previously removed.   
    if (startDay - endDay > 1) {
        days -= 2;
    }
    // Remove start day if span starts on Sunday but ends before Saturday
    if (startDay == 0 && endDay != 6) {
        days--;  
    }
    // Remove end day if span ends on Saturday but starts after Sunday
    if (endDay == 6 && startDay != 0) {
        days--;
    }
    /* Here is the code */
    /*for (var i in holidays) {
      if ((holidays[i] >= d0) && (holidays[i] <= d1)) {
      	days--;
      }
    }*/
	
    return days;
}