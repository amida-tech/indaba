
function resetStatus() {
//document.getElementById("saveComplete").innerHTML = "";
}

function addAllQuestions(horseId, assignId) {
    jConfirm($.i18n.message('common.js.alert.add.all.questions'),
            $.i18n.message('common.js.alert.title.confirm'), function(r) {
        if (r) {
            var parameters = new Object();
            parameters.horseid = horseId;
            parameters.assignid = assignId;
            parameters.action = "addall";

            $.ajax({
                type: "POST",
                url: "surveyProblemList.do",
                data: parameters,
                cache: false,
                async: false,
                success: function(result) {
                    window.location.reload();
                    //document.getElementById("questionlist").innerHTML = result;
                },
                error: function(result) {
                    alert(result);
                }
            });
        }
    })
}

function addTaggedToQuestions(horseId, tag) {
    jConfirm($.i18n.message('common.js.alert.add.tagged.questions'),
            $.i18n.message('common.js.alert.title.confirm'), function(r) {
        if (r) {
            var parameters = new Object();
            parameters.horseid = horseId;
            parameters.tag = tag;
            parameters.action = "addtagged";

            $.ajax({
                type: "POST",
                url: "surveyProblemList.do",
                data: parameters,
                cache: false,
                async: false,
                success: function(result) {
                    window.location.reload();
                    //document.getElementById("questionlist").innerHTML = result;
                },
                error: function(result) {
                    alert(result);
                }
            });
        }
    })
}

function removeQuestion(saId, question) {
    var parameters = new Object();
    parameters.said = saId;
    parameters.action = "remove";

    $.ajax({
        type: "POST",
        url: "surveyProblemList.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            window.location.reload();
        },
        error: function(result) {
            alert(result);
        }
    });
}

function loadInternalDiscussions(param) {
    var parameters = new Object();
    parameters.horseid = param.horseId;
    parameters.viewMode = param.viewMode;
    parameters.contentVersionId = param.contentVersionId;
    parameters.action = "getInternalMsgboardId";
    var msgboardId = -1;

    $.ajax({
        type: "POST",
        url: param.ctxPath + "/review.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            msgboardId = JSON.parse(result);
        },
        error: function(result) {
            msgboardId = -1;
        }
    });

    parameters = new Object();
    parameters.ctxPath = param.ctxPath;
    parameters.userId = param.userId;
    parameters.userName = param.userName;
    parameters.name = "discussion";
    parameters.containerName = "discussion";
    parameters.displayName = $.i18n.message('common.js.msg.privatediscussion');
    parameters.type = "ccw";
    parameters.checkPermission = param.checkPermission;
    parameters.msgboardId = msgboardId;
    parameters.folded = param.folded;
    parameters.viewMode = param.viewMode;
    parameters.discussionType = 'CONTENT_INTERNAL_DISCUSSION';
    if (param.syncStatus === "1" && param.assignId !== "0") {
        parameters.syncStatus = param.syncStatus;
        parameters.syncStatusFun = function() {
            updateStatus(param.ctxPath, param.assignId, 4);
        };
    }
    new discussionBoard(parameters);
}

