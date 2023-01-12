

function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function displaySalesList(data){
	$('#sales-table').DataTable().destroy();
	var $tbody = $('#sales-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.brand+ '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>'  + e.revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

function paginate() {
	$('#sales-table').DataTable();
}

function getSalesReport(){
    var $form = $("#sales-form");
	var json = toJson($form);
    var url = getReportUrl()+"/sales";
    console.log(url);
    $.ajax({
        url: url,
        type: 'POST',
        data: json,
        headers: {
            'Content-Type': 'application/json'
        },	   
        success: function(response) {
                swal("Hurray", "Created Report Successfully", "success");
                console.log(response);
                displaySalesList(response);  
        },
        error: function(response){
                swal("Oops!", response.responseJSON.message, "error");
        }
     });
}

//INITIALIZATION CODE
function init(){
    $('#submit-filter').click(getSalesReport);
}

$(document).ready(init);

