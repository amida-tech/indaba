function loadInternalDiscussions(param) {
	var parameters = new Object();
	parameters.horseid = param.horseId;
	parameters.action = "getInternalMsgboardId";
	
	var msgboardId;
	
	$.ajax({
		type: "POST",
		url: param.ctxPath + "/review.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			if (result.indexOf("invalid") > -1)
				window.location.href = ctx;
			
			if (result.indexOf("error") > -1)
				alert(result);
			
			msgboardId = JSON.parse(result);
		},
		error: function(result) {
			alert(result);
		}
	});
	
	parameters = new Object();
	parameters.ctxPath = param.ctxPath;
	parameters.userId = param.userId;
        parameters.userName = param.userName;
	parameters.name = "discussion";
	parameters.containerName = "discussion";
	parameters.displayName = "Staff Discussion";
	parameters.type = "ccw";
	parameters.checkPermission = param.checkPermission;
	parameters.msgboardId = msgboardId;
	parameters.folded = param.folded;
	if (param.syncStatus == "1" && param.assignId != "0") {
		parameters.syncStatus = true;
		parameters.syncStatusFun = function() {
			updateStatusAndPercentage(param.ctxPath, param.assignId, 4, 0.5);
		};
	}
	new discussionBoard(parameters);
}

function loadStaffDiscussions(param) {
	var parameters = new Object();
	parameters.horseid = param.horseId;
	parameters.action = "getStaffAuthorMsgboardId";
	
	var msgboardId;
	
	$.ajax({
		type: "POST",
		url: param.ctxPath + "/review.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			if (result.indexOf("invalid") > -1)
				window.location.href = ctx;
			
			if (result.indexOf("error") > -1)
				alert(result);
			
			msgboardId = JSON.parse(result);
		},
		error: function(result) {
			alert(result);
		}
	});
	
        parameters = new Object();
	parameters.ctxPath = param.ctxPath;
	parameters.userId = param.userId;
        parameters.userName = param.userName;
	parameters.name = "staffDiscussion";
	parameters.containerName = "staffDiscussion";
	parameters.displayName = "Staff / Author Discussion";
	parameters.type = "jsr";
	parameters.checkPermission = param.checkPermission;
	parameters.msgboardId = msgboardId;
	parameters.folded = param.folded;
	if (param.syncStatus == "1" && param.assignId != "0") {
		parameters.syncStatus = true;
		parameters.syncStatusFun = function() {
			updateStatusAndPercentage(param.ctxPath, param.assignId, 4, 0.5);
		};
	}
	new discussionBoard(parameters);
}

function loadPeerReviews(param) {
	var peerReviews;
	var container = document.getElementById(param.containerName);
	
	var parameters = new Object();
	parameters.horseid = param.horseId;
	parameters.action = "getJournalPeerReview";
	
	$.ajax({
		type: "POST",
		url: param.ctxPath + "/review.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			if (result.indexOf("invalid") > -1)
				window.location.href = ctx;
			
			if (result.indexOf("error") > -1)
				alert(result);
			
			peerReview = JSON.parse(result);
		},
		error: function(result) {
			alert(result);
		}
	});
	
	if (peerReview == null) {
		$(container).hide();
		return;
	}

	$("button[name=SaveOpinions]").live("click", function() {
		var parameters = new Object();
		parameters.horseid = param.horseId;
		parameters.assignid = param.assignId;
		parameters.action = "saveJournalPeerReviewOpinions";
		parameters.opinions = $("div.content textarea[name=opinions]").val();

		$.ajax({
			type: "POST",
			url: param.ctxPath + "/review.do",
			data: parameters,
			cache: false,
			async: false,
			success: function(result) {
				if (result.indexOf("invalid") > -1)
					window.location.href = ctx;
				
				if (result.indexOf("error") > -1)
					alert(result);
				
				if (result == "OK")
					jAlert("Your review has been successfully saved!", "OK", null);
			},
			error: function(result) {
				alert(result);
			}
		});
	});
	
	$(container).append("<div id=\"peerReview" + peerReview.msgboardId + "\" class=\"box\"></div>");
		
	parameters = new Object();
	parameters.ctxPath = param.ctxPath;
	parameters.userId = param.userId;
        parameters.userName = param.userName;
	parameters.name = "peerReview" + peerReview.msgboardId;
	parameters.containerName = "peerReview" + peerReview.msgboardId;
	parameters.displayName = "Discussion";
	parameters.type = "jpr";
	parameters.checkPermission = param.checkPermission;
	parameters.msgboardId = peerReview.msgboardId;
	parameters.components = "all";
	if (param.syncStatus == "1" && param.assignId != "0") {
		parameters.syncStatus = true;
		parameters.syncStatusFun = function() {
			updateStatusAndPercentage(param.ctxPath, param.assignId, 4, 0.5);
		};
	}
	new discussionBoard(parameters);
}

