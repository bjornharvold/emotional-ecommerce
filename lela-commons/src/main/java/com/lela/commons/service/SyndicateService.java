package com.lela.commons.service;

import java.util.List;

import com.lela.domain.Syndicatable;

public interface SyndicateService {

	/**
	 * returns a list of {@link Syndicatable}s
	 * @param sortBy - What to sort the list by {@see Syndicatable}
	 * @param numberOfSyndicatablesToFetch - An integer between 0 and Integer.MAXVALUE
	 * @param stringLengthOfInitialContent - The length of each Syndicatable content
	 * @param ascending - sort order
	 * @param onlyPublished - Show only pblished Syndicatables
	 * @return
	 */
	List<Syndicatable> getSyndicateList(final String sortBy, final int numberOfSyndicatablesToFetch, final int stringLengthOfInitialContent, final boolean ascending, final boolean onlyPublished) ;
	
	/**
	 * Returns  Syndicatable corresponding to the identifier passed in
	 * @param id
	 * @return
	 */
	Syndicatable getSyndicatableByIdentifier(String id);
}
