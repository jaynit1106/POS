//INITIALIZING VARIABLES
var itemList=[];
var deleteList=[]
productMap = new Map();

//URL FUNCTIONS
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/product";
}

function getSchedulerUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/scheduler";
}

function getBaseUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl;
}

function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem";
}

//API CALLING FUNCTIONS
function addOrder(event){
	//Set the values to update
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: null,
	   headers: {
       	'Content-Type': 'application/json'
       },	   
	   success: function(response) {
	   		getOrderList();
	   		swal("Hurray", "Order added successfully", "success");  
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

function getOrderList(){
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderList(data);  
	   },
	   error: function(response){
	   		swal("Oops!", response.responseJSON.message, "error");
	   }
	});
}

function getItemList(url){
	$.ajax({
		url: url,
		type: 'GET',
		success: function(data) {
				displayItemData(data);  
		},
		error: function(response){
				swal("Oops!", response.responseJSON.message, "error");
		}
	 });
}

function submitOrder(){
	var url = getOrderItemUrl();
	let form = convertArrayToJson();
	if(itemList.length-deleteList.length==0){
		swal("Oops!","Cart cannot be empty", "error");
		return;
	}
	$.ajax({
		url: url,
		type: 'POST',
		data: form,
		headers: {
			'Content-Type': 'application/json'
		},	   
		success: function(response) {
				swal("Hurray", "Order added successfully", "success");
				$('#edit-order-modal').modal('toggle');
				getOrderList();  
		},
		error: function(response){
				swal("Oops!", response.responseJSON.message, "error");
		}
	 });
}

