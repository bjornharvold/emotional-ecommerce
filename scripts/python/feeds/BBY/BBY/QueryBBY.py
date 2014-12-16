__author__ = 'John'

from premix import ProductQuery
from fuzzywuzzy import process, myfuzz
import XMLDict

# APIKEY = "65azx6f6a8je6cm6e64ytysc"
CJ_KEY = "3419488"

def query(APIKEY,categoryPath,IsMarketPlace,f,allBrands,models):
    # abcat0101000 tvs
    # abcat0400000 cameras
    if IsMarketPlace:
        product_query = ProductQuery.all().filter('categoryPath.id =', categoryPath).filter('marketplace =', 'true')
    else:
        product_query = ProductQuery.all().filter('categoryPath.id =', categoryPath)
    product_query.url(APIKEY, pid=CJ_KEY)
    results = product_query.fetch(APIKEY, pid=CJ_KEY,retry=3)
    if results.error is not None:
        print results.error_message
    else:
        if results is not None:
            while results.current_page <= results.total_pages:
                for product in results.products:
                    print product.name
                    print >>f, "<Item>"
                    print >>f, XMLDict.convert_dict_to_xml(product.data)
                    try:
                        if "manufacturer" in product.attributes:
                            brandFuzzMatch = process.extractOne(product.manufacturer,allBrands)
                            if brandFuzzMatch is not None:
                                # print >>f, "<brandFuzzMatch>" + brandFuzzMatch[0][0] + "</brandFuzzMatch>"
                                print >>f, '<brandFuzzMatch score="' + str(brandFuzzMatch[1]) + '">' + "<![CDATA[" + unicode(brandFuzzMatch[0][0]).encode('utf-8') + "]]>" + '</brandFuzzMatch>'
                                if models[brandFuzzMatch[0][0]] is not None:
                                    #modelFuzzMatch = process.extractOne(product.model_number, models[brandFuzzMatch[0][0]])
                                    modelFuzzMatch = process.extractOne(product.model_number, models[brandFuzzMatch[0][0]],None,myfuzz.WRatio)
                                    if modelFuzzMatch is not None:
                                        print >>f, '<modelFuzzMatch score="' + str(modelFuzzMatch[1]) + '">' + modelFuzzMatch[0][0] + '</modelFuzzMatch>'
                                    else:
                                        print >>f, "<modelFuzzMatch>None</modelFuzzMatch>"
                                else:
                                    print >>f, "<modelFuzzMatch>None</modelFuzzMatch>"
                            else:
                                print >>f, "<brandFuzzMatch>None</brandFuzzMatch>"

                    except Exception, error:
                        print "An unexpected exception: ", error
                        print "... while processing " + product.name
                    print >>f, "</Item>"
                results = results.next
                if results is None:
                    break

