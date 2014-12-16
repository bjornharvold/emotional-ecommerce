#!/usr/bin/python2.4
# -*- coding: utf-8 -*-
#
# Copyright 2010 Google Inc. All Rights Reserved.

"""Basic query against the public shopping search API"""

#import json
import pprint
import XMLDict
import urllib
import time
import datetime
import mysql.connector as db

import httplib2
httplib2.debuglevel = 0

from apiclient.discovery import build
from oauth2client.file import Storage
from oauth2client.client import OAuth2WebServerFlow
from oauth2client.tools import run
from string import Template
from fuzzywuzzy import fuzz

SHOPPING_API_VERSION = 'v1'
DEVELOPER_KEY = 'AIzaSyBsBLSdEwdoGxIapOT278RDO4ptDLo_3eg'

#todo: switch source to lela_eav database
#todo: externalize and encrypt category strings and API keys
#todo: test cases
#todo: save current in archive folder before running
#todo: fix to use item table - see CJ

def main():

    """
    Load products from GAN using our Google Affiliate ID
    """
    filePath = "C://Consumer//Feeds//GAN//"
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")

    categories = [
        ("highchair",15)
    ]

    storage = Storage('shopping97.dat')
    credentials = storage.get()

    if credentials is None or credentials.invalid == True:
        flow = OAuth2WebServerFlow(
            client_id='636752767086.apps.googleusercontent.com',
            client_secret='MliIEykDIFIdGEv9JKwAVAUG',
            scope='https://www.googleapis.com/auth/shoppingapi',
            user_agent='shoppingapi-cmdline-sample/1.0')
        credentials = run(flow, storage)

    http = httplib2.Http()
    credentials.authorize(http)
    client = build('shopping', SHOPPING_API_VERSION, http=http, developerKey=DEVELOPER_KEY)
    resource = client.products()

    cnx = db.connect(user='root',password='madonna',db='lela_eav')
    cur = cnx.cursor()

    for (file,categoryID) in categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w')
        cur.execute("call GetUPCItems(" + str(categoryID) + ")")

        toCheck = cur.fetchall()
        print >> f,"<Items>"
        foundItems = dict()

        for (categoryID, itemID, brand, productModel) in toCheck:
            q_query = "%s %s" % (brand, productModel)
            print q_query

            time.sleep(2)

            request = resource.list(source='gan:504648', country='US',pp=1,q=q_query)
            response = request.execute()

            if response["totalItems"] > 0:
                for item in response['items']:
                    if item["product"]["googleId"] not in foundItems:
                        newList = list()
                        if "mpns" in item["product"]:
                            newList.append(item["product"]["mpns"])
                        else:
                            newList.append("None")
                        if "brand" in item["product"]:
                            newList.append(item["product"]["brand"])
                        else:
                            newList.append("None")
                        foundItems[item["product"]["googleId"]] = newList

                        item["product"]["LelaCategoryID"]=categoryID
                        item["product"]["LelaItemID"]=itemID

                        if "brand" in item["product"]:
                            item["product"]["brandFuzzRatio"]=fuzz.token_set_ratio(unicode(brand).encode('utf-8'),unicode(item["product"]["brand"]).encode('utf-8'))
                        item["product"]["productFuzzRatio"]= fuzz.token_set_ratio(unicode(q_query).encode('utf-8'), unicode(item["product"]["title"]).encode('utf-8'))

                        print >>f, XMLDict.convert_dict_to_xml(item)
                    else:
                        print "duplicate item %s" % (item["product"]["googleId"])

        print >> f,"</Items>"
        f.close()
    cnx.close()

if __name__ == '__main__':
    main()
