__author__ = 'John'

from string import Template
from amazonproduct import API
from lxml import etree
from pyDes import *

import base64
import datetime
import mysql.connector as db
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
    k = triple_des(envKey, CBC, "\0\0\0\0\0\0\0\0", pad='%', padmode=PAD_NORMAL)
    return k.decrypt(base64.b64decode(password))

#todo: passed in env var
ENV_KEY = 'a8cYF;c4nq_06>50'

cp = ConfigParser.RawConfigParser()
cp.read('AMZN.cfg')

#zz=cp.get('aws','aws_key')
AWS_KEY = decrypt(cp.get('aws','aws_key'),ENV_KEY)
SECRET_KEY = decrypt(cp.get('aws','secret_key'),ENV_KEY)
DB_PW = decrypt(cp.get('database','password'),ENV_KEY)

#todo: test cases
#todo: save current in archive folder before running
#todo: limit scope to Sold By Amazon?
#todo - Collect image sets into additional files

def main():

    """
    Gets products from Amazon based on their UPC.
    """
    api = API(AWS_KEY, SECRET_KEY, 'us')

    filePath = cp.get('file','filepath')
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

    configured_categories = cp.items('categories_ASIN')

    for (file, categoryID) in configured_categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w')
        cursor_args = categoryID
        cur.execute("SELECT Source, ASIN FROM vw_allcategory_upcs WHERE CategoryID IN (%s) ORDER BY ASIN DESC", (cursor_args,))
        allASINs = cur.fetchall()

        print >> f,'<?xml version="1.0" encoding="ISO-8859-1"?>'
        print >> f,"<Items>"

        for (ASIN_source, asin) in allASINs:
            print '%s: %s' % (file,asin)
            try:
                time.sleep(2)

                items = LookupASIN.lookup(asin,AWS_KEY,SECRET_KEY)
                if items is not None:
                    for item in items:
                        print >> f, etree.tostring(item, pretty_print=True)

            except() as e:
                print 'exception:'
        print >> f,"</Items>"

    f.close()

    cur.close()
    cnx.close()

if __name__ == '__main__':
    main()
