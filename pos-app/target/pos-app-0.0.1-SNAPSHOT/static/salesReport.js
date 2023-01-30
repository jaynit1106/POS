
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
	//Set the values to update
	var url = getBrandUrl() + "/unique"
	var data = {}
	data["brand"]=document.getElementById('brands').value;
	data["category"]="";
	var json = JSON.stringify(data);
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		addCategoryDropdown(response)
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

//PAGINATION UTIL
function paginate() {
	$('#sales-table').DataTable();
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
	displaySalesList();
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
    $('#submit-filter').click(getSalesReport);
    document.getElementById('brands').addEventListener("change",getCategoryList);
    document.getElementById('category').addEventListener("change",displaySalesList);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(setDate);


