
var oldStatusSelectIndex;
var oldAssignIdSelectIndex;
var oldPrioritySelectIndex;
var oldBlockWorkFlowChecked;
var oldBlockPulishingChecked;

function disableBlockCheckBox() {
    var blockWorkFlowCheckBox = document.getElementById("blockWorkFlowCheckBox");
    if (blockWorkFlowCheckBox != null) {
        blockWorkFlowCheckBox.disabled = true;
    }
    var blockPublishingCheckBox = document.getElementById("blockPublishingCheckBox");
    if (blockPublishingCheckBox != null) {
        blockPublishingCheckBox.disabled = true;
    }
}

function enableBlockCheckBox() {
    var blockWorkFlowCheckBox = document.getElementById("blockWorkFlowCheckBox");
    if (blockWorkFlowCheckBox != null) {
        blockWorkFlowCheckBox.disabled = false;
    }
    var blockPublishingCheckBox = document.getElementById("blockPublishingCheckBox");
    if (blockPublishingCheckBox != null) {
        blockPublishingCheckBox.disabled = false;
    }
}

function hiddenBlockCheckBoxTr() {
    if (document.getElementById("workFlowTr") != null) {
        document.getElementById("workFlowTr").style.display = "none";
    }
    if (document.getElementById("publishingTr")) {
        document.getElementById("publishingTr").style.display = "none";
    }
}

function showBlockCheckBoxTr() {
    if (document.getElementById("workFlowTr") != null) {
        document.getElementById("workFlowTr").style.display = "";
    }
    if (document.getElementById("publishingTr")) {
        document.getElementById("publishingTr").style.display = "";
    }
}

