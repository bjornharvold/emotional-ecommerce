function Map() {
    var day = new Date(this.cdt.getFullYear(), this.cdt.getMonth(), this.cdt.getDate());
    var map = {
        total: 1
    };

    if (this.ssctns) {
        for (var i=0, il=this.ssctns.length; i<il; i++) {
            var s = this.ssctns[i];
            if (s.attrs) {
                for (var j=0, jl=s.attrs.length; j<jl; j++) {
                    var a = s.attrs[j];
                    if (a.ky = 'ffltccntrlnm') {
                        map[a.vl] = 1;
                    }
                }
            }
        }
    }

    emit(day, map);
}