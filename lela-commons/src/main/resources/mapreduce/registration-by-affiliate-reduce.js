function Reduce(key, values) {
    var reduced = { total: 0 }; // initialize a doc (same format as emitted value)

    values.forEach(function(val) {
        for (key in val) {
            reduced[key] = reduced[key] ? reduced[key] + val[key] : val[key];
        }
    });

    return reduced;
}