// PAGINATION METHODS
function paginateOrder() {
	$('#order-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}
function paginateView() {
	$('#view-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}
function paginateItems() {
    $('#items-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

//UI DISPLAY METHODS
function displayItemData(data){
	var $tbody = $('#view-table').find('tbody');
	$tbody.empty();
	var counter = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice +'</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginateView();
}

function displayOrderList(data){
	$('#order-table').DataTable().destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-dark" onclick="viewModal(' + e.id + ')"><i class="fa-solid fa-eye"></i></button>';
		var downloadPdf = '<button class="btn btn-primary" onclick="downloadPdf(' + e.id + ')"><i class="fa-solid fa-download"></i></button>';
		var date = new Date(e.timestamp).toLocaleString().replace(",","");
		var arr = date.split("/")
		date = arr[1]+'/'+arr[0]+'/'+arr[2];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + date + '</td>'
		+ '<td>' + buttonHtml + "  " + downloadPdf + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginateOrder();
}

function displayItemList(){
	$('#items-table').DataTable().destroy();
	var $tbody = $('#items-table').find('tbody');
	$tbody.empty();
	var itr = 0;
	var serialId=1;
	for(var i in itemList){
		if(deleteList.find(function (element) {return element == i;})!=undefined){
			itr++;
			continue;
		}
		var e = JSON.parse(itemList[i]);
		productMap.set(e.barcode,itr);
		var buttonHtml = '<button id="button'+itr+'"class="btn btn-dark" onclick="editItem(' + itr + ')"><i class="fa-solid fa-pen-to-square"></i></button>';
		var deleteButton = '<button class="btn btn-danger" onclick="deleteItem(' + itr + ')"><i class="fa-solid fa-trash"></i></button>';
		var row = '<tr>'
		+'<td><form id="row'+ itr +'">'+serialId+'</form>'
		+ '<td><label for="barcode" class="form-control" form="row'+itr+'" name="barcode" id="barcode'+itr+'">'+e.barcode+'</label></td>'
		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="quantity" id="quantity'+itr+'" value="'+e.quantity+'" disabled></td>'
		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="sellingPrice" id="price'+itr+'" value="'+e.sellingPrice+'" disabled></td>'
		+ '<td>' + buttonHtml +"   "+ deleteButton + '</td>'
		+ '</tr>';
        $tbody.append(row);
		itr++;
		serialId++;
	}
	paginateItems();
}

//MODAL TOGGLING METHODS
function viewModal(id){
	$('#view-order-modal').modal('toggle');
	var url = getOrderItemUrl()+'/'+id;
	getItemList(url);
}

function displayCreateItemModal(){
	displayItemList();
	itemList=[];
	deleteList=[];
	productMap = new Map();
	var $tbody = $('#items-table').find('tbody');
	$tbody.empty();
	$('#edit-order-modal').modal('toggle');
}

//DOWNLOAD ACTIONS
function downloadPdf(id){
	// var pdfName = "invoice "+id;
	var url = getBaseUrl()+"/api/order/download/"+id;
	window.location.href = url;
}

//UTIL FUNCTIONS
function addBarcodeDropdown(data){
	var barcodeSelect = document.getElementById("barcodes");
	for(var i in data){
		var e = data[i];
        var barcodeOption = document.createElement('option');
        barcodeOption.text = barcodeOption.value = e;
        barcodeSelect.add(barcodeOption, 1);
	}
}

function deleteItem(id){
	deleteList.push(id);
	let barcode = JSON.parse(itemList[id]).barcode;
	productMap.delete(barcode);
	displayItemList();
}

function editItem(id){
    document.getElementById('price'+id).disabled=false;
    document.getElementById('quantity'+id).disabled=false;
    document.getElementById('button'+id).innerHTML = "Save";
    document.getElementById('button'+id).onclick = function(){edits(id);}
}
function edits(id){
    var $form = $("#row"+id);
    var json = toJson($form);
    json = JSON.parse(json);
    json.barcode = JSON.parse(itemList[id]).barcode;
    json = JSON.stringify(json);
    itemList[id]=json;
    document.getElementById('price'+id).disabled=true;
    document.getElementById('quantity'+id).disabled=true;
    document.getElementById('button'+id).innerHTML = '<i class="fa-solid fa-pen-to-square"></i>';
    document.getElementById('button'+id).onclick = function(){editItem(id);}
    displayItemList();
}

function addItem(){
	var $form = $("#item-form");
	var json = toJson($form);
	var e =JSON.parse(json);
	if(e.quantity=="" || e.barcode=="" || e.sellingPrice==""){
		swal("Oops!", "The fields cannot be empty", "error");
		return;
	}
	if(e.quantity<=0 ){
		swal("Oops!", "Please enter valid quantity", "error");
		return;
	}
	if(e.sellingPrice<0 ){
		swal("Oops!", "Please enter valid price", "error");
		return;
	}
	if(productMap.get(e.barcode)!=undefined){
		let id = productMap.get(e.barcode);
		let  q = parseInt(JSON.parse(itemList[id]).quantity);
		let price = parseInt(JSON.parse(itemList[id]).sellingPrice);
		if(price!=parseInt(e.sellingPrice)){
			swal("Oops!", "The selling price does not match within the products", "error");
			return;
		}
		q+=parseInt(e.quantity);
		e.quantity = q;
		itemList[id] = JSON.stringify(e);
		displayItemList();
		return;
	}
	itemList.push(json);
	document.getElementById("item-form").reset();
	displayItemList();

}


//ARRAY CONVERSION FUNCTION
function convertArrayToJson(){
	let json = [];
	for(s in itemList){
		if(deleteList.find(function (element) {return element == s;})!=undefined)continue;
		let data ={};
		data["barcode"]=JSON.parse(itemList[s]).barcode;
		data["quantity"]=JSON.parse(itemList[s]).quantity;
		data["sellingPrice"]=JSON.parse(itemList[s]).sellingPrice;
		json.push(data);
	}
	return JSON.stringify(json);
}

//INITIALIZATION CODE
function init(){
	$('#create-order').click(displayCreateItemModal);
	$('#add-item').click(addItem);
	$('#submit-order').click(submitOrder);
	$('#refresh-order').click(getOrderList);
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getBarcodeList);

