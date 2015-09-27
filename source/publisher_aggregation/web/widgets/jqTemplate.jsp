<%@page isELIgnored="true"%>
<script id="stepTemplate" type="text/x-jquery-tmpl">
    {{each steps}}
        <li 
        {{if $index == 0}}
        class='current'
        {{else $index == steps.length-1}}
        class='last'
        {{/if}}
        >
            <dl>
                <dt>Step ${$index+1}:</dt>
                <dd>${$value}</dd>
            </dl>
        </li>
    {{/each}}
</script>
<script id="productTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td align="center"><input type="radio" name="product" value="${id}"/></td>
        <td>${project}</td>
        <td>${product}</td>
    </tr>
</script>
<script id="targetTemplate1" type="text/x-jquery-tmpl">
    <tr>
        <td align="right" width="75">
            <input type="hidden" name="horse-${tid}" value="${hid}"/>
            <input type="checkbox" name="target" value="${tid}"/>
        </td>
        <td>${tname}</td>
        <td align="right" width="120"><input type="checkbox" name="includeLogo" value="${tid}"/></td>
    </tr>
</script>
<script id="targetTemplate2" type="text/x-jquery-tmpl">
    <tr>
        <td align="right" width="75">
            <input type="hidden" name="horse-${tid}" value="${hid}"/>
            <input type="checkbox" name="target" value="${tid}"/>
        </td>
        <td>${tname}</td>
    </tr>
</script>
<script id="urlTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td
        {{if configType != 3}}
         class="no-wrap"
        {{/if}}
        >${target}</td>
        <td>${url}</td>
        <td align="center"><button class='btn-preview'>Preview</button></td>
        <td align="center" width="90"><div style="position:relative"><button class='btn-copy-url'>Copy URL</button></div></td>
    </tr>
</script>