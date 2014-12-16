ALTER VIEW
    vw_televisions_data
 AS
SELECT
    `td`.`TV_ID`              AS `TV_ID`,
    `td`.`ProdID`             AS `ProdID`,
    `b`.`BrandID`             AS `BrandID`,
    `b`.`BrandName`           AS `BrandName`,
    `td`.`Model`              AS `Model`,
    `td`.`ModelNumber`        AS `ModelNumber`,
    `td`.`CNETAdjModelNumber` AS `CNETAdjModelNumber`,
    `td`.`CNETModelNo`        AS `CNETModelNo`,
    `td`.`ProductOverview`    AS `ProductOverview`,
    `td`.`Validated`          AS `Validated`,
    `td`.`Priority`           AS `Priority`,
    `td`.`AvailableAmazon`    AS `AvailableAmazon`,
    (
        CASE
            WHEN (locate('amzn.com/',`td`.`AvailableAmazon`) <> 0)
            THEN SUBSTR(`td`.`AvailableAmazon`,(locate('amzn.com/',`td`.`AvailableAmazon`) + 9),10)
        END)                                                  AS `ASIN`,
    `td`.`Description`                                        AS `Description`,
    `td`.`DesignStyle`                                        AS `DesignStyle`,
    `td`.`BrandURL`                                           AS `BrandURL`,
    `td`.`ModelURL`                                           AS `ModelURL`,
    `td`.`CNETMostPopular`                                    AS `CNETMostPopular`,
    `td`.`CNETURL`                                            AS `CNETURL`,
    `td`.`CNETEditorsRating`                                  AS `CNETEditorsRating`,
    `td`.`CNETDesign`                                         AS `CNETDesign`,
    `td`.`CNETFeatures`                                       AS `CNETFeatures`,
    `td`.`CNETPerformance`                                    AS `CNETPerformance`,
    `td`.`AmazonMostPopular`                                  AS `AmazonMostPopular`,
    `td`.`BuycomMostPopular`                                  AS `BuycomMostPopular`,
    `td`.`BestBuyBestSelling`                                 AS `BestBuyBestSelling`,
    `td`.`WalmartBestSelling`                                 AS `WalmartBestSelling`,
    `td`.`CRModelURL`                                         AS `CRModelURL`,
    `td`.`CRTested`                                           AS `CRTested`,
    `td`.`CROverallScore`                                     AS `CROverallScore`,
    `td`.`CR3DPerformance`                                    AS `CR3DPerformance`,
    `td`.`CRHDPictureQuality`                                 AS `CRHDPictureQuality`,
    `td`.`CRSDPictureQuality`                                 AS `CRSDPictureQuality`,
    `td`.`CRSoundQuality`                                     AS `CRSoundQuality`,
    `td`.`CRRemoteEaseOfUse`                                  AS `CRRemoteEaseOfUse`,
    `td`.`CROnScreenMenuEaseOfUse`                            AS `CROnScreenMenuEaseOfUse`,
    `td`.`CRVersatility`                                      AS `CRVersatility`,
    `td`.`CRViewingAngle`                                     AS `CRViewingAngle`,
    `td`.`CREnergyCostYr`                                     AS `CREnergyCostYr`,
    `td`.`CRLEDBacklight`                                     AS `CRLEDBacklight`,
    `td`.`ListPrice`                                          AS `ListPrice`,
    `td`.`ListPriceRange`                                     AS `ListPriceRange`,
    `l`.`General_ProductType`                                 AS `General_ProductType`,
    `l`.`Display_DiagonalSize`                                AS `Display_DiagonalSize`,
    `l`.`General_Width`                                       AS `General_Width`,
    `l`.`General_Depth`                                       AS `General_Depth`,
    `l`.`General_Height`                                      AS `General_Height`,
    `l`.`General_Weight`                                      AS `General_Weight`,
    `l`.`NetworkInternetMultimedia_Functionality`      AS `NetworkInternetMultimedia_Functionality`,
    `l`.`NetworkInternetMultimedia_NetworkProtocolsSupported` AS
    `NetworkInternetMultimedia_NetworkProtocolsSupported`,
    `l`.`NetworkInternetMultimedia_Connectivity`      AS `NetworkInternetMultimedia_Connectivity`,
    `l`.`General_3DTechnology`                        AS `General_3DTechnology`,
    `l`.`3DTechnology`                                AS `3DTechnology`,
    `l`.`RemoteControl_Type`                          AS `RemoteControl_Type`,
    `l`.`General_Combinedwith`                        AS `General_Combinedwith`,
    `l`.`Display_DisplayFormat`                       AS `Display_DisplayFormat`,
    `l`.`Display_MotionEnhancementTechnology`         AS `Display_MotionEnhancementTechnology`,
    `l`.`SoundOutputMode`                             AS `SoundOutputMode`,
    `l`.`AudioSystem_SoundOutputMode`                 AS `AudioSystem_SoundOutputMode`,
    `l`.`DVR_DVR`                                     AS `DVR_DVR`,
    `l`.`VideoFeatures_ParentalChannelLock`           AS `VideoFeatures_ParentalChannelLock`,
    `l`.`Display_V-ChipControl`                       AS `Display_V-ChipControl`,
    `ls`.`Miscellaneous/Refurbished`                  AS `Miscellaneous/Refurbished`,
    `ls`.`Television/ParentalChannelLock`             AS `Television/ParentalChannelLock`,
    `ls`.`Television/Technology`                      AS `Television/Technology`,
    `ls`.`Television/Resolution`                      AS `Television/Resolution`,
    `ls`.`Network_InternetMultimedia/Connectivity`    AS `Network_InternetMultimedia/Connectivity`,
    `ls`.`Television/HDCPCompatible`                  AS `Television/HDCPCompatible`,
    `ls`.`Television/LCDBacklightTechnology`          AS `Television/LCDBacklightTechnology`,
    `ls`.`Television/DisplayFormat`                   AS `Television/DisplayFormat`,
    `ls`.`EnvironmentalStandards/ENERGYSTARQualified` AS
    `EnvironmentalStandards/ENERGYSTARQualified`,
    `l`.`EnvironmentalStandards_ENERGYSTARVersion`   AS `EnvironmentalStandards_ENERGYSTARVersion`,
    `ls`.`Television/HDMIPortsQty`                   AS `Television/HDMIPortsQty`,
    `l`.`Display_ColorTemperatureControl`            AS `Display_ColorTemperatureControl`,
    `l`.`RemoteControl_Features`                     AS `RemoteControl_Features`,
    `l`.`RemoteControl_SupportedDevices`             AS `RemoteControl_SupportedDevices`,
    `l`.`Display_AdditionalFeatures`                 AS `Display_AdditionalFeatures`,
    `l`.`MediaPlayer_USBPort`                        AS `MediaPlayer_USBPort`,
    `l`.`MediaPlayer_SupportedFlashMemoryCards`      AS `MediaPlayer_SupportedFlashMemoryCards`,
    `l`.`Power_PowerConsumptionStandbySleep`         AS `Power_PowerConsumptionStandbySleep`,
    `l`.`ManufacturerWarranty_ServiceSupport`        AS `ManufacturerWarranty_ServiceSupport`,
    `l`.`General_Bundledwith`                        AS `General_Bundledwith`,
    `l`.`General_PCInterface`                        AS `General_PCInterface`,
    `l`.`VideoFeatures_FreezeMemo`                   AS `VideoFeatures_FreezeMemo`,
    `l`.`MediaPlayer_SupportedAudioFormats`          AS `MediaPlayer_SupportedAudioFormats`,
    `l`.`MediaPlayer_SupportedPicturesFormats`       AS `MediaPlayer_SupportedPicturesFormats`,
    `l`.`MediaPlayer_SupportedVideoFormats`          AS `MediaPlayer_SupportedVideoFormats`,
    `l`.`AudioSystem_SpeakerSystem`                  AS `AudioSystem_SpeakerSystem`,
    `l`.`General_TimerFunctions`                     AS `General_TimerFunctions`,
    `l`.`Display_ViewingAngle`                       AS `Display_ViewingAngle`,
    `l`.`Display_DisplayMenuLanguage`                AS `Display_DisplayMenuLanguage`,
    `l`.`Display_ViewingAngleVertical`               AS `Display_ViewingAngleVertical`,
    `l`.`VideoFeatures_ExtendedDataServiceXDS`       AS `VideoFeatures_ExtendedDataServiceXDS`,
    `l`.`VideoFeatures_ClosedCaptionCapability`      AS `VideoFeatures_ClosedCaptionCapability`,
    `l`.`AudioSystem_AudioControls`                  AS `AudioSystem_AudioControls`,
    `l`.`AudioSystem_Equalizer`                      AS `AudioSystem_Equalizer`,
    `l`.`StandsMounts_WallMountIncluded`             AS `StandsMounts_WallMountIncluded`,
    `l`.`StandsMounts_StandFeatures`                 AS `StandsMounts_StandFeatures`,
    `l`.`StandsMounts_Stand`                         AS `StandsMounts_Stand`,
    `l`.`Power_PowerConsumptionOperational`          AS `Power_PowerConsumptionOperational`,
    `l`.`EnvironmentalStandards_ENERGYSTARQualified` AS
    `EnvironmentalStandards_ENERGYSTARQualified`,
    `l`.`Display_PixelResponseTime`          AS `Display_PixelResponseTime`,
    `ls`.`Television/ImageAspectRatio`       AS `Television/ImageAspectRatio`,
    `ls`.`Television/Technology`             AS `Television_Technology`,
    `ls`.`Television/DiagonalSize`           AS `Television_DiagonalSize`,
    `ls`.`Television/Type`                   AS `Television_Type`,
    `ls`.`Television/Resolution`             AS `Television_Resolution`,
    `ls`.`Television/HDCPCompatible`         AS `Television_HDCPCompatible`,
    `ls`.`Television/LCDBacklightTechnology` AS `Television_LCDBacklightTechnology`,
    `ls`.`Television/DisplayFormat`          AS `Television_DisplayFormat`,
    `ls`.`Television/HDMIPortsQty`           AS `Television_HDMIPortsQty`,
    `ls`.`Television/ImageAspectRatio`       AS `Television_ImageAspectRatio`,
    `ls`.`Television/ParentalChannelLock`             AS `Television_ParentalChannelLock`,
    `l`.`Display_V-ChipControl`                       AS `Display_V_ChipControl`
FROM
    (((`tv_data` `td`
JOIN
    `television_load_extended` `l`
ON
    ((
            `td`.`ProdID` = `l`.`ProdID`)))
JOIN
    `television_load` `ls`
ON
    ((
            `td`.`ProdID` = `ls`.`ProdID`)))
JOIN
    `brand` `b`
ON
    ((
            `b`.`BrandName` = `td`.`Brand`)))
WHERE
    (
        `td`.`Priority` IN ('H-Pilot',
                            'M-Pilot'))