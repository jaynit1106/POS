
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getBrandList();
	   		swal("Hurray", "Brand added successfully", "success");  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});

	return false;
}

var editBrandId=null;
function displayUpdateDialog(id){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	window.editBrandId = id	
	return false;
}

function updateBrand(){
	var url = getBrandUrl() + "/" + editBrandId;
	var $form = $("#brand-edit-form");
	var json = toJson($form);
	
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		swal("Hurray", "Brand updated successfully", "success");
	   		$('#edit-brand-modal').modal('toggle');
	   		getBrandList();   
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
	return false;
}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrandList(data);  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

// FILE UPLOAD METHODS
var fileData = [];
var errorData = [];
var processCount = 0;


function processData(){
	var file = $('#brandFile')[0].files[0];
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
		alert("Size too large");
		return;
	}
	
	//To avoid empty files
	if(fileData.length==0){
		alert("File is empty");
		return;
	}
	
	//If everything processed then return
	if(processCount==fileData.length){
		getBrandList();
	   	swal("Hurray", "Brand upload successfull", "success");
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getBrandUrl();

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
function displayPaginatedBrands(items,rows_per_page,page){
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	page--;
	let start = rows_per_page*page;
	let end = start + rows_per_page;
	let paginatedItems = items.slice(start,end);
	var counter = (page)*rows_per_page;
	counter++;

	for(let i=0 ; i<paginatedItems.length;i++){
		var e = paginatedItems[i];
		var buttonHtml = '<button onclick="displayUpdateDialog(' + e.id + ')">edit</button>';
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		counter++;
	}
}

function nextPage(){
	var pages = Math.ceil(brandData.length/5);
	var button = document.getElementById("next-button");
	var page = parseInt(button.value);
	displayPaginatedBrands(brandData,5,page);
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
	var pages = Math.ceil(brandData.length/5);
	var button = document.getElementById("previous-button");
	var page = parseInt(button.value);
	
	displayPaginatedBrands(brandData,5,page);
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
	if(brandData.length<=5){
		document.getElementById("next-button").style.visibility='hidden';
	}else{
		document.getElementById("next-button").style.visibility='visible';
	}
}


//UI DISPLAY METHODS
var brandData = [];
function displayBrandList(data){
	brandData=[];
	for(var i in data){
		var e = data[i];
		brandData.push(e);
	}
	
	document.getElementById("previous-button").value="0";
	document.getElementById("next-button").value="2";
	displayPaginatedBrands(brandData,5,1);
	hideNext();
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#brandFile');
	$file.val('');
	$('#brandFileName').html("Choose File");
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
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayBrandData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}




//INITIALIZATION CODE
function init(){
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayBrandData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
	$('#next-button').on('click',nextPage);
	$('#previous-button').on('click',prevPage);
}

$(document).ready(init);
$(document).ready(getBrandList);

