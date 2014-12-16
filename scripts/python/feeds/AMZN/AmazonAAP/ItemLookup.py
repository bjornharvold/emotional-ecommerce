__author__ = 'John'

from amazonproduct import API

AWS_KEY = '06SBPEW90EGFPQ05N702'
SECRET_KEY = 'PNa/pxFaB/A3OET2NSm3ZuoHmFIXxLwUaMLRzyhL'

api = API(AWS_KEY, SECRET_KEY, 'us')

root = api.item_lookup('B001FDAZGU', IdType='ASIN',ResponseGroup='OfferSummary')
offerSummary = root.Items.Item.OfferSummary
price = offerSummary.LowestNewPrice.FormattedPrice
print(price)
