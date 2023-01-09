
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addInventory(event){
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getInventoryList();
	   		swal("Hurray", "Inventory added successfully", "success");  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});

	return false;
}

var editInventoryId=null;
function displayUpdateDialog(id){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	window.editInventoryId = id	
	return false;
}

function updateInventory(){
	var url = getInventoryUrl() + "/" + editInventoryId;
	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	
	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		swal("Hurray", "inventory updated successfully", "success");
	   		$('#edit-inventory-modal').modal('toggle');
	   		getInventoryList();   
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
	return false;
}


function getInventoryList(){
	var url = getInventoryUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayInventoryList(data);  
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
	var file = $('#inventoryFile')[0].files[0];
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
		getInventoryList();
	   	swal("Hurray", "Inventory upload successfull", "success");
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	
	var json = JSON.stringify(row);
	var url = getInventoryUrl();

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
function displayPaginatedInventory(items,rows_per_page,page){
	var $tbody = $('#inventory-table').find('tbody');
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
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
		counter++;
	}
}

function nextPage(){
	var pages = Math.ceil(inventoryData.length/5);
	var button = document.getElementById("next-button");
	var page = parseInt(button.value);
	displayPaginatedInventory(inventoryData,5,page);
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
	var pages = Math.ceil(inventoryData.length/5);
	var button = document.getElementById("previous-button");
	var page = parseInt(button.value);
	
	displayPaginatedInventory(inventoryData,5,page);
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
	if(inventoryData.length<=5){
		document.getElementById("next-button").style.visibility='hidden';
	}else{
		document.getElementById("next-button").style.visibility='visible';
	}
}


//UI DISPLAY METHODS
var inventoryData = [];
function displayInventoryList(data){
	inventoryData=[];
	for(var i in data){
		var e = data[i];
		inventoryData.push(e);
	}
	
	document.getElementById("previous-button").value="0";
	document.getElementById("next-button").value="2";
	displayPaginatedInventory(inventoryData,5,1);
	hideNext();
}

function resetUploadDialog(){
	//Reset file name
	var $file = $('#inventoryFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");
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
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function displayinventoryData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}




//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#upload-data').click(displayinventoryData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
	$('#next-button').on('click',nextPage);
	$('#previous-button').on('click',prevPage);
}

$(document).ready(init);
$(document).ready(getInventoryList);

