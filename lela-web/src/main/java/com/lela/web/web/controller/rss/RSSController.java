package com.lela.web.web.controller.rss;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.lela.commons.service.SyndicateService;
import com.lela.domain.Syndicatable;
import com.lela.web.web.view.BlogRSSFeedView;

@RequestMapping(value="/rssfeed")
@Controller
public class RSSController {

	
	private final SyndicateService syndicateService;
	private final BlogRSSFeedView blogRSSFeedView;
	
	@Autowired
	public RSSController(SyndicateService syndicateService,
			 BlogRSSFeedView blogRSSFeedView
			){
		this.syndicateService = syndicateService;
		this.blogRSSFeedView = blogRSSFeedView;
	}
	
	@RequestMapping(value="/blog", method = RequestMethod.GET)
	public ModelAndView getAllBlogsAsRss() {
		List<Syndicatable> items = new ArrayList<Syndicatable>();
		//Get all blog entries
		List<Syndicatable> syndicateList = syndicateService.getSyndicateList(Syndicatable.SORT_BY_DATE, 15, 200, false, true);
		for (Syndicatable syndicatable : syndicateList) {
			items.add(syndicatable);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setView(blogRSSFeedView);
		mav.addObject("feedContent", items);
		return mav;
	}
	
	@RequestMapping(value="/blog/{blogUrl}", method = RequestMethod.GET)
	public ModelAndView getABlogAsRss(@PathVariable("blogUrl") String blogUrl) {
		List<Syndicatable> items = new ArrayList<Syndicatable>();
		//Get specific blog entry
		Syndicatable syndicatable = syndicateService.getSyndicatableByIdentifier(blogUrl);
		items.add(syndicatable);
		ModelAndView mav = new ModelAndView();
		mav.setView(blogRSSFeedView);
		mav.addObject("feedContent", items);
		return mav;
	}
	
}
