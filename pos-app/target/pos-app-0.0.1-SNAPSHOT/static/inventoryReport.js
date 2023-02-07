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
	var url = getBrandUrl() + "/unique/"+document.getElementById('brands').value;
    	$.ajax({
           url: url,
           type: 'GET',
           success: function(data) {
                addCategoryDropdown(data);
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
		+ '<td style="text-align:center;">' + counter + '</td>'
		+ '<td style="text-align:center;">' + e.brand + '</td>'
		+ '<td style="text-align:center;">'  + e.category + '</td>'
		+ '<td style="text-align:center;">'  + e.quantity + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

//PAGINATE METHODS
function paginate() {
	$('#InventoryReport-table').DataTable({
        dom: 'Bfrtip',
        buttons:[
            {
                extend:'pdf',
                customize: function (doc) {
                      doc.content[1].table.widths =
                          Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                    },
                title:'Inventory Report',
                filename:'inventoryReport'
            },
            {
                  extend:'csv',
                  title:'Inventory Report',
                  filename:'inventoryReport'
              },
              {
                  extend:'excel',
                  title:'Inventory Report',
                  filename:'inventoryReport'
              }

        ]
    });
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
}


//INITIALIZATION CODE
function init(){
    document.getElementById('brands').addEventListener("change",getCategoryList);
    $('#submit-filter').click(getInventoryReportList);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getCategoryList);
