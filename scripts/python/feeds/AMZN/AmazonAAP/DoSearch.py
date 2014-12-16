__author__ = 'John'
from amazonproduct import API
from amazonproduct import errors
from lxml import etree
import time

# def searchFor(Item):

api = API('06SBPEW90EGFPQ05N702','PNa/pxFaB/A3OET2NSm3ZuoHmFIXxLwUaMLRzyhL','us')
# node = api.item_search('Electronics', Manufacturer="Samsung", Title="R5067W")

# total_results = node.Items.TotalResults.pyval
# total_pages = node.Items.TotalPages.pyval
# a=10
# get all items from result set and
# print author and title
# print node.tag

for page in api.item_search('Electronics', Condition="New", Brand="Samsung"):
    time.sleep(2)
    for product in page.Items.Item:
        print >> f, etree.tostring(product, pretty_print=True)
        print product.ItemAttributes.Title