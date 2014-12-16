package com.lela.commons.test.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.lela.domain.document.AffiliateAccount;
import com.lela.domain.dto.AffiliateAccountAndImage;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import com.lela.commons.repository.AffiliateAccountRepository;
import com.lela.commons.service.CacheService;
import com.lela.commons.service.impl.AffiliateServiceImpl;
import com.lela.domain.document.Campaign;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;

@RunWith(MockitoJUnitRunner.class)
public class AffiliateServiceUnitTests {
    @Mock private  AffiliateAccountRepository affiliateAccountRepository;
    @Mock private  FileStorage fileStorage;
    @Mock private  CacheService cacheService;
    @Spy private AffiliateAccountAndImage affiliateAccountAndImage;
    @Mock private  MultipartFile multipartFile;
    
    @InjectMocks private AffiliateServiceImpl affiliateService;
    
    private AffiliateAccount affiliate;
    
    @Before
    public void beforeEach(){
    	//this.campaignService = new AffiliateServiceImpl(cacheService, campaignRepository, fileStorage);
    	this.affiliate = new AffiliateAccount();
    	this.affiliate.setRlnm("test");
    	this.affiliateAccountAndImage.setAffiliate(affiliate);
    }
    
    @Test
    public void testSaveCampaignWithBannerImage(){
    	AffiliateAccount affiliateWithImage = new AffiliateAccount();
    	affiliateWithImage.setBnrmgrl("aUrl");
        AffiliateAccount affiliateWithNewImage = new AffiliateAccount();
    	affiliateWithNewImage.setBnrmgrl("newUrl");

    	when(affiliateAccountAndImage.getBannerImageFile()).thenReturn(multipartFile);
    	when(multipartFile.isEmpty()).thenReturn(false);
    	when(affiliateAccountRepository.findByUrlName(anyString())).thenReturn(affiliateWithImage);
    	when(fileStorage.storeFile(any(FileData.class))).thenReturn("newUrl");
    	when(affiliateAccountRepository.save(any(AffiliateAccount.class))).thenReturn(affiliateWithNewImage);

    	affiliateService.saveAffiliateAccountAndImage(affiliateAccountAndImage);

    	verify(fileStorage).storeFile(any(FileData.class));
    	Assert.assertTrue(affiliateAccountAndImage.getAffiliate().getBnrmgrl().equals("newUrl"));
    	
    }
    
    @Test
    public void testSaveCampaignWithNoBannerImageDeleteOfExistingImage(){
    	affiliateAccountAndImage.getAffiliate().setBnrmgrl("anExistingUrl");
    	when(affiliateAccountAndImage.getBannerImageFile()).thenReturn(null);
    	when(affiliateAccountAndImage.isDeleteImage()).thenReturn(true);
    	
    	affiliateService.saveAffiliateAccountAndImage(affiliateAccountAndImage);
    	verify(fileStorage).removeObjectFromBucket(anyString(), anyString());
    	verify(affiliateAccountRepository).save(any(AffiliateAccount.class));
    	  	
    }
    
    @Test
    public void testSaveCampaignWithNoBannerImageAndNoExistingImage(){
    	affiliateAccountAndImage.getAffiliate().setBnrmgrl(null);
    	when(affiliateAccountAndImage.getBannerImageFile()).thenReturn(null);
    	when(affiliateAccountAndImage.isDeleteImage()).thenReturn(true);
    	
    	affiliateService.saveAffiliateAccountAndImage(affiliateAccountAndImage);
    	verify(fileStorage, never()).removeObjectFromBucket(anyString(), anyString());
    	verify(affiliateAccountRepository).save(any(AffiliateAccount.class));
    	  	
    }
}
