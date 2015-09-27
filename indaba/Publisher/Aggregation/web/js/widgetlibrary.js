(function($, global){
    global.currentStep = 0;

    global.adjustSteps = function(){
        $('#steps-bar > li').removeClass();
        $('#steps-bar > li:last').addClass('last');
        $('#steps-bar > li:eq('+(global.currentStep)+')').addClass('current');
        $('#steps-bar > li:eq('+(global.currentStep-1)+')').addClass('lastDone');
        $('#steps-bar > li:lt('+(global.currentStep-1)+')').addClass('done');

        $('#uniform-btn-done').hide();
        switch(global.currentStep){
            case 0:
                $('#uniform-btn-previous').hide();
                $('#uniform-btn-next').show();
                break;
            case $('#steps-bar>li').length-1:
                $('#uniform-btn-previous').add('#uniform-btn-done').show();
                $('#uniform-btn-next').hide();
                break;
            default:
                $('#uniform-btn-previous').show();
                $('#uniform-btn-next').show();
                break;
        }

        $('.step-content').hide();
        $('.step-content:eq('+global.currentStep+')').show();
    }

    global.validateStep = function(){
        var idx = global.currentStep;
        if (idx > 0 && global.widget.configType == 2)
            idx++;
        
        switch(idx){
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
        if (global.widget){
            if (global.widget.configType == 1)
                $('#tr-include-logo').remove();
            else
                $('#th-include-logo').remove();
        }
        // steps
        $('#uniform-btn-previous').add('#uniform-btn-done').hide();
        $('#steps-content div:first-child').show();

        $('#btn-previous').click(function(){
            global.currentStep--;
            global.adjustSteps();
        });

        $('#tbl-url').delegate('.btn-preview', 'click', function(){
            var tr = $(this).closest('tr');
            var url = $('td:eq(1)', tr).text();
            var newWindow = window.open(url,'resultWindow','toolbar=0,resizable=0,scrollbars,status=no,toolbar=no,location=no,menu=no,width=800,height=600');
            newWindow.focus();
        });

        $(':checkbox', 'th').change(function(){
            var elm = $(this);
            var idx = $.inArray(elm.closest('th')[0], elm.closest('tr').children());
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
            html += '<img src="' + this.image + '" width="180" height="180" alt="" title="'+this.description+'"/>';
            html += '<h3>' + this.name + '</h3>';
            html += '</a></div>';
            $('#doc-gallary').append(html);
        })
        $('#doc-gallary').append('<div style="clear:both;"/>');


        /********** specific widget **********/
        $('#tabs-1').append('<div class="gallary-items"/>');
        $.each(widgets, function(idx, elm){
            var html = '<div class="gallary-item" title="'+this.name+'">';
            var cls = '';
            if (idx == global.widgetId-1)
                cls = 'class="selected"';
            html += '<a href="' + this.target+ '" ' + cls +'>';
            html += '<img src="' + this.image + '" width="88" height="88" alt="" title="'+this.description+'"/>';
            html += '<h5>' + this.name + '</h5>';
            html += '</a></div>';
            $('.gallary-items', '#tabs-1').append(html);
        });
        
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