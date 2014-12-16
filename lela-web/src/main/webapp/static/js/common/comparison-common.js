function checkComparisonCookie()
{
    var lelaComparisonItems=$.cookie("lela_comparison");
    if (lelaComparisonItems!=null && lelaComparisonItems!="")
    {
        return true;
    }
    else
    {
        return false;
    }
}

function ComparisonItem() {
    this.rlnm = "";
    this.ctgry = "";
    this.img = "";
}

function updateComparisonCheckboxes() {
    $.each($('.compare-check'), function() {
        if(itemInComparisonCookie($(this).attr('rlnm'))) {
            $(this).show().find('.c-btn').addClass('c-btn-checked');
            $('.compareButton[rlnm="'+$(this).attr('rlnm')+'"]').show().find('.c-btn').addClass('c-btn-checked');
        }
    });
}

function addItemToComparisonCookie(category, item, image)
{

    var lelaComparison=$.cookie("lela_comparison");

    if (lelaComparison!=null && lelaComparison!="")
    {

        var comparisonArray = lelaComparison.split('|');

        var i;
        var cat = 0;

        for(i=0; i<comparisonArray.length; i++) {

            var itm = JSON.parse(comparisonArray[i]);
            if(itm.ctgry == category) {
                cat++;
            }

        }

        if(cat >= 3) {
            return false;
        }
        else {

            var newItem = new ComparisonItem();
            newItem.rlnm = item;
            newItem.ctgry = category;
            newItem.img = image;

            comparisonArray.push(JSON.stringify(newItem));

            $.cookie("lela_comparison", comparisonArray.join('|'), { path: '/' });
            return true;
        }

    }
    else
    {
        var newItem = new ComparisonItem();
        newItem.rlnm = item;
        newItem.ctgry = category;
        newItem.img = image;

        var comparisonArray = [JSON.stringify(newItem)];

        $.cookie("lela_comparison", comparisonArray.join('|'), { path: '/' });
        return true;
    }
}

function itemsInComparisonCategory(category)
{
    var lelaComparison=$.cookie("lela_comparison");

    if (lelaComparison!=null && lelaComparison!="")
    {
        var comparisonArray = lelaComparison.split('|');
        var returnArray = new Array;

        for(i=0; i<comparisonArray.length; i++) {

            var itm = JSON.parse(comparisonArray[i]);
            if(itm.ctgry == category) {
                returnArray.push(itm);
            }

        }

        return returnArray;

    }
    else return false;
}

function removeItemFromComparisonCookie(category, item)
{
    var lelaComparison=$.cookie("lela_comparison");

    if (lelaComparison!=null && lelaComparison!="")
    {
        var comparisonArray = lelaComparison.split('|');
        var cat = 0;

        for(i=0; i<comparisonArray.length; i++) {

            var itm = JSON.parse(comparisonArray[i]);
            if(itm.ctgry == category && itm.rlnm == item) {
                comparisonArray.splice(i,1);
                break;
            }

        }

        $.cookie("lela_comparison", comparisonArray.join('|'), { path: '/' });

        return true;
    }
}

function itemInComparisonCookie(item) {

    var lelaComparison=$.cookie("lela_comparison");

    if (lelaComparison!=null && lelaComparison!="")
    {

        var comparisonArray = lelaComparison.split('|');
        var cat = 0;

        for(i=0; i<comparisonArray.length; i++) {

            var itm = JSON.parse(comparisonArray[i]);
            if(itm.rlnm == item) {
                return true;
            }

        }

        return false;
    }
    else {
        return false;
    }
}

function getComparisonUrl() {

    var lelaComparison=$.cookie("lela_comparison");

    if (lelaComparison!=null && lelaComparison!="")
    {

        var comparisonArray = lelaComparison.split('|');
        var url;

        for(i=0; i<comparisonArray.length; i++) {

            var itm = JSON.parse(comparisonArray[i]);

            if(i == 0) {
                url = itm.rlnm;
            }
            else {
                url += ","+itm.rlnm;
            }

        }

        return url;
    }
    else {
        return false;
    }

}