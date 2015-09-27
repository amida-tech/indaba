function submitJournalAssignment(ctx, horseid, assignid) {
	var parameters = new Object();
	
	parameters.assignid = assignid;
	parameters.horseid = horseid;
	
	var result;
	
	$.ajax({
		type: "POST",
		url: ctx + "/submitJournal.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(response) {
			result = JSON.parse(response);
		},
		error: function(response) {
			alert(response);
		}
	});
	
	if (result == 0) {
		window.location.href = ctx + "/yourcontent.do";
	}
}

function submitJournalPeerReview(ctx, horseid, assignid) {
	var parameters = {};
	
	var opinions = $("textarea[name=opinions]").val();
	if (opinions == undefined || opinions == "") {
            alert("Please provide your opinions!");
            return;
	}
	
	parameters.opinions = opinions;
	parameters.assignid = assignid;
	parameters.horseid = horseid;
	
	var result;
	
	$.ajax({
		type: "POST",
		url: ctx + "/submitJournal.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(response) {
			result = JSON.parse(response);
		},
		error: function() {
			jAlert("Failed to submit your review because of internal error!");
		}
	});
	
	if (result == 0) {
		window.location.href = ctx + "/yourcontent.do";
	}
}

function submitIndicatorReview(e0, ctx, horseid, assignid, questionid, referType, answerType) {
    e0.disabled = "disabled";

    var parameters = {};
    parameters.ctx = ctx;
    parameters.horseid = horseid;
    parameters.assignid = assignid;
    parameters.questionid = questionid;
    parameters.type = "review";
    parameters.comments = $("textarea[name=comments]").val();

        if (selectionCheck(parameters, answerType) == false) {
            e0.disabled = "";
            return;
        }

        if (referenceCheck(parameters, referType) == false) {
            e0.disabled = "";
            return;
        }

        if (prReviewCheck(parameters, referType) == false) {
            e0.disabled = "";
            return;
        }

    /*
        var prreviews = new Array();
	var i = 0;
	$("div[id^=reviewOpinion]").each(function() {
            var prreview = {};
            prreview.id = this.id.substring(14);

            var opinion = $("input[name=opinion][type=radio]:checked", this).val();
            if (opinion == undefined) {
                alert("Please select your opinion!");
                e0.disabled = "";
                return false;
            }
            prreview.opinion = opinion;

            var answerType = parseInt($("input[type=hidden][name=answerType]").val());
            switch(opinion) {
            case "0":
            case "3":
                    break;
            case "2":
                    if (answerType == 1) { // single choice
                        prreview.suggestedScore = $("input[name^=selection]:checked", this).val();
                    } else if (answerType == 2) { // multi-choice
                        prreview.suggestedScore = "";
                        $("input[name^=selection]:checked", $(container).parent("div")).each(function(){
                            prreview.suggestedScore += $(this).val();
                            prreview.suggestedScore += ",";
                        });
                    } else if (answerType == 3) { // integer type
                        prreview.suggestedScore = $("input[name^=selection]", this).val();
                    } else if (answerType == 4) { // float type
                        prreview.suggestedScore = $("input[name^=selection]", this).val();
                    } else if (answerType == 5) { // text type
                        prreview.suggestedScore = $("textarea[name^=selection]", this).val();
                    }

                    var p = $("tr.opinion2", this);
                    prreview.comments = $("textarea.reviewer-comment", p).val();

                    if (prreview.suggestedScore == undefined || prreview.suggestedScore == "") {
                        alert("Please provide your suggested scoring change as well as comments supporting your change!");
                        e0.disabled = "";
                        return false;
                    }

                    if (prreview.comments == '') {
                        alert("Please provide your suggested scoring change as well as comments supporting your change!");
                        e0.disabled = "";
                        return false;
                    }
                    break;
            case "1":
                    p = $("tr.opinion1", this);
                    prreview.comments = $("textarea.reviewer-comment", p).val();
                    if (prreview.comments == '') {
                        alert("Please enter your comments!");
                        e0.disabled = "";
                        return false;
                    }
                    break;
            }
            prreviews[i++] = prreview;
            return true;
	});
//	parameters.prcomments = prcomments;
	parameters.prreviews = JSON.stringify(prreviews);
    */
   
    var rc = _submitIndicator(parameters);
    if (rc.succeed) {
            //jAlert("Thank you for your review!", "Review Saved", null);
        document.getElementById("saveComplete").innerHTML = "&nbsp; Saved";
    } else {
        jAlert(rc.errorMsg);
    }
    
    e0.disabled = "";
}

