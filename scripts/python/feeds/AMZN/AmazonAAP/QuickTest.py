__author__ = 'John'
from amazonproduct import API
from amazonproduct import errors
from lxml import etree
import LookupASIN
import GetParent

originalASIN = 'B0045Y1ISK'
p = GetParent.get_parent(originalASIN)
if p is not None:
    items = LookupASIN.lookup(p)
    if items is not None:
        for item in items:
            print etree.tostring(item, pretty_print=True)
            if item.Variations is not None:
                if item.Variations.TotalVariations is not None and item.Variations.TotalVariations > 0:
                    ctr = 0
                    while ctr < item.Variations.TotalVariations:
                        print etree.tostring(item.Variations.Item[ctr],pretty_print=True)
                        ctr += 1
                        #for v in item.Variations.iter("Item"):
                        #print etree.tostring(v, pretty_print=True)

    else:
        print "items was None - maybe this ASIN %s had no parent?" % (originalASIN)