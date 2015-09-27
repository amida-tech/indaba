
function setQuestionTypeTable() {
    var $qstnTbl = $('table.qstn-type-tbl');
    $qstnTbl.find('input, textarea').uniform();

    // if (adjustPrReviews) $('#prReviews').show();
    $('.show-then-hide').show();

    $qstnTbl.mCustomScrollbar({
        horizontalScroll: true,
        theme: 'dark-thick',
        autoDraggerLength: true,
        scrollButtons: {
            enable: true,
            scrollType: 'continuous'
        },
        advanced:{
            autoScrollOnFocus: false,
            updateOnContentResize: true
        }
    });

    $('.show-then-hide').hide();
    // if (adjustPrReviews) $('#prReviews').hide();

    $qstnTbl.find('td.int input:text').keyup(function() {
        $(this).val($(this).val().replace(/[^0-9|^\-]/g, ''));
    });
    $qstnTbl.find('td.float input:text').keyup(function() {
        $(this).val($(this).val().replace(/[^0-9|^\-|^.]/g, ''));
    });
}