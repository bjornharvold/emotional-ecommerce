/** 
 * Copyright 2004-2009 DTRules.com, Inc.
 *   
 * Licensed under the Apache License, Version 2.0 (the "License");  
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at  
 *   
 *      http://www.apache.org/licenses/LICENSE-2.0  
 *   
 * Unless required by applicable law or agreed to in writing, software  
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  
 * See the License for the specific language governing permissions and  
 * limitations under the License.  
 **/ 
package com.motivator.testharness;


import java.io.PrintStream;

import com.motivator.compiler.CompileMotivators;
import com.dtrules.entity.IREntity;
import com.dtrules.infrastructure.RulesException;
import com.dtrules.interpreter.IRObject;
import com.dtrules.interpreter.RArray;
import com.dtrules.interpreter.RInteger;
import com.dtrules.session.DTState;
import com.dtrules.session.IRSession;
import com.dtrules.testsupport.ATestHarness;
import com.dtrules.testsupport.ITestHarness;
import com.dtrules.xmlparser.XMLPrinter;

public class TestCribMotivator_B extends ATestHarness {
    
	    public boolean  Trace()                   { return true;                            }
	    public boolean  Console()                 { return true;                            }
	    public String   getPath()                 { return CompileMotivators.path;              }
	    public String   getRulesDirectoryPath()   { return getPath()+"xml/Cribs/";                }
	    public String   getRuleSetName()          { return "CribMotivator_B";                        }
	    public String   getDecisionTableName()    { return "CRIBS_MB_Calculate";           }
	    public String   getRulesDirectoryFile()   { return "DTRules.xml";                   }             
	   
	    public static void main(String[] args) {
	        ITestHarness t = new TestCribMotivator_B();
	        t.runTests();
	    }
	    
	    public void printReport(int runNumber, IRSession session, PrintStream _out) throws RulesException {
	    	 XMLPrinter xout = new XMLPrinter(_out);  
	    	 DTState state   = session.getState();  
	    	 IREntity job     = state.findEntity("job");
	    	 RArray   results = job.get("job.cribs").rArrayValue();
	    	 //RInteger result = (RInteger) job.get("result");
	         //RInteger cribId = (RInteger) job.get("CribID");
	         RInteger brandId = (RInteger) job.get("BrandID");	         
	            
			 //System.out.println("result " + result.intValue());
			 //System.out.println("cribId " + cribId.intValue());
			 System.out.println("brandId " + brandId.intValue());
	    	 
	    }
	 
	    private void prt(XMLPrinter xout, IREntity entity, String attrib){
	        IRObject value = entity.get(attrib);
	        xout.printdata(attrib,value);
	    }
	    
	}    
	    
