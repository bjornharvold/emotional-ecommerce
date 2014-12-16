/** 
 * Copyright 2004-2010 DTRules.com, Inc.
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

package com.motivator.compiler;

import com.dtrules.compiler.excel.util.Excel2XML;


/**
 * @author Paul Snow
 *
 */
public class CompileMotivators {

    /**
     * In Eclipse, System.getProperty("user.dir") returns the project
     * directory.  We add a slash to insure the path ends with a slash.
     */
    public static String path    = System.getProperty("user.dir")+"/lela-motivator-rules/dtrules";
    
    /**
     * Routine to compile decision tables.
     * @param args
     * @throws Exception
     */
    public static void main(String args[]) throws Exception { 
        try {
        	String [] maps = { "main" };
        	// System.out.println(path);
        	
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_B","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_C","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_D","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_E","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_F","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","StrollerMotivator_G","lela-motivator-rules/dtrules");
//
//        	Excel2XML.compile(path,"DTRules.xml","CribMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CribMotivator_B","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CribMotivator_C","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CribMotivator_E","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CribMotivator_G","lela-motivator-rules/dtrules");
//
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_B","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_C","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_E","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_F","lela-motivator-rules/dtrules");        	
//        	Excel2XML.compile(path,"DTRules.xml","CarSeatsMotivator_G","lela-motivator-rules/dtrules");
//
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_B","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_C","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_D","lela-motivator-rules/dtrules");        	
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_E","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_F","lela-motivator-rules/dtrules");        	
//        	Excel2XML.compile(path,"DTRules.xml","CarrierMotivator_G","lela-motivator-rules/dtrules");
//
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_B","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_C","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_D","lela-motivator-rules/dtrules");        	
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_E","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_F","lela-motivator-rules/dtrules");        	
//        	Excel2XML.compile(path,"DTRules.xml","DigicamMotivator_G","lela-motivator-rules/dtrules");
//
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_B","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_C","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_D","lela-motivator-rules/dtrules");        	
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_E","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_F","lela-motivator-rules/dtrules");        	
//			Excel2XML.compile(path,"DTRules.xml","BabyMonitorMotivator_G","lela-motivator-rules/dtrules");
//			
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_A","lela-motivator-rules/dtrules");
//        	Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_B","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_C","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_D","lela-motivator-rules/dtrules");        	
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_E","lela-motivator-rules/dtrules");
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_F","lela-motivator-rules/dtrules");        	
//			Excel2XML.compile(path,"DTRules.xml","TelevisionMotivator_G","lela-motivator-rules/dtrules");

//          Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_A","lela-motivator-rules/dtrules");
//          Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_B","lela-motivator-rules/dtrules");
//          Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_C","lela-motivator-rules/dtrules");
            Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_D","lela-motivator-rules/dtrules");
//          Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_E","lela-motivator-rules/dtrules");
//          Excel2XML.compile(path,"DTRules.xml","FacebookMotivator_F","lela-motivator-rules/dtrules");
        	
        } catch ( Exception ex ) {
            System.out.println("Failed to convert the Excel files");
            ex.printStackTrace();
            throw ex;
        }
        
        
        
     }
}
