__author__ = 'John'

import httplib2
import mysql.connector as db
import urllib
import time

from string import Template
import xml.etree.ElementTree as ET

from datetime import datetime
from fuzzywuzzy import fuzz

httplib2.debuglevel = 0

CJ_DEV_KEY = "009d20bb507bbe871aada971c71f4a848087fba761a646de2bce4c16871357ad68d0dd29671ae748777bc260f28b2abef068068b9b4bfee1f9ea28c3ebfdb7d3d3/00953b952c279b196e392a09a9f90a293679f08554b53bf19ee15c5ab907a1ea4b6e6296878521d4b29d1c09433f0b9ba37d6f5c7d5cba1e71835e06de78beec61"
CJ_LELA_SITE = "5391124"

#manufacturer-name
#upc
#todo: switch source to lela_eav database
#todo: externalize and encrypt category strings and API keys
#todo: test cases
#todo: save current in archive folder before running
#todo: CJ API is limited to 25 requests per minute
#todo: maybe just query by brand and keyword = category, to get more hits to score

def main():
    filePath = "C://Consumer//Feeds//CJ//"
    fileStr = Template("${category}_${suffix}.xml")
    now = datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(user='root',password='madonna',db='lela_eav')
    cur = cnx.cursor()

    h = httplib2.Http()

    category_file = fileStr.substitute(category="television", suffix=suffix)
    f = open(filePath + category_file,'w')
    print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
    print >> f,"<Items>"

    cur.execute("SELECT DISTINCT SUBSTRING_INDEX(IdentifierValue, '/', 1) AS ModelNumber FROM item_identifier WHERE CategoryID = 21 AND IdentifierTypeID = 3")
    toCheck = cur.fetchall()

    for modelNumber in toCheck:
        try:
            current_page = 1
            count = 1
            time.sleep(5) # try and stay below 25 requests per minute
            print modelNumber
            resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-sku=' + modelNumber[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
            main = ET.fromstring(content)
            for product in main:
                current_page = int(product.attrib["page-number"])
                if int(product.attrib["records-returned"]):
                    total_pages = int(product.attrib["total-matched"]) / int(product.attrib["records-returned"])
                    while current_page <= total_pages:
                        a=10
                        for item in product:
                            x= ET.SubElement(item, "LelaCategoryID")
                            x.text = "21"
                            print >>f, ET.tostring(item)

                        current_page += 1
                        if current_page <= total_pages:
                            time.sleep(5)
                            resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-sku='+ modelNumber[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                            main = ET.fromstring(content)
        except():
            print 'exception:'

    print >> f,"</Items>"
    f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
