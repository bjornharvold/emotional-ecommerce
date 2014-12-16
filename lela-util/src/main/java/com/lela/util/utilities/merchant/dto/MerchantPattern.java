package com.lela.util.utilities.merchant.dto;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/13/12
 * Time: 11:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class MerchantPattern {
    private int merchantId;
    private String pattern;

    public MerchantPattern(int merchantId, String pattern) {
        this.merchantId = merchantId;
        this.pattern = pattern;
    }

    public int getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(int merchantId) {
        this.merchantId = merchantId;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MerchantPattern that = (MerchantPattern) o;

        if (merchantId != that.merchantId) return false;
        if (pattern != null ? !pattern.equals(that.pattern) : that.pattern != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = merchantId;
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        return result;
    }
}
