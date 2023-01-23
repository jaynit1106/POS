//URL FUNCTIONS
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report/inventory";
}

//API CALLING FUNCTIONS
function getInventoryReportList(){
    console.log('hello');
	var url = getInventoryReportUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryReportList(data);  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

//UI METHODS
function displayInventoryReportList(data){
	$('#InventoryReport-table').DataTable().destroy();
	var $tbody = $('#InventoryReport-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

//PAGINATE METHODS
function paginate() {
	$('#InventoryReport-table').DataTable();
}


//INITIALIZATION CODE
function init(){
}

$(document).ready(init);
$(document).ready(getInventoryReportList);

