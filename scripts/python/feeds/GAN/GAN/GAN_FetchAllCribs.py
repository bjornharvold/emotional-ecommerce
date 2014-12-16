import pprint
import XMLDict
import urllib
import time
import datetime
import mysql.connector as db
import base64
import ConfigParser
import re
import stopwatch
import httplib2
httplib2.debuglevel = 0

from apiclient.discovery import build
from oauth2client.file import Storage
from oauth2client.client import OAuth2WebServerFlow
from oauth2client.tools import run
from string import Template
from fuzzywuzzy import process, fuzz
from pyDes import *

SHOPPING_API_VERSION = 'v1'
GAN_AFFILIATE = 'gan:504648'
GAN_COUNTRY = 'US'
RESULTS_PER_PAGE = 100
MAX_RESULTS = 1000 # Google restrictions

def decrypt(configValue, envKey):
    """
    Decrypts an encrypted config value
    """
    matcher = re.match('ENC\\(([^)]+)\\)', configValue)
    password = matcher.group(1)
    k = triple_des(envKey, CBC, "\0\0\0\0\0\0\0\0", pad='%', padmode=PAD_NORMAL)
    return k.decrypt(base64.b64decode(password))

#todo: passed in env var
ENV_KEY = 'a8cYF;c4nq_06>50'

#todo: passed as parm
MODEL_QUERY_TYPE = "ModelName"
#MODEL_QUERY_TYPE = "ModelNoQuery"

cp = ConfigParser.RawConfigParser()
cp.read('GAN.cfg')

DEVELOPER_KEY = decrypt(cp.get('gan','developer_key'),ENV_KEY)
CLIENT_SECRET = decrypt(cp.get('gan','client_secret'),ENV_KEY)
DB_PW = decrypt(cp.get('database','password'),ENV_KEY)

#todo: test cases
#todo: save current in archive folder before running
#todo: fix to use item table - see CJ
#todo: get back partial response for desired fields - see https://developers.google.com/shopping-search/v1/performance
#todo: paging - maxResults and nextPageToken
#todo: doc - GAN only allows 1000 results tops
#todo: not just cribs - any item title with an item in babybrands

def main():

    """
    Load products from GAN using our Google Affiliate ID
    """
    filePath = "C://Consumer//Feeds//GAN//"
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")

    storage = Storage('shopping97.dat')
    credentials = storage.get()

    #client_secret='MliIEykDIFIdGEv9JKwAVAUG',

    if credentials is None or credentials.invalid == True:
        flow = OAuth2WebServerFlow(
            client_id='636752767086.apps.googleusercontent.com',
            client_secret=CLIENT_SECRET,
            scope='https://www.googleapis.com/auth/shoppingapi',
            user_agent='shoppingapi-cmdline-sample/1.0')
        credentials = run(flow, storage)

    http = httplib2.Http()
    credentials.authorize(http)
    client = build('shopping', SHOPPING_API_VERSION, http=http, developerKey=DEVELOPER_KEY)
    resource = client.products()

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

    configured_categories = cp.items('categories_baby')
    modelQuery = cp.get(MODEL_QUERY_TYPE,'ModelQuery')
    modelIdentifer = cp.get(MODEL_QUERY_TYPE,'ModelIdentifier')
    modelMinimumPrice = cp.get(MODEL_QUERY_TYPE,'MinimumPrice')

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
        t = stopwatch.Timer()

        startIndex = 1

        try:
            time.sleep(2)
            # args = urllib.urlencode({'country' : GAN_COUNTRY, 'maxResults':RESULTS_PER_PAGE, 'startIndex': startIndex,'restrictBy': 'brand='+brand[0]})
            # request = resource.list(source='gan:504648', args)
            # request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,q='crib',price=[minprice,maxprice])
            request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,restrictBy='title=crib,price=[' + str(minprice) + ',' + str(maxprice) + ']')
            response = request.execute()

            if response["totalItems"] > 0:
                # response.startIndex
                # response.itemsPerPage
                # response.currentItemCount
                print "total results in range: " + str(response["totalItems"])
                current_page = int(response["startIndex"])
                total_pages = min(MAX_RESULTS,int(response["totalItems"])) / int(response["currentItemCount"])
                while current_page <= total_pages:
                    print "page " + str(current_page)
                    itemId=0
                    for item in response['items']:
                        itemId+=1
                        brandFuzzMatch = process.extractOne(item["product"]["brand"],babyBrands)
                        if brandFuzzMatch is not None:
                            item["product"]["brandFuzzMatch"] = unicode(brandFuzzMatch[0][0]).encode('utf-8')
                            item["product"]["brandFuzzMatchScore"] = brandFuzzMatch[1]
                            if models[brandFuzzMatch[0][0]] is not None:
                                if item["product"].get(modelIdentifer) is not None:
                                    modelFuzzMatch = process.extractOne(item["product"][modelIdentifer], models[brandFuzzMatch[0][0]],None,fuzz.WRatio)
                                    if modelFuzzMatch is not None:
                                        item["product"]["modelFuzzMatch"] = modelFuzzMatch[0][0]
                                        item["product"]["LelaItemID"] = modelFuzzMatch[0][1]
                                        item["product"]["LelaCategoryID"] = modelFuzzMatch[0][2]
                                        item["product"]["modelFuzzMatchScore"] = modelFuzzMatch[1]
                                    else:
                                        item["product"]["modelFuzzMatch"] = "None"
                                else:
                                    item["product"]["modelFuzzMatch"] = "None"
                            else:
                                item["product"]["modelFuzzMatch"] = "None"
                        else:
                            item["product"]["brandFuzzMatch"] = "None"
#                        print "brand: %s page: %s of %s: item: %s time so far: %s" % (brand[0],current_page,total_pages,itemId,str(t.elapsed))
                        print "item: " + str(itemId) + " " + item["product"]["title"]
                        print >>f, "<product>" + XMLDict.convert_dict_to_xml(item["product"]) + "</product>"

                    current_page += 1

            if current_page < total_pages:
                time.sleep(2)
                startIndex = (current_page * RESULTS_PER_PAGE) + 1
                #request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,q=crib,'price=[' + minprice + ',' + maxprice + ']')
                #request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,q='crib',restrictBy='price=[' + str(minprice) + ',' + str(maxprice) + ']')
                request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,restrictBy='title=crib,price=[' + str(minprice) + ',' + str(maxprice) + ']')
                response = request.execute()

        except Exception, error:
            print "An unexpected exception: ", error

    print >> f,"</Items>"
    f.close()

    cnx.close()

if __name__ == '__main__':
    main()