function onEditButtonClick() {
    if (document.getElementById("editButton").innerHTML == $j.i18n.message('common.btn.edit')) {
        //do edit reset;
        oldStatusSelectIndex = document.getElementById("caseStatusSelect").selectedIndex;
        var assignUser = document.getElementById("assignUserSelect");
        if (assignUser != null) {
            oldAssignIdSelectIndex = assignUser.selectedIndex;
            assignUser.disabled = false;
        }
        oldPrioritySelectIndex = document.getElementById("casePrioritySelect").selectedIndex;

        var blockWorkFlowCheckBox = document.getElementById("blockWorkFlowCheckBox");
        if (blockWorkFlowCheckBox != null) {
            oldBlockWorkFlowChecked = document.getElementById("blockWorkFlowCheckBox").checked;
        }
        var blockPublishingCheckBox = document.getElementById("blockPublishingCheckBox");
        if (blockPublishingCheckBox != null) {
            oldBlockPulishingChecked = document.getElementById("blockPublishingCheckBox").checked;
        }

        document.getElementById("editButton").innerHTML = $j.i18n.message('common.btn.view');
        document.getElementById("editViewImg").src= "images/view.png";
        document.getElementById("title").disabled = false;
        document.getElementById("submitDetail").disabled = false;
        document.getElementById("caseStatusSelect").disabled = false;
        document.getElementById("casePrioritySelect").disabled = false;
        document.getElementById("description").disabled = false;

        enableBlockCheckBox();

        var newAttachUserSelect = document.getElementById("newAttachUserSelect");
        if (newAttachUserSelect != null) {
            newAttachUserSelect.style.display = "block";
        }

        document.getElementById("newAttachUserInput").style.display = "block";

        var newAttachContentSelect = document.getElementById("newAttachContentSelect");
        if (newAttachContentSelect != null) {
            newAttachContentSelect.style.display = "block";
        }

        document.getElementById("attachContentInput").style.display = "block";

        if (document.getElementById("newTagSelect") != null) {
            document.getElementById("newTagSelect").style.display = "block";
        }
        if (document.getElementById("attachTagInput") != null) {
            document.getElementById("attachTagInput").style.display = "block";
        }
        
        document.getElementById("attachFile").style.display = "block";
        //document.getElementById("oldTags").style.display = "none";

        for (var i = 1; ;i++) {
            if (document.getElementById("fileDelete"+String(i)) == null){
                break;
            }
            else{
                document.getElementById("fileDelete"+String(i)).style.display = "block";
            }
        }


        document.getElementById("submitDetail").style.display = "block";
        document.getElementById("oldAttachUserSelect").style.display = "none";

        if (document.getElementById("oldTagSelect") != null) {
            var str = document.getElementById("oldTagSelect").innerHTML.replace(/^\s+|\s+|\n$/g, ' ');
            var attachTagNames = document.getElementById("attachTagNames");
            if (attachTagNames != null && str.length > 0) {
                attachTagNames.value = str;
            }
        }
        if (document.getElementById("oldTagSelectIds") != null) {
            str = document.getElementById("oldTagSelectIds").innerHTML.replace(/^\s+|\s+|\n$/g, ' ');
            var attachTagIds = document.getElementById("attachTagIds");
            if (attachTagIds != null && str.length > 0) {
                attachTagIds.value = str;
            }
        }

        str = document.getElementById("oldAttachContentSelect").innerHTML.replace(/^\s+|\s+|\n$/g, ' ').replace(/<\/?[^>]*>/g, '');
        var attachContentNames = document.getElementById("attachContentNames");
        if (attachContentNames != null && str.length > 0) {
            attachContentNames.value = str;
        }

        str = document.getElementById("oldAttachContentSelectIds").innerHTML.replace(/^\s+|\s+|\n$/g, ' ');
        var attachContentIds = document.getElementById("attachContentIds");
        if (attachContentIds != null && str.length > 0) {
            attachContentIds.value = str;
        }

        str = document.getElementById("oldAttachUserSelect").innerHTML.replace(/^\s+|\s+|\n$/g, ' ').replace(/<\/?[^>]*>/g, '');
        var attachUserNames = document.getElementById("attachUserNames");
        if (attachUserNames != null && str.length > 0) {
            attachUserNames.value = str;
        }

        str = document.getElementById("oldAttachUserSelectIds").innerHTML.replace(/^\s+|\s+|\n$/g, ' ');
        var attachUserIds = document.getElementById("attachUserIds");
        if (attachUserIds != null && str.length > 0) {
            attachUserIds.value = str;
        }
        //edit here
        document.getElementById("oldAttachContentSelect").style.display = "none";
        document.getElementById("selectedContentFilters").style.display = "block";
        if (document.getElementById("selectedTagFilters") != null) {
            document.getElementById("selectedTagFilters").style.display = "block";
        }
        if (document.getElementById("oldTagSelect") != null) {
            document.getElementById("oldTagSelect").style.display = "none";
        }
    } else {
        //do unedit reset; 
        document.getElementById("editButton").innerHTML = $j.i18n.message('common.btn.edit');
        document.getElementById("editViewImg").src= "images/edit.png";

        document.getElementById("title").disabled = true;
        document.getElementById("title").value = document.getElementById("oldTitle").value;

        document.getElementById("submitDetail").disabled = true;

        document.getElementById("caseStatusSelect").disabled = true;
        document.getElementById("caseStatusSelect").selectedIndex = oldStatusSelectIndex;

        document.getElementById("casePrioritySelect").disabled = true;
        document.getElementById("casePrioritySelect").selectedIndex = oldPrioritySelectIndex;

        document.getElementById("description").disabled = true;
        document.getElementById("description").value = document.getElementById("oldDescription").value;

        //edit here
        document.getElementById("oldAttachUserSelect").style.display = "block";
        document.getElementById("oldAttachContentSelect").style.display = "block";
        if (document.getElementById("oldTagSelect") != null) {
            document.getElementById("oldTagSelect").style.display = "block";
        }
        if (document.getElementById("selectedContentFilters") != null) {
            document.getElementById("selectedContentFilters").style.display = "none";
        }
        if (document.getElementById("selectedTagFilters") != null) {
            document.getElementById("selectedTagFilters").style.display = "none";
        }

        var assignUser = document.getElementById("assignUserSelect");
        if (assignUser != null) {
            assignUser.disabled = true;
            assignUser.selectedIndex = oldAssignIdSelectIndex;
        }

        var newAttachUserSelect = document.getElementById("newAttachUserSelect");
        if (newAttachUserSelect != null) {
            newAttachUserSelect.style.display = "none";
        }
        
        document.getElementById("newAttachUserInput").style.display = "none";

        var newAttachContentSelect = document.getElementById("newAttachContentSelect");
        if (newAttachContentSelect != null) {
            newAttachContentSelect.style.display = "none";
        }

        document.getElementById("attachContentInput").style.display = "none";

        if (document.getElementById("newTagSelect") != null) {
            document.getElementById("newTagSelect").style.display = "none";
        }
        if (document.getElementById("attachTagInput") != null) {
            document.getElementById("attachTagInput").style.display = "none";
        }
        
        document.getElementById("attachFile").style.display = "none";
        //document.getElementById("oldTags").style.display = "block";

        for (var i = 1; ;i++) {
            if (document.getElementById("fileDelete"+String(i)) == null){
                break;
            }
            else{
                document.getElementById("fileDelete"+String(i)).style.display = "none";
            }
        }

        var blockWorkFlowCheckBox = document.getElementById("blockWorkFlowCheckBox");
        if (blockWorkFlowCheckBox != null) {
            blockWorkFlowCheckBox.checked = oldBlockWorkFlowChecked;
        }
        var blockPublishingCheckBox = document.getElementById("blockPublishingCheckBox");
        if (blockPublishingCheckBox != null) {
            blockPublishingCheckBox.checked = oldBlockPulishingChecked;
        }
        disableBlockCheckBox();
        //showBlockCheckBoxTr();

        document.getElementById("submitDetail").style.display = "none";
    }
}

