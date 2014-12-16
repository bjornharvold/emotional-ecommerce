function Map() {
    var addDoubles = function(a, b, precision){  var x = Math.pow(10, precision || 2); if(a == undefined) a = 0; if(b == undefined) b = 0;return (Math.round(a * x) + Math.round(b * x)) / x;};

    if (!this.qzstrtdt) { this.qzstrtdt = this.qzcmpltdt; }
    if (!this.rgstrtdt) { this.rgstrtdt = this.rgcmpltdt; }
    if (!this.rgtp) { this.rgtp = "WEBSITE"; }

    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate()-1);

    var result = {
        unique: 1,
        visits: this.vstcnt ? this.vstcnt: 0,
        pages: 0,
        returned: this.vstcnt > 1 ? 1 : 0,
        bounced: (this.vstcnt < 1 || (this.vstcnt == 1 && this.vsts[0].clckcnt <= 1)) ? 1 : 0,
        stores: this.rdrctcnt ? this.rdrctcnt: 0,
        purchases: 0,
        sales: 0.0,
        commission: 0.0,
        quizStart: {
            TOTAL: 0
        },
        quizComplete: {
            TOTAL: 0
        },
        regStartTotal: 0,
        regStart: {
        },
        regCompleteTotal: 0,
        regComplete: {
        },
        byMonth: {},
        byYear: {}
    };
    this.rdrcts.forEach(function(r) {
            if(r.sls != null && r.sls.length>0)
            {
                result.purchases ++;
            }
            result.sales = addDoubles(result.sales, r.ttlsls);
            result.commission = addDoubles(result.commission, r.ttlcmmssn);
    });

    for (var i=0; i<this.vsts.length; i++) {
        var visit = this.vsts[i];
        result.pages += visit.clckcnt;

        // Previous Day
        if (visit.cdt.getFullYear() == yesterday.getFullYear() && visit.cdt.getMonth() == yesterday.getMonth() && visit.cdt.getDate() == yesterday.getDate()) {
            if (!result.yesterday) {
                result.yesterday = {
                    period: yesterday,
                    unique: 1,
                    visits: 0,
                    pages: 0,
                    returned: 0,
                    bounced: 0,
                    stores: 0,
                    purchases: 0,
                    sales: 0,
                    commission: 0,
                    quizStart: {
                        TOTAL: 0
                    },
                    quizComplete: {
                        TOTAL: 0
                    },
                    regStartTotal: 0,
                    regStart: {
                    },
                    regCompleteTotal: 0,
                    regComplete: {
                    }
                };
            }

            result.yesterday.visits++;
            result.yesterday.pages += visit.clckcnt;
            result.yesterday.returned = i>0 ? 1 : result.yesterday.returned;
            result.yesterday.bounced = (i==0 && (this.vstcnt < 1 || (this.vstcnt == 1 && this.vsts[0].clckcnt <= 1))) ? 1 : 0;

            if (!result.yesterday.quizStart.TOTAL && this.qzstrtdt && this.qzstrtdt.getFullYear() == visit.cdt.getFullYear() && this.qzstrtdt.getMonth() == visit.cdt.getMonth() && this.qzstrtdt.getDate() == visit.cdt.getDate()) {
                result.yesterday.quizStart.TOTAL = 1;
                result.yesterday.quizStart[visit.dvctp] = 1;
            }

            if (!result.yesterday.quizComplete.TOTAL && this.qzcmpltdt && this.qzcmpltdt.getFullYear() == visit.cdt.getFullYear() && this.qzcmpltdt.getMonth() == visit.cdt.getMonth() && this.qzcmpltdt.getDate() == visit.cdt.getDate()) {
                result.yesterday.quizComplete.TOTAL = 1;
                result.yesterday.quizComplete[visit.dvctp] = 1;
            }

            if (!result.yesterday.regStartTotal && this.rgstrtdt && this.rgstrtdt.getFullYear() == visit.cdt.getFullYear() && this.rgstrtdt.getMonth() == visit.cdt.getMonth() && this.rgstrtdt.getDate() == visit.cdt.getDate()) {
                result.yesterday.regStartTotal = 1;
                result.yesterday.regStart[this.rgtp] = result.regStart[this.rgtp] || {};
                result.yesterday.regStart[this.rgtp].TOTAL = 1;
                result.yesterday.regStart[this.rgtp][visit.dvctp] = 1;
            }

            if (!result.yesterday.regCompleteTotal && this.rgcmpltdt && this.rgcmpltdt.getFullYear() == visit.cdt.getFullYear() && this.rgcmpltdt.getMonth() == visit.cdt.getMonth() && this.rgcmpltdt.getDate() == visit.cdt.getDate()) {
                result.yesterday.regCompleteTotal = 1;
                result.yesterday.regComplete[this.rgtp] = result.regComplete[this.rgtp] || {};
                result.yesterday.regComplete[this.rgtp].TOTAL = 1;
                result.yesterday.regComplete[this.rgtp][visit.dvctp] = 1;
            }

            // Process store clicks
            this.rdrcts.forEach(function(r) {
                if (r.vstd.toString() == visit["_id"].toString()) {
                    result.yesterday.stores ++;
                    if(r.sls != null && r.sls.length>0)
                    {
                        result.yesterday.purchases ++;
                    }
                    result.yesterday.sales = addDoubles(result.yesterday.sales, r.ttlsls);
                    result.yesterday.commission = addDoubles(result.yesterday.commission, r.ttlcmmssn);
                }
            });
        }

        var date = new Date(visit.cdt.getFullYear(), visit.cdt.getMonth(), 1);
        var month = result.byMonth[date];
        if (!month) {
            month = {
                period: date,
                unique: 1,
                visits: 0,
                pages: 0,
                returned: 0,
                bounced: 0,
                stores: 0,
                purchases: 0,
                sales: 0,
                commission: 0,
                quizStart: {
                    TOTAL: 0
                },
                quizComplete: {
                    TOTAL: 0
                },
                regStartTotal: 0,
                regStart: {
                },
                regCompleteTotal: 0,
                regComplete: {
                }
            };
            result.byMonth[date] = month;
        }

        var yearKey = new Date(visit.cdt.getFullYear(), 0, 1);
        var year = result.byYear[yearKey];
        if (!year) {
            year = {
                period: yearKey,
                unique: 1,
                visits: 0,
                pages: 0,
                returned: 0,
                bounced: 0,
                stores: 0,
                purchases: 0,
                sales: 0,
                commission: 0,
                quizStart: {
                    TOTAL: 0
                },
                quizComplete: {
                    TOTAL: 0
                },
                regStartTotal: 0,
                regStart: {
                },
                regCompleteTotal: 0,
                regComplete: {
                }
            };
            result.byYear[yearKey] = year;
        }

        // Month stats
        month.visits++;
        month.pages += visit.clckcnt;
        month.returned = i>0 ? 1 : month.returned;
        month.bounced = (i==0 && (this.vstcnt < 1 || (this.vstcnt == 1 && this.vsts[0].clckcnt <= 1))) ? 1 : 0;

        if (!result.quizStart.TOTAL && this.qzstrtdt && this.qzstrtdt.getFullYear() == visit.cdt.getFullYear() && this.qzstrtdt.getMonth() == visit.cdt.getMonth()) {
            result.quizStart.TOTAL = 1;
            result.quizStart[visit.dvctp] = 1;
            month.quizStart.TOTAL = 1;
            month.quizStart[visit.dvctp] = 1;
            year.quizStart.TOTAL = 1;
            year.quizStart[visit.dvctp] = 1;
        }

        if (!result.quizComplete.TOTAL && this.qzcmpltdt && this.qzcmpltdt.getFullYear() == visit.cdt.getFullYear() && this.qzcmpltdt.getMonth() == visit.cdt.getMonth()) {
            result.quizComplete.TOTAL = 1;
            result.quizComplete[visit.dvctp] = 1;
            month.quizComplete.TOTAL = 1;
            month.quizComplete[visit.dvctp] = 1;
            year.quizComplete.TOTAL = 1;
            year.quizComplete[visit.dvctp] = 1;
        }

        if (!result.regStartTotal && this.rgstrtdt && this.rgstrtdt.getFullYear() == visit.cdt.getFullYear() && this.rgstrtdt.getMonth() == visit.cdt.getMonth()) {
            result.regStartTotal = 1;
            result.regStart[this.rgtp] = result.regStart[this.rgtp] || {};
            result.regStart[this.rgtp].TOTAL = 1;
            result.regStart[this.rgtp][visit.dvctp] = 1;

            month.regStartTotal = 1;
            month.regStart[this.rgtp] = month.regStart[this.rgtp] || {};
            month.regStart[this.rgtp].TOTAL = 1;
            month.regStart[this.rgtp][visit.dvctp] = 1;

            year.regStartTotal = 1;
            year.regStart[this.rgtp] = year.regStart[this.rgtp] || {};
            year.regStart[this.rgtp].TOTAL = 1;
            year.regStart[this.rgtp][visit.dvctp] = 1;
        }

        if (!result.regCompleteTotal && this.rgcmpltdt && this.rgcmpltdt.getFullYear() == visit.cdt.getFullYear() && this.rgcmpltdt.getMonth() == visit.cdt.getMonth()) {
            result.regCompleteTotal = 1;
            result.regComplete[this.rgtp] = result.regComplete[this.rgtp] || {};
            result.regComplete[this.rgtp].TOTAL = 1;
            result.regComplete[this.rgtp][visit.dvctp] = 1;

            month.regCompleteTotal = 1;
            month.regComplete[this.rgtp] = month.regComplete[this.rgtp] || {};
            month.regComplete[this.rgtp].TOTAL = 1;
            month.regComplete[this.rgtp][visit.dvctp] = 1;

            year.regCompleteTotal = 1;
            year.regComplete[this.rgtp] = year.regComplete[this.rgtp] || {};
            year.regComplete[this.rgtp].TOTAL = 1;
            year.regComplete[this.rgtp][visit.dvctp] = 1;
        }

        // Year stats
        year.visits++;
        year.pages += visit.clckcnt;
        year.returned = i>0 ? 1 : year.returned;
        year.bounced = (i==0 && (this.vstcnt < 1 || (this.vstcnt == 1 && this.vsts[0].clckcnt <= 1))) ? 1 : 0;

        // Process store clicks
        this.rdrcts.forEach(function(r) {
            if (r.vstd.toString() == visit["_id"].toString()) {
                month.stores ++;
                year.stores ++;
                if(r.sls != null && r.sls.length>0)
                {
                    month.purchases ++;
                    year.purchases ++;
                }
                month.sales = addDoubles(month.sales, r.ttlsls);
                month.commission = addDoubles(month.commission, r.ttlcmmssn);
                year.sales = addDoubles(year.sales, r.ttlsls);
                year.commission = addDoubles(year.commission, r.ttlcmmssn);
            }
        });
    }

    emit(this.ffltccntrlnm, result);
}