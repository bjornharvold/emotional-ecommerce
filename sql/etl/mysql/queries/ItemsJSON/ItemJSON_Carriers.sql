SET SESSION group_concat_max_len = 2000000;

SELECT 
  GROUP_CONCAT(REPLACE(CONCAT('{"nm":"',v.Model, '",',
'"lcl":"en_US",',
'"tp":"PRODUCT",',
'"tld":"',IFNULL(v.CarrierID,'null'),'",',
'"rlnm":"',v.URLName,'",',
IF(v.objectID IS NOT NULL,CONCAT('"id":"',v.objectID,'",'),'"id": null,'),
'"sbttrs":[',
'{"ky":"Brand", "vl":"',IFNULL(v.Brand,'null'),'"},',
'{"ky":"ProductModelName", "vl":"',IFNULL(v.Model,'null'),'"},',
'{"ky":"ListPrice", "vl":',IFNULL(CAST(REPLACE(v.ListPrice,'$','') AS DECIMAL(8,2)),'null'),'},',
getLowestPriceInfo(13,v.CarrierID),',',
'{"ky":"ProductImageUrl", "vl":"',IFNULL(getDefaultProductImage(13,v.CarrierID),'null'),'"}',
'],',
'"ctgry":',getCategory(13),',', 
'"wnr":',getOwner(13,v.BrandID),',', 
'"clrs":[',IFNULL(getProductImages(13,v.CarrierID),''),'],',
IF(getProductPurchaseInfo(13,v.CarrierID) IS NOT NULL,CONCAT('"mrchnts":[',getProductPurchaseInfo(13,v.CarrierID),'],'),''),
'"strs":[',IFNULL(getStrs(13,v.CarrierID),''),'],',
'"attrs":[',
IFNULL(getProductDetailGroups('{"ky":"Model", "vl":"',13),'{"ky":"Model", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Model,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ModelNumber", "vl":"',13),'{"ky":"ModelNumber", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ModelNumber,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierType", "vl":"',13),'{"ky":"CarrierType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierTypeSlingsAndPouches", "vl":"',13),'{"ky":"CarrierTypeSlingsAndPouches", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierTypeSlingsAndPouches,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierTypeMeiTaiOrAsian", "vl":"',13),'{"ky":"CarrierTypeMeiTaiOrAsian", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierTypeMeiTaiOrAsian,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierTypeWrapAround", "vl":"',13),'{"ky":"CarrierTypeWrapAround", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierTypeWrapAround,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierTypeSoftCarriers", "vl":"',13),'{"ky":"CarrierTypeSoftCarriers", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierTypeSoftCarriers,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CarrierTypeFrameCarriers", "vl":"',13),'{"ky":"CarrierTypeFrameCarriers", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CarrierTypeFrameCarriers,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(SummaryCarrierType IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"SummaryCarrierType_1", "vl":"',13),SummaryCarrierType,'"},'),''),  
IF(SummaryCarrierType IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"SummaryCarrierType_2", "vl":"',13),SummaryCarrierType,'"},'),''),  
IF(SummaryCarrierType IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"SummaryCarrierType_3", "vl":"',13),SummaryCarrierType,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"BestForNewborns", "vl":"',13),'{"ky":"BestForNewborns", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForNewborns,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForToddlers", "vl":"',13),'{"ky":"BestForToddlers", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForToddlers,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForPetiteParents", "vl":"',13),'{"ky":"BestForPetiteParents", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForPetiteParents,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForTallParents", "vl":"',13),'{"ky":"BestForTallParents", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForTallParents,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForOutdoor", "vl":"',13),'{"ky":"BestForOutdoor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForOutdoor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.BestForSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"BestForSummary_1", "vl":"',13),BestForSummary,'"},'),''),  
IF(v.BestForSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"BestForSummary_2", "vl":"',13),BestForSummary,'"},'),''),  
IF(v.BestForSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"BestForSummary_3", "vl":"',13),BestForSummary,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"FeatureModern", "vl":"',13),'{"ky":"FeatureModern", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureModern,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureComfyParent", "vl":"',13),'{"ky":"FeatureComfyParent", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureComfyParent,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureGrowsWithChild", "vl":"',13),'{"ky":"FeatureGrowsWithChild", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureGrowsWithChild,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureReversible", "vl":"',13),'{"ky":"FeatureReversible", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureReversible,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureOrganic", "vl":"',13),'{"ky":"FeatureOrganic", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureOrganic,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeaturePacifierStorage", "vl":"',13),'{"ky":"FeaturePacifierStorage", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeaturePacifierStorage,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.StyleFeatureSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"StyleFeatureSummary_1", "vl":"',13),StyleFeatureSummary,'"},'),''),  
IF(v.StyleFeatureSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"StyleFeatureSummary_2", "vl":"',13),StyleFeatureSummary,'"},'),''),  
IF(v.StyleFeatureSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"StyleFeatureSummary_3", "vl":"',13),StyleFeatureSummary,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"Priority", "vl":"',13),'{"ky":"Priority", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Priority,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BBBrandRating", "vl":"',13),'{"ky":"BBBrandRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BBBrandRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BBModelRecommended", "vl":"',13),'{"ky":"BBModelRecommended", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BBModelRecommended,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BabbleRecomended", "vl":"',13),'{"ky":"BabbleRecomended", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BabbleRecomended,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"GigglesListed", "vl":"',13),'{"ky":"GigglesListed", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.GigglesListed,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AvailableAmazon", "vl":"',13),'{"ky":"AvailableAmazon", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AvailableAmazon,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ASIN", "vl":"',13),'{"ky":"ASIN", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ASIN,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AmazonBestseller", "vl":"',13),'{"ky":"AmazonBestseller", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AmazonBestseller,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AmazonCustomerRating", "vl":"',13),'{"ky":"AmazonCustomerRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AmazonCustomerRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BRUsBestseller", "vl":"',13),'{"ky":"BRUsBestseller", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BRUsBestseller,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BRUSTopRated", "vl":"',13),'{"ky":"BRUSTopRated", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BRUSTopRated,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"NAPPAAwardWinner", "vl":"',13),'{"ky":"NAPPAAwardWinner", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.NAPPAAwardWinner,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"JPMAInnovationAwardWinner", "vl":"',13),'{"ky":"JPMAInnovationAwardWinner", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.JPMAInnovationAwardWinner,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"PTPAAwardWinner", "vl":"',13),'{"ky":"PTPAAwardWinner", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.PTPAAwardWinner,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"iParentingMediaAwardWinner", "vl":"',13),'{"ky":"iParentingMediaAwardWinner", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.iParentingMediaAwardWinner,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DesignStyle", "vl":"',13),'{"ky":"DesignStyle", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DesignStyle,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BrandURL", "vl":"',13),'{"ky":"BrandURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BrandURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ModelURL", "vl":"',13),'{"ky":"ModelURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ModelURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BrandLevelMarketingPosition", "vl":"',13),'{"ky":"BrandLevelMarketingPosition", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BrandLevelMarketingPosition,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CollectionLevelMarketingPositon", "vl":"',13),'{"ky":"CollectionLevelMarketingPositon", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CollectionLevelMarketingPositon,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ProductOverview", "vl":"',13),'{"ky":"ProductOverview", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ProductOverview,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Country", "vl":"',13),'{"ky":"Country", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Country,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"JPMACertified", "vl":"',13),'{"ky":"JPMACertified", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.JPMACertified,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ASTM", "vl":"',13),'{"ky":"ASTM", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ASTM,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TUV", "vl":"',13),'{"ky":"TUV", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TUV,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Oeko_TexStandard100", "vl":"',13),'{"ky":"Oeko_TexStandard100", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Oeko_TexStandard100,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallEvent", "vl":"',13),'{"ky":"RecallEvent", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallEvent,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallURL", "vl":"',13),'{"ky":"RecallURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallDate", "vl":"',13),'{"ky":"RecallDate", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallDate,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallUnits", "vl":"',13),'{"ky":"RecallUnits", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallUnits,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallHazard", "vl":"',13),'{"ky":"RecallHazard", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallHazard,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallIncidents", "vl":"',13),'{"ky":"RecallIncidents", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallIncidents,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RecallSeverity", "vl":"',13),'{"ky":"RecallSeverity", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RecallSeverity,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ColorsNeon", "vl":"',13),'{"ky":"ColorsNeon", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ColorsNeon,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ColorsVeryBrightORPrimary", "vl":"',13),'{"ky":"ColorsVeryBrightORPrimary", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ColorsVeryBrightORPrimary,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ColorsNeutral", "vl":"',13),'{"ky":"ColorsNeutral", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ColorsNeutral,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ColorsDeepBluePurples", "vl":"',13),'{"ky":"ColorsDeepBluePurples", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ColorsDeepBluePurples,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ColorsGrayBlack", "vl":"',13),'{"ky":"ColorsGrayBlack", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ColorsGrayBlack,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TotalColors", "vl":"',13),'{"ky":"TotalColors", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TotalColors,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ManufacturerColorNames", "vl":"',13),'{"ky":"ManufacturerColorNames", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ManufacturerColorNames,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MinimumAge", "vl":"',13),'{"ky":"MinimumAge", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MinimumAge,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumAge", "vl":"',13),'{"ky":"MaximumAge", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumAge,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MinimumWeight", "vl":"',13),'{"ky":"MinimumWeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MinimumWeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumWeight", "vl":"',13),'{"ky":"MaximumWeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumWeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MinimumHeight", "vl":"',13),'{"ky":"MinimumHeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MinimumHeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumHeight", "vl":"',13),'{"ky":"MaximumHeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumHeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ParentMinimumHeight", "vl":"',13),'{"ky":"ParentMinimumHeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ParentMinimumHeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ParentMaximumHeight", "vl":"',13),'{"ky":"ParentMaximumHeight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ParentMaximumHeight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ParentMinimumWaist", "vl":"',13),'{"ky":"ParentMinimumWaist", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ParentMinimumWaist,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ParentMaximumWaist", "vl":"',13),'{"ky":"ParentMaximumWaist", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ParentMaximumWaist,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Positions", "vl":"',13),'{"ky":"Positions", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Positions,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AdjustableSeating", "vl":"',13),'{"ky":"AdjustableSeating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AdjustableSeating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MachineWashable", "vl":"',13),'{"ky":"MachineWashable", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MachineWashable,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"PaddedStraps", "vl":"',13),'{"ky":"PaddedStraps", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.PaddedStraps,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AdjustableStraps", "vl":"',13),'{"ky":"AdjustableStraps", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AdjustableStraps,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"StrapOrientation", "vl":"',13),'{"ky":"StrapOrientation", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.StrapOrientation,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"WeightDistribution", "vl":"',13),'{"ky":"WeightDistribution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.WeightDistribution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"RemovableFront", "vl":"',13),'{"ky":"RemovableFront", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.RemovableFront,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"SideEntry", "vl":"',13),'{"ky":"SideEntry", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.SideEntry,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.InfantInsert IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"InfantInsert", "vl":"',InfantInsert),v.InfantInsert,'"},'),''),
IFNULL(getProductDetailGroups('{"ky":"HeadSupport", "vl":"',13),'{"ky":"HeadSupport", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.HeadSupport,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Reversible", "vl":"',13),'{"ky":"Reversible", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Reversible,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Convertible", "vl":"',13),'{"ky":"Convertible", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Convertible,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FabricType", "vl":"',13),'{"ky":"FabricType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FabricType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"PaddedFabric", "vl":"',13),'{"ky":"PaddedFabric", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.PaddedFabric,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Ventilation", "vl":"',13),'{"ky":"Ventilation", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Ventilation,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.AdultCupHolder IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"AdultCupHolder", "vl":"',AdultCupHolder),v.AdultCupHolder,'"},'),''),
IF(v.PacifierHolder IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"PacifierHolder", "vl":"',PacifierHolder),v.PacifierHolder,'"},'),''),
IFNULL(getProductDetailGroups('{"ky":"Canopy", "vl":"',13),'{"ky":"Canopy", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Canopy,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.Bib IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"Bib", "vl":"',Bib),v.Bib,'"},'),''),
IF(v.RainCover IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"RainCover", "vl":"',RainCover),v.RainCover,'"},'),''),
IF(v.SunCover IS NOT NULL,CONCAT(getProductDetailGroupsAccessories(13,'{"ky":"SunCover", "vl":"',SunCover),v.SunCover,'"},'),''),
IFNULL(getProductDetailGroups('{"ky":"ListPrice", "vl":"',13),'{"ky":"ListPrice", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ListPrice,'\r',''),'\n',''),'\t',''),'null'),'"},',
getLowestPriceInfo(13,v.CarrierID),',',
IFNULL(getProductDetailGroups('{"ky":"ListPriceRange", "vl":"',13),'{"ky":"ListPriceRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ListPriceRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"SalePrice", "vl":"',13),'{"ky":"SalePrice", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.SalePrice,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"NewModelDiscount", "vl":"',13),'{"ky":"NewModelDiscount", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.NewModelDiscount,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"SourceURLDiscountedModel", "vl":"',13),'{"ky":"SourceURLDiscountedModel", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.SourceURLDiscountedModel,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FacebookURL", "vl":"',13),'{"ky":"FacebookURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FacebookURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BrandFacebookLikes", "vl":"',13),'{"ky":"BrandFacebookLikes", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BrandFacebookLikes,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TwitterName", "vl":"',13),'{"ky":"TwitterName", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TwitterName,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TwitterURL", "vl":"',13),'{"ky":"TwitterURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TwitterURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BrandTwitterFollowers", "vl":"',13),'{"ky":"BrandTwitterFollowers", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BrandTwitterFollowers,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"YoutubeChannelURL", "vl":"',13),'{"ky":"YoutubeChannelURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.YoutubeChannelURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"YoutubeChannelSubscribers", "vl":"',13),'{"ky":"YoutubeChannelSubscribers", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.YoutubeChannelSubscribers,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"YoutubeChannelViews", "vl":"',13),'{"ky":"YoutubeChannelViews", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.YoutubeChannelViews,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"YoutubeChannelUploadViews", "vl":"',13),'{"ky":"YoutubeChannelUploadViews", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.YoutubeChannelUploadViews,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ControlUnion", "vl":"',13),'{"ky":"ControlUnion", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ControlUnion,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CPSIACompliance", "vl":"',13),'{"ky":"CPSIACompliance", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CPSIACompliance,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"SGSCSTC", "vl":"',13),'{"ky":"SGSCSTC", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.SGSCSTC,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"EN", "vl":"',13),'{"ky":"EN", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.EN,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CelebrityOwned", "vl":"',13),'{"ky":"CelebrityOwned", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CelebrityOwned,'\r',''),'\n',''),'\t',''),'null'),'"}],',
'"mtvtrs" : {',
'"A":',IFNULL(m.A,0),',',
'"B":',IFNULL(m.B,0),',',
'"C":',IFNULL(m.C,0),',',
'"E":',IFNULL(m.E,0),',',
'"F":',IFNULL(m.F,0),',',
'"G":',IFNULL(m.G,0),',',
'"H":',IFNULL(m.H,0),',',
'"I":',IFNULL(m.I,0),',',
'"J":',IFNULL(m.J,0),',',
'"K":',IFNULL(m.K,0),',',
'"L":',IFNULL(m.L,0),'',
'}}'),'"null"','null')) AS payload   
FROM vw_carrier_data_ui v
JOIN vw_carrier_motivators m
  ON v.CarrierID = m.ItemID AND
  m.CategoryID = v.CategoryID
WHERE v.CarrierID = 58
AND getDefaultProductImage(13,v.CarrierID) IS NOT NULL 
AND getLowestPriceInfo(13,v.CarrierID) IS NOT NULL
