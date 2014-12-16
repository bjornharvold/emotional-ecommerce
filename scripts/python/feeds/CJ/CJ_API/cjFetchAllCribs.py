__author__ = 'John'
'''
Experiment - cribs
'''
import httplib2
import mysql.connector as db
import urllib
import time
import base64
import xml.etree.ElementTree as ET
import ConfigParser
import re

from string import Template
from datetime import datetime
from fuzzywuzzy import process, fuzz
from pyDes import *

def decrypt(configValue, envKey):
    """
    Decrypts an encrypted config value
    """
    matcher = re.match('ENC\\(([^)]+)\\)', configValue)
    password = matcher.group(1)
    k = triple_des(envKey, CBC, "\0\0\0\0\0\0\0\0", pad='%', padmode=PAD_NORMAL)
    return k.decrypt(base64.b64decode(password))

#todo: passed in env var
#ENV_KEY = '%MF6gqww^0%Eig$JBX@mNLlQ2%C5bTP6x^m3vSAVr@sjI!SWdiOKNT&2'
ENV_KEY = 'a8cYF;c4nq_06>50'

cp = ConfigParser.RawConfigParser()
cp.read('CJ.cfg')

CJ_DEV_KEY = decrypt(cp.get('cj','cj_dev_key'),ENV_KEY)
DB_PW = decrypt(cp.get('database','password'),ENV_KEY)

httplib2.debuglevel = 0

# CJ_DEV_KEY = "009d20bb507bbe871aada971c71f4a848087fba761a646de2bce4c16871357ad68d0dd29671ae748777bc260f28b2abef068068b9b4bfee1f9ea28c3ebfdb7d3d3/00953b952c279b196e392a09a9f90a293679f08554b53bf19ee15c5ab907a1ea4b6e6296878521d4b29d1c09433f0b9ba37d6f5c7d5cba1e71835e06de78beec61"
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

    #cnx = db.connect(host='latest.lela.com',port=3307,user='lelaetl',password='jYZto9DfsQcrVWLGb64vJEeKzNTSR2pXI8dmOa1y0BliAgwuMP',db='lelaetl')
    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    # cnx = db.connect(host='localhost',port=3306,user='lelaetl',password='lelaetl',db='lelaetl')
    cur = cnx.cursor()

    h = httplib2.Http()

    configured_categories = cp.items('categories_baby')

    cur.execute("SELECT brand FROM vw_BabyBrands")
    babyBrands = cur.fetchall()
    models = dict()
    modelCur = cnx.cursor()

    for brand in babyBrands:
        print "Collecting models for: %s" % (brand[0])
        cursor_args = (brand[0])
        modelCur.execute("SELECT ProductModelName, ItemID, CategoryID FROM vw_babybrandmodels WHERE Brand=%s", (cursor_args,))
        if modelCur.rowcount <> 0:
            models[brand[0]] = modelCur.fetchall()

    price_ranges = [(100,150),(151,200),(201,300),(301,400),(401,500),(501,600),(601,700),(701,800),(801,900),(901,1000),(1000,5000)]

    category_file = fileStr.substitute(category=configured_categories[0][0], suffix=suffix)
    f = open(filePath + category_file,'w',0)
    print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
    print >> f,"<Items>"

    for (minprice,maxprice) in price_ranges:
        print "working on range " + str(minprice) + " " + str(maxprice)

        try:
            current_page = 1
            count = 1
            time.sleep(2) # try and stay below 25 requests per minute

            # resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-name=' + brand[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
            args = urllib.urlencode({'website-id' : CJ_LELA_SITE, 'records-per-page':100, 'advertiser-ids':'joined','serviceable-area':'US','keywords':'+crib','low-price':str(minprice),'high-price':str(maxprice),'page-number':str(current_page)})
            resp, content = h.request('https://product-search.api.cj.com/v2/product-search?' + args,'GET',headers={'Authorization': CJ_DEV_KEY})
            main = ET.fromstring(content)

            current_page = int(main.find('products').attrib['page-number'])
            # print int(main.find('products').attrib['records-returned'])
            if int(main.find('products').attrib['records-returned']):
                total_pages = int(main.find('products').attrib['total-matched']) / int(main.find('products').attrib['records-returned'])
                while current_page <= total_pages:
                    print "page " + str(current_page)
                    itemId=0
                    for product in main:
                        for item in product:
                            itemId+=1
                            # Do brand fuzzy match
                            brandFuzzMatch = process.extractOne(item.find("manufacturer-name").text,babyBrands)
                            x= ET.SubElement(item, "brandFuzzMatch")
                            x.text = unicode(brandFuzzMatch[0][0]).encode('utf-8')
                            x= ET.SubElement(item, "brandFuzzMatchScore")
                            x.text = unicode(brandFuzzMatch[1]).encode('utf-8')
                            if models[brandFuzzMatch[0][0]] is not None:
                                modelFuzzMatch = process.extractOne(item.find("name").text, models[brandFuzzMatch[0][0]],None,fuzz.WRatio)
                                if modelFuzzMatch is not None:
                                    x= ET.SubElement(item, "modelFuzzMatch")
                                    x.text = unicode(modelFuzzMatch[0][0]).encode('utf-8')
                                    x= ET.SubElement(item, "LelaItemID")
                                    x.text = unicode(modelFuzzMatch[0][1]).encode('utf-8')
                                    x= ET.SubElement(item, "LelaCategoryID")
                                    x.text = unicode(modelFuzzMatch[0][2]).encode('utf-8')
                                    x= ET.SubElement(item, "modelFuzzMatchScore")
                                    x.text = unicode(modelFuzzMatch[1]).encode('utf-8')
                            print "item: " + str(itemId) + " " + item.find("name").text
                            print >>f, ET.tostring(item)

                    current_page += 1

                    if current_page <= total_pages:
                        time.sleep(2)
                        #resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-sku='+ brand[0] + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                        #args = urllib.urlencode({'website-id' : CJ_LELA_SITE, 'records-per-page':100, 'advertiser-ids':'joined','serviceable-area':'US','upc':upc[1:],'page-number':str(current_page)})
                        args = urllib.urlencode({'website-id' : CJ_LELA_SITE, 'records-per-page':100, 'advertiser-ids':'joined','serviceable-area':'US','keywords':'+crib','low-price':str(minprice),'high-price':str(maxprice),'page-number':str(current_page)})
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
