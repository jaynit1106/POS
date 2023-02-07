
//INITIALIZING VARIABLES
var editBrandId=null;


//URL FUNCTIONS 
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
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
	   		$('#addBrandModal').modal('toggle');
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});

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
		 swal("Oops!","only 5000 rows allowed", "error");
		return;
	}
	
	//To avoid empty files
	if(fileData.length==0){
		 swal("Oops!","file is empty", "error");
		return;
	}
	
	//If everything processed then return
	if(processCount==fileData.length){
		getBrandList();
	   	swal("Hurray", "Brand upload successfull", "success");
	   	document.getElementById('process-data').disabled = true;
	   	if(errorData.length>0){
	   	    document.getElementById('download-errors').disabled = false;
	   	}
		return;
	}
	
	//Process next row
	var row = fileData[processCount];
	processCount++;
	var title = Object.keys(row);
	if(title[0]!='brand' || title[1]!='category' || title.length!=2){
	    swal("Oops!","incorrect tsv format please check the sample file", "error");
	    return;
	}

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

function paginate() {
	$('#brand-table').DataTable();
	
	// $('.dataTables_length').addClass('bs-select');
}

//UI DISPLAY METHODS
var brandData = [];

function displayUpdateDialog(ids){
	// data = JSON.parse(data);
	document.getElementById("brand-edit-form").reset();
	$('#edit-brand-modal').modal('toggle');
	document.getElementById('editBrand').value = brandData[ids].brand;
	document.getElementById('editCategory').value = brandData[ids].category;
	//Get the ID
	window.editBrandId = brandData[ids].id;	
	return false;
}

function displayBrandList(data){
	// brandData=data;
	$('#brand-table').DataTable().destroy();
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var counter=1;
	brandData=[];
	for(var i in data){
		var e = data[i];
		brandData.push(e);
		let id = counter-1;
		var buttonHtml = '<button class="btn btn-dark" onclick="displayUpdateDialog(' + id +')"><i class="fa-solid fa-pen-to-square"></i></button>';
		var row = '<tr>'
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + e.brand + '</td>'
		+ '<td style="text-align:center;">'  + e.category + '</td>';
		if(role=='supervisor'){
		    row+='<td style="text-align:center;" th:if="${info.getRole()}=='+role+'">' + buttonHtml + '</td>';
		}
		row += '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
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
	document.getElementById('process-data').disabled = true;
	document.getElementById('download-errors').disabled = true;
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
    var ok = String(fileName).split(/(\\|\/)/g).pop();
    if(ok.split('.')[1]!="tsv"){
        swal("Oops!", "please select a tsv file", "error");
        return;
    }
    processCount = 0;
    fileData = [];
    errorData = [];
	$('#brandFileName').html(String(fileName).split(/(\\|\/)/g).pop());
	document.getElementById('process-data').disabled = false;
}

function displayBrandData(){
 	resetUploadDialog(); 	
	$('#upload-brand-modal').modal('toggle');
}


function toggleAddBrand(){
    $('#addBrandModal').modal('toggle');
}
//INITIALIZATION CODE
function init(){
	$('#add').click(addBrand);
	$('#add-brand').click(toggleAddBrand);
	$('#update-brand').click(updateBrand);
	$('#upload-data').click(displayBrandData);
	$('#process-data').click(processData);
	$('#download-errors').click(downloadErrors);
    $('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);

