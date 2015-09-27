
String.prototype.empty = function() {
    return ( this == "" || this.length == 0 ) ?true:false;
}
String.prototype.replaceAll  = function(s1,s2){   
    return this.replace(new RegExp(s1,"gm"),s2);   
}  
Array.prototype.indexOf = function(elt /*, from*/){
    var len = this.length;
    var from = Number(arguments[1]) || 0;
    from = (from < 0)
    ? Math.ceil(from)
    : Math.floor(from);
    if (from < 0)
        from += len;

    for (; from < len; from++)
    {
        if (from in this &&
            this[from] === elt)
            return from;
    }
    return -1;
}

Array.prototype.remove = function(s) {
    var i = this.indexOf(s);
    if(i != -1) this.splice(i, 1);
}