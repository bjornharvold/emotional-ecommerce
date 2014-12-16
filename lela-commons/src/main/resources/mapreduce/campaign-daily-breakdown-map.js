function map() {
    var date = this.cdt.getFullYear() + '-' + (this.cdt.getMonth() + 1) + '-' + this.cdt.getDate();
    var result = {
        total: 1,
        assoc: 0,
        totalQuiz: 0,
        assocQuiz: 0
    };

    // Finished Quiz
    if (this.mtvtrmp != null) {
        var m = this.mtvtrmp["QUIZ"];
        if (m != null && m.A > 0 && m.B > 0 && m.C > 0 && m.D > 0 && m.E > 0 && m.F > 0 && m.G > 0) {
            result.totalQuiz = 1;
        }
    }

    // Has association
    var assoc;
    var attr;
    if (this.ssctns != null) {
        for (var i=0; i<this.ssctns.length; i++) {
            assoc = this.ssctns[i];
            if (assoc.attrs != null) {
                for (var j=0; j<assoc.attrs.length; j++) {
                    attr = assoc.attrs[j];
                    if (attr.ky == 'cmpgnrlnm' && attr.vl == rlnm) {
                        result.assoc = 1;
                        result.assocQuiz = result.totalQuiz
                    }
                }
            }
        }
    }

    emit(date, result);
}