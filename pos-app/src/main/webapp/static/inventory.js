//INITIALIZING VARIABLES
var editInventoryId=null;
//URL FUNCTIONS
function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/inventorys";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
}

//API CALLING FUNCTIONS
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

function getBarcodeList(){
	var url = getProductUrl()+"/barcode";
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
			addBarcodeDropdown(data);
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
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
function paginate() {
	$('#inventory-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

//UI DISPLAY METHODS
inventoryData = []
function displayInventoryList(data){
	$('#inventory-table').DataTable().destroy();
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	var counter=1;
	inventoryData=[];
	for(var i in data){
		var e = data[i];
		inventoryData.push(e)
		let id =counter-1;
		var buttonHtml = '<button class="btn btn-dark" onclick="displayUpdateDialog(' + id + ')"><i class="fa-solid fa-pen-to-square"></i></button>';
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		if(role=='supervisor'){
            row+='<td th:if="${info.getRole()}=='+role+'">' + buttonHtml + '</td>';
        }
        row += '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
}

function displayUpdateDialog(ids){
	$('#edit-inventory-modal').modal('toggle');
	document.getElementById('inventory-edit-form').reset();
	document.getElementById('editQuantity').value = inventoryData[ids].quantity;
	//Get the ID
	window.editInventoryId = inventoryData[ids].id;	
	return false;
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
	$('#inventoryFileName').html(String(fileName).split(/(\\|\/)/g).pop());
}

function displayinventoryData(){
 	resetUploadDialog(); 	
	$('#upload-inventory-modal').modal('toggle');
}

//UTIL METHODS
function addBarcodeDropdown(data){
	var barcodeSelect = document.getElementById("barcodes");
	for(var i in data){
		var e = data[i];
        var barcodeOption = document.createElement('option');
        barcodeOption.text = barcodeOption.value = e;
        barcodeSelect.add(barcodeOption, 1);
	}
}


//INITIALIZATION CODE
function init(){
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#upload-data').click(displayinventoryData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#inventoryFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getInventoryList);
$(document).ready(getBarcodeList);

