__author__ = 'John'

import httplib2
import mysql.connector as db
import urllib
import time
import ConfigParser
import re
import base64

from string import Template, upper
#from lxml import etree
#from lxml import objectify
import xml.etree.ElementTree as ET

from datetime import datetime
from fuzzywuzzy import fuzz
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

CJ_LELA_SITE = "5391124"

#manufacturer-name
#upc

def main():
    filePath = "C://Consumer//Feeds//CJ//"
    fileStr = Template("${category}_${suffix}.xml")
    now = datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

    h = httplib2.Http()
    configured_categories = cp.items('categories_baby')

    for (file, categoryID) in configured_categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w')
        print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
        print >> f,"<Items>"

        cur.execute("SELECT brand FROM vw_BabyBrands")
        toCheck = cur.fetchall()

        for (brand) in toCheck:
            print("%s") % (brand[0])
            try:
                current_page = 1
                count = 1
                time.sleep(2) # try and stay below 25 requests per minute
                #resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=5391124&keywords=GPS&serviceable-area=US','GET',headers={'Authorization':'009d20bb507bbe871aada971c71f4a848087fba761a646de2bce4c16871357ad68d0dd29671ae748777bc260f28b2abef068068b9b4bfee1f9ea28c3ebfdb7d3d3/00953b952c279b196e392a09a9f90a293679f08554b53bf19ee15c5ab907a1ea4b6e6296878521d4b29d1c09433f0b9ba37d6f5c7d5cba1e71835e06de78beec61'})
                # resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&manufacturer-name='+ brand + '&keywords=' + productModel + '&advertiser-ids=joined&serviceable-area=US&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                searchBrand = urllib.quote("+" + " +".join(brand[0].split()).encode('utf-8'))
                resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&records-per-page=100&advertiser-ids=joined&serviceable-area=US&manufacturer-name='+ searchBrand + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})

                main = ET.fromstring(content)

                for product in main:
                    current_page = int(product.attrib["page-number"])
                    if int(product.attrib["records-returned"]):
                        total_pages = int(product.attrib["total-matched"]) / int(product.attrib["records-returned"])
                        while current_page <= total_pages:
                            a=10
                            for item in product:
                                # brand -> product.manufacturer-name
                                # productModel -> product.name
#                                x= ET.SubElement(item, "LelaCategoryID")
#                                x.text = unicode(categoryID)
#                                x= ET.SubElement(item, "LelaItemID")
#                                x.text = unicode(itemID)
                                x= ET.SubElement(item,"brandFuzzMatch")
                                x.text = unicode(brand[0]).encode('utf-8')
                                x= ET.SubElement(item, "brandFuzzMatchScore")
                                x.text = unicode(fuzz.token_set_ratio(unicode(upper(brand[0])).encode('utf-8'),unicode(upper(item.find("manufacturer-name").text)).encode('utf-8')))

                                print >>f, ET.tostring(item)

                            current_page += 1
                            if current_page <= total_pages:
                                time.sleep(2)
                                resp, content = h.request('https://product-search.api.cj.com/v2/product-search?website-id=' + CJ_LELA_SITE + '&advertiser-ids=joined&serviceable-area=US&manufacturer-name='+ searchBrand + '&page-number='+ str(current_page),'GET',headers={'Authorization': CJ_DEV_KEY})
                                ##main = objectify.fromstring(content)
                                main = ET.fromstring(content)

            except():
                print 'exception:'

        print >> f,"</Items>"
        f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
