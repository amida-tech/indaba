//////////////////////
//
// Define answer type
// 
//////////////////////
var ANSWER_TYPE_SINGLE_CHOICE = 1;
var ANSWER_TYPE_MULTI_CHOICE = 2;
var ANSWER_TYPE_INTEGER = 3;
var ANSWER_TYPE_FLOAT = 4;
var ANSWER_TYPE_TEXT = 5;
var ANSWER_TYPE_TABLE = 6;


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
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });

    if (result === 0) {
        window.location.href = ctx + "/yourcontent.do";
    }
}

function submitJournalPeerReview(ctx, horseid, assignid) {
    var parameters = {};

    var opinions = $("textarea[name=opinions]").val();
    if (opinions === undefined || opinions === "") {
        /*
         jAlert($.i18n.message('common.js.alert.askopinions'), $.i18n.message('common.js.alert.title.error'), function(){
         $('textarea[name=opinions]').focus();
         });
         */
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.error'),
            text: $.i18n.message('common.js.alert.askopinions'),
            type: 'error'
        });
        $('textarea[name=opinions]').focus();
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
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.submitreview'),
                type: 'error'
            });
        }
    });

    if (result === 0) {
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
    parameters.answerType = answerType;
    parameters.type = "review";
    parameters.comments = $("textarea[name=comments]").val();

    if (!selectionCheck(parameters, answerType)
            || !referenceCheck(parameters, referType)
            || !prReviewCheck(parameters, answerType)) {
        e0.disabled = "";
        return;
    }

    var rc = _submitIndicator(parameters);    
    if (rc.succeed) {
        //jInfo($.i18n.message('common.btn.saved'));
        if (rc.done) {
            enableCompleteAssignment();
        }
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.success'),
            text: $.i18n.message('common.js.msg.success_saved'),
            type: 'success'
        });
    } else {
        //jAlert(rc.errorMsg);        
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.error'),
            text: rc.errorMsg,
            type: 'error'
        });
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

    var rc = _submitIndicator(parameters);
    if (rc.succeed) {
        //jInfo($.i18n.message('common.btn.saved'));
        if (rc.done) {
            enableCompleteAssignment();
        }
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.success'),
            text: $.i18n.message('common.js.msg.success_saved'),
            type: 'success'
        });
    } else {
        //jAlert(rc.errorMsg);
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.error'),
            text: rc.errorMsg,
            type: 'error'
        });
    }

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

    if (!selectionCheck(parameters, answerType)
            || !referenceCheck(parameters, referType)
            || !prReviewCheck(parameters, answerType)) {
        e0.disabled = "";
        return;
    }

    var rc = _submitIndicator(parameters);
    if (rc.succeed) {
        //jInfo($.i18n.message('common.btn.saved'));
        if (rc.done) {
            enableCompleteAssignment();
        }
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.success'),
            text: $.i18n.message('common.js.msg.success_saved'),
            type: 'success'
        });
    } else {
        //jAlert(rc.errorMsg);
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.error'),
            text: rc.errorMsg,
            type: 'error'
        });
    }

    e0.disabled = "";
}

function referenceCheck(parameters, referType) {
    var sourceDesc = $('textarea[name=sourceDesc]').val();
    if (sourceDesc === undefined)
        sourceDesc = "";
    else
        sourceDesc = sourceDesc.trim();

    var source = "";
    if (referType === 0) {
        if (sourceDesc === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.choicesource'),
                type: 'error'
            });
            return false;
        }
    } else if (referType === ANSWER_TYPE_SINGLE_CHOICE) {
        source = $('input[name=source]:checked').val();
        if (typeof(source) === "undefined") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.choicesource'),
                type: 'error'
            });
            $('input:checkbox[name=source]:first').focus();
            return false;
        }
    } else {
        source = "";
        $('input[name=source]:checked').each(function() {
            source += $(this).val();
            source += ",";
        });
        if (source === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.needonesource'),
                type: 'error'
            });
            $('input:checkbox[name=source]:first').focus();
            return false;
        }
        if (sourceDesc === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.asksoucedesc'),
                type: 'error'
            });
            $('textarea[name="sourceDesc"]').focus();
            return false;
        }
    }
    parameters.source = source;
    parameters.sourceDesc = sourceDesc;
    return true;
}

