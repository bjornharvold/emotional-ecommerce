package com.lela.commons.test.service;

import com.google.common.collect.Lists;
import com.lela.commons.repository.CategoryInitiatorQueryRepository;
import com.lela.commons.repository.CategoryInitiatorResultRepository;
import com.lela.commons.service.CategoryInitiatorService;
import com.lela.commons.service.UserTrackerService;
import com.lela.commons.service.impl.CategoryInitiatorServiceImpl;
import com.lela.commons.test.AbstractFunctionalTest;
import com.lela.domain.document.CategoryInitiatorQuery;
import com.lela.domain.document.CategoryInitiatorResult;
import com.lela.domain.document.User;
import com.lela.util.utilities.storage.FileStorage;
import com.mattwilliamsnyc.service.remix.*;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 2:05 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(MockitoJUnitRunner.class)
public class CategoryInitiatorServiceUnitTest{


    @Mock
    CategoryInitiatorQueryRepository categoryInitiatorQueryRepository;

    @Mock
    CategoryInitiatorResultRepository categoryInitiatorResultRepository;

    @Mock
    MyRemix remix;

    @InjectMocks
    CategoryInitiatorServiceImpl categoryInitiatorService;

    @Test
    public void testFindAllQuerys()
    {
        when(categoryInitiatorQueryRepository.findAll()).thenReturn(Lists.newArrayList(new CategoryInitiatorQuery())) ;
        assertEquals("No queries found.", 1, categoryInitiatorService.findAllQuerys().size());
    }

    @Before
    public void setup()
    {
        ReflectionTestUtils.setField(categoryInitiatorService, "remix", remix);
    }

    @Test
    public void testRun() throws Exception
    {
        ProductsResponse productsResponse = mock(MyProductsResponse.class);
        //ProductsResponse spy = mock(ProductsResponse.class);
        Product product = new Product();
        List<Product> products = new ArrayList<Product>();
        products.add(product);

        doReturn(productsResponse).when(remix).getProducts(any(List.class), any(Map.class), any(String.class));
        doReturn(1).when(productsResponse).total();
        doReturn(1).when(productsResponse).totalPages();
        when(productsResponse.currentPage()).thenReturn(1).thenReturn(2).thenReturn(3);
        doReturn(products).when(productsResponse).list();

        CategoryInitiatorServiceImpl spy = spy(categoryInitiatorService);
        doReturn(mock(FileStorage.class)).when(spy).createFileStorage();

        CategoryInitiatorQuery query = new CategoryInitiatorQuery();
        spy.run(query);

        //assertEquals("No products returned", products, productResults);
    }

    @Test
    public void testFindAllResults()
    {
        CategoryInitiatorQuery query = new CategoryInitiatorQuery();
        query.setId(ObjectId.get());
        when(categoryInitiatorResultRepository.findByCategoryInitiatorQuery(query.getId())).thenReturn(Lists.newArrayList(new CategoryInitiatorResult()));
        assertEquals("No queries found.", 1, categoryInitiatorService.findResults(query).size());

    }

    @Test
    public void testSaveQuery()
    {
        CategoryInitiatorQuery categoryInitiatorQuery = new CategoryInitiatorQuery();
        categoryInitiatorService.save(categoryInitiatorQuery);
        verify(categoryInitiatorQueryRepository).save(categoryInitiatorQuery);
    }

    @Test
    public void testSaveResult()
    {
        CategoryInitiatorResult categoryInitiatorResult = new CategoryInitiatorResult();
        categoryInitiatorService.save(categoryInitiatorResult);
        verify(categoryInitiatorResultRepository).save(categoryInitiatorResult);
    }

    @Test
    public void testGetQuery()
    {
        ObjectId id = ObjectId.get();
        categoryInitiatorService.getQuery(id.toString());
        verify(categoryInitiatorQueryRepository).findOne(any(ObjectId.class));
    }

    @Test
    public void testGetResult()
    {
        ObjectId id = ObjectId.get();
        categoryInitiatorService.getResult(id.toString());
        verify(categoryInitiatorResultRepository).findOne(any(ObjectId.class));
    }

    @Test
    public void testDeleteQuery()
    {
        ObjectId id = ObjectId.get();
        categoryInitiatorService.deleteQuery(id.toString());
        verify(categoryInitiatorQueryRepository).delete(any(ObjectId.class));
    }

    @Test
    public void testDeleteResult()
    {
        ObjectId id = ObjectId.get();
        categoryInitiatorService.deleteResult(id.toString());
    }

    @Test
    public void testFindResults()
    {
        CategoryInitiatorQuery categoryInitiatorQuery = new CategoryInitiatorQuery();
        categoryInitiatorQuery.setId(new ObjectId());
        categoryInitiatorService.findResults(categoryInitiatorQuery);
        verify(categoryInitiatorResultRepository).findByCategoryInitiatorQuery(categoryInitiatorQuery.getId());
    }

    @Test
    public void testFindQuerysForUser()
    {
        ObjectId id = ObjectId.get();
        categoryInitiatorService.findQuerysForUser(id.toString());
        verify(categoryInitiatorQueryRepository).findByUserCode(any(ObjectId.class));
    }

    private class MyRemix extends Remix{
        private MyRemix(String apiKey) {
            super(apiKey);
        }

        @Override
        public ProductsResponse getProducts(List<String> filters, Map<String, String> params, String separator) throws RemixException {
            return null;
        }
    }

    private class MyProductsResponse extends ProductsResponse{
        private MyProductsResponse(HttpURLConnection connection) throws RemixException {
            super(connection);
        }

        @Override
        public int total() {
            return 1;
        }

        @Override
        public int totalPages() {
            return 1;
        }

        @Override
        public int currentPage() {
            return 1;
        }

        @Override
        public List<Product> list() {
            return null;
        }
    }


}
