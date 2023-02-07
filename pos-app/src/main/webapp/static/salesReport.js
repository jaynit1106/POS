
let salesData = []


//URL FUNCTIONS
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports";
}

//JSON Converting function
function toJsonArr($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    let itr = 0;
    for(s in serialized){
        if(itr==0)data[serialized[s]['name']] = serialized[s]['value']+"T00:00:00Z";
        if(itr==1)data[serialized[s]['name']] = serialized[s]['value']+"T23:59:59Z";
        itr++;
    }
    var json = JSON.stringify(data);
    return json;
}
//API CALLING FUNCTIONS
function getSalesReport(){
    var $form = $("#sales-form");
	var json = toJsonArr($form);
    var url = getReportUrl()+"/sales";
	if(JSON.parse(json).startDate>JSON.parse(json).endDate){
		swal("Oops!","Pls select a valid range", "error");
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
                swal("Hurray", "Created Report Successfully", "success");
                console.log(response);
                salesData = response; 
                displaySalesList();
                $('#filterModal').modal('toggle');
        },
        error: function(response){
                swal("Oops!", response.responseJSON.message, "error");
        }
     });
}

function getBrandList(){
	var url = getBrandUrl()+"/unique";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			addBrandDropdown(data);
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

function getCategoryList(){
	var url = getBrandUrl() + "/unique/"+document.getElementById('brands').value;
    	$.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                addCategoryDropdown(data);
           },
           error: function(response){
                swal("Oops!", response.responseJSON.message, "error");
           }
        });
    	return false;
}
//CREATING TABLES
function displaySalesList(){
    let data = salesData;
	$('#sales-table').DataTable().destroy();
	var $tbody = $('#sales-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
        if(document.getElementById('brands').value != "All"){
            if(document.getElementById('brands').value != String(e.brand))continue;
        }

        if(document.getElementById('category').value != "All"){
            if(document.getElementById('category').value != String(e.category))continue;
        }

        var rev = parseFloat(e.revenue);
        rev=rev.toFixed(2);
		var row = '<tr>'
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + e.brand+ '</td>'
		+ '<td style="text-align:center;">'  + e.category + '</td>'
		+ '<td style="text-align:center;">'  + e.quantity + '</td>'
		+ '<td style="text-align:right;">'  + rev + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

//PAGINATION UTIL
function paginate() {
	$('#sales-table').DataTable({
	    dom: 'Bfrtip',
	    buttons:[
	        {
	            extend:'pdf',
	            customize: function (doc) {
                    doc.content[1].table.widths =
                        Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                  },
	            title:'Sales Report',
	            filename:'salesReport'
	        },
	        {
                extend:'csv',
                title:'Sales Report',
                filename:'salesReport'
            },
            {
                extend:'excel',
                title:'Sales Report',
                filename:'salesReport'
            }

	    ]
	});
}


//OTHER UTIL FUNCTIONS
function addBrandDropdown(data){
	var brandSelect = document.getElementById("brands");
	for(var i in data){
		var e = data[i];
        var brandOption = document.createElement('option');
        brandOption.text = brandOption.value = e;
        brandSelect.add(brandOption, 1);
	}
}

function addCategoryDropdown(data){
    $("#category").empty();
	var categorySelect = document.getElementById("category");
	var categoryOption = document.createElement('option');
    categoryOption.text = categoryOption.value = "All";
    categorySelect.add(categoryOption, 0);
	for(var i in data){
		var e = data[i];
        var categoryOption = document.createElement('option');
        categoryOption.text = categoryOption.value = e;
        categorySelect.add(categoryOption, 1);
	}
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

function setStartDate(){
    document.getElementById('startDate').max = document.getElementById('endDate').value;
}

function toggleFilters(){
    $('#filterModal').modal('toggle');
}
//INITIALIZATION CODE
function init(){
    $('#add').click(getSalesReport);
    $('#submit-filter').click(toggleFilters);
    document.getElementById('brands').addEventListener("change",getCategoryList);
    document.getElementById('endDate').addEventListener("change",setStartDate);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getCategoryList);
$(document).ready(setDate);


