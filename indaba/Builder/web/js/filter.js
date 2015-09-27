function getSelectFilter(filterId) {
    var select = document.getElementById(filterId);
    var retVal = document.getElementById(filterId + "All").checked ? "0" : "1";
    for (var i = 0; i < select.length; i ++) {
        retVal += "," + select.options[i].value;
    }
    return retVal;
}

function getRadioFilter(filterName) {
    var radios = document.getElementsByName(filterName);
    var retVal;
    for (var i = 0; i < radios.length; i ++) {
        if (radios[i].checked) {
            retVal = radios[i].value;
            break;
        }
    }
    return retVal;
}

function resetRadio() {
    document.getElementById("targetAll").checked = true;
    document.getElementById("productAll").checked = true;
    document.getElementById("roleAll").checked = true;
    document.getElementById("teamAll").checked = true;
    document.getElementsByName("hasCase")[0].checked = true;
    document.getElementsByName("status")[0].checked = true;
}

function setFilters() {
    $("#waitInfo").show();

    $.ajax({
        type: "POST",
        url: "peopleFilter.do",
        data: "targetFilter=" + getSelectFilter("target") +
              "&productFilter=" + getSelectFilter("product") +
              "&roleFilter=" + getSelectFilter("role") +
              "&teamFilter=" + getSelectFilter("team") +
              "&hasCaseFilter=" + getRadioFilter("hasCase") +
              "&statusFilter=" + getRadioFilter("status"),
        success: function(result) {
            $("#userInfoList").remove();
            $("#waitInfo").hide();
            $("#newList").after(result.toString());
        },
        error: function(result) {
            alert(result);
        }
    });
}

function getCheckboxFilter(name) {
    var retVal = "";
    var chkboxes = document.getElementsByName(name);
    for (var i = 0; i < chkboxes.length; i ++) {
        if (chkboxes[i].checked) {
            retVal += chkboxes[i].value + ",";
        }
    }
    if (retVal.toString().charAt(retVal.toString().length - 1) == ',') {
        retVal = retVal.toString().substr(0, retVal.toString().length - 1);
    }
    
    return retVal;
}

function setCaseFilters() {
    $("#waitInfo").show();

    $.ajax({
        type: "POST",
        url: "casesFilter.do",
        data: getCaseFiltersData(),
        success: function(result) {
            $("#casesList").remove();
            $("#waitInfo").hide();
            $("#newList").after(result.toString());
        },
        error: function(result) {
            alert(result);
        }
    });
}

function getCaseFiltersData(){
    var s = '';
    s += "targetFilter=" + getSelectFilter("target") +
          "&productFilter=" + getSelectFilter("product") +
          "&statusFilter=" + getCheckboxFilter("caseStatus");
    if (document.getElementById("cTag") != null)
        s += "&cTagFilter=" + getSelectFilter("cTag");
    else
        s += "&cTagFilter=0";
    return s;
}


