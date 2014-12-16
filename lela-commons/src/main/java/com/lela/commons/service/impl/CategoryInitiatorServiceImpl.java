package com.lela.commons.service.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.lela.commons.exception.CategoryInitiatorException;
import com.lela.commons.repository.CategoryInitiatorQueryRepository;
import com.lela.commons.repository.CategoryInitiatorResultRepository;
import com.lela.commons.service.CategoryInitiatorService;
import com.lela.domain.document.CategoryInitiatorQuery;
import com.lela.domain.document.CategoryInitiatorResult;
import com.lela.domain.document.CategoryInitiatorSubquery;
import com.lela.domain.enums.AffiliateApiType;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import com.lela.util.utilities.storage.s3.S3FileStorage;
import com.mattwilliamsnyc.service.remix.Product;
import com.mattwilliamsnyc.service.remix.ProductsResponse;
import com.mattwilliamsnyc.service.remix.Remix;
import com.mattwilliamsnyc.service.remix.RemixException;
import com.mattwilliamsnyc.service.remix.util.RemixUtil;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.XStream;

import javax.annotation.Nullable;
import java.util.*;

import static com.google.common.base.Joiner.*;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 1:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Service("categoryInitiatorService")
public class CategoryInitiatorServiceImpl implements CategoryInitiatorService{

    final static Logger logger = LoggerFactory.getLogger(CategoryInitiatorServiceImpl.class);

    private static final int pageSize = 100;

    CategoryInitiatorQueryRepository categoryInitiatorQueryRepository;

    CategoryInitiatorResultRepository categoryInitiatorResultRepository;

    Remix remix;

    private static final String bucketName = "initiator";

    private final String accessKey;
    private final String secretKey;

