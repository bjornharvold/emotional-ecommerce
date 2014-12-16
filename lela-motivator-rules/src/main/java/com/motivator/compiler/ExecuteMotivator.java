/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.motivator.compiler;

import com.dtrules.entity.IREntity;
import com.dtrules.infrastructure.RulesException;
import com.dtrules.interpreter.RInteger;
import com.dtrules.mapping.Mapping;
import com.dtrules.session.DTState;
import com.dtrules.session.IRSession;
import com.dtrules.session.RuleSet;
import com.dtrules.session.RulesDirectory;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * User: Chris Tallent
 * Date: 2/14/12
 * Time: 5:36 PM
 */
public class ExecuteMotivator {

//    private static final Logger log = LoggerFactory.getLogger(ExecuteMotivator.class);

    public static String path    = System.getProperty("user.dir") + "/lela-motivator-rules/dtrules/testfiles";

    public static void main(String[] args) {

        // Get rules directory from classpath
        InputStream s = ExecuteMotivator.class.getClassLoader().getResourceAsStream("dtrules/DTRules.xml");

        // Create the rules directory configuration in order
        // to determine which rulesets are defined
        RulesDirectory rd = new RulesDirectory(path,s);

        try {

            RuleSet rs = rd.getRuleSet("TelevisionMotivator_C");
            IRSession session = rs.newSession();
            Mapping mapping = session.getMapping();
            mapping.loadData(session, path + "/testcase.xml");
            session.execute("Television_MC_Calculate");

            printReport(session, System.out);
        }catch(RulesException e){
            // Should any error occur, print out the message.
            System.out.println(e.toString());
        }
    }

    public static void printReport(IRSession session, PrintStream _out) throws RulesException {
        DTState state   = session.getState();
        IREntity job     = state.findEntity("job");

        RInteger result = (RInteger) job.get("result");
        System.out.println("result: " + result.intValue());
    }
}
