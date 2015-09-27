function targetFilter() {
    var targetName = $("#targetName").val().toLowerCase();
    $("td.target-name").parent("tr").show();
    var count = 0;
    $("tr.even, tr.odd").each(function() {
        if (targetName.length > 0 && $.trim($(this).find("td.target-name").text()).toLowerCase().indexOf(targetName) < 0 ||
            $("#preview-" + $(this)[0].id).length > 0) {
            $(this).find("span").attr("class", "");     // uncheck
            $(this).hide();                             // filtered
        } else {
            $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
            count ++;
        }
    });
}

function typeFilter() {
    var targetType = $("#targetType option[value='" + $("#targetType").val() + "']").text();
    $("td.target-name").parent("tr").show();
    var count = 0;
    $("tr.even, tr.odd").each(function() {
        if (targetType.length > 0 && $.trim($(this).find("td.target-type").text()) != targetType ||
            $("#preview-" + $(this)[0].id).length > 0) {
            $(this).find("span").attr("class", "");     // uncheck
            $(this).hide();                             // filtered
        } else {
            $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
            count ++;
        }
    });
}

function tagFilter() {
    var targetTag = $("#targetTag option[value='" + $("#targetTag").val() + "']").text();
    $("td.target-name").parent("tr").show();
    var count = 0;
    $("tr.even, tr.odd").each(function() {
        if (targetTag.length > 0 && $(this).find("td.target-tag").text().indexOf(targetTag) < 0 ||
            $("#preview-" + $(this)[0].id).length > 0) {
            $(this).find("span").attr("class", "");     // uncheck
            $(this).hide();                             // filtered
        } else {
            $(this).attr("class", (count % 2 == 0) ? "even" : "odd");
            count ++;
        }
    });
}

function addSelections() {
    $("td[name=id]:has([type=checkbox]:checked)").each(function() {
        if ($(this).parent("tr").css("display") == "none") {
            return true;
        }
        selectedItems.push($(this).find("[type=checkbox]").val());
        var id = $(this).parent("tr").attr("id");
        var targetName = $.trim($(this).siblings("td.target-name").text());
        var targetType = $.trim($(this).siblings("td.target-type").text());
        var targetTag = $.trim($(this).siblings("td.target-tag").text());
        var targetIdTag = "<input name='targetId' type='hidden' value='" + $(this).find("[type=checkbox]").val() + "' />";
        var targetNameTag = "<td class='preview-name'>(<a href='#' onclick='return removeSelection(\"" + id + "\", \"" + $(this).find("[type=checkbox]").val() + "\")'><img src='images/delete.png' height='12' /></a>) " + targetName + "</td>";
        var targetTypeTag = "<td class='preview-desc'>" + targetType + "</td>";
        var targetTagTag = "<td class='preview-desc'>" + targetTag + "</td>";
        $("#preview-list").append("<tr id='preview-" + id + "'>" + targetIdTag + targetNameTag + targetTypeTag + targetTagTag + "</tr>");
        $(this).parent("tr").hide();
    });
//    $("td>div.checker>span.checked").each(function(){
//        if ($(this).parent("div").parent("td").parent("tr").css("display") == "none") {
//            return true;
//        }
//        var id = $(this).parent("div").parent("td").parent("tr").attr("id");
//        var targetName = $.trim($(this).parent("div").parent("td").siblings("td.target-name").text());
//        var targetType = $.trim($(this).parent("div").parent("td").siblings("td.target-type").text());
//        var targetTag = $.trim($(this).parent("div").parent("td").siblings("td.target-tag").text());
//        var targetIdTag = "<input name='targetIds' type='hidden' value='" + $(this).children("input").val() + "' />";
//        var targetNameTag = "<td class='preview-name'>(<a href='#' onclick='return removeSelection(\"" + id + "\")'><img src='images/delete.png' height='12' /></a>) " + targetName + "</td>";
//        var targetTypeTag = "<td class='preview-desc'>" + targetType + "</td>";
//        var targetTagTag = "<td class='preview-desc'>" + targetTag + "</td>";
//        $("#preview-list").append("<tr id='preview-" + id + "'>" + targetIdTag + targetNameTag + targetTypeTag + targetTagTag + "</tr>");
//        $(this).parent("div").parent("td").parent("tr").hide();
//    });
    togglePreviewList();
    return false;
}

function removeSelection(id, val) {
    selectedItems.remove(val);
    $("#preview-" + id).remove();
    $("#" + id).show();
    togglePreviewList();
    return false;
}

function togglePreviewList() {
    if ($("#preview-list").find("tr").length > 0) {
        $("#preview").show();
    } else {
        $("#preview").hide();
    }
}

function initCheckAll() {
    $("#checkall").click(function(){
        var check_status = this.checked;
        $("input.target-id").each(function(){
            if($(this).parents("tr").css("display") != "none") {
                this.checked = check_status;
                if (check_status) {
                    $(this).parent("span").attr("class", "checked");
                } else {
                    $(this).parent("span").attr("class", "");
                }
            }
        });
    });
}

function doSubmit() {
    if ($("#preview-list").find("tr").length <= 0) {
        alert("Please select target(s).");
        return false;
    }

    $("#targetList").val(selectedItems);
    return true;
}