function selectionCheck(parameters, answerType) {
    var selection;
    if (answerType === ANSWER_TYPE_SINGLE_CHOICE) {
        selection = $('input[name=selection]:checked').val();
        if (typeof(selection) === "undefined") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.askscoreoption'),
                type: 'error'
            });
            $('input:radio[name=selection]:first').focus();
            return false;
        }
    } else if (answerType === ANSWER_TYPE_MULTI_CHOICE) {
        selection = "";
        $('input[name=selection]:checked').each(function() {
            selection += $(this).val();
            selection += ",";
        });
        if (selection === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.askscoreoption'),
                type: 'error'
            });
            return false;
        }
    } else if (answerType === ANSWER_TYPE_TEXT) {
        selection = $('textarea[name=selection]:visible').val();
        if (selection === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.inputanswer'),
                type: 'error'
            });
            $('textarea[name=selection]:visible').focus();
            return false;
        }
    } else if (answerType === ANSWER_TYPE_TABLE) { // no need pre-check the form data
        extractTableQuestionForm(parameters, $('table.qstn-type-tbl'));
        return true;
    } else {
        selection = $('input[name=selection]:visible').val();
        if (selection === "") {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.inputanswer'),
                type: 'error'
            });
            $('textarea[name=selection]:visible').focus();
            return false;
        }
    }
    parameters.answer = selection;
    return true;
}

function extractTableQuestionForm(parameters, $table) {
    if(!parameters){
        parameters = {};
    }
    $('input:text, textarea', $table).each(function() {
        parameters[$(this).attr('name')] = $(this).val();
    });
    $('input:radio', $table).each(function() {
        if ($(this).attr('checked')) {
            parameters[$(this).attr('name')] = $(this).attr('value');
        }
    });
    var multiChoices = {};
    $('input:checkbox', $table).each(function() {
        var attrName = $(this).attr('name');
        var choices = multiChoices[attrName];
        if (!choices) {
            choices = [];
            multiChoices[attrName] = choices;

        }
        var choices = multiChoices[attrName];
        if ($(this).attr('checked')) {
            choices[choices.length] = $(this).attr('value');
        }
    });
    $.extend(parameters, multiChoices);
    return parameters;
}

function prReviewCheck(parameters, answerType) {
    var prreviews = new Array();
    var i = 0;
    var result = true;
    $("div[id^=reviewOpinion]").each(function() {
        var prreview = {};
        prreview.id = this.id.substring(14);

        var opinion = $("input[name=opinion][type=radio]:checked", this).val();
        if (opinion === undefined) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: $.i18n.message('common.js.alert.askopinions'),
                type: 'error'
            });
            result = false;
            return;
        }
        prreview.opinion = opinion;

        // var answerType = parseInt($("input[type=hidden][name=answerType]", this).val());
        switch (opinion) {
            case "0":
            case "3":
                break;
            case "2":
                if (answerType === ANSWER_TYPE_SINGLE_CHOICE) { // single choice
                    prreview.suggestedScore = $("input[name^=selection]:checked", this).val();
                } else if (answerType === ANSWER_TYPE_MULTI_CHOICE) { // multi-choice
                    prreview.suggestedScore = "";
                    $("input[name^=selection]:checked", this).each(function() {
                        prreview.suggestedScore += $(this).val();
                        prreview.suggestedScore += ",";
                    });
                } else if (answerType === ANSWER_TYPE_INTEGER) { // integer type
                    prreview.suggestedScore = $("input[name^=selection]", this).val();
                } else if (answerType === ANSWER_TYPE_FLOAT) { // float type
                    prreview.suggestedScore = $("input[name^=selection]", this).val();
                } else if (answerType === ANSWER_TYPE_TEXT) { // text type
                    prreview.suggestedScore = $("textarea[name^=selection]", this).val();
                }

                var p = $("tr.opinion2", this);
                prreview.comments = $("textarea.reviewer-comment", p).val();

                if (answerType !== ANSWER_TYPE_TABLE) {
                    if (prreview.suggestedScore === undefined || prreview.suggestedScore === "") {
                        ocsShowNotify({
                            title: $.i18n.message('common.js.alert.title.error'),
                            text: $.i18n.message('common.js.alert.suggestedscore'),
                            type: 'error'
                        });
                        result = false;
                        return;
                    }
                }

                if (prreview.comments === '') {
                    ocsShowNotify({
                        title: $.i18n.message('common.js.alert.title.error'),
                        text: $.i18n.message('common.js.alert.suggestedscore'),
                        type: 'error'
                    });
                    result = false;
                    return;
                }
                break;
            case "1":
                p = $("tr.opinion1", this);
                prreview.comments = $("textarea.reviewer-comment", p).val();
                if (prreview.comments === '') {
                    ocsShowNotify({
                        title: $.i18n.message('common.js.alert.title.error'),
                        text: $.i18n.message('common.js.alert.comments'),
                        type: 'error'
                    });
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
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "create");
}

