(function($, global){
    global.widgets = [
        {
            name:'Journal Display',
            image:'image/widget-1.png',
            target:'wms.jsp?widgetId=1',
            url:'widgets/journalDisplay.html',
            params:'horseId,includeReviews,includeLogo'
        },
        {
            name:'Vertical Scorecard',
            image:'image/widget-2.png',
            target:'wms.jsp?widgetId=2',
            url:'widgets/vcardDisplay.html',
            params:'horseId,includeLogo'
        },
        {
            name:'Horizontal Scorecard',
            image:'image/widget-3.png',
            target:'wms.jsp?widgetId=3',
            url:'widgets/hcardDisplay.html',
            params:'productId,includeLogo'
        },
        {
            name:'Data Summary',
            image:'image/widget-4.png',
            target:'wms.jsp?widgetId=4',
            url:'dataSummary.jsp',
            params:'horseId,includeLogo'
        },
        {
            name:'Indicator Summary',
            image:'image/widget-5.png',
            target:'wms.jsp?widgetId=5',
            url:'indicatorSummary.jsp',
            params:'horseId,includeLogo'
        },
        {
            name:'Sparkline',
            image:'image/widget-6.png',
            target:'wms.jsp?widgetId=6',
            url:'sparkline.jsp',
            params:'productId,horseId2,includeLogo'
        }
    ];

    global.currentStep = 0;

    global.adjustSteps = function(){
        $('#steps-bar > li').removeClass();
        $('#steps-bar > li:last').addClass('last');
        $('#steps-bar > li:eq('+(global.currentStep)+')').addClass('current');
        $('#steps-bar > li:eq('+(global.currentStep-1)+')').addClass('lastDone');
        $('#steps-bar > li:lt('+(global.currentStep-1)+')').addClass('done');

        $('#btn-done').hide();
        switch(global.currentStep){
            case 0:
                $('#btn-previous').hide();
                $('#btn-next').show();
                break;
            case 3:
                $('#btn-previous').add('#btn-done').show();
                $('#btn-next').hide();
                break;
            default:
                $('#btn-previous').show();
                $('#btn-next').show();
                break;
        }

        $('.step-content').hide();
        $('.step-content:eq('+global.currentStep+')').show();
    }

    global.validateStep = function(){
        switch(global.currentStep){
            case 0:
                if (!$('input[name="product"]:checked').val()){
                    alert('Please choose one product.');
                    return false;
                }
                break;
            case 1:
                if ($('input[name="target"]:checked').length == 0){
                    alert('Please select one or multiple targets.');
                    return false;
                }
                break;
            case 2:
                var b = true;
                $.each($('#param_helper, #param_frameId'), function(){
                    if (!$(this).val() || $(this).val()==$(this).attr('placeholder')){
                        alert('"helper" and "frameId" are required.');
                        b = false;
                        return b;
                    }
                });
                return b;
            case 3:
                break;
        }
        return true;
    }

    $(document).ready(function(){
        // steps
        $('#btn-previous').add('#btn-done').hide();
        $('#steps-content div:first-child').show();

        $('#btn-previous').click(function(){
            global.currentStep--;
            global.adjustSteps();
        });

        $(':checkbox', 'th').change(function(){
            var elm = $(this);
            var idx = $.inArray(elm.parent()[0], elm.closest('tr').children());
            $('div.step-table>table>tbody>tr>td:nth-child('+(idx+1)+')>:checkbox', elm.closest('.step-content')).attr('checked', elm.attr('checked'));
        });

        $('.sortable-th').click(function(){
            var idx = $.inArray(this, $(this).closest('tr').children());
            var d = parseInt($(this).attr('sortDirection')) ? 0 : 1;
            $(this).attr('sortDirection', d);
            var sorting = [[idx,d]];
            $('div.step-table>table', $(this).closest('.step-content')).trigger("sorton",[sorting]);
        });

        
        /********** widget gallary **********/
        $.each(widgets, function(){
            var html = '<div class="gallary-item">';
            html += '<a href="' + this.target+ '">';
            html += '<img src="' + this.image + '" width="180" height="180" alt=""/>';
            html += '<h3>' + this.name + '</h3>';
            html += '</a></div>';
            $('#doc-gallary').append(html);
        })
        $('#doc-gallary').append('<div style="clear:both;"/>');


        /********** specific widget **********/
        $('#tabs').tabs();
        $.each(widgets, function(idx, elm){
            var html = '<div class="gallary-item">';
            var cls = '';
            if (idx == global.widgetId-1)
                cls = 'class="selected"';
            html += '<a href="' + this.target+ '" ' + cls +'>';
            html += '<img src="' + this.image + '" width="88" height="88" alt=""/>';
            html += '<h5>' + this.name + '</h5>';
            html += '</a></div>';
            $('#tabs-1').append(html);
        });
        $('#tabs-1').append('<div style="clear:both;"/>');

        // placeholder
        $('input[placeholder], textarea[placeholder]').placeholder();

        // sortable css files
        $('#import-css-btn').click(function(){
            if ($.trim($('#css_path').val())){
                var html = '<li class="ui-state-default">';
                    html += '<span class="ui-icon ui-icon-closethick" title="click &quot;x&quot; to remove"></span>';
                    html += $('#css_path').val()+'</li>';
                $('#imported-css').append(html);
            }
        });
        $('#imported-css').sortable();
        $('#imported-css').delegate('li span', 'click', function(){
            $(this).parent().remove();
        });
    })
})(jQuery, this);