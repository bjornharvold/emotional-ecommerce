__author__ = 'John'

from amazonproduct import API
from amazonproduct import errors
from lxml import etree
import LookupASIN
import GetParent

originalASIN = 'B005I19LOC'
p = GetParent.get_parent(originalASIN)
if p is not None:
    items = LookupASIN.lookup(p)
    if items is not None:
        for item in items:
            print etree.tostring(item, pretty_print=True)

            if item.find('Variations') is not None:
                if item.Variations.find('TotalVariations') is not None:
                    if item.Variations.TotalVariations == 0:
                        moreitems = LookupASIN.lookup(originalASIN)
                        for item in moreitems:
                            print etree.tostring(item, pretty_print=True)

    else:
        print "items was None"