function submitIndicatorEdit(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "edit");
}

function submitIndicatorReviewResponse(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "reviewresponse");
}

function submitIndicatorFlagResponse(e0, ctx, horseid, assignid, questionid, answerType, referType) {
    _submitAnswer(e0, ctx, horseid, assignid, questionid, answerType, referType, "flagresponse");
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
    parameters.answerType = answerType;
    parameters.comments = $("textarea[name=comments]").val();

    if (!selectionCheck(parameters, answerType)
            || !referenceCheck(parameters, referType)
            || !prReviewCheck(parameters, answerType)) {
        e0.disabled = "";
        return;
    }

    var rc = _submitIndicator(parameters);
    if (rc.succeed) {
        if (rc.done) {
            enableCompleteAssignment();
        }
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.success'),
            text: $.i18n.message('common.js.msg.success_saved'),
            type: 'success'
        });
        //document.getElementById("saveComplete").innerHTML = "&nbsp; " + $.i18n.message('common.btn.saved');
    } else {
        ocsShowNotify({
            title: $.i18n.message('common.js.alert.title.error'),
            text: rc.errorMsg,
            type: 'error'
        });
    }
    e0.disabled = "";
}

function _submitIndicator(parameters) {
    $('.qstn-cell-invalid-answer').removeClass('qstn-cell-invalid-answer');

    var result = "";
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
            result = {
                succeed: false,
                errorMsg: $.i18n.message('common.js.err.interval')
            };
        }
    });
    if (result === "") {
        result = {
            succeed: false,
            errorMsg: $.i18n.message('common.js.err.interval')
        };
    }

    if (result.errorField) {
        $('#' + result.errorField).addClass('qstn-cell-invalid-answer');
    }

    return result;
}

function _submitSurveyAssignment(ctx, type, horseid, assignid) {
    var parameters = new Object();

    parameters.assignid = assignid;
    parameters.horseid = horseid;

    parameters.type = type;

    $.ajax({
        type: "POST",
        url: ctx + "/submitSurvey.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            var json = JSON.parse(response);
            if (json.ret == 0) {
                window.location.href = ctx + "/yourcontent.do";
            } else {
                /*
                ocsShowNotify({
                    title: $.i18n.message('common.js.alert.title.error'),
                    text: json.data,
                    type: 'error'
                });
                */
               ocsError(json.data);
            }
        },
        error: function(response) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });
}

function submitSurveyReviewAssignment(ctx, horseid, assignid) {
    jConfirm($.i18n.message('common.js.alert.sumbitassignment'), $.i18n.message('common.js.alert.title.confirm'), function(choice) {
        if (choice) {
            _submitSurveyAssignment(ctx, "review", horseid, assignid);
        }
    });
}

