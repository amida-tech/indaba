(function($) {
    $.fn.validationEngineLanguage = function() {
    };
    $.validationEngineLanguage = {
        newLang: function() {
            $.validationEngineLanguage.allRules = {
                "required": {// Add your regex rules here, you can take telephone as an example
                    "regex": "none",
                    "alertText": "* This field is required",
                    "alertTextCheckboxMultiple": "* Please select an option",
                    "alertTextCheckbox": "* This checkbox is required",
                    "alertTextDateRange": "* Both date range fields are required"
                },
                "requiredInFunction": {
                    "func": function(field, rules, i, options) {
                        return (field.val() == "test") ? true : false;
                    },
                    "alertText": "* Field must equal test"
                },
                "dateRange": {
                    "regex": "none",
                    "alertText": "* Invalid ",
                    "alertText2": "Date Range"
                },
                "dateTimeRange": {
                    "regex": "none",
                    "alertText": "* Invalid ",
                    "alertText2": "Date Time Range"
                },
                "minSize": {
                    "regex": "none",
                    "alertText": "* Minimum ",
                    "alertText2": " characters allowed"
                },
                "maxSize": {
                    "regex": "none",
                    "alertText": "* Maximum ",
                    "alertText2": " characters allowed"
                },
                "groupRequired": {
                    "regex": "none",
                    "alertText": "* You must fill one of the following fields"
                },
                "min": {
                    "regex": "none",
                    "alertText": "* Minimum value is "
                },
                "max": {
                    "regex": "none",
                    "alertText": "* Maximum value is "
                },
                "past": {
                    "regex": "none",
                    "alertText": "* Date prior to "
                },
                "future": {
                    "regex": "none",
                    "alertText": "* Date past "
                },
                "maxCheckbox": {
                    "regex": "none",
                    "alertText": "* Maximum ",
                    "alertText2": " options allowed"
                },
                "minCheckbox": {
                    "regex": "none",
                    "alertText": "* Please select ",
                    "alertText2": " options"
                },
                "length": {
                    "regex": "none",
                    "alertText": "* length MUEB be between ",
                    "alertText2": " and  ",
                    "alertText3": " !"
                },
                "creditCard": {
                    "regex": "none",
                    "alertText": "* Invalid credit card number"
                },
                "phone": {
                    // credit: jquery.h5validate.js / orefalo
                    "regex": /^([\+][0-9]{1,3}[\ \.\-])?([\(]{1}[0-9]{2,6}[\)])?([0-9\ \.\-\/]{3,20})((x|ext|extension)[\ ]?[0-9]{1,4})?$/,
                    "alertText": "* Invalid phone number"
                },
                "email": {
                    // HTML5 compatible email regex ( http://www.whatwg.org/specs/web-apps/current-work/multipage/states-of-the-type-attribute.html#    e-mail-state-%28type=email%29 )
                    "regex": /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/,
                    "alertText": "* Invalid email address"
                },
                "integer": {
                    "regex": /^[\-\+]?\d+$/,
                    "alertText": "* Not a valid integer"
                },
                "positiveInteger": {
                    "regex": /^\d*$/,
                    "alertText": "* Not a valid positive integer"
                },
                "number": {
                    // Number, including positive, negative, and floating decimal. credit: orefalo
                    "regex": /^[\-\+]?(([0-9]+)([\.,]([0-9]+))?|([\.,]([0-9]+))?)$/,
                    "alertText": "* Invalid floating decimal number"
                },
                "date": {
                    "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/,
                    "alertText": "* Invalid date, must be in YYYY-MM-DD format"
                },
                "afterNow": {
                    "func": function(field, rules, i, options) {
                        if (!field.val()) {
                            return true;
                        }
                        var now = new Date();
                        now.setHours(0);
                        now.setMinutes(0);
                        now.setSeconds(0);
                        now.setMilliseconds(0);
                        var fromDate = new Date(field.val());
                        return (now <= fromDate) ? true : false;
                    },
                    "alertText": "* It is overdue!"
                },
                "selectOne": {
                    "func": function(field, rules, i, options) {
                        return field.val() && field.val() >= 0;
                    },
                    "alertText": "* No option selected"
                },
                "ipv4": {
                    "regex": /^((([01]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))[.]){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))$/,
                    "alertText": "* Invalid IP address"
                },
                "url": {
                    "regex": /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i,
                    "alertText": "* Invalid URL"
                },
                "onlyNumberSp": {
                    "regex": /^[0-9\ ]+$/,
                    "alertText": "* Numbers only"
                },
                "onlyLetterSp": {
                    "regex": /^[a-zA-Z\ \']+$/,
                    "alertText": "* Letters only"
                },
                "onlyLetterNumber": {
                    "regex": /^[0-9a-zA-Z]+$/,
                    "alertText": "* No special characters allowed"
                },
                // --- CUSTOM RULES -- Those are specific to the demos, they can be removed or changed to your likings
                "ajaxUserCall": {
                    "url": "ajaxValidateFieldUser",
                    // you may want to pass extra data on the ajax call
                    "extraData": "name=eric",
                    "alertText": "* This user is already taken",
                    "alertTextLoad": "* Validating, please wait"
                },
                "ajaxUserCallPhp": {
                    "url": "phpajax/ajaxValidateFieldUser.php",
                    // you may want to pass extra data on the ajax call
                    "extraData": "name=eric",
                    // if you provide an "alertTextOk", it will show as a green prompt when the field validates
                    "alertTextOk": "* This username is available",
                    "alertText": "* This user is already taken",
                    "alertTextLoad": "* Validating, please wait"
                },
                "ajaxNameCall": {
                    // remote json service location
                    "url": "ajaxValidateFieldName",
                    // error
                    "alertText": "* This name is already taken",
                    // if you provide an "alertTextOk", it will show as a green prompt when the field validates
                    "alertTextOk": "* This name is available",
                    // speaks by itself
                    "alertTextLoad": "* Validating, please wait"
                },
                "ajaxNameCallPhp": {
                    // remote json service location
                    "url": "phpajax/ajaxValidateFieldName.php",
                    // error
                    "alertText": "* This name is already taken",
                    // speaks by itself
                    "alertTextLoad": "* Validating, please wait"
                },
                "validate2fields": {
                    "alertText": "* Please input HELLO"
                },
                //tls warning:homegrown not fielded 
                "dateFormat": {
                    "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(?:(?:0?[1-9]|1[0-2])(\/|-)(?:0?[1-9]|1\d|2[0-8]))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^(0?2(\/|-)29)(\/|-)(?:(?:0[48]00|[13579][26]00|[2468][048]00)|(?:\d\d)?(?:0[48]|[2468][048]|[13579][26]))$/,
                    "alertText": "* Invalid Date"
                },
                //tls warning:homegrown not fielded 
                "dateTimeFormat": {
                    "regex": /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1}$|^(?:(?:(?:0?[13578]|1[02])(\/|-)31)|(?:(?:0?[1,3-9]|1[0-2])(\/|-)(?:29|30)))(\/|-)(?:[1-9]\d\d\d|\d[1-9]\d\d|\d\d[1-9]\d|\d\d\d[1-9])$|^((1[012]|0?[1-9]){1}\/(0?[1-9]|[12][0-9]|3[01]){1}\/\d{2,4}\s+(1[012]|0?[1-9]){1}:(0?[1-5]|[0-6][0-9]){1}:(0?[0-6]|[0-6][0-9]){1}\s+(am|pm|AM|PM){1})$/,
                    "alertText": "* Invalid Date or Date Format",
                    "alertText2": "Expected Format: ",
                    "alertText3": "mm/dd/yyyy hh:mm:ss AM|PM or ",
                    "alertText4": "yyyy-mm-dd hh:mm:ss AM|PM"
                },
                'ajaxCheckConfNameExists': {
                    "url": "/exists/name",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#confId']
                },
                'ajaxCheckConfCidExists': {
                    "url": "/exists/cid",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#confId']
                },
                'ajaxCheckTrackDNameExists': {
                    "url": "/track/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#trackId']
                },
                'ajaxCheckTrackSNameExists': {
                    "url": "/track/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#trackId']
                },
                'ajaxCheckTrackTypeDNameExists': {
                    "url": "/track-type/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#trackTypeId']
                },
                'ajaxCheckTrackTypeSNameExists': {
                    "url": "/track-type/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#trackTypeId']
                },
                'ajaxCheckLocSNameExists': {
                    "url": "/location/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#confId','#locationId']
                },
                'ajaxCheckSessionNameExists': {
                    "url": "/session/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#confId','#sessionId']
                },
                'ajaxCheckPreNameExists': {
                    "url": "/pres/exists/subject",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#presentationId']
                },
                'ajaxCheckPosterboardTitleExists': {
                    "url": "/posterboard/exists/title",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#posterboardId']
                },
                'ajaxCheckBallotNameExists': {
                    "url": "/ballot/exists/name",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#ballotId']
                },
                'ajaxCheckOrgNameExists': {
                    "url": "/org/exists/name",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#orgId']
                },
                'ajaxCheckFacNameExists': {
                    "url": "/fac/exists/name",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#facId']
                },
                'ajaxCheckSlotNameExists': {
                    "url": "/slot/exists/name",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#slotId']
                },
                'ajaxCheckCatTypeDNameExists': {
                    "url": "/cattype/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#ctId']
                },
                'ajaxCheckCatTypeSNameExists': {
                    "url": "/cattype/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#ctId']
                },
                'ajaxCheckCatDNameExists': {
                    "url": "/cat/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#ctId', '#catId']
                },
                'ajaxCheckCatSNameExists': {
                    "url": "/cat/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#ctId', '#catId']
                },
                'ajaxCheckSponsorDNameExists': {
                    "url": "/sponsor/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#slId']
                },
                'ajaxCheckSponsorSNameExists': {
                    "url": "/sponsor/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#slId']
                },
                'ajaxCheckMembershipDNameExists': {
                    "url": "/membership/exists/dname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#mlId']
                },
                'ajaxCheckMembershipSNameExists': {
                    "url": "/membership/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#mlId']
                },
                'ajaxCheckSurveyNameExists': {
                    "url": "/survey/exists/sname",
                    "alertText": "* Already exists",
                    "extraDataDynamic": ['#surveyId']
                }
            };
        }
    };

    $.validationEngineLanguage.newLang();

})(jQuery);
