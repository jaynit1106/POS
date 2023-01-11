
function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/order";
}

function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/orderitem";
}

//BUTTON ACTIONS
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


// PAGINATION METHODS
function paginate() {
	$('#order-table').DataTable();
    $('#items-table').DataTable();
	$('#view-table').DataTable();
	$('.dataTables_length').addClass('bs-select');
}

//UI DISPLAY METHODS
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

function displayItemData(data){
	var $tbody = $('#view-table').find('tbody');
	$tbody.empty();
	var counter = 1;
	for(var i in data){
		var e = data[i];
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + e.sellingPrice +'</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
}

function viewModal(id){
	$('#view-order-modal').modal('toggle');
	var url = getOrderItemUrl()+'/'+id;
	getItemList(url);
}

function displayOrderList(data){
	$('#order-table').DataTable().destroy();
	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="viewModal(' + e.id + ')">view</button>';
		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.timestamp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
}


var itemList=[];
function displayCreateItemModal(){
	displayItemList();
	itemList=[];
	deleteList=[];
	productMap = new Map();
	var $tbody = $('#items-table').find('tbody');
	$tbody.empty();
	$('#edit-order-modal').modal('toggle');
}
var deleteList=[]
function deleteItem(id){
	deleteList.push(id);
	let barcode = JSON.parse(itemList[id]).barcode;
	productMap.delete(barcode);
	displayItemList();
}

productMap = new Map();

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
		var buttonHtml = '<button onclick="editItem(' + itr + ')">edit</button>';
		var deleteButton = '<button onclick="deleteItem(' + itr + ')">delete</button>';
		var row = '<tr>'
		+'<td><form id="row'+ itr +'">'+serialId+'</form>'
		+ '<td><input type="text" class="form-control" form="row'+itr+'" name="barcode" id="barcode" value="'+e.barcode+'"></td>'
		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="quantity" id="quantity" value="'+e.quantity+'"></td>'
		+ '<td><input type="number" class="form-control" form="row'+itr+'"name="sellingPrice" id="price" value="'+e.sellingPrice+'"></td>'
		+ '<td>' + buttonHtml +"   "+ deleteButton + '</td>'
		+ '</tr>';
        $tbody.append(row);
		itr++;
		serialId++;
	}
	paginate();
}

function editItem(id){
	var $form = $("#row"+id);
	var json = toJson($form);
	itemList[id]=json;
	displayItemList();
}

function addItem(){
	var $form = $("#item-form");
	var json = toJson($form);
	var e =JSON.parse(json);
	if(productMap.get(e.barcode)!=undefined){
		let id = productMap.get(e.barcode);
		let  q = parseInt(JSON.parse(itemList[id]).quantity);
		q+=parseInt(e.quantity);
		e.quantity = q;
		itemList[id] = JSON.stringify(e);
		displayItemList();
		return;
	}
	itemList.push(json);
	document.getElementById("item-form").reset();
	displayItemList();
	$("#item-form").reset();

}
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
function submitOrder(){
	var url = getOrderItemUrl();
	let form = convertArrayToJson();
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
//INITIALIZATION CODE
function init(){
	$('#create-order').click(displayCreateItemModal);
	$('#add-item').click(addItem);
	$('#submit-order').click(submitOrder);
}

$(document).ready(init);
$(document).ready(getOrderList);

