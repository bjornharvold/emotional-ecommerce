package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lela.commons.service.BlogService;
import com.lela.commons.service.SyndicateService;
import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.Syndicatable;
import com.lela.domain.document.Blog;
import com.lela.domain.enums.PublishStatus;

@Service("syndicateService")
public class SyndicateServiceImpl implements SyndicateService {

	private final BlogService blogService;
	
	@Autowired
	public SyndicateServiceImpl(BlogService blogService){
		this.blogService = blogService;
	}
	
	/**
	 * @see SyndicateService
	 */
	@Override
	public List<Syndicatable> getSyndicateList(final String sortBy, final int numberOfSyndicatablesToFetch, final int stringLengthOfInitialContent, final boolean ascending, final boolean onlyPublished) { 

		Page<Blog> blogPage = blogService.findBlogs(0, Integer.MAX_VALUE); //get all blogs to sort
		List<Syndicatable> syndicateList = new ArrayList<Syndicatable>();
		
		
		//Convert to list and truncate if necessary
		for (Blog blog : blogPage) {
			if (onlyPublished){
				if (blog.getStts() == PublishStatus.PUBLISHED){
					syndicateList.add(truncate(blog, stringLengthOfInitialContent));
				}
			} else {
				syndicateList.add(truncate(blog, stringLengthOfInitialContent));
			}
		}
		
		//sort
		Collections.sort(syndicateList, new Comparator<Syndicatable>() {

			@Override
			public int compare(Syndicatable o1, Syndicatable o2) {
				int ret = 0;
				if (Syndicatable.SORT_BY_DATE.equals(sortBy)){
					if ((o1.getPublishDate() != null) && (o2.getPublishDate() != null)){
						ret =  (ascending?1:-1) * (o1.getPublishDate().compareTo(o2.getPublishDate()));
					}
				}
				if (Syndicatable.SORT_BY_TITLE.equals(sortBy)){
					if (!StringUtils.isEmpty(o1.getTitle()) && !StringUtils.isEmpty(o2.getTitle())){
						ret =  (ascending?1: -1) * (o1.getTitle().compareTo(o2.getTitle()));
					}
				}
				return ret;
			}

		});
		
		//Now show only the number requested
		List<Syndicatable> finalList = new ArrayList<Syndicatable>();
		int count = 0;
		for (Syndicatable syndicatable : syndicateList) {
			if (count < numberOfSyndicatablesToFetch) {
				finalList.add(syndicatable);
				count++;
			}
		}

		
		return finalList;
	}
	
	/**
	 * @see SyndicateService
	 */
	@Override
	public Syndicatable getSyndicatableByIdentifier(String id){
		return blogService.findBlogByUrlName(id);
	}
	
	private Syndicatable truncate(Blog blog, int stringLengthOfInitialContent){
		if (!StringUtils.isEmpty(blog.getContent())){
			if (blog.getContent().length() > stringLengthOfInitialContent){
				String text = Jsoup.parse(blog.getContent()).text();
				blog.setCntn(text.substring(0, stringLengthOfInitialContent) + WebConstants.ELLIPSIS);
			}
		}
		return blog;
	}

}
