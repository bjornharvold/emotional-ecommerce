select
General_TimerFunctions,
Display_ViewingAngle,
Display_DisplayMenuLanguage,
ifnull(length(Display_DisplayMenuLanguage) - length(replace(Display_DisplayMenuLanguage, ',', '')) + 1, 0) as Display_DisplayMenuLanguage_Count,
Display_ViewingAngleVertical,
VideoFeatures_ExtendedDataServiceXDS,
VideoFeatures_ClosedCaptionCapability,
AudioSystem_AudioControls,
AudioSystem_Equalizer,
StandsMounts_WallMountIncluded,
CRRemoteEaseOfUse,
CROnScreenMenuEaseOfUse,
CRViewingAngle,
StandsMounts_StandFeatures,
StandsMounts_Stand,
NetworkInternetMultimedia_Connectivity
from vw_televisions_data