function loadPRReviews(param) {
	var peerReviews;
	var container = document.getElementById(param.containerName);
	
	var parameters = new Object();
	parameters.horseid = param.horseId;
	parameters.superedit = param.superedit;
	parameters.action = "getJournalPRReviews";
	
	$.ajax({
		type: "POST",
		url: param.ctxPath + "/review.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(result) {
			if (result.indexOf("invalid") > -1)
				window.location.href = ctx;
			
			if (result.indexOf("error") > -1)
				alert(result);

			peerReviews = JSON.parse(result);
		},
		error: function(result) {
			alert(result);
		}
	});
	
	if (peerReviews == null) {
		$(container).hide();
		return;
	}
	
	for (var i = 0; i < peerReviews.length; i++) {
		var peerReview = peerReviews[i];
		var editable = param.supereidt == "1" || (param.checkPermission != "1" && peerReview.submitTime != null);
		
		var html = "<div class=\"box\"><h3>"; 
		
		html += peerReview.reviewer.displayUsername;
		
		html +=	"<a class=\"toggleVisible\" href=\"#\"><img src='images/collapse_icon.png' alt='collapse' /></a></h3>" +
			"<div class=\"content\">" +
			"<table>" +
			"<tr>" +
			"<td class=\"newTopic\" align='left'>" +
			"<h4>Peer Review, Intended for Publication:</h4>" +
			"<textarea " + (editable ? "" : "readonly=\"true\"") + ">" + (peerReview.opinions == undefined ? "" : peerReview.opinions) + "</textarea>" +
			(editable ? "<button style=\"float:right; margin: 5px 70px;\" class=\"add medium button blue icon-save\" name=\"SaveOpinions" + peerReview.id + "\">Save</button>" : "") +
			"<div class=\"clear\"></div>" +
			"</td></tr></table></div>" +
			"<div class=\"content\"><div id=\"peerReview" + peerReview.msgboardId + "\" class=\"box\"></div></div>" +
			"</div>";
		
		$(container).append(html);
		
		parameters = new Object();
		parameters.ctxPath = param.ctxPath;
		parameters.userId = param.userId;
                parameters.userName = param.userName;
                parameters.name = "peerReview" + peerReview.msgboardId;
		parameters.containerName = "peerReview" + peerReview.msgboardId;
		parameters.displayName = "Discussion";
		parameters.type = "jprr";
		parameters.checkPermission = param.checkPermission;
		parameters.msgboardId = peerReview.msgboardId;
		parameters.components = "all";
		if (param.syncStatus == "1" && param.assignId != "0") {
			parameters.syncStatus = true;
			parameters.syncStatusFun = function() {
				updateStatusAndPercentage(param.ctxPath, param.assignId, 4, 0.5);
			};
		}
		
		new discussionBoard(parameters);
	}
	
	$("button[name^=SaveOpinions]").each(function() {
		var peerreviewid = this.name.substring(12);
		$(this).bind("click", function(){
			var parameters = {};
			
			parameters.horseid = param.horseId;
			parameters.assignid = param.assignId;
			parameters.peerreviewid = peerreviewid;
			parameters.action = "changeJournalPeerReviewOpinions";
			parameters.opinions = $("textarea", $(this).parent()).val();
			
			$.ajax({
				type: "POST",
				url: param.ctxPath + "/review.do",
				data: parameters,
				cache: false,
				async: false,
				success: function(result) {
					if (result.indexOf("invalid") > -1)
						window.location.href = ctx;
					
					if (result.indexOf("error") > -1)
						alert(result);
					
					if (result == "OK")
						jAlert("Your review has been successfully saved!", "OK", null);
				},
				error: function(result) {
					alert(result);
				}
			});
		});
		
	});
}

function iHaveQuestions(ctx, horseid, assignid) {
    var prompt = "Do you want to submit your questions now?";
    jConfirm(prompt, "Confirmation", function(r) {
        if (r) {
            var parameters = new Object();

            parameters.assignid = assignid;
            parameters.horseid = horseid;
            parameters.action = "journalReviewResponse";

            $.ajax({
                type: "POST",
                url: ctx + "/iHaveQuestions.do",
                data: parameters,
                cache: false,
                async: false,
                success: function(response) {
                    $("#haveQuestions")[0].innerHTML = "";
                },
                error: function(response) {
                    alert(response);
                }
            });
        }
    });
}

function trySubmitDone(ctx, horseid, assignid) {
    var parameters = new Object();

    parameters.assignid = assignid;
    parameters.horseid = horseid;
    parameters.action = "journalSubmitDone";

    $.ajax({
        type: "POST",
        url: ctx + "/iHaveQuestions.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            var msg;
            if (response == "OK") {
                msg = "Are you sure you have completed the assignment and want to submit now?";
            } else {
                msg = "Your questions have not been answered yet.\nAre you sure you have completed the assignment and want to submit now?";
            }

            jConfirm(msg, "Confirmation", function(r) {
                if (r){
                    submitJournalAssignment(ctx, horseid, assignid);
                }
            });
            return false;
        },
        error: function(response) {
            alert(response);
        }
    });
}