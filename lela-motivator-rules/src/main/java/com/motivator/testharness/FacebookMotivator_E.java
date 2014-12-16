package com.motivator.testharness;

import com.motivator.compiler.CompileMotivators;
import com.dtrules.entity.IREntity;
import com.dtrules.infrastructure.RulesException;
import com.dtrules.interpreter.IRObject;
import com.dtrules.interpreter.RInteger;
import com.dtrules.session.DTState;
import com.dtrules.session.IRSession;
import com.dtrules.testsupport.ATestHarness;
import com.dtrules.testsupport.ITestHarness;
import com.dtrules.xmlparser.XMLPrinter;

import java.io.PrintStream;

/**
 * Created by IntelliJ IDEA.
 * User: Martin Gamboa
 * Date: 2/14/12
 * Time: 1:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class FacebookMotivator_E extends ATestHarness {

	    public boolean  Trace()                   { return true;                            }
	    public boolean  Console()                 { return true;                            }
	    public String   getPath()                 { return CompileMotivators.path;              }
	    public String   getRulesDirectoryPath()   { return getPath()+"xml/Facebook/";                }
	    public String   getRuleSetName()          { return "FacebookMotivator_E";                        }
	    public String   getDecisionTableName()    { return "Facebook_ME_Calculate";           }
	    public String   getRulesDirectoryFile()   { return "DTRules.xml";                   }

	    public static void main(String[] args) {
	        ITestHarness t = new FacebookMotivator_E();
	        t.runTests();
	    }

	    public void printReport(int runNumber, IRSession session, PrintStream _out) throws RulesException {
	    	 XMLPrinter xout = new XMLPrinter(_out);
	    	 DTState state   = session.getState();
	    	 IREntity job     = state.findEntity("job");

	    	 RInteger result = (RInteger) job.get("result");
	    	 System.out.println("result: " + result.intValue());
	    }

	    private void prt(XMLPrinter xout, IREntity entity, String attrib){
	        IRObject value = entity.get(attrib);
	        xout.printdata(attrib,value);
	    }

	}
