function reduce(key, values) {

    var result = {
        total: 0,
        assoc: 0,
        totalQuiz: 0,
        assocQuiz: 0
    };

    values.forEach(function(value) {
        result.total += value.total;
        result.assoc += value.assoc;
        result.totalQuiz += value.totalQuiz;
        result.assocQuiz += value.assocQuiz;
    });

    return result;
}