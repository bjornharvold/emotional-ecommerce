package com.lela.data.excel.command;

import com.lela.data.enums.ItemReportingAttribute;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 8/2/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ItemWorkbookCommand {
    boolean showMerchantOffers = false;
    boolean showItemRecalls = false;
    boolean showBrandAttributes = false;
    boolean emailWhenComplete = true;

    //A, B, C, D, E, F, G
    String[] showMotivator = new String[]{};

    ItemReportingAttribute showAttribute;

    public boolean isShowMerchantOffers() {
        return showMerchantOffers;
    }

    public void setShowMerchantOffers(boolean showMerchantOffers) {
        this.showMerchantOffers = showMerchantOffers;
    }

    public boolean isShowItemRecalls() {
        return showItemRecalls;
    }

    public void setShowItemRecalls(boolean showItemRecalls) {
        this.showItemRecalls = showItemRecalls;
    }

    public String[] getShowMotivator() {
        return showMotivator;
    }

    public void setShowMotivator(String[] showMotivator) {
        this.showMotivator = showMotivator;
    }

    public ItemReportingAttribute getShowAttribute() {
        return showAttribute;
    }

    public void setShowAttribute(ItemReportingAttribute showAttribute) {
        this.showAttribute = showAttribute;
    }

    public boolean isShowBrandAttributes() {
        return showBrandAttributes;
    }

    public void setShowBrandAttributes(boolean showBrandAttributes) {
        this.showBrandAttributes = showBrandAttributes;
    }
    public boolean isEmailWhenComplete() {
        return emailWhenComplete;
    }

    public void setEmailWhenComplete(boolean emailWhenComplete) {
        this.emailWhenComplete = emailWhenComplete;
    }


}
