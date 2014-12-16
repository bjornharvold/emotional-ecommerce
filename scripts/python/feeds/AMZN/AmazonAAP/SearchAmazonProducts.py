__author__ = 'John'
#todo - Collect image sets into additional files

from amazonproduct import API
from amazonproduct import errors
from lxml import etree, objectify
from GetParent import get_parent
from pyDes import *
from string import Template
from urllib import urlencode
from fuzzywuzzy import process, fuzz

import datetime
import base64
import mysql.connector as db
import urllib
import time
import LookupASIN

import ConfigParser
import re

def decrypt(configValue, envKey):
    """
    Decrypts an encrypted config value
    """
    matcher = re.match('ENC\\(([^)]+)\\)', configValue)
    password = matcher.group(1)
    #cipher = Blowfish(envKey)
    #cipher.initCTR()
    #return cipher.decryptCTR(password)
    k = triple_des(envKey, CBC, "\0\0\0\0\0\0\0\0", pad='%', padmode=PAD_NORMAL)
    return k.decrypt(base64.b64decode(password))

#todo: passed in env var
#ENV_KEY = '%MF6gqww^0%Eig$JBX@mNLlQ2%C5bTP6x^m3vSAVr@sjI!SWdiOKNT&2'
ENV_KEY = 'a8cYF;c4nq_06>50'

cp = ConfigParser.RawConfigParser()
cp.read('AMZN.cfg')

#zz=cp.get('aws','aws_key')
AWS_KEY = decrypt(cp.get('aws','aws_key'),ENV_KEY)
SECRET_KEY = decrypt(cp.get('aws','secret_key'),ENV_KEY)
DB_PW = decrypt(cp.get('database','password'),ENV_KEY)

def main():

    """
    Search Amazon product based on a search index, and parameters
    SearchIndex=Baby
    Parameters
        Brand
        Availability="Available" - only available items
        MerchantId="Amazon"      - only Amazon as the seller, no merchants
        Title - the model

    """
    api = API(AWS_KEY, SECRET_KEY, 'us')

    filePath = "C://Consumer//Feeds//AmazonAAP//"
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

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

    for (file, categoryID) in configured_categories:
        category_file = fileStr.substitute(category=configured_categories[0][0], suffix=suffix)
        f = open(filePath + category_file,'w',0)

        cur.execute("SELECT Brand, ProductModelName, ItemID FROM vw_babybrandmodels where CategoryID IN (%s)",(categoryID,))

        toCheck = cur.fetchall()
        print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
        print >> f,"<Items>"
        for (brand,model,itemID) in toCheck:
            print(brand + " " + model)
            try:
                for page in api.item_search('Baby', Condition='New', Title=model, ResponseGroup='ItemAttributes,Offers,Variations,VariationSummary'):
                    time.sleep(2)
                    for product in page.Items.Item:
                        #TODO: fuzzy match brand and model here
                        brandFuzzMatch = process.extractOne(product.ItemAttributes.Brand,babyBrands)
                        a = 10
                        product.brandFuzzMatch = unicode(brandFuzzMatch[0][0]).encode('utf-8')
                        product.brandFuzzMatchScore = unicode(brandFuzzMatch[1]).encode('utf-8')
                        #x= objectify.SubElement(product, "brandFuzzMatch")
                        #x= etree.SubElement(product, "brandFuzzMatch")
                        #x = unicode(brandFuzzMatch[0][0]).encode('utf-8')
                        #x= etree.SubElement(product, "brandFuzzMatchScore")
                        #x = unicode(brandFuzzMatch[1]).encode('utf-8')
                        if models[brandFuzzMatch[0][0]] is not None:
                            modelFuzzMatch = process.extractOne(product.ItemAttributes.Title, models[brandFuzzMatch[0][0]],None,fuzz.WRatio)
                            if modelFuzzMatch is not None:
                                product.modelFuzzMatch = unicode(modelFuzzMatch[0][0]).encode('utf-8')
                                product.modelFuzzMatchScore = unicode(modelFuzzMatch[1]).encode('utf-8')
                                #x= etree.SubElement(product, "modelFuzzMatch")
                                #x= unicode(modelFuzzMatch[0][0]).encode('utf-8')
                                #x= etree.SubElement(product, "LelaItemID")
                                product.LelaItemID = unicode(modelFuzzMatch[0][1]).encode('utf-8')
                                product.LelaCategoryID = unicode(modelFuzzMatch[0][2]).encode('utf-8')
                                #x= unicode(modelFuzzMatch[0][1]).encode('utf-8')
                                #x= etree.SubElement(product, "LelaCategoryID")
                                #x= unicode(modelFuzzMatch[0][2]).encode('utf-8')
                                #x= etree.SubElement(product, "modelFuzzMatchScore")
                                #x= unicode(modelFuzzMatch[1]).encode('utf-8')

                        etree.cleanup_namespaces(product)
                        print >> f, etree.tostring(product, pretty_print=True)
                        #print product.ItemAttributes.Title

            except Exception, error:
                print "An unexpected exception: ", type(error)


    print >> f,"</Items>"
    f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
