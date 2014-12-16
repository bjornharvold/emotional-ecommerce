import urllib
import re

def get_parent(ASIN):
    base_url = 'http://amzn.com/'
    content = urllib.urlopen(base_url + ASIN).read()
    m = re.search("'parent_asin',(.*?)\)", content)
    if m:
        quote = m.group(1)
    else:
        quote = 'no parent available for: ' + ASIN
    return quote

if __name__ == '__main__':
    print get_parent('B004ANFDOG')