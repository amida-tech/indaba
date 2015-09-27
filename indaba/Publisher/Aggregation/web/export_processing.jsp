<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript" charset="utf-8">
    function downLoad(){
        window.location.href="exportDownload.do?zipFolder=${param.zipFolder}";
    }

    function refresh_process_content(action, step){
        if(${process_flag}!=null&&(${process_flag}==1)){
            $.ajax({
                type: "POST",
                url: action,
                data: "step=" + step + "&zipFolder=${param.zipFolder}",
                success: function(result) {
                    $('#export_Processing').empty();
                    $('#export_Processing').append(result);
                    if(${process_flag}!=null&&(${process_flag}==1)){
                        var func = "refresh_process_content('" + action + "', " + step + ")";
                        window.setTimeout(func, 1000);
                    }
                },
                error: function(result) {
                    alert("service error");//TODO
                }
            });
        }
    }
</script>
<c:if test="${process_flag == 1}">
    <div align ="center">
        <div class="preview-cnt" style="width: 220px; text-align: left">
            Your export is processing...${sessionScope["processPercent"]}%
            <img id="uploading-img" alt="uploading image" src="images/loading.gif" width="20px;" style="vertical-align: middle;">
        </div>
    </div>
</c:if>
<c:if test="${process_flag == 2}">
    <div align="center">
        <div class="preview-cnt" style="width: 220px; text-align: left">
            Your export is ready.
        </div>
        <div class="preview-cnt" style="width: 220px; text-align: left">
            File name: ${sessionScope["zipFileName"]}
        </div>
        <div class="preview-cnt" style="width: 220px; text-align: left">
            File size: ${sessionScope["zipSize"]}
        </div>
    </div>
    <div id="submitButtons" class="submitButtons" align="center">
        <input type="button" value="Download ZIP"  onclick="downLoad();" />

        <br /><br />
        <a href="createJournalExport.do?step=1">Back to Indaba Publisher</a>
    </div>
</c:if>
