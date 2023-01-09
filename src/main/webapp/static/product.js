var editProductId=null;


function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}


//BUTTON ACTIONS
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

function addBrandCategoryDropdown(data){
	var brandSelect = document.getElementById("brands");
	var categorySelect = document.getElementById("category");
	for(var i in data){
		var e = data[i];
		
		var brandOption = document.createElement('option');
        brandOption.text = brandOption.value = e.brand;
        brandSelect.add(brandOption, 1);
        
		var categoryOption = document.createElement('option');
        categoryOption.text = categoryOption.value = e.category;
        categorySelect.add(categoryOption, 1);
        
	}
}

function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			addBrandCategoryDropdown(data);   		   
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
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
function diplayPaginatedProducts(items,rows_per_page,page){
	var $tbody = $('#product-table').find('tbody');
	$tbody.empty();
	page--;
	let start = rows_per_page*page;
	let end = start + rows_per_page;
	let paginatedItems = items.slice(start,end);
	var counter = (page)*rows_per_page;
	counter++;

	for(let i=0 ; i<paginatedItems.length;i++){
		var e = paginatedItems[i];
		var buttonHtml = ' <button onclick="toggleEditProduct(' + e.id + ')">edit</button>';
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>' + e.category + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		counter++;
	}
}

function nextPage(){
	var pages = Math.ceil(productData.length/5);
	var button = document.getElementById("next-button");
	var page = parseInt(button.value);
	diplayPaginatedProducts(productData,5,page);
	if(page == pages){
		button.style.visibility = 'hidden';

	}else{
		button.style.visibility = 'visible';
	}
	page++;


	button.value=page.toString();
	
	button = document.getElementById("previous-button");
	page = parseInt(button.value);
	page++;
	button.value = page.toString();
	button.style.visibility = 'visible';
	
}

function prevPage(){
	var button = document.getElementById("previous-button");
	var page = parseInt(button.value);
	
	diplayPaginatedProducts(productData,5,page);
	page--;
	if(page == 0){
		button.value="0";
		button.style.visibility = 'hidden';
		document.getElementById("next-button").value="2";
		hideNext();
		return;
	}else{
		button.style.visibility = 'visible';
	}
	button.value=page.toString();

	button = document.getElementById("next-button");
	page = parseInt(button.value);
	page--;
	button.value = page.toString();
	button.style.visibility = 'visible';
}

function hideNext(){
	if(productData.length<=5){
		document.getElementById("next-button").style.visibility='hidden';
	}else{
		document.getElementById("next-button").style.visibility='visible';
	}
}

//UI DISPLAY METHODS

var productData = [];
function displayProductList(data){
	productData=[];
	for(var i in data){
		var e = data[i];
		productData.push(e);
	}
	document.getElementById("previous-button").value="0";
	document.getElementById("next-button").value="2";
	diplayPaginatedProducts(productData,5,1);
	hideNext();
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
	$('#productFileName').html(fileName);
}

function toggleEditProduct(id){
	$('#edit-product-modal').modal('toggle');
	window.editProductId=id;
	return;
}

function toggleAddProduct(id){
	resetUploadDialog();
	$('#upload-product-modal').modal('toggle');
	return;
}




//INITIALIZATION CODE
function init(){
	$('#add-product').click(addProduct);
	$('#update-product').click(updateProduct);
	$('#upload-data').click(toggleAddProduct);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#productFile').on('change', updateFileName);
	$('#next-button').on('click',nextPage);
	$('#previous-button').on('click',prevPage);
		
}

$(document).ready(init);
$(document).ready(getProductList);
$(document).ready(getBrandList);