function loadSurveyStaffDiscussions(param) {
    var parameters = new Object();
    parameters.horseid = param.horseId;
    parameters.viewMode = param.viewMode;
    parameters.contentVersionId = param.contentVersionId;
    parameters.action = "getStaffAuthorMsgboardId";

    var msgboardId;

    $.ajax({
        type: "POST",
        url: param.ctxPath + "/review.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
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
    parameters.displayName = $.i18n.message('common.js.msg.discussion');
    parameters.type = "ssr";
    parameters.checkPermission = param.checkPermission;
    parameters.msgboardId = msgboardId;
    parameters.folded = param.folded;
    parameters.viewMode = param.viewMode;
    parameters.tool = param.tool;

    if (param.syncStatus === "1" && param.assignId !== "0") {
        parameters.syncStatus = param.syncStatus;
        parameters.syncStatusFun = function() {
            updateStatus(param.ctxPath, param.assignId, 4);
        };
    }
    new discussionBoard(parameters);
}

function loadSurveyAnswerDiscussions(param) {
    var parameters = new Object();
    parameters.horseid = param.horseId;
    parameters.questionid = param.questionId;
    parameters.viewMode = param.viewMode;
    parameters.contentVersionId = param.contentVersionId;
    parameters.action = "getSurveyAnswerInternalMsgboardId";

    var msgboardId;

    $.ajax({
        type: "POST",
        url: param.ctxPath + "/review.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
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
    parameters.displayName = $.i18n.message('common.js.msg.privatediscussion');
    parameters.type = "ccw";
    parameters.checkPermission = param.checkPermission;
    parameters.msgboardId = msgboardId;
    parameters.folded = param.folded;
    parameters.viewMode = param.viewMode;
    if (param.syncStatus === "1" && param.assignId !== "0") {
        parameters.syncStatus = param.syncStatus;
        parameters.syncStatusFun = function() {
            updateStatus(param.ctxPath, param.assignId, 4);
        };
    }
    new discussionBoard(parameters);
}

function loadSurveyAnswerStaffDiscussions(param) {
    var parameters = new Object();
    parameters.horseid = param.horseId;
    parameters.questionid = param.questionId;
    parameters.viewMode = param.viewMode;
    parameters.contentVersionId = param.contentVersionId;
    parameters.action = "getSurveyAnswerStaffAuthorMsgboardId";

    var msgboardId;

    $.ajax({
        type: "POST",
        url: param.ctxPath + "/review.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
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
    parameters.displayName = $.i18n.message('common.js.msg.discussion');
    parameters.type = "ssr";
    parameters.checkPermission = param.checkPermission;
    parameters.msgboardId = msgboardId;
    parameters.folded = param.folded;
    parameters.disabled = param.disabled;
    parameters.viewMode = param.viewMode;
    parameters.tool = param.tool;

    if (param.syncStatus === "1" && param.assignId !== "0") {
        parameters.syncStatus = param.syncStatus;
        parameters.syncStatusFun = function() {
            updateStatus(param.ctxPath, param.assignId, 4);
        };
    }
    new discussionBoard(parameters);
}
function saveSurveyAnswerPeerReview(param) {
    var container = document.getElementById(param.containerName);
    var parameters = new Object();

    parameters.horseid = param.horseId;
    parameters.assignid = param.assignId;
    parameters.peerReviewId = param.peerReviewId;
    parameters.questionid = param.questionId;
    parameters.answerType = param.answerType;
    parameters.type = "peerreview";
    parameters.suggestedScore = '';
    parameters.comments = '';

    var opinion = $("input[name=opinion][type=radio]:checked").val();
    if (param.answerType !== ANSWER_TYPE_TABLE && opinion === undefined) {
        jAlert($.i18n.message('common.js.alert.askopinions'));
        return false;
    }
    parameters.opinion = opinion;

    switch (opinion) {
        case "0":
        case "3":
            break;
        case "2":
            if (param.answerType === ANSWER_TYPE_SINGLE_CHOICE) { // single choice
                parameters.suggestedScore = $("input[name=selection]:checked", $(container).parent("div")).val();
            } else if (param.answerType === ANSWER_TYPE_MULTI_CHOICE) { // multi-choice
                $("input[name=selection]:checked", $(container).parent("div")).each(function() {
                    parameters.suggestedScore += $(this).val();
                    parameters.suggestedScore += ",";
                });
            } else if (param.answerType === ANSWER_TYPE_INTEGER) { // integer type
                parameters.suggestedScore = $("input[name=selection]", $(container).parent("div")).val();
            } else if (param.answerType === ANSWER_TYPE_FLOAT) { // float type
                parameters.suggestedScore = $("input[name=selection]", $(container).parent("div")).val();

            } else if (param.answerType === ANSWER_TYPE_TEXT) { // text type
                parameters.suggestedScore = $("textarea[name=selection]", $(container).parent("div")).val();
            } else if (param.answerType === ANSWER_TYPE_TABLE) { // table type
                extractTableQuestionForm(parameters, $('div#prReviews table.qstn-type-tbl'));
            }

            var p = $("tr.opinion2");
            parameters.comments = $("textarea.reviewer-comment", p).val();

            if (parameters.comments === '' || (param.answerType !== ANSWER_TYPE_TABLE && (parameters.suggestedScore === undefined || parameters.suggestedScore === ""))) {
                jAlert($.i18n.message('common.js.alert.suggestedscore'));
                return false;
            }

            break;
        case "1":
            var p = $("tr.opinion1");
            parameters.comments = $("textarea.reviewer-comment", p).val();

            if (parameters.comments === '') {
                jAlert($.i18n.message('common.js.alert.comments'));
                return false;
            }

            break;
    }
    $.ajax({
        type: "POST",
        url: param.ctxPath + "/submitIndicator.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            //				if (result.indexOf("OK") > -1)
            //					alert("Your response has been saved successfully!");
            var rc = JSON.parse(result);
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
            if (rc.errorField) {
                $('#' + rc.errorField).addClass('qstn-cell-invalid-answer');
            }
        },
        error: function(result) {
            return {
                succeed: false,
                errorMsg: $.i18n.message('common.js.err.interval')
            };
        }
    });
    return false;
}
function loadSurveyPeerReview(param) {
    var container = document.getElementById(param.containerName);

    $("input[name=opinion][type=radio]").live("click", function() {
        var opinion = $("input[name=opinion][type=radio]:checked").val();
        selectOpinion(opinion);
    });

    $("button.saveReview").live("click", function() {
        saveSurveyAnswerPeerReview(param);
    });

    $(container).append("<div id=\"peerReview" + param.msgBoardId + "\" class=\"box\"></div>");

    var parameters = new Object();
    parameters.ctxPath = param.ctxPath;
    parameters.userId = param.userId;
    parameters.answerType = param.answerType;
    parameters.userName = param.userName;
    parameters.name = "peerReview" + param.msgBoardId;
    parameters.containerName = "peerReview" + param.msgBoardId;
    parameters.displayName = $.i18n.message('common.js.msg.privatediscussion');
    parameters.type = "spr";
    parameters.checkPermission = param.checkPermission;
    parameters.msgboardId = param.msgBoardId;
    parameters.folded = param.folded;
    parameters.peerReviewId = param.peerReviewId;
    if (param.syncStatus === "1" && param.assignId !== "0") {
        parameters.syncStatus = param.syncStatus;
        parameters.syncStatusFun = function() {
            updateStatus(param.ctxPath, param.assignId, 4);
        };
    }

    if (!param.hideDiscussion || param.hideDiscussion === 'false') {
        new discussionBoard(parameters);
    }
}

function loadSurveyPRReviews(param) {
    var peerReviews;
    var container = document.getElementById(param.containerName);

    var parameters = new Object();
    parameters.horseid = param.horseId;
    parameters.questionid = param.questionId;
    parameters.superedit = param.superedit;
    parameters.checkrights = param.checkRights;
    parameters.saVerId = param.saVerId;
    parameters.viewMode = param.viewMode;
    parameters.action = "getSurveyPRReviews";

    //alert(param.answerEditable);

    $.ajax({
        type: "POST",
        url: param.ctxPath + "/review.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            peerReviews = JSON.parse(result);
        },
        error: function(xhr, status, error) {
            //alert(xhr.status);
        }
    });

    if (peerReviews === null || peerReviews.length === 0)
        return;

    for (var i = 0; i < peerReviews.length; i++) {
        var pr = peerReviews[i];

        parameters = new Object();
        parameters.reviewerid = pr.reviewer.userId;
        parameters.horseid = param.horseId;
        parameters.questionid = param.questionId;
        parameters.commentsEditable = param.commentsEditable;
        parameters.opinionEditable = param.opinionEditable;
        parameters.answerEditable = param.answerEditable;
        parameters.hideDiscussion = param.hideDiscussion;
        parameters.contents = param.contents;
        parameters.saVerId = param.saVerId;
        parameters.viewMode = param.viewMode;
        parameters.highlightParts = param.highlightParts;

        $.ajax({
            type: "POST",
            url: param.ctxPath + "/surveyAnswerPeerReviewDisplay.do",
            data: parameters,
            cache: false,
            async: false,
            success: function(result) {
                $(container).append(result);
            },
            error: function(result) {
                alert(result);
            }
        });

        parameters = new Object();
        parameters.ctxPath = param.ctxPath;
        parameters.userId = param.userId;
        parameters.userName = param.userName;
        parameters.name = "peerReview_" + pr.id;
        parameters.containerName = "peerReview_" + pr.id;
        parameters.displayName = $.i18n.message('common.js.msg.discussion');

        parameters.type = "spr";
        parameters.checkPermission = param.checkPermission;
        parameters.msgboardId = pr.msgboardId;
        parameters.folded = param.folded;
        parameters.viewMode = param.viewMode;
        if (param.syncStatus === "1" && param.assignId !== "0") {
            parameters.syncStatus = param.syncStatus;
            parameters.syncStatusFun = function() {
                updateStatus(param.ctxPath, param.assignId, 4);
            };
        }

        if (!param.hideDiscussion || param.hideDiscussion === 'false') {
            new discussionBoard(parameters);
        }
    }

    $("[id^=reviewOpinion]").each(function() {
        var c = this;
        $("input[name=opinion][type=radio]", this).live("click", function() {
            var opinion = $("input[name=opinion][type=radio]:checked", c).val();
            selectOpinion(opinion, c);           
        });
    });
}

function selectOpinion(opinion, c) {
    switch (opinion) {
        case "0":
            $("tr.opinion2", c).hide();
            $("tr.opinion1", c).hide();
            break;
        case "1":
            $("tr.opinion2", c).hide();
            $("tr.opinion1", c).show();
            break;
        case "2":
            $("tr.opinion1", c).hide();
            $("tr.opinion2", c).show();
            break;
        case "3":
            $("tr.opinion2", c).hide();
            $("tr.opinion1", c).hide();
            break;
    }
}


function loadSurveyAnswerQuestionSidebar(ctx, action, horseid, assignid, answerid, disp_answerid, returl) {
    var parameters = new Object();
    parameters.action = action;
    parameters.horseid = horseid;
    parameters.assignid = assignid;
    parameters.answerid = answerid;
    parameters.disp_answerid = disp_answerid;
    parameters.returl = returl;

    $.ajax({
        type: "POST",
        url: ctx + "/surveyProblemList.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            $("#problem_list")[0].innerHTML = result;
            $('#questions-box .tipTip').tipTip({
                'defaultPosition': 'right'
            });
            if (loadProblemListCallback) {
                loadProblemListCallback();
            }
        },
        error: function(result) {
            alert(result);
        }
    });
    return false;
}

function loadSurveyAnswerQuestionSidebarWithoutAction(ctx, horseid, assignid, answerid, disp_answerid, returl) {
    var parameters = new Object();
    parameters.horseid = horseid;
    parameters.assignid = assignid;
    parameters.answerid = answerid;
    parameters.disp_answerid = disp_answerid;
    parameters.returl = returl;
    parameters.reviewResponse = "TRUE";

    $.ajax({
        type: "POST",
        url: ctx + "/surveyProblemList.do",
        data: parameters,
        cache: false,
        async: false,
        success: function(result) {
            $("#problem_list")[0].innerHTML = result;
            if (loadProblemListCallback) {
                loadProblemListCallback();
            }
        },
        error: function(result) {
            jAlert(result);
        }
    });
}