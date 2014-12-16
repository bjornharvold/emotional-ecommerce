__author__ = 'John'

import datetime
import mysql.connector as db

from string import Template
from pyDes import *

import base64
import QueryBBY
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
ENV_KEY = 'a8cYF;c4nq_06>50'

cp = ConfigParser.RawConfigParser()
cp.read('BBY.cfg')

API_KEY = decrypt(cp.get('bby','api_key'),ENV_KEY)
DB_PW = decrypt(cp.get('database','password'),ENV_KEY)

#todo: test cases
#todo: save current in archive folder before running

def main():

    """
    This version just pulls *all* products from Best Buy (camera and tv), but also matches a brand if it can.
    Using an API from https://bitbucket.org/gumptioncom/premix/overview
    """
    filePath = "C://Consumer//Feeds//BBY//"
    fileStr = Template('${category}_${suffix}.xml')
    now = datetime.datetime.now()
    suffix = now.strftime("%Y%m%d")

    cnx = db.connect(host=cp.get('database','host'),port=cp.getint('database','port'),user=cp.get('database','user'),password=DB_PW,db=cp.get('database','db'))
    cur = cnx.cursor()

    configured_categories = cp.items('categories')

    for (file, categoryID_category_path) in configured_categories:
        category_file = fileStr.substitute(category=file, suffix=suffix)
        f = open(filePath + category_file,'w',0)
        categoryID = categoryID_category_path.split()[0]
        category_path = categoryID_category_path.split()[1]
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

        print >> f,"<Items>"

        print "***"
        print "Working on BBY"
        print "***"

        QueryBBY.query(API_KEY, category_path,False,f,allBrands,models)

        print "***"
        print "Working on marketplace"
        print "***"

        QueryBBY.query(API_KEY, category_path,True,f,allBrands,models)

        print >> f,"</Items>"
        print "Done with category: %s" % (file)
        f.close()

    cnx.close()

if __name__ == '__main__':
    main()