function submitSurveyPeerReviewAssignment(ctx, horseid, assignid) {
    jConfirm($.i18n.message('common.js.alert.sumbitassignment'), $.i18n.message('common.js.alert.title.confirm'), function(choice) {
        if (choice) {
            _submitSurveyAssignment(ctx, "peerreview", horseid, assignid);
        }
    });
}

function submitSurveyPRReviewAssignment(ctx, horseid, assignid) {
    _submitSurveyAssignment(ctx, "prreview", horseid, assignid);
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
            if (response === "OK") {
                msg = $.i18n.message('common.js.alert.sumbitassignment');
            } else {
                msg = $.i18n.message('common.js.alert.pendingquestion');
            }

            jConfirm(msg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
                if (r) {
                    submitSurveyPRReviewAssignment(ctx, horseid, assignid);
                }
            });
            return false;
        },
        error: function(response) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
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
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
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
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });

    return result;
}


function trySubmitSurveyReview(ctx, horseid, assignid, returl) {
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
            if (response === "OK") {
                msg = $.i18n.message('common.js.alert.sumbitassignment');
            } else {
                msg = $.i18n.message('common.js.alert.pendingquestion');
            }

            jConfirm(msg, $.i18n.message('common.js.alert.title.confirm'), function(r) {
                if (r) {
                    _submitSurveyAssignment(ctx, "review", horseid, assignid);
                }
            });
            return false;
        },
        error: function(response) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });
}


function submitProblems(params) {
    var parameters = new Object();

    parameters.assignid = params.assignid;
    parameters.horseid = params.horseid;

    var rusers = document.getElementById("ruserid");
    parameters.ruserid = rusers.options[rusers.selectedIndex].value;
    var rusername = rusers.options[rusers.selectedIndex].text;

    var contents = "";

    $('input[name=content]:checked').each(function() {
        contents += $(this).val();
    });

    parameters.contents = contents;
    parameters.duedate = document.getElementById("duedate").value;

    parameters.action = "surveyReviewResponse";

    $.ajax({
        type: "POST",
        url: params.ctx + "/iHaveQuestions.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            jInfo($.i18n.message("common.js.questions.sent", [rusername]), $.i18n.message('common.btn.done'));
            if (sendQuestionsCallback) {
                sendQuestionsCallback({
                    rusername: rusername
                });
            } else {
            }
        },
        error: function(response) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });
}


function trySubmitProblems(ctx, horseid, assignid) {
    jConfirm($.i18n.message('common.js.alert.sumitquestion'), $.i18n.message('common.js.alert.title.confirm'),
            function(r) {
                if (r) {
                    submitProblems({
                        ctx: ctx,
                        horseid: horseid,
                        assignid: assignid
                    });
                }
            });
}


function submitDone(ctx, horseid, assignid, returl) {
    var prompts = $.i18n.message('common.js.alert.sumbitassignment');

    jConfirm(prompts, $.i18n.message('common.js.alert.title.confirm'), function(choice) {
        if (choice) {
            handleSubmitDone(ctx, horseid, assignid, returl);
        }
    });
}


function handleSubmitDone(ctx, horseid, assignid, returl) {
    var parameters = new Object();

    parameters.assignid = assignid;
    parameters.horseid = horseid;
    parameters.returl = returl;

    $.ajax({
        type: "POST",
        url: ctx + "/surveySubmit.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(response) {
            var json = JSON.parse(response);
            if (json.ret == 0) {
                window.location.href = ctx + "/yourcontent.do";
            } else {
                ocsError(json.data);
                /*
                ocsShowNotify({
                    title: $.i18n.message('common.js.alert.title.error'),
                    text: json.data,
                    type: 'error'
                });
                */
            }
        },
        error: function(response) {
            ocsShowNotify({
                title: $.i18n.message('common.js.alert.title.error'),
                text: response,
                type: 'error'
            });
        }
    });
}


