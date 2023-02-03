brandData = []
//URL FUNCTIONS
function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content")
	return baseUrl + "/api/brands";
}


//API CALLING FUNCTIONS
function getBrandReport(){
    var url = getBrandUrl();
    $.ajax({
       url: url,
       type: 'GET',
       success: function(data) {
            brandData=data;
            displayBrandsList();
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
//CREATING TABLES
function displayBrandsList(){
	let data = brandData;
	$('#brand-table').DataTable().destroy();
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	var counter=1;
	for(var i in data){
		var e = data[i];
        if(document.getElementById('brands').value != "All"){
            if(document.getElementById('brands').value != String(e.brand))continue;
        }

        if(document.getElementById('category').value != "All"){
            if(document.getElementById('category').value != String(e.category))continue;
        }

		var row = '<tr>'
		+ '<td>' + counter + '</td>'
		+ '<td>' + e.brand+ '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '</tr>';
        $tbody.append(row);
        counter++;
	}
	paginate();
	
}

//PAGINATION UTIL
function paginate() {
	$('#brand-table').DataTable({
	    dom: 'Bfrtip',
	    buttons:[
	        {
	            extend:'pdf',
	            customize: function (doc) {
                    doc.content[1].table.widths =
                        Array(doc.content[1].table.body[0].length + 1).join('*').split('');
                  },
	            title:'Brand Report',
	            filename:'brandReport'
	        },
	        {
                extend:'csv',
                title:'Brand Report',
                filename:'brandReport'
            },
            {
                extend:'excel',
                title:'Brand Report',
                filename:'brandReport'
            }

	    ]
	});
}


//OTHER UTIL FUNCTIONS
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
	displayBrandsList();
}

//INITIALIZATION CODE
function init(){
    document.getElementById('brands').addEventListener("change",getCategoryList);
    document.getElementById('category').addEventListener("change",displayBrandsList);
}

$(document).ready(init);
$(document).ready(getBrandList);
$(document).ready(getBrandReport);



