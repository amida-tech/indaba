/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function ocsShowNotify(params) {
    $.pnotify.defaults.history = false;
    $.pnotify_remove_all();
    /*
    var stack_bar_bottom = {
        "dir1": "down",
        "dir2": "down"
    };
    */
    var stack_bar_bottom = {"dir1": "up", "dir2": "right", "spacing1": 0, "spacing2": 0};
    var options = {
        addclass: "stack-bar-bottom",
        cornerclass: "",
        stack: stack_bar_bottom,
        width: "70%",
        styling: 'jqueryui'
    };

    options.title = params.title;
    options.text = params.text;
    options.type = params.type;

    if (options.type == 'error') {
        options.hide = false;
        options.sticker = false;
    }

    $.pnotify(options);
}

