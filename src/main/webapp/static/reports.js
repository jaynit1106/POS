function getSchedulerUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/scheduler";
}

function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/report";
}

function getSchedulerList(){
	var url = getSchedulerUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displaySchedulerList(data);  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

function displaySchedulerList(data){
	$('#scheduler-table').DataTable().destroy();
	var $tbody = $('#scheduler-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + String(e.date).split("-").reverse().join("-") + '</td>'
		+ '<td>'  + e.invoiced_orders_count + '</td>'
		+ '<td>'  + e.invoiced_items_count + '</td>'
		+ '<td>'  + e.total_revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

function paginate() {
	$('#scheduler-table').DataTable();
}

function getSalesReport(){
    var $form = $("#report-form");
	var json = toJson($form);
    var url = getReportUrl()+"/sales";
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
$(document).ready(getSchedulerList);

