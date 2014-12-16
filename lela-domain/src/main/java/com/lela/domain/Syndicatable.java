package com.lela.domain;

import java.util.Date;

/**
 * This interface may be implemented by any object that can be syndicated like Blogs, Articles, Books or Papers
 * 
 * All known implementations {@link Blog}
 * 
 * @author pankajtandon
 *
 */
public interface Syndicatable {

	public static final String SORT_BY_DATE = "sortByDate";
	public static final String SORT_BY_TITLE = "sortByTitle";
	
	public String getIdentifier() ;
	public String getLink() ;
	public String getTitle() ;
	public String getContent() ;
	public Date getPublishDate() ;
}
