import java.lang.String;
import java.lang.System;
import java.util.*;
import java.util.List;
import java.util.Map;

import com.mattwilliamsnyc.service.remix.util.RemixUtil;
import org.junit.Test;
import com.mattwilliamsnyc.service.remix.*;

public class ApiTest
{
    String apiKey = "65azx6f6a8je6cm6e64ytysc";

    private static int pageSize = 10;

    @Test
    public void testProductSearch() throws Exception
    {
        Remix remix = new Remix(apiKey);

        List filters = new ArrayList();

        filters.add("categoryPath.id=abcat0901000");

        //List subFilters = new ArrayList();
        filters.add("search=apple");

        //filters.add(subFilters);

        Map params = new HashMap();
        params.put("show", "all");
        params.put("pageSize", String.valueOf(pageSize));
        ProductsResponse productsResponse = remix.getProducts(filters, params, RemixUtil.AND_DELIMITER);
        System.out.println("total " + productsResponse.total());
        System.out.println("pages " + productsResponse.totalPages());
        System.out.println("currentPage " + productsResponse.currentPage());

        List products = new ArrayList();
        while(productsResponse.currentPage() < productsResponse.totalPages())
        {
            for(Product product:productsResponse.list())
            {
                System.out.println(product.getName() + " " + product.getSku() + " " + product.getUpc());
                products.add(product);
            }
            params.put("page", String.valueOf(productsResponse.currentPage() + 1));
            productsResponse = remix.getProducts(filters, params, RemixUtil.AND_DELIMITER);
        }
    }

    @Test
    public void testProductFilters() throws Exception
    {
        Remix remix = new Remix(apiKey);

        List filters = new ArrayList();
        filters.add("(search=WOODCHUCKcase|search=Hemisphere)");
        //filters.add("search=blender");

        //abcat0200000 audio & mp3
        //abcat0800000 mobile phones
        //abcat0912011 blenders
        filters.add("(categoryPath.id=abcat0200000|categoryPath.id=abcat0800000|categoryPath.id=abcat0912011)");
        //filters.add("categoryPath.id=abcat0800000");
        //filters.add("categoryPath.id=abcat0912011");

        Map params = new HashMap();
        params.put("show", "all");
        params.put("pageSize", "100");
        ProductsResponse productsResponse = remix.getProducts(filters, params, RemixUtil.AND_DELIMITER);
        System.out.println("total " + productsResponse.total());
        System.out.println("pages " + productsResponse.totalPages());
        for(Product product:productsResponse.list())
        {
            System.out.println(product.getName() + " " + product.getSku() + " " + product.getUpc());
        }
    }

    @Test
    public void testBlenders() throws Exception
    {
        Remix remix = new Remix(apiKey);

        List filters = new ArrayList();
        filters.add("(categoryPath.id=abcat0912011)");
        filters.add("(search=blender|search=mixer)");
        filters.add("(name=hamilton%20beach|name=oster|name=cuisinart|name=waring%20pro|name=ninja)");
        //filters.add("search=blender");

        //abcat0200000 audio & mp3
        //abcat0800000 mobile phones
        //abcat0912011 blenders
        //filters.add("(categoryPath.id=abcat0200000|categoryPath.id=abcat0800000|categoryPath.id=abcat0912011)");
        //filters.add("categoryPath.id=abcat0800000");
        //filters.add("categoryPath.id=abcat0912011");

        Map params = new HashMap();
        params.put("show", "all");
        params.put("pageSize", "100");
        ProductsResponse productsResponse = remix.getProducts(filters, params, RemixUtil.OR_DELIMITER);
        System.out.println("total " + productsResponse.total());
        System.out.println("pages " + productsResponse.totalPages());
        for(Product product:productsResponse.list())
        {
            System.out.println(product.getName() + " " + product.getSku() + " " + product.getUpc());
        }


    }

    @Test
    public void testScanners()
    {
        //Scanners:
        //All items from BB category "Printers"
        //All items from BB category "Scanners"
        //All items from any category with the keyword "Scanner"
        //All items from any category with the brand "Mustek" OR brand "Brother" OR brand "Epson"
    }
}