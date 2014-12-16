select
TV_ID,
BrandID,
CNETMostPopular,
BuycomMostPopular,
BestBuyBestSelling,
WalmartBestSelling,
General_ProductType,
BrandName,
Display_DiagonalSize
from vw_televisions_data

DELETE FROM productmotivator WHERE categoryID = 21