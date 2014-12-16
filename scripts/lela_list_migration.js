// move stores over to list card board
db.userSupplement.find({$or : [{strs : {$exists : true}}, {tms : {$exists : true}}, {wndtms : {$exists : true}}]}).forEach(function(a) {
    if (!a.brds) {
        var board = {
          "nm" : "Default",
          "dt" : new Date()
        };
        a.brds = [board];
    }
    var order = 1;
    var cards = [];
    if (a.tms && a.tms.length > 0) {
        for (var i = 0; i < a.tms.length; i++) {
            var item = a.tms[i];
            var resultCount = db.item.find({rlnm : item.rlnm}).count();
            if (resultCount > 0) {
                var lc = {
                    "rdr" : NumberInt(order),
                    "rlnm" : item.rlnm,
                    "tp" : "ITEM",
                    "dt" : new Date(),
                    "wn" : false
                };
                cards[order-1] = lc;
                order++;
            }
        }
    }
    if (a.wndtms && a.wndtms.length > 0) {
        for (var i = 0; i < a.wndtms.length; i++) {
            var item = a.wndtms[i];
            var resultCount = db.item.find({rlnm : item.rlnm}).count();
            if (resultCount > 0) {
                var lc = {
                    "rdr" : NumberInt(order),
                    "rlnm" : item.rlnm,
                    "tp" : "ITEM",
                    "dt" : new Date(),
                    "wn" : true
                };
                cards[order-1] = lc;
                order++;
            }
        }
    }
    if (a.strs && a.strs.length > 0) {
        for (var i = 0; i < a.strs.length; i++) {
            var store = a.strs[i];
            var resultCount = db.store.find({rlnm : store.rlnm}).count();
            if (resultCount > 0) {
                var lc = {
                    "rdr" : NumberInt(order),
                    "rlnm" : store.rlnm,
                    "tp" : "STORE",
                    "dt" : new Date(),
                    "wn" : false
                };
                cards[order-1] = lc;
                order++;
            }
        }
    }
    a.brds[0].crds = cards;
    db.userSupplement.update({cd : a.cd}, a, false, false);
});

// remove old data
db.userSupplement.update({$or : [{strs : {$exists : true}}, {tms : {$exists : true}}, {wndtms : {$exists : true}}]}, {$unset : {strs : 1, tms : 1, wndtms : 1}}, false, true);
