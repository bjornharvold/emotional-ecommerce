package com.lela.commons.test.service;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.impl.SyndicateServiceImpl;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.Syndicatable;
import com.lela.domain.document.Blog;
import com.lela.domain.enums.PublishStatus;

@RunWith(MockitoJUnitRunner.class)
public class SyndicateServiceUnitTest {

	@Mock BlogService blogService;
	
	@InjectMocks private SyndicateServiceImpl syndicateService;

	private Page<Blog> blogPage = null;
	private Date today = new DateTime().toDate();
	private Date yesterday = new DateTime().minusDays(1).toDate();
	private Date dayBefore = new DateTime().minusDays(2).toDate();

	@Before
	public  void beforeEach(){
		List<Blog> blogList = new ArrayList<Blog>();
		Blog first = new Blog();
		first.setCdt(today);
		first.setCntn("A First String this long");
		first.setNm("1a First Name");
		first.setRlnm("firstUrl");
		first.setStts(PublishStatus.PUBLISHED);
		blogList.add(first);
		
		Blog second = new Blog();
		second.setCdt(yesterday);
		second.setCntn("A Second String this long");
		second.setNm("2a Second Name");
		second.setRlnm("secondUrl");
		second.setStts(PublishStatus.UNPUBLISHED);
		blogList.add(second);
		
		Blog third = new Blog();
		third.setCdt(dayBefore);
		third.setCntn("A Third String.");
		third.setNm("3a Third Name");
		third.setRlnm("thirdUrl");
		third.setStts(PublishStatus.PUBLISHED);
		blogList.add(third);
		
		blogPage = new PageImpl<Blog>(blogList);
	}
	
	@Test
	public void testSortOfSyndicatable(){
		when(blogService.findBlogs(anyInt(), anyInt())).thenReturn(blogPage);
		List<Syndicatable> syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 100, 100, true, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three blogs not returned", syndicatableList.size() == 3);
		Assert.assertTrue("The sort order is incorrect", syndicatableList.get(0).getPublishDate().equals(dayBefore));
		
		
		//reverse
		syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 100, 100, false, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three blogs not returned", syndicatableList.size() == 3);
		Assert.assertTrue("The sort order is incorrect", syndicatableList.get(0).getPublishDate().equals(today));
		
		
		//title sort
		syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_TITLE, 100, 100, true, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three blogs not returned", syndicatableList.size() == 3);
		Assert.assertTrue("The sort order by title is incorrect", syndicatableList.get(0).getTitle().startsWith("1"));
		
		//reverse
		syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_TITLE, 100, 100, false, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three blogs not returned", syndicatableList.size() == 3);
		Assert.assertTrue("The sort order by title is incorrect", syndicatableList.get(0).getTitle().startsWith("3"));
	}
	
	@Test
	public void testTruncationOfSyndicatable(){
		when(blogService.findBlogs(anyInt(), anyInt())).thenReturn(blogPage);
		List<Syndicatable> syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 100, 17, false, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three blogs not returned", syndicatableList.size() == 3);
		Assert.assertTrue("Ellipsis not specified on truncated syndicted content", syndicatableList.get(0).getContent().endsWith(WebConstants.ELLIPSIS)); //first
		Assert.assertTrue("Ellipsis specified on non truncated syndicted content", !syndicatableList.get(2).getContent().endsWith(WebConstants.ELLIPSIS));//third
	}
	
	@Test
	public void testNumberOfPublished(){
		when(blogService.findBlogs(anyInt(), anyInt())).thenReturn(blogPage);
		List<Syndicatable> syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 100, 17, false, true);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Two published blogs not returned", syndicatableList.size() == 2);
		
		syndicatableList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 100, 17, false, false);
		
		Assert.assertNotNull(syndicatableList);
		Assert.assertTrue("Three published or unpublished blogs not returned", syndicatableList.size() == 3);
	}
}
