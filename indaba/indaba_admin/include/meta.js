function updateRole() {
	var action='update';
	var role_id = document.getElementById('role_id').value;
	var name = document.getElementById('role_name').value;
	var description = document.getElementById('role_description').value;
	alert("HELLO from updateRole " + description);
	$.ajax({
		type: "GET",
		cache: "failse",
		dataType: "html",
		url: "meta_functions.php",
		data: "meta=role&action=" + action + "&id=" + role_id + 
			"&name=" + name + 
			"&description=" + description,
		success: function(data) {
			$("#metadetails p").html(data);
		}
	});
}

function updateRole1() {
	alert("HELLO");
}