function submitIndicatorPeerReview(e0, ctx, horseid, assignid, questionid, referType) {
    e0.disabled = "disabled";
    
    var parameters = {};
	parameters.ctx = ctx;
	parameters.horseid = horseid;
	parameters.assignid = assignid;
	parameters.questionid = questionid;
	parameters.type = "peerreivew";
	
    var rc=_submitIndicator(parameters);
    if (rc.succeed) {
        //jAlert("Thank you for your review!", "Review Saved", null);
        document.getElementById("saveComplete").innerHTML = "&nbsp; Saved";
    } else
        jAlert(rc.errorMsg);
    
    e0.disabled = "";
}

function submitIndicatorPRReview(e0, ctx, horseid, assignid, questionid, referType, answerType) {
    e0.disabled = "disabled";
    
    var parameters = {};
    parameters.ctx = ctx;
    parameters.horseid = horseid;
    parameters.assignid = assignid;
    parameters.questionid = questionid;
    parameters.type = "prreview";
    parameters.comments = $("textarea[name=comments]").val();

    if (selectionCheck(parameters, answerType) == false) {
        e0.disabled = "";
        return;
    }

    if (referenceCheck(parameters, referType) == false) {
        e0.disabled = "";
        return;
    }

    if (prReviewCheck(parameters, referType) == false) {
        e0.disabled = "";
        return;
    }
	
/*
	var prreviews = new Array();
	var i = 0;
	$("div[id^=reviewOpinion]").each(function(){
		var prreview = {};
		
		prreview.id = this.id.substring(14);
		
		var opinion = $("input[name=opinion][type=radio]:checked", this).val();
		if (opinion == undefined) {
			alert("Please select your opinion!");
			return;
		}
		prreview.opinion = opinion;
		
		var answerType = parseInt($("input[type=hidden][name=answerType]").val());
		
		switch(opinion) {
		case "0":
		case "3":
			break;
		case "2":
			if (answerType == 1) { // single choice
				prreview.suggestedScore = $("input[name^=selection]:checked", this).val();
			} else if (answerType == 2) { // multi-choice
				$("input[name^=selection]:checked", $(container).parent("div")).each(function(){
					prreview.suggestedScore += $(this).val();
					prreview.suggestedScore += ",";
                                });
			} else if (answerType == 3) { // integer type
				prreview.suggestedScore = $("input[name^=selection]", this).val();
			} else if (answerType == 4) { // float type
				prreview.suggestedScore = $("input[name^=selection]", this).val();
				
			} else if (answerType == 5) { // text type
				prreview.suggestedScore = $("textarea[name^=selection]", this).val();
				
			}
			
			var p = $("tr.opinion2", this);
			prreview.comments = $("textarea.reviewer-comment", p).val();

			if (prreview.suggestedScore == undefined || prreview.suggestedScore == "") {
				alert("Please provide your suggested scoring change as well as comments supporting your change!");
				e0.disabled = "";
                                return false;
			}
			
			if (prreview.comments == '') {
				alert("Please provide your suggested scoring change as well as comments supporting your change!");
				e0.disabled = "";
                                return false;
			}
			
			break;
		case "1":
			var p = $("tr.opinion1", this);
			prreview.comments = $("textarea.reviewer-comment", p).val();
			
			if (prreview.comments == '') {
				alert("Please enter your comments!");
                                e0.disabled = "";
				return false;
			}
			
			break;
		}
		
		prreviews[i++] = prreview;
	});
//	parameters.prcomments = prcomments;
	parameters.prreviews = JSON.stringify(prreviews);
    */

    var rc=_submitIndicator(parameters);
    if (rc.succeed) {
        document.getElementById("saveComplete").innerHTML = "&nbsp; Saved";
        //    jAlert("Thank you for your review!", "Review Saved", null);
    } else
    	jAlert(rc.errorMsg);
    
    e0.disabled = "";
}

