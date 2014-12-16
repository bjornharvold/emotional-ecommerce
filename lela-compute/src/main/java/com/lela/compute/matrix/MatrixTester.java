/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.compute.matrix;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bjorn Harvold
 * Date: 2/15/12
 * Time: 1:25 AM
 * Responsibility:
 */
public class MatrixTester {

    public static void main(String[] args) throws Exception {
        MatrixTester mt = new MatrixTester();
        mt.doRun();
    }

    private void doRun() {
        Map<Integer, Integer> uniqueTotalRelevancies = new HashMap<Integer, Integer>();

        for (int itemMotivatorA = 0; itemMotivatorA < 10; itemMotivatorA++) {
            for (int userMotivatorA = 0; userMotivatorA < 10; userMotivatorA++) {
                for (int itemMotivatorB = 0; itemMotivatorB < 10; itemMotivatorB++) {
                    for (int userMotivatorB = 0; userMotivatorB < 10; userMotivatorB++) {
                        for (int itemMotivatorC = 0; itemMotivatorC < 10; itemMotivatorC++) {
                            for (int userMotivatorC = 0; userMotivatorC < 10; userMotivatorC++) {
                                for (int itemMotivatorD = 0; itemMotivatorD < 10; itemMotivatorD++) {
                                    for (int userMotivatorD = 0; userMotivatorD < 10; userMotivatorD++) {
                                        for (int itemMotivatorE = 0; itemMotivatorE < 10; itemMotivatorE++) {
                                            for (int userMotivatorE = 0; userMotivatorE < 10; userMotivatorE++) {
                                                for (int itemMotivatorF = 0; itemMotivatorF < 10; itemMotivatorF++) {
                                                    for (int userMotivatorF = 0; userMotivatorF < 10; userMotivatorF++) {
                                                        for (int itemMotivatorG = 0; itemMotivatorG < 10; itemMotivatorG++) {
                                                            for (int userMotivatorG = 0; userMotivatorG < 10; userMotivatorG++) {
                                                                for (int itemMotivatorH = 0; itemMotivatorH < 10; itemMotivatorH++) {
                                                                    for (int userMotivatorH = 0; userMotivatorH < 10; userMotivatorH++) {
                                                                        Integer totalRelevancy = 0;
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorA, userMotivatorA);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorB, userMotivatorB);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorC, userMotivatorC);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorD, userMotivatorD);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorE, userMotivatorE);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorF, userMotivatorF);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorG, userMotivatorG);
                                                                        totalRelevancy += computeMotivatorMatch(itemMotivatorH, userMotivatorH);

                                                                        System.out.println(totalRelevancy);
                                                                        uniqueTotalRelevancies.put(totalRelevancy, totalRelevancy);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Integer relevancy : uniqueTotalRelevancies.keySet()) {
            System.out.println(relevancy);
        }
    }


    private Integer computeMotivatorMatch(Integer itemMotivator, Integer userMotivator) {
        Integer resultMotivatorValue = 0;

        // if it is not greater than 0, there is no relevancy at all
        if (userMotivator > 0) {

            // e.g. 3
            Integer itemDistance = itemMotivator - userMotivator;
            StringBuilder uniqueIdentifier = new StringBuilder(userMotivator.toString());

            if (itemDistance > 0) {

                // product motivator is greater than user motivator we need to change the
                // item motivator value to be less
                // than user motivator but with the same distance away from the user motivator
                // e.g. user motivator is 6
                // e.g. item motivator is 9
                // e.g. distance is 3
                // e.g. so we need to subtract 3 from 6 to end up on the opposite side of the 6 spectrum
                // e.g. result is 63
                if (userMotivator - itemDistance < 0) {
                    uniqueIdentifier.append("0");
                } else {
                    uniqueIdentifier.append((userMotivator - itemDistance));
                }
            } else {

                // e.g. "69"
                uniqueIdentifier.append(itemMotivator.toString());
            }

            // e.g. 69
            Integer uniqueIdentifierValue = Integer.parseInt(uniqueIdentifier.toString());

            // e.g. 69 % 60 = 9
            Integer subtractFromUniqueIdentifierValue = uniqueIdentifierValue % (userMotivator * 10);

            // if modulus 10 is 0 here it means the last number was a 0
            // and that there is no relevancy here worth thinking about
            if (uniqueIdentifierValue % 10 == 0) {
                resultMotivatorValue = 0;
            } else {

                // e.g. 6 * (69 - 60) = 54
                resultMotivatorValue = userMotivator
                        * (uniqueIdentifierValue
                        - (uniqueIdentifierValue - subtractFromUniqueIdentifierValue));
            }

            /*
            * System.out.println("key: " + key);
            * System.out.println("user motivator value: " + userMotivatorValue);
            * System.out.println("item motivator value: " + itemMotivatorValue);
            * System.out.println("item distance: " + itemDistance);
            * System.out.println("unique identifer: " + uniqueIdentifier.toString());
            * System.out.println("unique identifer value: " + uniqueIdentifierValue);
            * System.out.println("motivator relevancy: " + resultMotivatorValue);
            */
        }

        return resultMotivatorValue;
    }
}
