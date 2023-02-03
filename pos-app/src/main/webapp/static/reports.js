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
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + String(e.date).split("-").reverse().join("-") + '</td>'
		+ '<td style="text-align:center;">'  + e.invoiced_orders_count + '</td>'
		+ '<td style="text-align:center;">'  + e.invoiced_items_count + '</td>'
		+ '<td style="text-align:center;">'  + e.total_revenue + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

//PAGINATE FUNCTIONS
function paginate() {
	$('#scheduler-table').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend:'pdf',
                customize: function (doc) {
                      doc.content[1].table.widths =
                          Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                    },
                title:'Daily Report',
                filename:'dailyReport'
            },
            {
                  extend:'csv',
                  title:'Daily Report',
                  filename:'dailyReport'
              },
              {
                  extend:'excel',
                  title:'Daily Report',
                  filename:'dailyReport'
              }

        ]
    }
	);
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
    var prevYear = new Date().getFullYear()-1;

    //for 7 days range
    var days=7;
    var date = new Date();
    var last = new Date(date.getTime() - (days * 24 * 60 * 60 * 1000));
    var day =String(last.getDate()).padStart(2, '0');
    var month=String(last.getMonth()+1).padStart(2, '0');
    var year=last.getFullYear();
    var beforeWeek = year+"-"+month+"-"+day;

    today = yyyy+"-"+mm+"-"+dd;
    minDate = prevYear+"-"+mm+"-"+dd;
    document.getElementById('startDate').max = today;
    document.getElementById('startDate').min = minDate;
    document.getElementById('endDate').max = today;
    document.getElementById('endDate').min = minDate;
    document.getElementById('startDate').value = beforeWeek;
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