function referenceCheck(parameters, referType) {
    var sourceDesc = $('textarea[name=sourceDesc]').val();
    if (sourceDesc == undefined)
        sourceDesc = "";

    var source = "";
    if (referType == 0) {
        //source = $('textarea[name=source]').val();
        //if(source == ""){
        if(sourceDesc == ""){
            alert("Please input source information");
            return false;
        }
    } else if(referType == 1){
        source = $('input[name=source]:checked').val();
        if(typeof(source)=="undefined"){
            alert("Please choose a source");
            return false;
        }
    } else {
        source = "";
        $('input[name=source]:checked').each(function(){
            source += $(this).val();
            source += ",";
        });
        if(source == ""){
            alert("Please choose sources");
            return false;
        }
        if(sourceDesc == ""){
            alert("Please input source description");
            return false;
        }
    }
    parameters.source = source;
    parameters.sourceDesc = sourceDesc;
    return true;
}

function selectionCheck(parameters, answerType) {
    var selection;
    if (answerType == 1){
        selection = $('input[name=selection]:checked').val();
        if(typeof(selection)=="undefined"){
            alert("Please choose a score option.");
            return false;
        }
    } else if(answerType == 2){
        selection = "";
        $('input[name=selection]:checked').each(function(){
            selection += $(this).val();
            selection += ",";
        });
        if(selection == ""){
            alert("Please choose a score option.");
            return false;
        }
    } else if(answerType == 5){
        selection = $('textarea[name=selection]:visible').val();
        if(selection == ""){
            alert("Please input your answer");
            return false;
        }
    } else{
        selection = $('input[name=selection]:visible').val();
        if(selection == ""){
            alert("Please input your answer");
            return false;
        }
    }
    parameters.answer = selection;
    return true;
}

function prReviewCheck(parameters) {
    var prreviews = new Array();
    var i = 0;
    var result = true;
    $("div[id^=reviewOpinion]").each(function() {
        var prreview = {};
        prreview.id = this.id.substring(14);

        var opinion = $("input[name=opinion][type=radio]:checked", this).val();
        if (opinion == undefined) {
            alert("Please select your opinion!");
            result = false;
            return;
        }
        prreview.opinion = opinion;

        var answerType = parseInt($("input[type=hidden][name=answerType]").val());
        switch(opinion) {
        case "0":
        case "3":
                break;
        case "2":
                if (answerType == 1) { // single choice
                    prreview.suggestedScore = $("input[name^=selection]:checked", this).val();
                } else if (answerType == 2) { // multi-choice
                    prreview.suggestedScore = "";
                    $("input[name^=selection]:checked", $(container).parent("div")).each(function(){
                        prreview.suggestedScore += $(this).val();
                        prreview.suggestedScore += ",";
                    });
                } else if (answerType == 3) { // integer type
                    prreview.suggestedScore = $("input[name^=selection]", this).val();
                } else if (answerType == 4) { // float type
                    prreview.suggestedScore = $("input[name^=selection]", this).val();
                } else if (answerType == 5) { // text type
                    prreview.suggestedScore = $("textarea[name^=selection]", this).val();
                }

                var p = $("tr.opinion2", this);
                prreview.comments = $("textarea.reviewer-comment", p).val();

                if (prreview.suggestedScore == undefined || prreview.suggestedScore == "") {
                    alert("Please provide your suggested scoring change as well as comments supporting your change!");
                    result = false;
                    return;
                }

                if (prreview.comments == '') {
                    alert("Please provide your suggested scoring change as well as comments supporting your change!");
                    result = false;
                    return;
                }
                break;
        case "1":
                p = $("tr.opinion1", this);
                prreview.comments = $("textarea.reviewer-comment", p).val();
                if (prreview.comments == '') {
                    alert("Please enter your comments!");
                    result = false;
                    return;
                }
                break;
        }
        prreviews[i++] = prreview;
        return;
    });
//	parameters.prcomments = prcomments;
    parameters.prreviews = JSON.stringify(prreviews);
    return result;
}

function submitIndicatorAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "answer");
}

function submitIndicatorEdit(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "edit");
}

function submitIndicatorReviewResponse(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "reviewresponse");
}

function submitIndicatorOverallReview(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "overallreview");
}

function _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, action) {
    e0.disabled = "disabled";
    
    var parameters = {};
    parameters.ctx = ctx;
    parameters.horseid = horseid;
    parameters.assignid = assignid;
    parameters.questionid = questionid;
    parameters.type = action;
    parameters.comments = $("textarea[name=comments]").val();

    if (selectionCheck(parameters, answerType) == false) {
        e0.disabled = "";
        return;
    }

    if (referenceCheck(parameters, referType) == false) {
        e0.disabled = "";
        return;
    }

    if (prReviewCheck(parameters, referType) == false) {
        e0.disabled = "";
        return;
    }

    var rc=_submitIndicator(parameters);
    if (rc.succeed) {
        document.getElementById("saveComplete").innerHTML = "&nbsp; Saved";
        //    jAlert("Thank you for your review!", "Review Saved", null);
    } else {
        jAlert(rc.errorMsg);
    }

    e0.disabled = "";
}

