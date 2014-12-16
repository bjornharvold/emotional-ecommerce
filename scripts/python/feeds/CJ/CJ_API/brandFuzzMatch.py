__author__ = 'John'
from fuzzywuzzy import fuzz

def fuzzMatch(brandName, allBrands):
    result = None
    for brand in allBrands:
        f = fuzz.partial_ratio(brandName,brand[0])
        if f > 75:
            result = brand
            break
    return result

if __name__ == '__main__':
    allBrands = []
    allBrands.append("Dynex Products")
    allBrands.append("Insignia")
    allBrands.append("LG Electronics")
    allBrands.append("JVC")
    allBrands.append("Magnavox")
    allBrands.append("Mitsubishi")

    print fuzzMatch("LG", allBrands)

