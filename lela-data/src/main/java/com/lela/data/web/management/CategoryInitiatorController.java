package com.lela.data.web.management;

import com.lela.commons.service.CategoryInitiatorService;
import com.lela.data.domain.entity.Item;
import com.lela.domain.document.CategoryInitiatorQuery;
import com.lela.domain.document.CategoryInitiatorResult;
import com.lela.domain.document.CategoryInitiatorSubquery;
import com.lela.domain.enums.AffiliateApiType;
import com.lela.util.utilities.storage.FileStorage;
import com.lela.util.utilities.storage.dto.FileData;
import com.lela.util.utilities.storage.s3.S3FileStorage;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 11/2/12
 * Time: 9:40 AM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/manage/categorys/initiator")
@Controller
public class CategoryInitiatorController {

    final static Logger logger = LoggerFactory.getLogger(CategoryInitiatorController.class);

    private final CategoryInitiatorService categoryInitiatorService;

    @Resource(name="bestBuyCategories")
    private Map<String, String> bestBuyCategories;

    @Autowired(required = true)
    public CategoryInitiatorController(CategoryInitiatorService categoryInitiatorService)
    {
        this.categoryInitiatorService = categoryInitiatorService;
    }

    @RequestMapping(value = "/index", produces = "text/html")
    public String index( Model uiModel)
    {
        /* Display a list of queries */
        List<CategoryInitiatorQuery> querys = categoryInitiatorService.findAllQuerys();
        uiModel.addAttribute("querys", querys);
        return "manage/categorys/initiator/index";
    }

    @RequestMapping(value = "/results/{id}", produces = "text/html")
    public String results(@PathVariable("id") String id, Model uiModel)
    {
        /* Display a list of queries */
        CategoryInitiatorQuery query = categoryInitiatorService.getQuery(id);
        List<CategoryInitiatorResult> results = categoryInitiatorService.findResults(query);
        uiModel.addAttribute("query", query);
        uiModel.addAttribute("results", results);
        return "manage/categorys/initiator/results";
    }

    /*Run a query*/
    @RequestMapping(value = "/run/{id}", produces = "text/html")
    public String run(@PathVariable("id") String id, @RequestParam(required = true, defaultValue = "/manage/categorys/initiator/index") String rlnm){

        runAsync(id);

        return "redirect:/"+rlnm;
    }

    @Async
    private void runAsync(String id)
    {
        CategoryInitiatorQuery categoryInitiatorQuery = categoryInitiatorService.getQuery(id);
        this.categoryInitiatorService.run(categoryInitiatorQuery);

    }

    @RequestMapping(value = "/download/{id}", method=RequestMethod.GET)
    public void download(@PathVariable("id") String id, HttpServletResponse response) throws Exception
    {

        CategoryInitiatorResult result = categoryInitiatorService.getResult(id);
        response.setHeader("Content-Disposition", "attachment; filename=" + "test.xml");
        response.setContentType("text/xml");
        logger.info(result.getRl());
        URL url = new URL(result.getRl());
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        OutputStream os = response.getOutputStream();
        IOUtils.copy(reader, os);
        os.flush();
        os.close();
    }

    /*Create a new query*/
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = "text/html")
    public String create(Model uiModel)
    {
        uiModel.addAttribute("categoryInitiatorQuery", getNewCategoryInitiatorQuery());
        addEnums(uiModel);
        return "manage/categorys/initiator/create";
    }

    private CategoryInitiatorQuery getNewCategoryInitiatorQuery()
    {
        CategoryInitiatorQuery query = new CategoryInitiatorQuery();
        query.setId(ObjectId.get());
        query.setKywrds(new String[3]);
        return query;
    }

    @RequestMapping(value = {"/create","/update/*"}, method = RequestMethod.POST, produces = "text/html")
    public String save(@Valid CategoryInitiatorQuery categoryInitiatorQuery, @RequestParam(value="add", required = false) String add, @RequestParam(value="save", required = false) String save,  @RequestParam(value="remove", required = false) String remove, BindingResult bindingResult, Model uiModel)
    {
        uiModel.addAttribute("categoryInitiatorQuery", categoryInitiatorQuery);
        addEnums(uiModel);
        if(bindingResult.hasErrors())
        {

            uiModel.addAttribute(bindingResult);
            return "manage/categorys/initiator/create";
        }

        if(StringUtils.isNotBlank(save))
        {
            categoryInitiatorService.save(categoryInitiatorQuery);
            return "redirect:/manage/categorys/initiator/index";
        }
        else if(StringUtils.isNotBlank(add))
        {
            CategoryInitiatorSubquery[] subqueries = categoryInitiatorQuery.getQrys().get(AffiliateApiType.valueOf(add)).getQrys();
            subqueries = (CategoryInitiatorSubquery[])ArrayUtils.add(subqueries, new CategoryInitiatorSubquery());
            categoryInitiatorQuery.getQrys().get(AffiliateApiType.valueOf(add)).setQrys(subqueries);

            return "manage/categorys/initiator/create";
        }
        else if(StringUtils.isNotBlank(remove))
        {
            //TODO remove the designated query
            //BEST_BUY[0] for example
            String[] tokens = StringUtils.split(remove, "[");//yields ( BEST_BUY,0] )
            AffiliateApiType type = AffiliateApiType.valueOf(tokens[0]);
            String index = StringUtils.remove(tokens[1], "]");
            CategoryInitiatorSubquery[] subqueries = categoryInitiatorQuery.getQrys().get(type).getQrys();
            subqueries = (CategoryInitiatorSubquery[])ArrayUtils.remove(subqueries, Integer.valueOf(index));
            categoryInitiatorQuery.getQrys().get(type).setQrys(subqueries);
            return "manage/categorys/initiator/create";
        }
        else
        {
            return "redirect:/manage/categorys/initiator/index";
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET, produces = "text/html")
    public String update(@PathVariable("id") String id, Model uiModel)
    {
        CategoryInitiatorQuery categoryInitiatorQuery = categoryInitiatorService.getQuery(id);
        uiModel.addAttribute("categoryInitiatorQuery", categoryInitiatorQuery);
        addEnums(uiModel);
        return "manage/categorys/initiator/create";
    }

    /*
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST, produces = "text/html")
    public String saveUpdate(@PathVariable("id") String id, @Valid CategoryInitiatorQuery categoryInitiatorQuery, @RequestParam(value="add", required = false) String add, @RequestParam(value="save", required = false) String save, @RequestParam(value="remove", required = false) String remove, BindingResult bindingResult, Model uiModel) throws Exception
    {
        save(categoryInitiatorQuery, add, save, remove, bindingResult, uiModel);

        uiModel.addAttribute("categoryInitiatorQuery", categoryInitiatorQuery);
        addEnums(uiModel);
        return "manage/categorys/initiator/update";
    }
    */


    @RequestMapping(method = RequestMethod.GET, value = "/delete/{id}", produces = "text/html")
    public String delete(@PathVariable ("id") String id)
    {
        categoryInitiatorService.deleteResult(id);
        //redirect to...
        return "manage/categorys/initiator/index";
    }

    private void addEnums(Model model)
    {
        for (AffiliateApiType e : AffiliateApiType.values()) {
            model.addAttribute(e.toString(), e);
        }
        model.addAttribute("bestBuyCategories", bestBuyCategories);
    }
}
