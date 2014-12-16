/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.lela.domain.enums.MotivatorSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 8/7/12
 * Time: 1:44 PM
 * Responsibility:
 */
public class Motivator implements Serializable {
    private static final long serialVersionUID = 7473259853062001073L;

    /** motivator source */
    private MotivatorSource tp;

    /** Motivator map */
    private Map<String, Integer> mtvtrs;

    public Motivator() {
    }

    public Motivator(Motivator original) {
        this.tp = original.getTp();
        this.mtvtrs = new HashMap<String, Integer>();
        this.mtvtrs.putAll(original.getMtvtrs());
    }

    public Motivator(MotivatorSource tp,  Map<String, Integer> mtvtrs) {
        this.tp = tp;
        this.mtvtrs = mtvtrs;
    }

    public MotivatorSource getTp() {
        return tp;
    }

    public void setTp(MotivatorSource tp) {
        this.tp = tp;
    }

    public Map<String, Integer> getMtvtrs() {
        return mtvtrs;
    }

    public void setMtvtrs(Map<String, Integer> mtvtrs) {
        this.mtvtrs = mtvtrs;
    }

    public boolean hasStyleMotivators() {
        boolean result = false;

        Integer motivatorH = 0;
        Integer motivatorI = 0;
        Integer motivatorJ = 0;
        Integer motivatorK = 0;
        Integer motivatorL = 0;

        if (mtvtrs.containsKey("H")) {
            motivatorH = mtvtrs.get("H");
        }
        if (mtvtrs.containsKey("I")) {
            motivatorI = mtvtrs.get("I");
        }
        if (mtvtrs.containsKey("J")) {
            motivatorJ = mtvtrs.get("J");
        }
        if (mtvtrs.containsKey("K")) {
            motivatorK = mtvtrs.get("K");
        }
        if (mtvtrs.containsKey("L")) {
            motivatorL = mtvtrs.get("L");
        }

        // for the quiz scores to be good enough to calculate motivator scores
        // we need these motivators to be greater than 0
        if (motivatorH > 0 && motivatorI > 0 && motivatorJ > 0 &&
                motivatorK > 0 && motivatorL > 0) {
            result = true;
        }

        return result;
    }
}
