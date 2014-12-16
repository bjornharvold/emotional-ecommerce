/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.motivator.compiler;

import com.dtrules.compiler.excel.util.Excel2XML;
import com.dtrules.interpreter.RName;
import com.dtrules.session.RuleSet;
import com.dtrules.session.RulesDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * User: Chris Tallent
 * Date: 2/14/12
 * Time: 5:36 PM
 */
public class CompileAllMotivators {

    private static final Logger log = LoggerFactory.getLogger(CompileAllMotivators.class);

    public static String path    = System.getProperty("user.dir")+"/lela-motivator-rules/dtrules";

    public static void main(String[] args) {

        // Create the rules directory configuration in order
        // to determine which rulesets are defined
        RulesDirectory rd = new RulesDirectory(path,"DTRules.xml");
        HashMap<RName,RuleSet> rulesets = rd.getRulesets();
        
        // Run through each ruleset and compile it from excel
        if (rulesets != null) {
            
            // Create a sorted list of the names to process
            List<String> names = new ArrayList<String>();
            for (RName name : rulesets.keySet()) {
                names.add(name.getName());
            }
            
            Collections.sort(names);

            // Compile each ruleset
            for (String name : names) {
                System.out.println("Processing ruleset name: " + name);
                
                try {
                    Excel2XML excel2XML = new Excel2XML(path, "DTRules.xml", name);
                    excel2XML.compileRuleSet(path, "DTRules.xml", name, null, null, 10);

                    // Check to see if there were errors in this build
                    if (excel2XML.getDTCompiler().getErrors().size() > 0) {
                        throw new RuntimeException("Errors found during compilation of: " + name);
                    }
                } catch (Exception e) {
                    // Error while compiling ruleset
                    throw new RuntimeException("Error compiling ruleset: " + name, e);
                }
            }
        }
    }
}
