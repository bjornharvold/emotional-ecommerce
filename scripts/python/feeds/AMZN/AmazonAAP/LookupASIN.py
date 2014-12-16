__author__ = 'John'
from amazonproduct import API
from amazonproduct import errors

def lookup(ASIN,AWS_KEY,SECRET_KEY):

    api = API(AWS_KEY,SECRET_KEY,'us')
    try:
        root = api.item_lookup(ASIN, IdType='ASIN',ResponseGroup='ItemAttributes,Images,Offers,Variations,VariationImages')
        nspace = root.nsmap.get(None, '')
        items = root.xpath('//aws:Items/aws:Item', namespaces={'aws': nspace})
        return items
    except:
        return None

