//INITIALIZING VARIABLES
var itemList=[];
var deleteList=[]
productMap = new Map();

//URL FUNCTIONS
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orders";
}

function getProductUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/products";
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
	return baseUrl + "/api/orderitems";
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
errorData = [];
errorName = new Map();
function submitOrder(){
	errorData = [];
	document.getElementById('submit-order').disabled = true;
	var url = getOrderUrl();
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
				localStorage.clear();
				$('#edit-order-modal').modal('toggle');
				getOrderList();  
		},
		error: function(response){
            document.getElementById('submit-order').disabled = false;
            var json = JSON.parse(response.responseJSON.message);
            errorName.clear()
            for(var i in json){
                e = json[i];
                errorName.set(Object.keys(e)[0],e[Object.keys(e)[0]]);
                errorData.push(e);
            }
            swal("Oops!", "There are "+errorData.length+" errors", "error");
            displayItemList();
		}
	 });
}
function downloadErrors(){
	writeFileData(errorData);
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
	var total = 0;
	for(var i in data){
		var e = data[i];
		total+=parseFloat(e.sellingPrice*e.quantity);
		var row = '<tr>'
		+ '<td style="text-align:center;"><label>'+counter+'</label></td>'
		+ '<td style="text-align:center;"><label>'+e.name+'</label></td>'
		+ '<td style="text-align:center;"><label>'+e.barcode+'</label></td>'
		+ '<td style="text-align:center;"><label>'+e.quantity+'</label></td>'
		+ '<td style="text-align:right;"><label>'+e.sellingPrice+'</label></td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	var row = '<tr>'
        		+ '<td style="text-align:center;visibility:hidden">'+counter+'</td>'
        		+ '<td style="text-align:center;"> </td>'
        		+ '<td style="text-align:center;"> </td>'
        		+ '<td style="text-align:center;font-weight:bold;">'  + 'Total Cost' + '</td>'
        		+ '<td style="text-align:right;font-weight:bold;">'  + total.toFixed(2) + '</td>'
        		+ '</tr>';
        $tbody.append(row);
	paginateView();
}

function displayOrderList(data){
	$('#order-table').DataTable().destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button class="btn btn-dark" data-toggle="tooltip" data-placement="top" title="view order"  onclick="viewModal(' + e.id + ')"><i class="fa-solid fa-eye"></i></button>';
		var downloadPdf = '<button class="btn btn-primary" data-toggle="tooltip" data-placement="top" title="download invoice"  onclick="downloadPdf(' + e.id + ')"><i class="fa-solid fa-download"></i></button>';
		var date = new Date(e.timestamp).toLocaleString().replace(",","");
		var arr = date.split("/")
		date = arr[1]+'/'+arr[0]+'/'+arr[2];
		var row = '<tr>'
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + e.id + '</td>'
		+ '<td style="text-align:center;">' + date + '</td>'
		+ '<td style="text-align:center;">' + buttonHtml + "  " + downloadPdf + '</td>'
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
	localStorage.items = JSON.stringify(itemList);
	localStorage.removeItems = JSON.stringify(deleteList);
	for(var i in itemList){
		if(deleteList.find(function (element) {return element == i;})!=undefined){
			itr++;
			continue;
		}
		var e = JSON.parse(itemList[i]);
		productMap.set(e.barcode,itr);

		var buttonHtml = '<button id="button'+itr+'"class="btn btn-dark" data-toggle="tooltip" data-placement="top" title="edit item"   onclick="editItem(' + itr + ')"><i class="fa-solid fa-pen-to-square"></i></button>';
		var deleteButton = '<button class="btn btn-danger" data-toggle="tooltip" data-placement="top" title="delete item"  onclick="deleteItem(' + itr + ')"><i class="fa-solid fa-trash"></i></button>';
		var row;
		var sp = parseFloat(e.sellingPrice);
        sp=sp.toFixed(2);
		if(errorName.get(e.barcode)==undefined){
		    row = '<tr>'
            		+'<td><form id="row'+ itr +'">'+serialId+'</form>'
            		+ '<td><label for="barcode" class="form-control" form="row'+itr+'" name="barcode" id="barcode'+itr+'">'+e.barcode+'</label></td>'
            		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="quantity" id="quantity'+itr+'" value="'+e.quantity+'" disabled></td>'
            		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="sellingPrice" id="price'+itr+'" value="'+sp+'" disabled></td>'
            		+ '<td>' + buttonHtml +"   "+ deleteButton + '</td>'
            		+ '</tr>';
		}else{
		    var infoButton = '<button type="button" class="btn btn-danger" data-toggle="tooltip" data-placement="top" title="'+ errorName.get(e.barcode) +'"><i class="fa-sharp fa-solid fa-circle-info"></i></button>';
            row = '<tr>'
            +'<td><form style="color:red" id="row'+ itr +'">'+serialId+'</form>'
            + '<td><label style="color:red" for="barcode" class="form-control" form="row'+itr+'" name="barcode" id="barcode'+itr+'">'+e.barcode+'</label></td>'
            + '<td><input style="color:red" type="number" class="form-control" form="row'+itr+'"name="quantity" id="quantity'+itr+'" value="'+e.quantity+'" disabled></td>'
            + '<td><input style="color:red" type="number" class="form-control" form="row'+itr+'"name="sellingPrice" id="price'+itr+'" value="'+sp+'" disabled></td>'
            + '<td>' + buttonHtml +"   "+ deleteButton + "   " + infoButton + '</td>'
            + '</tr>';
		}
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
productMap = new Map();
function displayCreateItemModal(){
	document.getElementById('submit-order').disabled = false;
    if(localStorage.items==undefined)itemList=[]
    else itemList=JSON.parse(localStorage.items)

    if(localStorage.removeItems==undefined)deleteList=[]
    else deleteList=JSON.parse(localStorage.removeItems);


	displayItemList();
//	itemList=[];
//	deleteList=[];
//	productMap = new Map();
//	var $tbody = $('#items-table').find('tbody');
//	$tbody.empty();
	$('#edit-order-modal').modal('toggle');
}

//DOWNLOAD ACTIONS
function downloadPdf(id){
	// var pdfName = "invoice "+id;
	var url = getBaseUrl()+"/api/orders/download/"+id;
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
	errorName.delete(barcode);
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
    if(!validateForm($form))return;
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

	if(!validateForm($form))return;
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

function cancelOrder(){
    itemList=[]
    deleteList=[]
    errorName.clear();
    productMap.clear();
    localStorage.clear();
    $('#edit-order-modal').modal('toggle');
}
//INITIALIZATION CODE
function init(){
	$('#create-order').click(displayCreateItemModal);
	$('#add-item').click(addItem);
	$('#submit-order').click(submitOrder);
	$('#refresh-orders').click(getOrderList);
    $('#cancel-order').click(cancelOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);
$(document).ready(getBarcodeList);

