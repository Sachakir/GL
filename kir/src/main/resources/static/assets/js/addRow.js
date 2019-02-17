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
      $("#vf").text($(this).find("td:nth-child(3)").text());
      if($(this).find("td:nth-child(3)").text()=="Validé"){
          $("#vf").css("color","green");
      }
      else if($(this).find("td:nth-child(3)").text()=="Refusé"){
        $("#vf").css("color","red");
      }
      else{
        $("#vf").css("color","gray");
      }
    });
    $('#ListNDFValidation').on('click', 'tbody tr', function(event) {
        
        $(this).addClass('highlight').siblings().removeClass('highlight');
        $("#mission").text("Mission: "+$(this).find("td:nth-child(2)").text());
        $("#employe").text("Nom de l'employé: "+$(this).find("td:nth-child(1)").text());
        $("#date").text("Date: "+$(this).find("td:nth-child(3)").text());
        $("#montant").text("Montant  total: "+$(this).find("td:nth-child(4)").text()+"€");
        $("#validationrh").text($(this).find("td:nth-child(5)").text());
        $("#validationcds").text($(this).find("td:nth-child(6)").text());
        $("#validationf").text($(this).find("td:nth-child(7)").text());
        $("#demandeid").text($(this).find("td:nth-child(8)").text());
        
        //var fic=$(this).find("td:nth-child(8)").text();
        var fic=$(this).find("td:nth-child(8)").text();
        //alert();
		document.getElementById('linko').href = fic;
        
      });
    document.getElementById("editer").onclick = function() {bouttonDeRow()};
    

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