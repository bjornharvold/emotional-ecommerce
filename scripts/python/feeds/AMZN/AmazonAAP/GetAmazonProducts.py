from string import Template
from urllib import urlencode
import datetime

__author__ = 'John'
#todo - Collect image sets into additional files

from amazonproduct import API
from amazonproduct import errors
from lxml import etree
from GetParent import get_parent
from pyDes import *

import mysql.connector as db
import urllib
import time
import LookupASIN
import base64
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

#todo: switch source to lela_eav database
#todo: externalize and encrypt category strings and API keys
#todo: test cases
#todo: save current in archive folder before running
#todo: limit scope to Sold By Amazon?
def main():

    """
    Gets products from Amazon based on their ASIN. We supply an ASIN, and this module figures out the parent ASIN, and
    then gets all products under that parent ASIN (variations)
    """
    api = API(AWS_KEY, SECRET_KEY, 'us')

    filePath = cp.get('file','filepath')
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")
    """
    The categories are specified with the category url, followed by the query to use to query the API for that category
    """
    categories = [
        [("stroller"),("SELECT upc, ASIN FROM stroller_data WHERE LENGTH(ifnull(ASIN,'')) > 0")],
        [("crib"),("SELECT 'upc', ASIN FROM cribs_data WHERE length(ifnull(ASIN, '')) > 0 AND locate('/',ASIN)=0")],
        [("carseat"),("SELECT 'upc', ASIN FROM carseat_data WHERE length(ifnull(ASIN, '')) > 0 AND locate('/',ASIN)=0")],
        [("carrier"),("SELECT 'upc', ASIN FROM carrier_data WHERE length(ifnull(ASIN, '')) > 0 AND locate('/',ASIN)=0")],
        [("babymonitor"),("SELECT 'upc', ASIN FROM babymonitor_data WHERE length(ifnull(ASIN, '')) > 0 AND locate('/',ASIN)=0")],
        [("highchair"),("SELECT 'upc', ASIN FROM highchair_data WHERE length(ifnull(ASIN, '')) > 0 AND locate('/',ASIN)=0")],
        [("digitalcamera"),(
            """
                SELECT upc, ASIN FROM
                  (
                SELECT 'upc'
                     , CASE
                       WHEN (locate('amzn.com/', AmazonASIN) <> 0) THEN
                         substr(AmazonASIN, (locate('amzn.com/', AmazonASIN) + 9), 10)
                       ELSE
                         AmazonASIN
                       END
                       AS ASIN
                FROM
                  cameras_data
                  ) a
                WHERE
                  length(ifnull(ASIN, '')) > 0
                  AND locate('Available', ASIN) = 0
            """
            )]
    ]

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

    configured_categories = cp.items('categories')

    for (file,query) in configured_categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w')
        cur.execute(query)

        toCheck = cur.fetchall()
        print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
        print >> f,"<Items>"
        for x in toCheck:
            print(x)
            try:
                checkASIN = x[1]
                p = get_parent(checkASIN)
                if p is None or p == checkASIN:
                    print "(ASIN is the parent)"
                    p = checkASIN
                else:
                    print "Parent for ASIN %s: %s" % (checkASIN, p.replace(" ", "").replace("'", ""))

                time.sleep(2)

                items = LookupASIN.lookup(p)
                if items is not None:
                    for item in items:
                        print >> f, etree.tostring(item, pretty_print=True)
                        if item.find('Variations') is not None:
                            if item.Variations.find('TotalVariations') is not None:
                                if not item.Variations.TotalVariations:
                                    items = LookupASIN.lookup(checkASIN)
                                    for item in items:
                                        print >>f, etree.tostring(item, pretty_print=True)

            except() as e:
                print 'exception:'

        print >> f,"</Items>"
        f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
