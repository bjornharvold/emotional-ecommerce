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
from fuzzywuzzy import process, myfuzz
import stopwatch

SHOPPING_API_VERSION = 'v1'
DEVELOPER_KEY = 'AIzaSyBsBLSdEwdoGxIapOT278RDO4ptDLo_3eg'
GAN_AFFILIATE = 'gan:504648'
GAN_COUNTRY = 'US'
RESULTS_PER_PAGE = 100
MAX_RESULTS = 1000 # Google restrictions

#todo: switch source to lela_eav database
#todo: externalize and encrypt category strings and API keys
#todo: test cases
#todo: save current in archive folder before running
#todo: fix to use item table - see CJ
#todo: get back partial response for desired fields - see https://developers.google.com/shopping-search/v1/performance
#todo: paging - maxResults and nextPageToken
#todo: doc - GAN only allows 1000 results tops

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

    categories = [
        [("tv"),(21)],
        [("camera"),(20)]
    ]

    for (file,categoryID) in categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w',0)
        cursor_args = categoryID
        cur.execute("SELECT Source, UPC FROM uniqueupc WHERE CategoryID IN (%s) ORDER BY UPC DESC", (cursor_args,))
        allUPCs = cur.fetchall()

        t = stopwatch.Timer()

        print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
        print >> f,"<Items>"

        for (upc_source,upc) in allUPCs:
            startIndex = 1

            print upc

            try:
                time.sleep(1)
                # args = urllib.urlencode({'country' : GAN_COUNTRY, 'maxResults':RESULTS_PER_PAGE, 'startIndex': startIndex,'restrictBy': 'brand='+brand[0]})
                # request = resource.list(source='gan:504648', args)
                request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,restrictBy='gtin=' + upc)
                response = request.execute()

                if response["totalItems"] > 0:
                    # response.startIndex
                    # response.itemsPerPage
                    # response.currentItemCount
                    current_page = int(response["startIndex"])
                    total_pages = min(MAX_RESULTS,int(response["totalItems"])) / int(response["currentItemCount"])
                    while current_page <= total_pages:
                        itemId=0
                        for item in response['items']:
                            itemId+=1
                            item["product"]["LelaCategoryID"]=str(categoryID)
                            item["product"]["UPCSource"]=upc_source
                            print >>f, "<product>" + XMLDict.convert_dict_to_xml(item["product"]) + "</product>"

                        current_page += 1

                        if current_page < total_pages:
                            time.sleep(1)
                            startIndex = (current_page * RESULTS_PER_PAGE) + 1
                            request = resource.list(source='gan:504648',country='US',maxResults=RESULTS_PER_PAGE,startIndex=startIndex,restrictBy='gtin=' + upc)
                            response = request.execute()

            except Exception, error:
                print "An unexpected exception: ", error

        print >> f,"</Items>"
        f.close()

    cnx.close()

if __name__ == '__main__':
    main()
