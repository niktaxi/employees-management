var attributes = [];
var attrService = baseUrl + "/attributes";
let _isNew, dialogAttr, newAttr, active;

$( document ).ready(function() {
    dialogAttr = $( "#dialog-attr" ).dialog({
      autoOpen: false,
      height: 400,
      width: 350,
      modal: true,
      buttons: {
        "Save": saveAttr,
        Cancel: function() {
          dialogAttr.dialog( "close" );
        }
      }
    });
});

function listAttrs(){
	$("#attrTable tbody").empty();

	 $.ajax({
	 	url: attrService+'/'+selected.data("id"), 
	 	success: function(result){
	    	attributes = result;
	    	for(let i=0; i<attributes.length; i++)
	    		addAttrRow(attributes[i]);
	  }});
}

function addAttrRow(a){
	let el = $("#attrTable tbody");
	el.append("<tr class='clickable' data-id=" + a.id + ">" +
		"<td>" + a.name + "</td>" + 
		"</td>" + "<td>" + a.value + "</td>"+
		"<td><button  onclick='attrDisplay(this, event);' class='btn btn-default'>" +
		"<span class='glyphicon glyphicon-edit' /></button></td>" +
		"<td><button  onclick='deleteAttr(this, event);' class='btn btn-default'>" +
		"<span class='glyphicon glyphicon-remove' /></button></td>" +
		"</tr>");
}

function attrDisplay(el, ev){
	ev.stopPropagation();
	_isNew = false;
	dialogAttr.dialog( "open" );
	dialogAttr.dialog('option', 'title', 'Edit attribute');
	let row = $(el).parents("tr");
	active = attributes.findIndex(a => a.id==row.data("id"));
	$( "#type" ).val(attributes[active].name);
	$( "#value" ).val(attributes[active].value);
}

function saveAttr(){
	newAttr = {};
	newAttr.name = $( "#type" ).val();
	newAttr.value = $( "#value" ).val();
	let validateEl = dialogAttr.children('.validateTips');
	
	if(newAttr.name==null || newAttr.name==''){
		updateTips(validateEl, 'Please fill in the name');
		return;
	}
	
	if(newAttr.value==null || newAttr.value==''){
		updateTips(validateEl, 'Please fill in the value');
		return;
	}
	
	if(!_isNew){
		newAttr.id = attributes[active].id;
		$.ajax({
			 	url: attrService, 
			 	method: 'POST',
    			contentType: "application/json; charset=utf-8",
			 	data: JSON.stringify(newAttr),
			 	success: function(result){
			 		dialogAttr.dialog( "close" );
			 		attributes[active].name = newAttr.name;
			 		attributes[active].value = newAttr.value;
			 		let row = $("#attrTable tbody").children()[active];
			 		row.children[0].innerText = newAttr.name;
			 		row.children[1].innerText = newAttr.value;
			 	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
	}
	else{
		$.ajax({
			 	url: attrService+'/'+selected.data("id"), 
			 	method: 'PUT',
    			contentType: "application/json; charset=utf-8",
			 	data: JSON.stringify(newAttr),
			 	success: function(result){
			 		addAttrRow(result);
			 		attributes.push(result);
			 		dialogAttr.dialog( "close" );
			  	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
	}
}

function createAttr(){
	_isNew = true;
	$( "#type" ).val('');
	$( "#value" ).val('');
	
	dialogAttr.dialog('option', 'title', 'Create attribute');
	dialogAttr.dialog( "open" );
}

function deleteAttr(el, ev){
	ev.stopPropagation();
	let row = $(el).parents("tr");
	let id = row.data("id");
	
	$.ajax({
			 	url: attrService+'/'+id, 
			 	method: 'DELETE',
			 	success: function(result){
			 		let index = attributes.findIndex(a => a.id==id);
			 		attributes.splice(index, 1);
			 		row.remove();
			 	},
			  	error: function (xhr, ajaxOptions, thrownError) {
			        alert(xhr.responseText);
			     }
			  });
}