var editProductId=null;

//URL FUNCTIONS
function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}


//API CALLING FUNCTIONS
function addProduct(event){
	//Set the values to update
	var $form = $("#product-form");
	var json = toJson($form);
	var url = getProductUrl();
	
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   		swal("Hurray", "Product added successfully", "success");  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});

	return false;
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

function getProductList(){
	var url = getProductUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductList(data);   
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

function updateProduct(){
	var url = getProductUrl() + "/" + editProductId;
	var $form = $("#product-edit-form");
	var json = toJson($form);
	
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getProductList();
	   		swal("Hurray", "Product updated successfully", "success");
			$('#edit-product-modal').modal('toggle');
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
	return false;
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#productFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){
	//Update progress
	updateUploadDialog();
	
	//To avoid large files
	if(fileData.length>5000){
		swal("Oops!","File size too large", "error");
		return;
	}
	
	//To avoid empty files
	if(fileData.length==0){
		swal("Oops!","File is empty", "error");
		return;
	}
	
	//If everything processed then return
	if(processCount==fileData.length){
		getProductList();
		swal("Hurray", "Upload successfull", "success");
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getProductUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		uploadRows();  
	   },
	   error: function(response){
	   		row.error=response.responseText
	   		errorData.push(row);
	   		uploadRows();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}


// PAGINATION METHODS
function paginate() {
	$('#product-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

//UI DISPLAY METHODS
productData=[]
function displayProductList(data){
	$('#product-table').DataTable().destroy();
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	productData=[];
	var counter=1;
	for(var i in data){
		var e = data[i];
		let id = counter-1;
		productData.push(e);
		var buttonHtml = '<button class="btn btn-dark" onclick="toggleEditProduct(' + id + ')"><i class="fa-solid fa-pen-to-square"></i></button>';
		var row = '<tr>'
		+ '<td style="text-align:center">' + counter + '</td>'
		+ '<td style="text-align:center">' + e.brand + '</td>'
		+ '<td style="text-align:center">'  + e.category + '</td>'
		+ '<td style="text-align:center">'  + e.name + '</td>'
		+ '<td style="text-align:right">'  + e.mrp + '</td>'
		if(role=='supervisor'){
            row+=' style="text-align:center" <td th:if="${info.getRole()}=='+role+'">' + buttonHtml + '</td>';
        }
        row += '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#productFile');
	$file.val('');
	$('#productFileName').html("Choose File");
	//Reset various counts
	processCount = 0;
	fileData = [];
	errorData = [];
	//Update counts	
	updateUploadDialog();
}

function updateUploadDialog(){
	$('#rowCount').html("" + fileData.length);
	$('#processCount').html("" + processCount);
	$('#errorCount').html("" + errorData.length);
}

function updateFileName(){
	var $file = $('#productFile');
	var fileName = $file.val();
	$('#productFileName').html(String(fileName).split(/(\\|\/)/g).pop());
}

function toggleEditProduct(ids){
	$('#edit-product-modal').modal('toggle');
	document.getElementById('product-edit-form').reset();
	document.getElementById('editBrand').value = productData[ids].brand;
	document.getElementById('editCategory').value = productData[ids].category;
	document.getElementById('editName').value = productData[ids].name;
	document.getElementById('editBarcode').value = productData[ids].barcode;
	document.getElementById('editMrp').value = productData[ids].mrp;
	window.editProductId=productData[ids].id;
	return;
}

function toggleAddProduct(id){
	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
	return;
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
    categoryOption.text = categoryOption.value = "Category";
    categorySelect.add(categoryOption, 0);
	for(var i in data){
		var e = data[i];
        var categoryOption = document.createElement('option');
        categoryOption.text = categoryOption.value = e;
        categorySelect.add(categoryOption, 1);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(toggleAddProduct);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
    $('#brands').on('change',getCategoryList);
		
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);
