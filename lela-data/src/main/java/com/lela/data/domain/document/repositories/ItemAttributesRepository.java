package com.lela.data.domain.document.repositories;

import com.lela.data.domain.document.ItemAttributes;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 5/8/12
 * Time: 7:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ItemAttributesRepository extends PagingAndSortingRepository<ItemAttributes, String> {
  ItemAttributes findByItemId(Long itemId);
}
