function Finalize(key, reduced) {

    reduced.pagesPerVisit = reduced.visits > 0 ? reduced.pages / reduced.visits : 0;
    reduced.bounceRate = reduced.unique > 0 ? reduced.bounced / reduced.unique : 0;
    reduced.returnedRate = (reduced.returned > 0 && reduced.unique > 0) ? reduced.returned / reduced.unique : 0;

    reduced.quizRate = {};
    for (key in reduced.quizStart) {
        reduced.quizRate[key] = (reduced.quizStart[key] && reduced.quizComplete[key]) ? reduced.quizComplete[key]/reduced.quizStart[key] : 0;
    }

    reduced.regRateTotal = reduced.regStartTotal ? reduced.regCompleteTotal / reduced.regStartTotal : 0;
    reduced.regRate = {};
    for (regType in reduced.regStart) {
        for (key in reduced.regStart[regType]) {
            reduced.regRate[regType] = reduced.regRate[regType] || {};
            reduced.regRate[regType][key] = (reduced.regStart[regType] && reduced.regStart[regType][key] && reduced.regComplete[regType]) ? reduced.regComplete[regType][key]/reduced.regStart[regType][key] : 0;
        }
    }

    //
    // Yesterday
    //
    if (reduced.yesterday) {
        reduced.yesterday.pagesPerVisit = reduced.yesterday.visits > 0 ? reduced.yesterday.pages / reduced.yesterday.visits : 0;
        reduced.yesterday.bounceRate = reduced.yesterday.unique > 0 ? reduced.yesterday.bounced / reduced.yesterday.unique : 0;
        reduced.yesterday.returnedRate = (reduced.yesterday.returned > 0 && reduced.yesterday.unique > 0) ? reduced.yesterday.returned / reduced.yesterday.unique : 0;

        reduced.yesterday.quizRate = {};
        for (key in reduced.yesterday.quizStart) {
            reduced.yesterday.quizRate[key] = (reduced.yesterday.quizStart[key] && reduced.yesterday.quizComplete[key]) ? reduced.yesterday.quizComplete[key]/reduced.yesterday.quizStart[key] : 0;
        }

        reduced.yesterday.regRateTotal = reduced.yesterday.regStartTotal ? reduced.yesterday.regCompleteTotal / reduced.yesterday.regStartTotal : 0;
        reduced.yesterday.regRate = {};
        for (regType in reduced.yesterday.regStart) {
            for (key in reduced.yesterday.regStart[regType]) {
                reduced.yesterday.regRate[regType] = reduced.yesterday.regRate[regType] || {};
                reduced.yesterday.regRate[regType][key] = (reduced.yesterday.regStart[regType] && reduced.yesterday.regStart[regType][key] && reduced.yesterday.regComplete[regType]) ? reduced.yesterday.regComplete[regType][key]/reduced.yesterday.regStart[regType][key] : 0;
            }
        }
    }

    // Calculate averages per month
    for (var date in reduced.byMonth) {
        var period = reduced.byMonth[date];
        period.pagesPerVisit = period.visits > 0 ? period.pages / period.visits : 0;
        period.bounceRate = period.unique > 0 ? period.bounced / period.unique : 0;
        period.returnedRate = (period.returned > 0 && period.unique > 0) ? period.returned / period.unique : 0;

        var daysInMonth = new Date(period.period.getFullYear(), period.period.getMonth(), 0).getDate();

        // Handle case of current month
        var now = new Date();
        if (now.getFullYear() === period.period.getFullYear() && now.getMonth() === period.period.getMonth()) {
            daysInMonth = now.getDate();
        }

        period.pagesPerVisitDayAvg = period.pagesPerVisit / daysInMonth;
        period.uniqueDayAvg = period.unique / daysInMonth;
        period.visitsDayAvg = period.visits / daysInMonth;
        period.pagesDayAvg = period.pages / daysInMonth;
        period.returnedDayAvg = period.returned / daysInMonth;
        period.storesDayAvg = period.stores / daysInMonth;
        period.purchasesDayAvg = period.purchases / daysInMonth;
        period.salesDayAvg = period.sales / daysInMonth;
        period.commissionDayAvg = period.commission / daysInMonth;

        period.quizRate = {};
        period.quizStartDayAvg = {};
        period.quizCompleteDayAvg = {};
        for (key in period.quizStart) {
            period.quizRate[key] = (period.quizStart[key] && period.quizComplete[key]) ? period.quizComplete[key]/period.quizStart[key] : 0;
            period.quizStartDayAvg[key] = period.quizStart[key] / daysInMonth;
        }

        for (key in period.quizComplete) {
            period.quizCompleteDayAvg[key] = period.quizComplete[key] / daysInMonth;
        }

        period.regStartTotalDayAvg = period.regStartTotal / daysInMonth;
        period.regCompleteTotalDayAvg = period.regCompleteTotal / daysInMonth;
        period.regRateTotal = period.regStartTotal ? period.regCompleteTotal / period.regStartTotal : 0;
        period.regRate = {};
        period.regStartDayAvg = {};
        period.regCompleteDayAvg = {};
        for (regType in period.regStart) {
            period.regRate[regType] = {};
            period.regStartDayAvg[regType] = {};
            for (key in period.regStart[regType]) {
                period.regRate[regType][key] = (period.regStart[regType] && period.regComplete[regType] && period.regStart[regType][key]) ? period.regComplete[regType][key]/period.regStart[regType][key] : 0;
                period.regStartDayAvg[regType][key] = period.regStart[regType][key] / daysInMonth;
            }
        }

        for (regType in period.regComplete) {
            period.regCompleteDayAvg[regType] = {};
            for (key in period.regComplete[regType]) {
                period.regCompleteDayAvg[regType][key] = period.regComplete[regType][key] / daysInMonth;
            }
        }
    }

    // Calculate averages per year
    for (var date in reduced.byYear) {
        var period = reduced.byYear[date];
        var months = 0;
        for (var month in period) {
            months++;
        }

        period.pagesPerVisit = period.visits > 0 ? period.pages / period.visits : 0;
        period.bounceRate = period.unique > 0 ? period.bounced / period.unique : 0;
        period.returnedRate = (period.returned > 0 && period.unique > 0) ? period.returned / period.unique : 0;

        period.pagesPerVisitDayAvg = period.pagesPerVisit / (months * 30);
        period.uniqueDayAvg = period.unique / (months * 30);
        period.visitsDayAvg = period.visits / (months * 30);
        period.pagesDayAvg = period.pages / (months * 30);
        period.returnedDayAvg = period.returned / (months * 30);
        period.storesDayAvg = period.stores / (months * 30);
        period.purchasesDayAvg = period.purchases / (months * 30);
        period.salesDayAvg = period.sales / (months * 30);
        period.commissionDayAvg = period.commission / (months * 30);

        period.pagesPerVisitMonthAvg = period.pagesPerVisit / months;
        period.uniqueMonthAvg = period.unique / months;
        period.visitsMonthAvg = period.visits / months;
        period.pagesMonthAvg = period.pages / months;
        period.returnedMonthAvg = period.returned / months;
        period.storesMonthAvg = period.stores / months;
        period.purchasesMonthAvg = period.purchases / months;
        period.salesMonthAvg = period.sales / months;
        period.commissionMonthAvg = period.commission / months;

        period.quizRate = {};
        period.quizStartDayAvg = {};
        period.quizCompleteDayAvg = {};
        period.quizStartMonthAvg = {};
        period.quizCompleteMonthAvg = {};
        for (key in period.quizStart) {
            period.quizRate[key] = (period.quizStart[key] && period.quizComplete[key]) ? period.quizComplete[key]/period.quizStart[key] : 0;
            period.quizStartDayAvg[key] = period.quizStart[key] / (months * 30);
            period.quizStartMonthAvg[key] = period.quizStart[key] / months;
        }

        for (key in period.quizComplete) {
            period.quizCompleteDayAvg[key] = period.quizComplete[key] / (months * 30);
            period.quizCompleteMonthAvg[key] = period.quizComplete[key] / months;
        }

        period.regStartTotalDayAvg = period.regStartTotal / (months * 30);
        period.regStartTotalMonthAvg = period.regStartTotal / months;
        period.regCompleteTotalDayAvg = period.regCompleteTotal / (months * 30);
        period.regCompleteTotalMonthAvg = period.regCompleteTotal / months;
        period.regRateTotal = period.regStartTotal ? period.regCompleteTotal / period.regStartTotal : 0;
        period.regRate = {};
        period.regStartDayAvg = {};
        period.regCompleteDayAvg = {};
        period.regStartMonthAvg = {};
        period.regCompleteMonthAvg = {};
        for (regType in period.regStart) {
            period.regRate[regType] = period.regRate[regType] || {};
            period.regStartDayAvg[regType] = {};
            period.regStartMonthAvg[regType] = {};
            for (key in period.regStart[regType]) {
                period.regRate[regType][key] = (period.regStart[regType] && period.regComplete[regType] && period.regStart[regType][key]) ? period.regComplete[regType][key]/period.regStart[regType][key] : 0;
                period.regStartDayAvg[regType][key] = period.regStart[regType][key] / (months * 30);
                period.regStartMonthAvg[regType][key] = period.regStart[regType][key] / months;
            }
        }

        for (regType in period.regComplete) {
            period.regCompleteDayAvg[regType] = {};
            period.regCompleteMonthAvg[regType] = {};
            for (key in period.regComplete[regType]) {
                period.regCompleteDayAvg[regType][key] = period.regComplete[regType][key] / (months * 30);
                period.regCompleteMonthAvg[regType][key] = period.regComplete[regType][key] / months;
            }
        }
    }

    return reduced;
}