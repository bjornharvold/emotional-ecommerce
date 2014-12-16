package com.lela.domain.dto.admin;

/**
 * User: Chris Tallent
 * Date: 6/12/12
 * Time: 10:10 PM
 */
public class CampaignDailyTotalValue {
    private Integer total;
    private Integer assoc;
    private Integer totalQuiz;
    private Integer assocQuiz;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getAssoc() {
        return assoc;
    }

    public void setAssoc(Integer assoc) {
        this.assoc = assoc;
    }

    public Integer getTotalQuiz() {
        return totalQuiz;
    }

    public void setTotalQuiz(Integer totalQuiz) {
        this.totalQuiz = totalQuiz;
    }

    public Integer getAssocQuiz() {
        return assocQuiz;
    }

    public void setAssocQuiz(Integer assocQuiz) {
        this.assocQuiz = assocQuiz;
    }
}