(function($){
    $(function(){
        filtersSetup();
    });

    function filtersSetup(){
        var filters = $('.filter', '#filters-box');
        filters.each(function(){
            var filter = $(this);
            var button = $("button",filter);
            var select = $("select",filter);
            var selectedFilters = $(".selectedFilters", filter);
            var selectedList = $("#selectedList", filter);
            button.click(function(){
                var option = $("option:selected",select);
                if(option.length==0)return;
                var snippet = "<span class='removeSelection' value='"+option[0].value+"' key='"+option[0].innerHTML+"' field='" + select.attr("name") + "'><a title='remove'/><label>"+option[0].innerHTML+"</label></span>";
                option.remove();
                selectedFilters.append(snippet);
                
                var checkbox = "<input type='checkbox' id='"+ select.attr("name") + "_id_" + option[0].value + "' name='"+ select.attr("name") + "Id' value='" + option[0].value + "' checked></input>";
                selectedList.append(checkbox);
            });

        });
        $(".removeSelection a").live("click",function(e){
            e.preventDefault();
            var span = $(this).parent();
            var value = span.attr('value');
            var key = span.attr('key');
            var select = span.parent().parent().find("select");
            var data = eval('obj_' + $(select).attr('id'));
            var index = indexOf(data, value);
            var option = "<option value='"+value+"'>"+key+"</option>";
            var inserted = false;
            select.find("option").each(function(){
                if(index <= indexOf(data, $(this).attr("value"))) {
                    $(option).insertBefore($(this));
                    inserted = true;
                    return false;
                }
            });
            if(!inserted) {
                select.append(option);
            }

            var selectedList = $("#selectedList", span.parent());
            var checkbox = $("#" + span.attr("field") + "_id_" + value, selectedList);
            checkbox.remove();
            span.remove();
        });
    }

    /**
     *  Set up the select options
     *  @param node - the selected jQuery object
     *  @param data - the option data(array)
     */
    function setupOptions(node, data) {
        node.empty(); // clear all of the options
        $.each(data, function(name, value){
            node.append("<option value='" + value + "'>" + name + "</option>");
        });
    }

    function indexOf(data, id) {
        var i = 0;
        $.each(data, function(name, value){
            if(id == value)
                return false;
            else
                ++i;
        });
        return i;
    }
})(jQuery)

function confirmSubmit() {
    return confirm($.i18n.message('common.js.alert.sumbitassignment'));
}


String.prototype.empty = function() {
    return ( this == "" || this.length == 0 ) ? true:false;
}

String.prototype.escapeHTML = function(){
    var result = "";
    for(var i = 0; i < this.length; i++){
        if(this.charAt(i) == "&" 
            && this.length-i-1 >= 4 
            && this.substr(i, 4) != "&amp;"){
            result = result + "&amp;";
        } else if(this.charAt(i)== "<"){
            result = result + "&lt;";
        } else if(this.charAt(i)== ">"){
            result = result + "&gt;";
        } else {
            result = result + this.charAt(i);
        }
    }
    return result;
};
