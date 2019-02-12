$(document).ready(function () {
    var counter = 0;

    $("#addrow").on("click", function () {
        var newRow = $("<tr>");
        var cols = "";

        cols += '<td><input type="text" style="width:100%;" name="name' + counter + '"/></td>';
        cols += '<td style="padding-right:0px;"><input type="date" style="width:100%;" style="width:143px;" name="mail' + counter + '"/></td>';
        cols += '<td style="padding-bottom:0px;padding-top:012px;"><input type="number" style="width:100%;padding-top:12px;height:30px;padding-bottom:12px;" name="phone' + counter + '"/></td>';
	
		cols += '<td><input type="file" name="name' + counter + '"/></td>';

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
    });

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
    { $('#addrow').css('visibility','invisible');}