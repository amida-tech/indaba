function toggleChosenItem($list, $item, $visible, $updateList) {
    if ($visible) $item.removeAttr("style");
    else $item.hide();

    $list.prop('selectedIndex',0);

    if ($updateList) $list.trigger("liszt:updated");
}


