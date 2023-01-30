//INITIALIZING VARIABLES
let flag = 1;
//URL FUNCTIONS
function getSchedulerUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/scheduler";
}

//API CALLING FUNCTIONS
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

//UI FUNCTIONS
function displaySchedulerList(data){
	$('#scheduler-table').DataTable().destroy();
	var $tbody = $('#scheduler-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		if(flag!=1){
			var start = document.getElementById('startDate').value;
			var end = document.getElementById('endDate').value;
			if(String(e.date)<start || String(e.date)>end)continue;
		}
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

//PAGINATE FUNCTIONS
function paginate() {
	$('#scheduler-table').DataTable();
}

//UTIL METHODS
function getFilteredList(){
	if( document.getElementById('startDate').value>document.getElementById('endDate').value){
		swal("Oops!","Pls select a valid range", "error");
		return;
	}
	flag = 0;
	getSchedulerList();
}

function getFullList(){
	flag = 1;
	getSchedulerList();
}

function setDate(){
    var today = new Date();
    var dd = String(today.getDate()).padStart(2, '0');
    var mm = String(today.getMonth() + 1).padStart(2, '0'); //January is 0!
    var yyyy = today.getFullYear();

    today = yyyy+"-"+mm+"-"+dd;
    document.getElementById('startDate').max = today;
    document.getElementById('endDate').max = today;
    document.getElementById('startDate').value = today;
    document.getElementById('endDate').value = today;
}

//INITIALIZATION CODE
function init(){
	
    $('#submit-filter').click(getFilteredList);
	$('#all-data').click(getFullList);
}

$(document).ready(init);
$(document).ready(getSchedulerList);
$(document).ready(setDate);