function _submitIndicator(parameters) {
    var result;
    $.ajax({
        type: "POST",
        url: "submitIndicator.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            result = JSON.parse(response);
        },
        error: function(response) {
            result = {succeed: false, errorMsg: "Server internal error, please concact to your administrator!"};
        }
    });
    
    return result;
}

function _submitSurveyAssignment(ctx, type, horseid, assignid) {
	var parameters = new Object();
	
	parameters.assignid = assignid;
	parameters.horseid = horseid;
	
	parameters.type = type;
	
	var result;
	
	$.ajax({
		type: "POST",
		url: ctx + "/submitSurvey.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(response) {
			result = JSON.parse(response);
		},
		error: function(response) {
			alert(response);
		}
	});
	
	return result;
}

function submitSurveyReviewAssignment(ctx, horseid, assignid) {
    if (_submitSurveyAssignment(ctx, "review", horseid, assignid) == 0) {
        // alert("You have completed this assignment. Thank you!");
//        window.location.href = ctx + "/surveyDisplay.do?action=display&horseid=" + horseid;
        window.location.href = ctx + "/yourcontent.do";
    }
}

function submitSurveyPeerReviewAssignment(ctx, horseid, assignid) {
    var rc=_submitSurveyAssignment(ctx, "peerreview", horseid, assignid);
    if (rc == 0) {
        // alert("You have completed this assignment. Thank you!");
//        window.location.href = ctx + "/surveyDisplay.do?action=display&horseid=" + horseid;
        window.location.href = ctx + "/yourcontent.do";
    } else if (rc > 0) {
	alert("You have " + rc + " answers left which have not been reviewed!");
    }
}

function submitSurveyPRReviewAssignment(ctx, horseid, assignid) {
    if (_submitSurveyAssignment(ctx, "prreview", horseid, assignid) == 0) {
        // alert("You have completed this assignment. Thank you!");
//        window.location.href = ctx + "/surveyDisplay.do?action=display&horseid=" + horseid;
        window.location.href = ctx + "/yourcontent.do";
    }
}

function trySubmitSurveyPRReview(ctx, horseid, assignid) {
    var parameters = new Object();

    parameters.assignid = assignid;
    parameters.horseid = horseid;
    parameters.action = "surveySubmitDone";

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
                msg = "There are pending questions that author has not responded.\nAre you sure you have completed the assignment and want to submit now?";
            }

            jConfirm(msg, "Confirmation", function(r) {
                if (r){
                    submitSurveyPRReviewAssignment(ctx, horseid, assignid);
                }
            });
            return false;
        },
        error: function(response) {
            alert(response);
        }
    });
}


function updateSurveyAnswerFlag(horseid, assignid, questionid) {
    var parameters = new Object();
    parameters.assignid = assignid;
    parameters.questionid = questionid;
    if (horseid) {
        parameters.horseid = horseid;
    }
    $.ajax({
        type: "POST",
        url: "updateSurveyAnswerFlag.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            result = response;
        },
        error: function(response) {
            alert(response);
        }
    });
}

function updateStatus(ctx, assignid, status) {
	return _updateStatusAndPercentage(ctx, "updateStatus", assignid, status);
}

function updateStatusAndPercentage(ctx, assignid, status, percentage) {
	return _updateStatusAndPercentage(ctx, "updateStatusAndPercentage", assignid, status, percentage);
}

function _updateStatusAndPercentage(ctx, action, assignid, status, percentage, horseid) {
	var parameters = new Object();
	
	parameters.assignid = assignid;
	parameters.status = status;
	parameters.percentage = percentage;
	if (horseid)
		parameters.horseid = horseid;
	
	parameters.action = action;
	
	var result;
	
	$.ajax({
		type: "POST",
		url: ctx + "/updateTaskAssignmentStatus.do",
		data: parameters,
		cache: false,
		async: false,
		success: function(response) {
			result = JSON.parse(response);
		},
		error: function(response) {
			alert(response);
		}
	});
	
	return result;
}