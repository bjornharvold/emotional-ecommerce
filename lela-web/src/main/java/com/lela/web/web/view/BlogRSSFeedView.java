package com.lela.web.web.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.lela.commons.web.utils.WebConstants;
import com.lela.domain.Syndicatable;
import com.lela.domain.document.Blog;
import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;

@Component
public class BlogRSSFeedView extends AbstractRssFeedView {


	@Value("${rss.blog.title:Lela Dot Com}")
	private String blogTitle;
	
	@Value("${rss.blog.description:Lela Blogs}")
	private String blogDescription;

	@Value("${rss.blog.link:http://www.lela.com/blog}")
	private String blogLink;
	
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel channel,
		HttpServletRequest request) {
 
		channel.setTitle(blogTitle);
		channel.setDescription(blogDescription);
		channel.setLink(blogLink);
 
		super.buildFeedMetadata(model, channel, request);
	}
	
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		@SuppressWarnings("unchecked")
		List<Syndicatable> syndicateList = (List<Syndicatable>) model.get("feedContent");
		List<Item> items = new ArrayList<Item>(syndicateList.size());
 
		for(Syndicatable syndicatable : syndicateList ){
 
			Item item = new Item();
 
			if (!StringUtils.isEmpty(syndicatable.getContent())) {
				if (syndicatable.getContent().endsWith(WebConstants.ELLIPSIS)){
					appendAnchorToContent((Blog)syndicatable);
				}
				Content content = new Content();
				content.setValue(syndicatable.getContent());
				content.setType(DEFAULT_CONTENT_TYPE);
				item.setContent(content);
			}
 
			item.setTitle(syndicatable.getTitle());
			item.setLink(blogLink + "/" + syndicatable.getLink());
			item.setPubDate(syndicatable.getPublishDate());
 
			items.add(item);
		}
 
		return items;
	}
	
	private void appendAnchorToContent(Blog blog){
		blog.setCntn(blog.getContent() + "<a href=\"" + blogLink + "/" + blog.getLink()  + "\" >More</a>" );  
	}

}
