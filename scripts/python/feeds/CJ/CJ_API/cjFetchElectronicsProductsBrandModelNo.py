__author__ = 'John'

import httplib2
import mysql.connector as db
import urllib
import time

from string import Template
import xml.etree.ElementTree as ET

from datetime import datetime
from brandFuzzMatch import fuzzMatch
from fuzzywuzzy import process, myfuzz

httplib2.debuglevel = 0

CJ_DEV_KEY = "009d20bb507bbe871aada971c71f4a848087fba761a646de2bce4c16871357ad68d0dd29671ae748777bc260f28b2abef068068b9b4bfee1f9ea28c3ebfdb7d3d3/00953b952c279b196e392a09a9f90a293679f08554b53bf19ee15c5ab907a1ea4b6e6296878521d4b29d1c09433f0b9ba37d6f5c7d5cba1e71835e06de78beec61"
CJ_LELA_SITE = "5391124"

#todo: externalize and encrypt category strings and API keys
#todo: test cases
#todo: save current in archive folder before running
#todo: CJ API is limited to 25 requests per minute
#todo: change hard-coded categoryID

def main():
    filePath = "C://Consumer//Feeds//CJ//"
    fileStr = Template("${category}_${suffix}.xml")
    now = datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(user='root',password='madonna',db='lela_eav')
    cur = cnx.cursor()

    h = httplib2.Http()

    categories = [
        [("camera"),(20)],
        [("tv"),(21)]
    ]

    for (file, categoryID) in categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w',0)
        cursor_args = categoryID
        cur.execute("SELECT DISTINCT b.BrandName FROM item i JOIN brand b ON i.BrandID = b.BrandID WHERE i.CategoryID IN (%s)", (cursor_args,))
        allBrands = cur.fetchall()
        models = dict()
        modelCur = cnx.cursor()

        for brand in allBrands:
            print "Collecting models for: %s" % (brand[0])
            cursor_args = (categoryID, brand[0])
            modelCur.execute("SELECT DISTINCT id.IdentifierValue FROM item i JOIN item_identifier id ON id.CategoryID = i.CategoryID AND id.ItemID = i.ItemID JOIN brand b ON b.BrandID = i.BrandID WHERE i.CategoryID IN (%s) AND id.IdentifierTypeID = 3 AND b.BrandName =%s", (cursor_args))
            if modelCur.rowcount <> 0:
                models[brand[0]] = modelCur.fetchall()

        # Loop through brand by brand
        for brand in allBrands:
            try:
                current_page = 1
                count = 1
                time.sleep(5) # try and stay below 25 requests per minute
                print brand

                # resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-name=' + brand[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                args = urllib.urlencode({'website-id' : CJ_LELA_SITE, 'records-per-page':100, 'advertiser-ids':'joined','low-price':'50','serviceable-area':'US','manufacturer-name':brand[0],'page-number':str(current_page)})
                resp, content = h.request('https://product-search.api.cj.com/v2/product-search?' + args,'GET',headers={'Authorization': CJ_DEV_KEY})
                main = ET.fromstring(content)

                current_page = int(main.find('products').attrib['page-number'])
                if int(main.find('products').attrib['records-returned']):
                    total_pages = int(main.find('products').attrib['total-matched']) / int(main.find('products').attrib['records-returned'])
                    while current_page <= total_pages:
                        a=10
                        for product in main:
                            for item in product:
                                x= ET.SubElement(item, "LelaCategoryID")
                                x.text = str(categoryID)
                                bf= ET.SubElement(item, "brandFuzzMatch")
                                mf= ET.SubElement(item, "modelFuzzMatch")
                                try:
                                    if item.find("manufacturer-name").text is not None:
                                        #brandFuzzMatch = fuzzMatch(item.find("manufacturer-name").text,allBrands)
                                        brandFuzzMatch = process.extractOne(item.find("manufacturer-name").text,allBrands)
                                        if brandFuzzMatch is not None:
                                            #bf.text= brandFuzzMatch[0]
                                            bf.text= "<![CDATA[" + unicode(brandFuzzMatch[0]).encode('utf-8') + "]]>"
                                            if models[brandFuzzMatch[0]] is not None:
                                                if item.find("manufacturer-sku").text is not None:
                                                    modelFuzzMatch = process.extractOne(item.find("manufacturer-sku").text, models[brandFuzzMatch[0]],None,myfuzz.WRatio)
                                                    if modelFuzzMatch is not None:
                                                        mf.attrib["score"]=str(modelFuzzMatch[1])
                                                        mf.text = modelFuzzMatch[0][0]
                                                    else:
                                                        mf.text="None"
                                                else:
                                                    bf.text="None"
                                        else:
                                            bf.text = "None"
                                except Exception, error:
                                    print "An unexpected exception: ", error

                                print >>f, ET.tostring(item)
                                #print ET.tostring(item)

                        current_page += 1
                        if current_page <= total_pages:
                            time.sleep(5)
                            #resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-sku='+ brand[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                            args = urllib.urlencode({'website-id' : CJ_LELA_SITE, 'records-per-page':100, 'advertiser-ids':'joined','low-price':'50','serviceable-area':'US','manufacturer-name':brand[0],'page-number':str(current_page)})
                            resp, content = h.request('https://product-search.api.cj.com/v2/product-search?' + args,'GET',headers={'Authorization': CJ_DEV_KEY})
                            main = ET.fromstring(content)

            except Exception, error:
                print "An unexpected exception: ", error

        print >> f,"</Items>"
        f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
