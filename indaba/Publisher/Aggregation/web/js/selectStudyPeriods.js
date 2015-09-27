function addSelections() {
    $("td[name=id]:has([type=checkbox]:checked)").each(function() {
        if ($(this).parent("tr").css("display") == "none") {
            return true;
        }
        selectedItems.push($(this).find("[type=checkbox]").val());
        var id = $(this).parent("tr").attr("id");
        var studyPeriodName = $.trim($(this).siblings("td.study-period-name").text());
        var studyPeriodDes = $.trim($(this).siblings("td.study-period-des").text());
        var studyPeriodIdTag = "<input name='studyPeriodIds' type='hidden' value='" + $(this).find("[type=checkbox]").val() + "' />";
        var studyPeriodNameTag = "<td class='preview-name'>(<a href='#' onclick='return removeSelection(\"" + id + "\", \"" + $(this).find("[type=checkbox]").val() + "\")'><img src='images/delete.png' height='12' /></a>) " + studyPeriodName + "</td>";
        var studyPeriodDesTag = "<td class='preview-desc'>" + studyPeriodDes + "</td>";
        $("#preview-list").append("<tr id='preview-" + id + "'>" + studyPeriodIdTag + studyPeriodNameTag + studyPeriodDesTag + "</tr>");
        $(this).parent("tr").hide();
    });
    togglePreviewList();
    return false;
}

function removeSelection(id, val) {
    selectedItems.remove(val);
    $("#preview-" + id).remove();
    togglePreviewList();
    $("#" + id).show();
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
        $("input.study-period-id").each(function(){
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
        alert("Please select study period(s).");
        return false;
    }

    $("#studyPeriodList").val(selectedItems);
    return true;
}