SET SESSION group_concat_max_len = 2000000;

SELECT 
GROUP_CONCAT(REPLACE(CONCAT('{"nm":"',v.Model, '",',
'"lcl":"en_US",',
'"tp":"PRODUCT",',
'"tld":"',IFNULL(v.CameraID,'null'),'",',
'"rlnm":"',v.URLName,'",',
IF(v.objectID IS NOT NULL,CONCAT('"id":"',v.objectID,'",'),'"id": null,'),
'"sbttrs":[',
'{"ky":"Brand", "vl":"',IFNULL(v.Brand,'null'),'"},',
'{"ky":"ProductModelName", "vl":"',IFNULL(v.Model,'null'),'"},',
'{"ky":"ListPrice", "vl":',IFNULL(v.AmazonListPrice,'null'),'},',
getLowestPriceInfo_CNET(20,v.CameraID),',',
'{"ky":"ProductImageUrl", "vl":"',IFNULL(getDefaultProductImage_CNET(20,v.CameraID),'null'),'"}',
'],',
'"ctgry":',getCategory(20),',', 
'"wnr":',getOwner(20,v.BrandID),',', 
'"clrs":[',IFNULL(getProductImages_CNET(20,v.CameraID),''),'],',
IF(getProductPurchaseInfo_CNET(20,v.CameraID) IS NOT NULL,CONCAT('"mrchnts":[',getProductPurchaseInfo_CNET(20,v.CameraID),'],'),''),
'"strs":[',IFNULL(getStrs_CNET(20,v.CameraID),''),'],',
'"attrs":[',
IFNULL(getProductDetailGroups('{"ky":"CameraType", "vl":"',20),'{"ky":"CameraType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraTypeCompact", "vl":"',20),'{"ky":"CameraTypeCompact", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraTypeCompact,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraTypeILC", "vl":"',20),'{"ky":"CameraTypeILC", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraTypeILC,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(SummaryCameraType IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"SummaryCameraType_1", "vl":"',20),SummaryCameraType,'"},'),''),  
IF(SummaryCameraType IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"SummaryCameraType_2", "vl":"',20),SummaryCameraType,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"BestForIndoor", "vl":"',20),'{"ky":"BestForIndoor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForIndoor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForNature", "vl":"',20),'{"ky":"BestForNature", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForNature,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForTravel", "vl":"',20),'{"ky":"BestForTravel", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForTravel,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForLandscape", "vl":"',20),'{"ky":"BestForLandscape", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForLandscape,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForCloseups", "vl":"',20),'{"ky":"BestForCloseups", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForCloseups,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForBaby", "vl":"',20),'{"ky":"BestForBaby", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForBaby,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForSnowBeach", "vl":"',20),'{"ky":"BestForSnowBeach", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForSnowBeach,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BestForKids", "vl":"',20),'{"ky":"BestForKids", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BestForKids,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.BestForSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"BestForSummary_1", "vl":"',20),BestForSummary,'"},'),''),  
IF(v.BestForSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"BestForSummary_2", "vl":"',20),BestForSummary,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"FeatureHighImageQuality", "vl":"',20),'{"ky":"FeatureHighImageQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureHighImageQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureLargeZoomRange", "vl":"',20),'{"ky":"FeatureLargeZoomRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureLargeZoomRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureHighVideoQuality", "vl":"',20),'{"ky":"FeatureHighVideoQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureHighVideoQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureLargeLCDSize", "vl":"',20),'{"ky":"FeatureLargeLCDSize", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureLargeLCDSize,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Feature3DCapability", "vl":"',20),'{"ky":"Feature3DCapability", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Feature3DCapability,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureRuggedized", "vl":"',20),'{"ky":"FeatureRuggedized", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureRuggedized,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureRakes", "vl":"',20),'{"ky":"FeatureRakes", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureRakes,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FeatureRechargeableBattery", "vl":"',20),'{"ky":"FeatureRechargeableBattery", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FeatureRechargeableBattery,'\r',''),'\n',''),'\t',''),'null'),'"},',
IF(v.FeatureSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"FeatureSummary_1", "vl":"',20),FeatureSummary,'"},'),''),  
IF(v.FeatureSummary IS NOT NULL,CONCAT(getProductDetailGroups('{"ky":"FeatureSummary_2", "vl":"',20),FeatureSummary,'"},'),''),  
IFNULL(getProductDetailGroups('{"ky":"DesignStyle", "vl":"',20),'{"ky":"DesignStyle", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DesignStyle,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETModelUrl", "vl":"',20),'{"ky":"CNETModelUrl", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETModelUrl,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETMostPopular", "vl":"',20),'{"ky":"CNETMostPopular", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETMostPopular,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETEditorRating", "vl":"',20),'{"ky":"CNETEditorRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETEditorRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETUserRating", "vl":"',20),'{"ky":"CNETUserRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETUserRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETNoReviews", "vl":"',20),'{"ky":"CNETNoReviews", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETNoReviews,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETDesign", "vl":"',20),'{"ky":"CNETDesign", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETDesign,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETFeatures", "vl":"',20),'{"ky":"CNETFeatures", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETFeatures,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETPerformance", "vl":"',20),'{"ky":"CNETPerformance", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETPerformance,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CNETImageQuality", "vl":"',20),'{"ky":"CNETImageQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CNETImageQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AmazonListPrice", "vl":"',20),'{"ky":"AmazonListPrice", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AmazonListPrice,'\r',''),'\n',''),'\t',''),'null'),'"},',
getLowestPriceInfo_CNET(20,v.CameraID),',',
IFNULL(getProductDetailGroups('{"ky":"dpreviewModelURL", "vl":"',20),'{"ky":"dpreviewModelURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.dpreviewModelURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"dpreviewRating", "vl":"',20),'{"ky":"dpreviewRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.dpreviewRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"dpreviewPct", "vl":"',20),'{"ky":"dpreviewPct", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.dpreviewPct,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRModelURL", "vl":"',20),'{"ky":"CRModelURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRModelURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRTested", "vl":"',20),'{"ky":"CRTested", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRTested,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CROverallScore", "vl":"',20),'{"ky":"CROverallScore", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CROverallScore,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CReportsFlashPhotos", "vl":"',20),'{"ky":"CReportsFlashPhotos", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CReportsFlashPhotos,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRVersatility", "vl":"',20),'{"ky":"CRVersatility", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRVersatility,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRBatteryLife", "vl":"',20),'{"ky":"CRBatteryLife", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRBatteryLife,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRVideoQuality", "vl":"',20),'{"ky":"CRVideoQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRVideoQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRLCDQuality", "vl":"',20),'{"ky":"CRLCDQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRLCDQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CRImageQuality", "vl":"',20),'{"ky":"CRImageQuality", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CRImageQuality,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CREaseofUse", "vl":"',20),'{"ky":"CREaseofUse", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CREaseofUse,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TechliciousRated", "vl":"',20),'{"ky":"TechliciousRated", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TechliciousRated,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BHMostPopular", "vl":"',20),'{"ky":"BHMostPopular", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BHMostPopular,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BHPhotoRating", "vl":"',20),'{"ky":"BHPhotoRating", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BHPhotoRating,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BabbleRecommended", "vl":"',20),'{"ky":"BabbleRecommended", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BabbleRecommended,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FacebookURL", "vl":"',20),'{"ky":"FacebookURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FacebookURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TwitterURL", "vl":"',20),'{"ky":"TwitterURL", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TwitterURL,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"TwitterName", "vl":"',20),'{"ky":"TwitterName", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.TwitterName,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AmazonASIN", "vl":"',20),'{"ky":"AmazonASIN", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AmazonASIN,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Description", "vl":"',20),'{"ky":"Description", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Description,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ProductDescription", "vl":"',20),'{"ky":"ProductDescription", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ProductDescription,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ProductOverview", "vl":"',20),'{"ky":"ProductOverview", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ProductOverview,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FocusAdjustment", "vl":"',20),'{"ky":"FocusAdjustment", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FocusAdjustment,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ImageProcessor", "vl":"',20),'{"ky":"ImageProcessor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ImageProcessor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ImageStabType", "vl":"',20),'{"ky":"ImageStabType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ImageStabType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"IncludedMemoryCard", "vl":"',20),'{"ky":"IncludedMemoryCard", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.IncludedMemoryCard,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"InternalStorage", "vl":"',20),'{"ky":"InternalStorage", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.InternalStorage,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LANandWirelessImageTransferringProtocols", "vl":"',20),'{"ky":"LANandWirelessImageTransferringProtocols", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LANandWirelessImageTransferringProtocols,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem", "vl":"',20),'{"ky":"LensSystem", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumVideoResolution", "vl":"',20),'{"ky":"MaximumVideoResolution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumVideoResolution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MinFocusRange", "vl":"',20),'{"ky":"MinFocusRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MinFocusRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ProductType", "vl":"',20),'{"ky":"ProductType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ProductType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"SensorResolution", "vl":"',20),'{"ky":"SensorResolution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.SensorResolution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"BatteryType", "vl":"',20),'{"ky":"BatteryType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.BatteryType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AdditionalFeatures", "vl":"',20),'{"ky":"AdditionalFeatures", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AdditionalFeatures,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AdditionalFeatures_FlashTerminal", "vl":"',20),'{"ky":"AdditionalFeatures_FlashTerminal", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AdditionalFeatures_FlashTerminal,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_EffectiveFlashRange", "vl":"',20),'{"ky":"CameraFlash_EffectiveFlashRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_EffectiveFlashRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_Features", "vl":"',20),'{"ky":"CameraFlash_Features", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_Features,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_FlashModes", "vl":"',20),'{"ky":"CameraFlash_FlashModes", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_FlashModes,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_GuideNumbermISO100", "vl":"',20),'{"ky":"CameraFlash_GuideNumbermISO100", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_GuideNumbermISO100,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"OutputConnections", "vl":"',20),'{"ky":"OutputConnections", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.OutputConnections,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ScreenResolution", "vl":"',20),'{"ky":"ScreenResolution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ScreenResolution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"General_BodyMaterial", "vl":"',20),'{"ky":"General_BodyMaterial", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.General_BodyMaterial,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem2nd_Features", "vl":"',20),'{"ky":"LensSystem2nd_Features", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem2nd_Features,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_Features", "vl":"',20),'{"ky":"LensSystem_Features", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_Features,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_LensManufacturer", "vl":"',20),'{"ky":"LensSystem_LensManufacturer", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_LensManufacturer,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AutoType", "vl":"',20),'{"ky":"AutoType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AutoType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ContinuousShootSpeed", "vl":"',20),'{"ky":"ContinuousShootSpeed", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ContinuousShootSpeed,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MainFeatures_FaceDetection", "vl":"',20),'{"ky":"MainFeatures_FaceDetection", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MainFeatures_FaceDetection,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MainFeatures_X_syncSpeed", "vl":"',20),'{"ky":"MainFeatures_X_syncSpeed", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MainFeatures_X_syncSpeed,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DimensionsWxDxH", "vl":"',20),'{"ky":"DimensionsWxDxH", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DimensionsWxDxH,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Weight", "vl":"',20),'{"ky":"Weight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Weight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_FocalLengthEquivalentto35mmCamera", "vl":"',20),'{"ky":"LensSystem_FocalLengthEquivalentto35mmCamera", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_FocalLengthEquivalentto35mmCamera,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_MacroFocusRange", "vl":"',20),'{"ky":"LensSystem_MacroFocusRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_MacroFocusRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AudioInput_MicrophoneOperationMode", "vl":"',20),'{"ky":"AudioInput_MicrophoneOperationMode", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AudioInput_MicrophoneOperationMode,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"AudioInput_Type", "vl":"',20),'{"ky":"AudioInput_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.AudioInput_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Battery_BatteryFormFactor", "vl":"',20),'{"ky":"Battery_BatteryFormFactor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Battery_BatteryFormFactor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Battery_SupportedBatteryFormFactor", "vl":"',20),'{"ky":"Battery_SupportedBatteryFormFactor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Battery_SupportedBatteryFormFactor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_RedEyeReduction", "vl":"',20),'{"ky":"CameraFlash_RedEyeReduction", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_RedEyeReduction,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"CameraFlash_Type", "vl":"',20),'{"ky":"CameraFlash_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.CameraFlash_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DigitalCamera_DigitalCameraType", "vl":"',20),'{"ky":"DigitalCamera_DigitalCameraType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DigitalCamera_DigitalCameraType,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DigitalCamera_SupportedFlashMemory", "vl":"',20),'{"ky":"DigitalCamera_SupportedFlashMemory", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DigitalCamera_SupportedFlashMemory,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Dimensions_Weight_Depth", "vl":"',20),'{"ky":"Dimensions_Weight_Depth", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Dimensions_Weight_Depth,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Dimensions_Weight_Height", "vl":"',20),'{"ky":"Dimensions_Weight_Height", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Dimensions_Weight_Height,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Dimensions_Weight_Weight", "vl":"',20),'{"ky":"Dimensions_Weight_Weight", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Dimensions_Weight_Weight,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Dimensions_Weight_Width", "vl":"',20),'{"ky":"Dimensions_Weight_Width", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Dimensions_Weight_Width,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"ScreenSize", "vl":"',20),'{"ky":"ScreenSize", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.ScreenSize,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DisplayProjector_DiagonalSizemetric", "vl":"',20),'{"ky":"DisplayProjector_DiagonalSizemetric", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DisplayProjector_DiagonalSizemetric,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DisplayProjector_DisplayFormFactor", "vl":"',20),'{"ky":"DisplayProjector_DisplayFormFactor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DisplayProjector_DisplayFormFactor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"DisplayProjector_Type", "vl":"',20),'{"ky":"DisplayProjector_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.DisplayProjector_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FlashMemory_FormFactor", "vl":"',20),'{"ky":"FlashMemory_FormFactor", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FlashMemory_FormFactor,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"FlashMemory_StorageCapacity", "vl":"',20),'{"ky":"FlashMemory_StorageCapacity", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.FlashMemory_StorageCapacity,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensApertureRange", "vl":"',20),'{"ky":"LensApertureRange", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensApertureRange,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_OpticalZoom", "vl":"',20),'{"ky":"LensSystem_OpticalZoom", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_OpticalZoom,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"OpticalZoom", "vl":"',20),'{"ky":"OpticalZoom", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.OpticalZoom,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"LensSystem_Type", "vl":"',20),'{"ky":"LensSystem_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.LensSystem_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumImageResolution", "vl":"',20),'{"ky":"MaximumImageResolution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumImageResolution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"OpticalSensor_SensorResolution", "vl":"',20),'{"ky":"OpticalSensor_SensorResolution", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.OpticalSensor_SensorResolution,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"OpticalViewfinder_Type", "vl":"',20),'{"ky":"OpticalViewfinder_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.OpticalViewfinder_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"VideoInput_DigitalZoom", "vl":"',20),'{"ky":"VideoInput_DigitalZoom", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.VideoInput_DigitalZoom,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"MaximumISO", "vl":"',20),'{"ky":"MaximumISO", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.MaximumISO,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"VideoInput_LightSensitivity", "vl":"',20),'{"ky":"VideoInput_LightSensitivity", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.VideoInput_LightSensitivity,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"VideoInput_StillImageFormat", "vl":"',20),'{"ky":"VideoInput_StillImageFormat", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.VideoInput_StillImageFormat,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"VideoInput_Type", "vl":"',20),'{"ky":"VideoInput_Type", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.VideoInput_Type,'\r',''),'\n',''),'\t',''),'null'),'"},',
IFNULL(getProductDetailGroups('{"ky":"Viewfinder_ViewfinderType", "vl":"',20),'{"ky":"Viewfinder_ViewfinderType", "vl":"'),IFNULL(REPLACE(REPLACE(REPLACE(v.Viewfinder_ViewfinderType,'\r',''),'\n',''),'\t',''),'null'),'"}],',
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
FROM vw_cameras_data_ui v
JOIN vw_camera_motivators m
  ON v.CameraID = m.ItemID AND
  m.CategoryID = v.CategoryID
WHERE v.CameraID = 20
AND getLowestPriceInfo_CNET(20,v.CameraID) IS NOT NULL

