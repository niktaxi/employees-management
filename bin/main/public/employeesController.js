var employees = [];
var baseUrl = "http://localhost:8080";
var empService = baseUrl + "/employees";
let isNew, dialogEmp, newEmployee, selected;

$( document ).ready(function() {
    listEmployees();
    
    dialogEmp = $( "#dialog-employee" ).dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "Save": saveEmployee,
        Cancel: function() {
          dialogEmp.dialog( "close" );
        }
      }
    });
});

function listEmployees(){
	selected=null;
	$("#attributes").css({ display: "none" });
	$("#employeesTable tbody").empty();

	 $.ajax({
	 	url: empService, 
	 	success: function(result){
	    	employees = result;
	    	for(let i=0; i<employees.length; i++)
	    		addEmployeeRow(employees[i]);
	  }});
}

function addEmployeeRow(e){
	let el = $("#employeesTable tbody");
	el.append("<tr class='clickable' onclick='select(this)' data-id=" + e.id + ">" +
		"<td>" + e.name + "</td>" + 
		"<td>" + toDateString(e.hireDate) + "</td>" + 
		"<td>" + (""+e.supervisorName!="null" ? e.supervisorName : "") + "</td>"+
		"<td><button  onclick='employeeDisplay(this, event);' class='btn btn-default'>" +
		"<span class='glyphicon glyphicon-edit' /></button></td>" +
		"<td><button  onclick='deleteEmployee(this, event);' class='btn btn-default'>" +
		"<span class='glyphicon glyphicon-remove' /></button></td>" +
		"</tr>");
}

function employeeDisplay(el, ev){
	ev.stopPropagation();
	isNew = false;
	dialogEmp.dialog( "open" );
	dialogEmp.dialog('option', 'title', 'Edit employee');
	let row = $(el).parents("tr");
	let employee = employees.find(e => e.id==row.data("id"));
	$( "#name" ).val(employee.name);
	$( "#hiredate" ).val(toISODateString(employee.hireDate));
	$( "#supervisor" ).val(employee.supervisorName);
}

function createEmployee(){
	isNew = true;
	$( "#name" ).val('');
	$( "#hiredate" ).val('');
	$( "#supervisor" ).val('');
	
	dialogEmp.dialog('option', 'title', 'Create employee');
	dialogEmp.dialog( "open" );
}

function deleteEmployee(el, ev){
	ev.stopPropagation();
	let id = $(el).parents("tr").data("id");
	
	$.ajax({
			 	url: empService+'/'+id, 
			 	method: 'DELETE',
			 	success: function(result){
					if(selected==$(el)){
						$("#attrTable tbody").empty();
						$("#attributes").css({ display: "none" });
						selected=null;
						attributes=null;
					}
			 		listEmployees();
			 	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
}

function saveEmployee(){
	newEmployee = {};
	newEmployee.name = $( "#name" ).val();
	newEmployee.hireDate = toTimestamp($( "#hiredate" ).val());
	newEmployee.supervisorName = $( "#supervisor" ).val();
	
	let validateEl = dialogEmp.children('.validateTips');
	if(newEmployee.name==null || newEmployee.name==''){
		updateTips(validateEl, 'Please fill in the name');
		return;
	}
	if(newEmployee.hireDate==null || isNaN(newEmployee.hireDate)){
		updateTips(validateEl, 'Please fill in the hire date');
		return;
	}
	
	if(!isNew){
		$.ajax({
			 	url: empService, 
			 	method: 'POST',
    			contentType: "application/json; charset=utf-8",
			 	data: JSON.stringify(newEmployee),
			 	success: function(result){
			 		dialogEmp.dialog( "close" );
			 		listEmployees();
			 	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
	}
	else{
		$.ajax({
			 	url: empService, 
			 	method: 'PUT',
    			contentType: "application/json; charset=utf-8",
			 	data: JSON.stringify(newEmployee),
			 	success: function(result){
			 		addEmployeeRow(result);
			 		employees.push(result);
			 		dialogEmp.dialog( "close" );
			  	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
	}
}

function select(row){
	if(selected && selected.data("id")==$(row).data("id"))
		return;

	if(selected){
		selected.removeClass('selected');
	}
	$(row).addClass('selected');
	selected=$(row);
	$("#attributes").css({ display: "block" });
	listAttrs();
}

function updateTips(tips, t ) {
  tips.text( t ).addClass( "ui-state-highlight" );
  
  setTimeout(function() {
    tips.removeClass( "ui-state-highlight", 1500 );
  }, 500 );
}

function toDateString(tmstp){
	let dt = new Date(tmstp);
	return dt.getDate()+"-"+(dt.getMonth()+1)+"-"+dt.getFullYear();
}

function toTimestamp(dateStr){
	let dateParts = dateStr.split("-");
	let dt = new Date(dateParts[0], dateParts[1]-1, dateParts[2]);
	return dt.valueOf();
}

function toISODateString(tmstp){
	let dt = new Date(tmstp);
	let ISOstr = dt.getFullYear() + '-' + (dt.getMonth()<9?'0':'') + (dt.getMonth()+1) + '-' + (dt.getDate()<10?'0':'') + dt.getDate();
	return ISOstr;
}
