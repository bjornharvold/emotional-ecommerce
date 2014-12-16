__author__ = 'John'
from amazonproduct import API
from amazonproduct import errors
#from lxml import etree

def lookup(AWS_KEY,SECRET_KEY,UPC,SearchIndex):

    api = API(AWS_KEY,SECRET_KEY,'us')
    try:
        root = api.item_lookup(UPC, IdType='UPC',SearchIndex=SearchIndex,  ResponseGroup='ItemAttributes,Images,Offers,Variations,VariationImages')
        nspace = root.nsmap.get(None, '')
        items = root.xpath('//aws:Items/aws:Item', namespaces={'aws': nspace})
        return items
    except:
        return None

if __name__ == '__main__':
    print lookup('074000373150')