package com.lela.data.web.management;

import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.domain.entity.*;
import com.lela.data.web.commands.LelaAttributesCommand;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/25/12
 * Time: 8:34 AM
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/manage/items/attributes")
@Controller
public class LelaAttributeController {

    /**
     * Index page
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
    public String index(Model uiModel) {
        uiModel.addAttribute("categorys" , Category.findAllCategorys());
        uiModel.addAttribute("itemSearchCommand", new ItemSearchCommand());
        return "manage/items/attributes";
    }

    /**
     * Run a search
     * @param itemSearchCommand
     * @param offset
     * @param maxSize
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.POST)
    public String find(ItemSearchCommand itemSearchCommand,Integer offset, Integer maxSize, Model uiModel) {
        uiModel.addAttribute("categorys" , Category.findAllCategorys());
        if(maxSize == null)
            maxSize = 20;
        if(offset == null)
            offset = 0;
        List<Item> items = Item.findItemByItemSearchCommand(itemSearchCommand, offset, maxSize);
        Long resultSize = Item.findCountItemByItemSearchCommand(itemSearchCommand);
        uiModel.addAttribute("items", items);
        uiModel.addAttribute("itemSearchCommand", new ItemSearchCommand());
        uiModel.addAttribute("resultSize", resultSize.intValue());
        return "manage/items/attributes";
    }

    /**
     * Edit an Item's Lela Attributes
     * @param itemId
     * @param uiModel
     * @return
     */
    @RequestMapping(value = "/edit", produces = "text/html", method = RequestMethod.GET)
    public String edit(Long itemId, Model uiModel)
    {
        Item item = Item.findItem(itemId);

        List<AttributeTypeCategory> attributeTypeCategories = AttributeTypeCategory.findLelaResearchAttributes(item.getCategory());

        LelaAttributesCommand lelaAttributesCommand = new LelaAttributesCommand();

        Map<AttributeType, ItemAttribute> itemAttributes = item.getItemAttributeMap();
        for(AttributeTypeCategory attributeTypeCategory:attributeTypeCategories)
        {
            ItemAttribute itemAttribute = itemAttributes.get(attributeTypeCategory.getAttributeType());
            String value = "";
            if(itemAttribute != null)
            {
                value = itemAttribute.getAttributeValue();
            }
            lelaAttributesCommand.getAttributes().put(attributeTypeCategory.getAttributeType().getLelaAttributeName(), value);
        }
        uiModel.addAttribute("item", item);
        uiModel.addAttribute("lelaAttributesCommand", lelaAttributesCommand);
        return "manage/items/attributes/edit";
    }

    @RequestMapping(value = "/edit", produces = "text/html", method = RequestMethod.POST)
    public String save(LelaAttributesCommand lelaAttributesCommand, Model uiModel)
    {
        //Validation?  No blanks

        Item item = Item.findItem(lelaAttributesCommand.getItemId());

        Map<AttributeType, ItemAttribute> itemAttributes = item.getItemAttributeMap();


        for(String lelaAttributeName:lelaAttributesCommand.getAttributes().keySet())
        {
            ItemAttribute itemAttribute = null;
            for(AttributeType attributeType:itemAttributes.keySet())
            {
                if(attributeType.getLelaAttributeName().equals(lelaAttributeName))
                {
                    itemAttribute = itemAttributes.get(attributeType);
                }
                if(itemAttribute!=null)
                {
                    break;
                }
            }
            itemAttribute.setAttributeValue(lelaAttributesCommand.getAttributes().get(lelaAttributeName));
            itemAttribute.merge();
        }
        item.merge();


        uiModel.addAttribute("item", item);
        uiModel.addAttribute("lelaAttributesCommand", lelaAttributesCommand);
        return "manage/items/attributes/edit";
    }
}