/**
 * Comment
 */
function alertMsg() {
    var alertInfoInput = document.getElementById("alertInfoInput");
    if (alertInfoInput != null) {
        var alertInfoStr = alertInfoInput.value;
        if (alertInfoStr.length > 0) {
            alert(alertInfoStr);
            alertInfoInput.value = "";
        }
    }
}

function clearAttachUsers() {
    document.getElementById("attachUserIds").value = "";
    document.getElementById("attachUserNames").value = "";
}

function addAttachUser() {
    var userIdAddInput = document.getElementById("attachUserIds");
    var userNameAddInput = document.getElementById("attachUserNames");
    var userAddSelect = document.getElementById("attachUserSelect");
    var userShow = document.getElementById("selectedUserFilters");

    var selectValue = userAddSelect.value;
    var selectIndex = userAddSelect.selectedIndex;
    var selectShowValue = userAddSelect.options[selectIndex].text;

    if (userNameAddInput.value.replace(/^\s+|\s+$/g, '') == "") {
        userNameAddInput.value = selectShowValue;
        userIdAddInput.value = selectValue;
    } else {
        if (userNameAddInput.value.indexOf(selectShowValue) == -1) {
            userNameAddInput.value = userNameAddInput.value + ";  " + selectShowValue;
            userIdAddInput.value = userIdAddInput.value + ";" + selectValue;
        }
    }
    var option = userAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachUser(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    userShow.innerHTML += snippet;
    userAddSelect.remove(selectIndex);


}

function deleteAttachUser(text, value) {
    var userIdAddInput = document.getElementById("attachUserIds");
    var userNameAddInput = document.getElementById("attachUserNames");
    var userAddSelect = document.getElementById("attachUserSelect");
    var userShow = document.getElementById("selectedUserFilters");
    var id_array = userIdAddInput.value.split(';');
    var name_array = userNameAddInput.value.split(';');

    userIdAddInput.value = "";
    userNameAddInput.value = "";
    if (id_array.length >= 1){
        for (var i = 0; i < id_array.length; i++){
            id_array[i] = id_array[i].replace(/(^\s*)|(\s*$)/g, "");
            if (id_array[i] != value){
                if (userIdAddInput.value == ""){
                    userIdAddInput.value = id_array[i];
                    userNameAddInput.value = name_array[i];
                } else {
                    userIdAddInput.value = userIdAddInput.value + ";" + id_array[i];
                    userNameAddInput.value = userNameAddInput.value + ";  " + name_array[i];
                }
            }

        }
    }
    var newOption = new Option(text, value);
    userAddSelect.options.add(newOption);

    jQuery('span', userShow).filter(function(){
       return jQuery.trim(text) == jQuery.trim(jQuery(this).text());
    }).remove();
}

function addSingleAttachUser(uid) {
    var userIdAddInput = document.getElementById("attachUserIds");
    var userNameAddInput = document.getElementById("attachUserNames");
    var userAddSelect = document.getElementById("attachUserSelect");
    var userShow = document.getElementById("selectedUserFilters");
    var selectIndex;
    for (var i = 0; i < userAddSelect.options.length; i++){
        if (userAddSelect.options[i].value == uid){
            selectIndex = i;
        }
    }
    var option = userAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachUser(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    userShow.innerHTML += snippet;
    userAddSelect.remove(selectIndex);
}

function clearAttachContent() {
    document.getElementById("attachContentIds").value = "";
    document.getElementById("attachContentNames").value = "";
    hiddenBlockCheckBoxTr();
    if (document.getElementById("blockWorkFlowCheckBox") != null) {
        document.getElementById("blockWorkFlowCheckBox").checked = false;
    }
    if (document.getElementById("blockPublishingCheckBox") != null) {
        document.getElementById("blockPublishingCheckBox").checked = false;
    }

}

function addAttachContent() {
    var contentIdAddInput = document.getElementById("attachContentIds");
    var contentNameAddInput = document.getElementById("attachContentNames");
    var contentAddSelect = document.getElementById("attachContentSelect");
    var contentShow = document.getElementById("selectedContentFilters");
    var selectValue = contentAddSelect.value;
    var selectIndex = contentAddSelect.selectedIndex;
    var selectShowValue = contentAddSelect.options[selectIndex].text;

    if (contentNameAddInput.value.replace(/^\s+|\s+$/g, '') == "") {
        contentNameAddInput.value = selectShowValue;
        contentIdAddInput.value = selectValue;
    } else {
        if (contentNameAddInput.value.indexOf(selectShowValue) == -1) {
            contentNameAddInput.value = contentNameAddInput.value + ";  " + selectShowValue;
            contentIdAddInput.value = contentIdAddInput.value + ";" + selectValue;
        }
    }
    var option = contentAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachContent(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    contentShow.innerHTML += snippet;
    contentAddSelect.remove(selectIndex);
    showBlockCheckBoxTr();
}

function deleteAttachContent(text, value) {
    var contentIdAddInput = document.getElementById("attachContentIds");
    var contentNameAddInput = document.getElementById("attachContentNames");
    var contentAddSelect = document.getElementById("attachContentSelect");
    var contentShow = document.getElementById("selectedContentFilters");
    var id_array = contentIdAddInput.value.split(';');
    var name_array = contentNameAddInput.value.split(';');

    contentIdAddInput.value = "";
    contentNameAddInput.value = "";
    if (id_array.length >= 1){
        for (var i = 0; i < id_array.length; i++){
            id_array[i] = id_array[i].replace(/(^\s*)|(\s*$)/g, "");
            if (id_array[i] != value){
                if (contentIdAddInput.value == ""){
                    contentIdAddInput.value = id_array[i];
                    contentNameAddInput.value = name_array[i];
                } else {
                    contentIdAddInput.value = contentIdAddInput.value + ";" + id_array[i];
                    contentNameAddInput.value = contentNameAddInput.value + ";  " + name_array[i];
                }
            }

        }
    }
    var newOption = new Option(text, value);
    contentAddSelect.options.add(newOption);

    jQuery('span', contentShow).filter(function(){
       return jQuery.trim(text) == jQuery.trim(jQuery(this).text());
    }).remove();
}

function addSingleAttachContent(contentId) {
    var contentIdAddInput = document.getElementById("attachContentIds");
    var contentNameAddInput = document.getElementById("attachContentNames");
    var contentAddSelect = document.getElementById("attachContentSelect");
    var contentShow = document.getElementById("selectedContentFilters");
    var selectIndex;
    for (var i = 0; i < contentAddSelect.options.length; i++){
        if (contentAddSelect.options[i].value == contentId){
            selectIndex = i;
        }
    }
    var option = contentAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachContent(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    contentShow.innerHTML += snippet;
    contentAddSelect.remove(selectIndex);
}

function clearAttachTags() {
    document.getElementById("attachTagIds").value = "";
    document.getElementById("attachTagNames").value = "";
}

function addAttachTags() {
    var tagIdAddInput = document.getElementById("attachTagIds");
    var tagNameAddInput = document.getElementById("attachTagNames");
    var tagAddSelect = document.getElementById("attachTagSelect");
    var tagShow = document.getElementById("selectedTagFilters");
    var selectValue = tagAddSelect.value;
    var selectIndex = tagAddSelect.selectedIndex;
    var selectShowValue = tagAddSelect.options[selectIndex].text;

    if (tagNameAddInput.value.replace(/^\s+|\s+$/g, '') == "") {
        tagNameAddInput.value = selectShowValue;
        tagIdAddInput.value = selectValue;
    } else {
        if (tagNameAddInput.value.indexOf(selectShowValue) == -1) {
            tagNameAddInput.value = tagNameAddInput.value + ";  " + selectShowValue;
            tagIdAddInput.value = tagIdAddInput.value + ";" + selectValue;
        }
    }

    var option = tagAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachTag(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    tagShow.innerHTML += snippet;
    tagAddSelect.remove(selectIndex);
}

function deleteAttachTag(text, value) {
    var tagIdAddInput = document.getElementById("attachTagIds");
    var tagNameAddInput = document.getElementById("attachTagNames");
    var tagAddSelect = document.getElementById("attachTagSelect");
    var tagShow = document.getElementById("selectedTagFilters");
    var id_array = tagIdAddInput.value.split(';');
    var name_array = tagNameAddInput.value.split(';');

    tagIdAddInput.value = "";
    tagNameAddInput.value = "";
    if (id_array.length >= 1){
        for (var i = 0; i < id_array.length; i++){
            id_array[i] = id_array[i].replace(/(^\s*)|(\s*$)/g, "");
            if (id_array[i] != value){
                if (tagIdAddInput.value == ""){
                    tagIdAddInput.value = id_array[i];
                    tagNameAddInput.value = name_array[i];
                } else {
                    tagIdAddInput.value = tagIdAddInput.value + ";" + id_array[i];
                    tagNameAddInput.value = tagNameAddInput.value + ";  " + name_array[i];
                }
            }

        }
    }
    var newOption = new Option(text, value);
    tagAddSelect.options.add(newOption);

    jQuery('span', tagShow).filter(function(){
       return jQuery.trim(text) == jQuery.trim(jQuery(this).text());
    }).remove();
}

function addSingleAttachTag(text) {
    var tagAddSelect = document.getElementById("attachTagSelect");
    var tagShow = document.getElementById("selectedTagFilters");
    var selectIndex;
    for (var i = 0; i < tagAddSelect.options.length; i++){
        if (tagAddSelect.options[i].text == text){
            selectIndex = i;
        }
    }
    var option = tagAddSelect.options[selectIndex];
    var snippet = "<span class='removeSelection' name='"+option.value+"' value='"+option.value+"' key='"+option.innerHTML+"'><a title='remove' onclick='deleteAttachTag(\""+option.text+"\", \""+option.value+"\")'></a><label>"+option.innerHTML+"</label></span>";

    tagShow.innerHTML += snippet;
    tagAddSelect.remove(selectIndex);
}
/**
 * Comment
 */
function addUserNote() {
    document.getElementById("type").value = "user";
    document.getElementById("noteForm").submit();
}

/**
 * Comment
 */
function addStaffNote() {
    document.getElementById("type").value = "staff";
    document.getElementById("noteForm").submit();
}

function getFiles() {
    var childNodes = document.getElementById("fileNames").childNodes;
    for (i = childNodes.length - 1; i >= 0; i --) {
        document.getElementById("fileNames").removeChild(childNodes[i]);
    }

    var fileList = document.getElementById("demo-list");
    var listItems = fileList.getElementsByTagName("li");
    for(i = 0; i < listItems.length; i ++) {
        if (listItems[i].getElementsByTagName("span")[1] == null) {
            continue;
        }
        if (listItems[i].getAttribute("class") == "file file-failed") {
            continue;
        }

        var hiddenInput = new Element('input', {
            'type':"hidden",
            'name':'fileNames',
            'value': listItems[i].getElementsByTagName("span")[1].innerHTML.toString().trim()
        });
        document.getElementById("fileNames").insertBefore(hiddenInput, null);
    }
}

function getCaseAttachment(fileId) {
   window.open("downloadFile.do?type=caseAttachment&fileId="+fileId,"");
}

function deleteAttachmentFile( i,  fileId) {
    document.getElementById("attachFile"+String(i)).style.display = "none";
    if (document.getElementById("deleteFileIds").value == "")
        document.getElementById("deleteFileIds").value += String(fileId);
    else
        document.getElementById("deleteFileIds").value += ";" + String(fileId);
}
