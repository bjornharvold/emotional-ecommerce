package com.lela.domain.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 1/20/12
 * Time: 1:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnBoardProducts {

    private List<OnBoardProduct> onBoardProductList;

    public OnBoardProducts() {
    }

    public OnBoardProducts(List<OnBoardProduct> onBoardProductList) {
        this.onBoardProductList = onBoardProductList;
    }

    public List<OnBoardProduct> getOnBoardProductList() {
        return onBoardProductList;
    }

    public void setOnBoardProductList(List<OnBoardProduct> onBoardProductList) {
        this.onBoardProductList = onBoardProductList;
    }
}
