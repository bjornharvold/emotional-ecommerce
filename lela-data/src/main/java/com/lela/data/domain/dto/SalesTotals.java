package com.lela.data.domain.dto;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 9/14/12
 * Time: 10:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class SalesTotals {

    Date month = new Date();
    BigDecimal salesTotal = BigDecimal.ZERO;
    BigDecimal commissionTotal = BigDecimal.ZERO;
    int quantity = 0;

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public BigDecimal getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(BigDecimal salesTotal) {
        this.salesTotal = salesTotal;
    }

    public BigDecimal getCommissionTotal() {
        return commissionTotal;
    }

    public void setCommissionTotal(BigDecimal commissionTotal) {
        this.commissionTotal = commissionTotal;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