    @Autowired
    public CategoryInitiatorServiceImpl(CategoryInitiatorQueryRepository categoryInitiatorQueryRepository, CategoryInitiatorResultRepository categoryInitiatorResultRepository, @Value("${bestbuy.key}") String apiKey, @Value("${amazon.access.key}") String accessKey, @Value("${amazon.secret.key}") String secretKey) {
        this.categoryInitiatorQueryRepository = categoryInitiatorQueryRepository;
        this.categoryInitiatorResultRepository = categoryInitiatorResultRepository;
        logger.debug(apiKey);
        remix = new Remix(apiKey);
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @Override
    public List<CategoryInitiatorQuery> findAllQuerys() {
        return Lists.newArrayList(categoryInitiatorQueryRepository.findAll());
    }

    @Override
    public void save(CategoryInitiatorQuery categoryInitiatorQuery) {
        categoryInitiatorQueryRepository.save(categoryInitiatorQuery);
    }

    @Override
    public void save(CategoryInitiatorResult categoryInitiatorResult) {
        categoryInitiatorResultRepository.save(categoryInitiatorResult);
    }

    @Override
    public CategoryInitiatorQuery getQuery(String id) {
        return categoryInitiatorQueryRepository.findOne(new ObjectId(id));
    }

    @Override
    public CategoryInitiatorResult getResult(String id) {
        return categoryInitiatorResultRepository.findOne(new ObjectId(id));
    }

    @Override
    public void deleteQuery(String id) {
        categoryInitiatorQueryRepository.delete(new ObjectId(id));
    }

    @Override
    public List<CategoryInitiatorResult> findResults(CategoryInitiatorQuery categoryInitiatorQuery) {
        return categoryInitiatorResultRepository.findByCategoryInitiatorQuery(categoryInitiatorQuery.getId());
    }

    @Override
    public List<CategoryInitiatorQuery> findQuerysForUser(String srid) {
        return categoryInitiatorQueryRepository.findByUserCode(new ObjectId(srid));
    }

    @Override
    public void deleteResult(String id) {
        //TODO Remove S3 file
        //CategoryInitiatorResult result = categoryInitiatorResultRepository.findOne(new ObjectId(id));
        //this.createFileStorage().removeObjectFromBucket(bucketName, result.getS3BjctKy());
        categoryInitiatorResultRepository.delete(new ObjectId(id));
    }

    @Override
    @Async
    public void run(CategoryInitiatorQuery categoryInitiatorQuery)
    {
        //http://api.remix.bestbuy.com/v1/products((search=iphone)|(search=keyword&search=))?page=12&pageSize=100&show=all&apiKey=65azx6f6a8je6cm6e64ytysc

        categoryInitiatorQuery.getKywrds();

        List<String> filters = new ArrayList();

        //Use the keywords to search all categories
        List<String> keywords = new ArrayList<String>();

        Collections.addAll(keywords, categoryInitiatorQuery.getKywrds());

        List<String> filteredKeywords = new ArrayList<String>();
        filteredKeywords.addAll(Collections2.filter(keywords, new Predicate<String>() {
            @Override
            public boolean apply(@Nullable String input) {
                return StringUtils.isNotBlank(input);
            }
        }));

        StringBuffer searchTerms = new StringBuffer("(");
        for(String keyword:filteredKeywords)
        {
            searchTerms.append("search=");
            searchTerms.append(keyword);
            if(filteredKeywords.indexOf(keyword) < (filteredKeywords.size() - 1))
            {
                searchTerms.append("|");
            }
        }
        searchTerms.append(")");
        filters.add(searchTerms.toString());

        //Do vendor specific searches
        if(categoryInitiatorQuery.getQrys() != null && categoryInitiatorQuery.getQrys().get(AffiliateApiType.BEST_BUY)!=null && categoryInitiatorQuery.getQrys().get(AffiliateApiType.BEST_BUY).getNbld())
        {
            for(CategoryInitiatorSubquery subquery : categoryInitiatorQuery.getQrys().get(AffiliateApiType.BEST_BUY).getQrys())
            {
                StringBuffer filter = new StringBuffer("(");

                boolean addAnAnd = false;
                if(!subquery.getCtgry().equals("ALL"))
                {
                    filter.append("categoryPath.id="+subquery.getCtgry());
                    addAnAnd = true;
                }
                if(StringUtils.isNotBlank(subquery.getKywrd()))
                {
                    if(addAnAnd)filter.append("&");
                    filter.append("search="+subquery.getKywrd());
                    addAnAnd = true;
                }
                if(StringUtils.isNotBlank(subquery.getNm()))
                {
                    if(addAnAnd)filter.append("&");
                    filter.append("name="+subquery.getNm());
                    addAnAnd = true;
                }
                filter.append(")");
                filters.add(filter.toString());
            }
        }

        Map params = new HashMap();
        params.put("show", "all");
        params.put("pageSize", String.valueOf(pageSize));

        List<Product> products = new ArrayList<Product>();

        CategoryInitiatorResult result = new CategoryInitiatorResult();
        result.setId(new ObjectId());
        result.setCdt(new Date());
        result.setQryid(categoryInitiatorQuery.getId());
        save(result);
        //Do initial call
        try {
            ProductsResponse productsResponse = null;

            logger.debug("fetching first page");
            while(productsResponse == null || productsResponse.currentPage() < productsResponse.totalPages())
            {

                productsResponse = remix.getProducts(filters, params, RemixUtil.OR_DELIMITER);
                logger.debug("pages " + productsResponse.totalPages());
                logger.debug("total " + productsResponse.total());
                logger.debug("currentPage " + productsResponse.currentPage());
                for(Product product:productsResponse.list())
                {
                    products.add(product);
                }
                result.setCnt(productsResponse.total());
                result.setRtrvd(products.size());
                save(result);
                params.put("page", String.valueOf(productsResponse.currentPage() + 1));
            }
        } catch (RemixException e) {
            throw new CategoryInitiatorException("There was a problem pulling the best buy data.", e);
        }
        catch(RuntimeException re)
        {
            logger.error(re.getMessage(), re);
            throw re;
        }
        logger.info("Found products: " + products.size());

        FileStorage storage = this.createFileStorage();
        XStream xstream = new XStream();
        String xml = xstream.toXML(products);
        logger.info("Uploading file");
        String url = storage.storeFile(new FileData(result.getId().toString()+".xml", xml.getBytes(), "text/xml"));

        logger.info("Uploaded file:"+url);
        result.setRl(url);
        result.setCmpltdt(new Date());
        save(result);

    }

    public FileStorage createFileStorage(){
        return new S3FileStorage(accessKey, secretKey, bucketName, false);
    }
}
