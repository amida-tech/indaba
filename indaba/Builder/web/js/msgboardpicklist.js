function _popupList(name) {
	this.name = name;
	this.picklistContainer = "";
	this.selectedDisplayBlock = "";
	this.selectedContainer = "";
	this.selectedInputName = "";
	this.popupPostUrl = "";
	this.popupButton = "";
}

_popupList.prototype.showPopupList = function() {
	var e = document.getElementById(this.popupButton);
	if (e) {
            var parents = $('*').has(e).filter(function(idx){
                return $(this).css('position')=='relative';
            });
            var left = $(e).position().left;
            var top = $(e).position().top;
            $.each(parents, function(i, n){
              left += $(n).position().left;
              top += $(n).position().top;
            });
            $("#" + this.picklistContainer).css("left", left);
            $("#" + this.picklistContainer).css("top", top + e.offsetHeight + 10);
	}
	
	var self = this;
    var parameters = new Object();
    parameters.varName = this.name;
    $(".popup").hide();
	
    $.ajax({
		type: "POST",
		url: self.popupPostUrl,
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
    		var container = document.getElementById(self.picklistContainer);
			container.innerHTML = result;
			show(self.picklistContainer);
		}
	});
};

_popupList.prototype.display = function() {
	var self = this;
	
	var c = document.getElementById(this.selectedDisplayBlock);
	c.innerHTML = "";

	var receivers = this.getItems();
	
	for (var i = 0; i < receivers.length; i++) {
		var item = receivers[i];
		var e = document.createElement("SPAN");
		e.innerHTML = item.displayName;
		
		var a = document.createElement("A");
		
		a.innerHTML = "(X);";
		a.className = "remove";
		a.href = "javascript:" + this.name + ".removeItem(" + item.id + ");";
		
		e.appendChild(a);
		
		var t = document.createElement("TEXTNODE");
		t.textContent = "  ";
		e.appendChild(t);
		
		c.appendChild(e);					
	}
};

_popupList.prototype.addItem = function (id, username) {
	var items = this.getItems();
	for (var i = 0; i < items.length; i++) {
		if (items[i].id == id) {
			hide(this.picklistContainer);
			return;
		}
	}
	
	var c = document.getElementById(this.selectedContainer);
	
	var e = document.createElement("INPUT");
	e.type = "hidden";
	e.name = this.selectedInputName;
	e.value = id;
	e.displayName = username;

	c.appendChild(e);

	this.display();

	hide(this.picklistContainer);
};

_popupList.prototype.removeItem = function(id) {
	var c = document.getElementById(this.selectedContainer);
	
	var ids = document.getElementsByName(this.selectedInputName);
	if (ids != null) {
		for (var i = 0; i < ids.length; i++) {
			var e = ids[i];
			if (e.value == id) {
				c.removeChild(e);
			}
		}
	}
	
	this.display();
};

_popupList.prototype.getItems = function() {
	var items = new Array();
	var c = document.getElementById(this.selectedContainer);
	
	var ids = document.getElementsByName(this.selectedInputName);
	if (ids != null) {
		for (var i = 0; i < ids.length; i++) {
			items.push({id : ids[i].value, displayName : ids[i].displayName});
		}
	}
	
	return items;
};

function show(elemName) {
	var e = document.getElementById(elemName);

	e.style.display = "";
	e.style.visibility = "visible";
}

function hide(elemName) {
	var e = document.getElementById(elemName);

	e.style.display = "none";
	e.style.visibility = "hidden";
}

function getDimX(el) {
	for ( var lx = 0; el != null; lx += el.offsetLeft, el = el.offsetParent)
		;
	return lx;
}

function getDimY(el) {
	for ( var ly = 0; el != null; ly += el.offsetTop, el = el.offsetParent)
		;
	return ly;
}