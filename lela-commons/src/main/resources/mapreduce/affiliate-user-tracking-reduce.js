function Reduce(key, values) {

    var addDoubles = function(a, b, precision){  var x = Math.pow(10, precision || 2); if(a == undefined) a = 0; if(b == undefined) b = 0;return (Math.round(a * x) + Math.round(b * x)) / x;};

    var yesterday = new Date();
    yesterday.setDate(yesterday.getDate()-1);

    var result = {
        unique: 0,
        visits: 0,
        pages: 0,
        returned: 0,
        bounced: 0,
        stores: 0,
        purchases: 0,
        sales: 0,
        commission: 0,
        quizStart: {
        },
        quizComplete: {
        },
        regStartTotal: 0,
        regStart: {
        },
        regCompleteTotal: 0,
        regComplete: {
        },
        byMonth: {},
        byYear: {},

        yesterday:  {
            period: yesterday,
            unique: 0,
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
        }
    };

    values.forEach(function(val) {
        result.unique += val.unique;
        result.visits += val.visits;
        result.pages += val.pages;
        result.returned += val.returned;
        result.bounced += val.bounced;
        result.stores += val.stores;
        result.purchases += val.purchases;
        result.sales = addDoubles(result.sales, val.sales);
        result.commission = addDoubles(result.commission, val.commission);

        for (var key in val.quizStart) {
            result.quizStart[key] = (key in result.quizStart) ? result.quizStart[key] + val.quizStart[key] : val.quizStart[key];
        }

        for (var key in val.quizComplete) {
            result.quizComplete[key] = (key in result.quizComplete) ? result.quizComplete[key] + val.quizComplete[key] : val.quizComplete[key];
        }

        result.regStartTotal += val.regStartTotal;
        for (var regType in val.regStart) {
            for (var key in val.regStart[regType]) {
                result.regStart[regType] = result.regStart[regType] || {};
                result.regStart[regType][key] = (key in result.regStart[regType]) ? result.regStart[regType][key] + val.regStart[regType][key] : val.regStart[regType][key];
            }
        }

        result.regCompleteTotal += val.regCompleteTotal;
        for (var regType in val.regComplete) {
            for (var key in val.regComplete[regType]) {
                result.regComplete[regType] = result.regComplete[regType] || {};
                result.regComplete[regType][key] = (key in result.regComplete[regType]) ? result.regComplete[regType][key] + val.regComplete[regType][key] : val.regComplete[regType][key];
            }
        }

        //
        // Handle Yesterday
        //
        if (val.yesterday) {
            result.yesterday.unique += val.yesterday.unique;
            result.yesterday.visits += val.yesterday.visits;
            result.yesterday.pages += val.yesterday.pages;
            result.yesterday.returned += val.yesterday.returned;
            result.yesterday.bounced += val.yesterday.bounced;
            result.yesterday.stores += val.yesterday.stores;
            result.yesterday.purchases += val.yesterday.purchases;
            result.yesterday.sales = addDoubles(result.yesterday.sales, val.yesterday.sales);
            result.yesterday.commission = addDoubles(result.yesterday.commission, val.yesterday.commission);

            for (var key in val.yesterday.quizStart) {
                result.yesterday.quizStart[key] = (key in result.yesterday.quizStart) ? result.yesterday.quizStart[key] + val.yesterday.quizStart[key] : val.yesterday.quizStart[key];
            }

            for (var key in val.yesterday.quizComplete) {
                result.yesterday.quizComplete[key] = (key in result.yesterday.quizComplete) ? result.yesterday.quizComplete[key] + val.yesterday.quizComplete[key] : val.yesterday.quizComplete[key];
            }

            result.yesterday.regStartTotal += val.yesterday.regStartTotal;
            for (var regType in val.yesterday.regStart) {
                for (var key in val.yesterday.regStart[regType]) {
                    result.yesterday.regStart[regType] = result.yesterday.regStart[regType] || {};
                    result.yesterday.regStart[regType][key] = (key in result.yesterday.regStart[regType]) ? result.yesterday.regStart[regType][key] + val.yesterday.regStart[regType][key] : val.yesterday.regStart[regType][key];
                }
            }

            result.yesterday.regCompleteTotal += val.yesterday.regCompleteTotal;
            for (var regType in val.yesterday.regComplete) {
                for (var key in val.yesterday.regComplete[regType]) {
                    result.yesterday.regComplete[regType] = result.yesterday.regComplete[regType] || {};
                    result.yesterday.regComplete[regType][key] = (key in result.yesterday.regComplete[regType]) ? result.yesterday.regComplete[regType][key] + val.yesterday.regComplete[regType][key] : val.yesterday.regComplete[regType][key];
                }
            }
        }

        //
        // Handle Months
        //
        for (var date in val.byMonth) {
            var valPeriod = val.byMonth[date];
            var period = result.byMonth[date];
            if (!period) {
                period = {
                    period: valPeriod.period,
                    unique: 0,
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
                result.byMonth[date] = period;
            }

            period.unique += valPeriod.unique;
            period.visits += valPeriod.visits;
            period.pages += valPeriod.pages;
            period.returned += valPeriod.returned;
            period.bounced += valPeriod.bounced;
            period.stores += valPeriod.stores;
            period.purchases += valPeriod.purchases;
            period.sales = addDoubles(period.sales, valPeriod.sales);
            period.commission = addDoubles(period.commission, valPeriod.commission);

            for (var key in valPeriod.quizStart) {
                period.quizStart[key] = (key in period.quizStart) ? period.quizStart[key] + valPeriod.quizStart[key] : valPeriod.quizStart[key];
            }

            for (var key in valPeriod.quizComplete) {
                period.quizComplete[key] = (key in period.quizComplete) ? period.quizComplete[key] + valPeriod.quizComplete[key] : valPeriod.quizComplete[key];
            }

            period.regStartTotal += valPeriod.regStartTotal;
            for (var regType in valPeriod.regStart) {
                for (var key in valPeriod.regStart[regType]) {
                    period.regStart[regType] = period.regStart[regType] || {};
                    period.regStart[regType][key] = (key in period.regStart[regType]) ? period.regStart[regType][key] + valPeriod.regStart[regType][key] : valPeriod.regStart[regType][key];
                }
            }

            period.regCompleteTotal += valPeriod.regCompleteTotal;
            for (var regType in valPeriod.regComplete) {
                for (var key in valPeriod.regComplete[regType]) {
                    period.regComplete[regType] = period.regComplete[regType] || {};
                    period.regComplete[regType][key] = (key in period.regComplete[regType]) ? period.regComplete[regType][key] + valPeriod.regComplete[regType][key] : valPeriod.regComplete[regType][key];
                }
            }
        }

        //
        // Handle Years
        //
        for (var date in val.byYear) {
            var valPeriod = val.byYear[date];
            var period = result.byYear[date];
            if (!period) {
                period = {
                    period: valPeriod.period,
                    unique: 0,
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
                result.byYear[date] = period;
            }

            period.unique += valPeriod.unique;
            period.visits += valPeriod.visits;
            period.pages += valPeriod.pages;
            period.returned += valPeriod.returned;
            period.bounced += valPeriod.bounced;
            period.stores += valPeriod.stores;
            period.purchases += valPeriod.purchases;
            period.sales = addDoubles(period.sales, valPeriod.sales);
            period.commission = addDoubles(period.commission, valPeriod.commission);

            for (var key in valPeriod.quizStart) {
                period.quizStart[key] = (key in period.quizStart) ? period.quizStart[key] + valPeriod.quizStart[key] : valPeriod.quizStart[key];
            }

            for (var key in valPeriod.quizComplete) {
                period.quizComplete[key] = (key in period.quizComplete) ? period.quizComplete[key] + valPeriod.quizComplete[key] : valPeriod.quizComplete[key];
            }

            period.regStartTotal += valPeriod.regStartTotal;
            for (var regType in valPeriod.regStart) {
                for (var key in valPeriod.regStart[regType]) {
                    period.regStart[regType] = period.regStart[regType] || {};
                    period.regStart[regType][key] = (key in period.regStart[regType]) ? period.regStart[regType][key] + valPeriod.regStart[regType][key] : valPeriod.regStart[regType][key];
                }
            }

            period.regCompleteTotal += valPeriod.regCompleteTotal;
            for (var regType in valPeriod.regComplete) {
                for (var key in valPeriod.regComplete[regType]) {
                    period.regComplete[regType] = period.regComplete[regType] || {};
                    period.regComplete[regType][key] = (key in period.regComplete[regType]) ? period.regComplete[regType][key] + valPeriod.regComplete[regType][key] : valPeriod.regComplete[regType][key];
                }
            }
        }

    });

    return result;
}