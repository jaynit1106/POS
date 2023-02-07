//INITIALIZING VARIABLES
let flag = 1;
//URL FUNCTIONS
function getSchedulerUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/scheduler";
}

//API CALLING FUNCTIONS
function getSchedulerList(){
	var $form = $("#sales-form");
    var json = toJson($form);
    var url = getSchedulerUrl();
    if( document.getElementById('startDate').value>document.getElementById('endDate').value){
        swal("Oops!","please select a valid range", "error");
        return;
    }

    $.ajax({
       url: url,
       type: 'POST',
       data: json,
       headers: {
        'Content-Type': 'application/json'
       },
       success: function(response) {
            displaySchedulerList(response);
            swal("Hurray", "report created successfully", "success");
            $('#filterModal').modal('toggle');
       },
       error: function(response){
            swal("Oops!", response.responseJSON.message, "error");
       }
    });

    return false;
}

//UI FUNCTIONS
function displaySchedulerList(data){
	$('#scheduler-table').DataTable().destroy();
	var $tbody = $('#scheduler-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var rev = parseFloat(e.total_revenue);
        rev=rev.toFixed(2);

		var row = '<tr>'
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + String(e.date).split("-").reverse().join("-") + '</td>'
		+ '<td style="text-align:center;">'  + e.invoiced_orders_count + '</td>'
		+ '<td style="text-align:center;">'  + e.invoiced_items_count + '</td>'
		+ '<td style="text-align:right;">'  + rev + '</td>'
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

function setStartDate(){
    document.getElementById('startDate').max = document.getElementById('endDate').value;
}

function toggleFilters(){
    $('#filterModal').modal('toggle');
}
//INITIALIZATION CODE
function init(){
    $('#add').click(getSchedulerList);
    $('#submit-filter').click(toggleFilters);
    document.getElementById('endDate').addEventListener("change",setStartDate);

}

$(document).ready(init);
$(document).ready(setDate);

