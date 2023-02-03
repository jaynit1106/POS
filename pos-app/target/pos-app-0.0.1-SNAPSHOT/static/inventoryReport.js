//URL FUNCTIONS
function getInventoryReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/reports/inventory";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}

//API CALLING FUNCTIONS
function getInventoryReportList(){
	var url = getInventoryReportUrl();
	var $form = $('#filter-form')
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'POST',
	   data:json,
	   headers: {
          'Content-Type': 'application/json'
       },
	   success: function(data) {
	   		displayInventoryReportList(data);  
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

//UTIL METHODS
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
	getInventoryReportList()
}


//INITIALIZATION CODE
function init(){
    document.getElementById('brands').addEventListener("change",getCategoryList);
    document.getElementById('category').addEventListener("change",getInventoryReportList);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getCategoryList);
$(document).ready(getInventoryReportList);