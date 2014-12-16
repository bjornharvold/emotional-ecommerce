package com.lela.data.web.commands;

import com.lela.data.domain.dto.ItemSearchCommand;
import com.lela.data.domain.entity.Category;
import com.lela.data.excel.command.ItemWorkbookCommand;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/3/12
 * Time: 11:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class ItemReportingCommand {
    ItemWorkbookCommand itemWorkbookCommand;
    ItemSearchCommand itemSearchCommand;

    private static final String extension = ".xlsx";
    public static String[] productMotivators = new String[]{"A", "B", "C", "D", "E", "F", "G"};
    public static String[] designStyleMotivators = new String[]{"H", "I", "J", "K", "L"};
    public static String[] allMotivators = ArrayUtils.addAll(productMotivators, designStyleMotivators);

    private String recipientEmailAddress;

    public ItemReportingCommand()
    {
        this.itemSearchCommand = new ItemSearchCommand();
        this.itemWorkbookCommand = new ItemWorkbookCommand();
    }

    public ItemWorkbookCommand getItemWorkbookCommand() {
        return itemWorkbookCommand;
    }

    public void setItemWorkbookCommand(ItemWorkbookCommand itemWorkbookCommand) {
        this.itemWorkbookCommand = itemWorkbookCommand;
    }

    public ItemSearchCommand getItemSearchCommand() {
        return itemSearchCommand;
    }

    public void setItemSearchCommand(ItemSearchCommand itemSearchCommand) {
        this.itemSearchCommand = itemSearchCommand;
    }

    public String getFilename()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMddyyyy");
        Date date = new Date();
        Category category = Category.findCategory(itemSearchCommand.getCategoryId());
        String filename = StringUtils.replace(category.getCategoryName()," ", "_") +"_" + StringUtils.replace(itemWorkbookCommand.getShowAttribute().getLabel()," ", "-") + "_" + format.format(date);
        return filename + extension;
    }

    public String getRecipientEmailAddress() {
        return recipientEmailAddress;
    }

    public void setRecipientEmailAddress(String recipientEmailAddress) {
        this.recipientEmailAddress = recipientEmailAddress;
    }

}
