/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.motivator.test;

import com.dtrules.entity.IREntity;
import com.dtrules.infrastructure.RulesException;
import com.dtrules.interpreter.RInteger;
import com.dtrules.mapping.DataMap;
import com.dtrules.mapping.Mapping;
import com.dtrules.session.DTState;
import com.dtrules.session.IRSession;
import com.dtrules.session.RuleSet;
import com.dtrules.session.RulesDirectory;
import com.lela.motivator.dto.ResultAndData;
import com.lela.motivator.junit.ParameterizedWithName;
import org.apache.commons.io.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * User: Chris Tallent
 * Date: 2/17/12
 * Time: 2:04 PM
 */
@RunWith(ParameterizedWithName.class)
public class DtrulesMotivatorTest {
    private static final Logger log = LoggerFactory.getLogger(DtrulesMotivatorTest.class);

    private static final String TEST_FOLDER = "dtrules";
    private static final String XLS_EXTENSION = "xls";
    private static final String XLSX_EXTENSION = "xlsx";
    
    private static final String RESULT_TEXT = "result";

    private static final boolean verbose;
    private static String testFolderArg;
    private static final Resource testFolder;
    static {
        testFolderArg = System.getProperty("dtrules.testFolder");
        if (testFolderArg == null) {
            testFolderArg = TEST_FOLDER;
        }
        System.out.println("DtrulesMotivatorTest using folder: " + testFolderArg);
        testFolder = new ClassPathResource(testFolderArg);

        String verboseArg = System.getProperty("dtrules.verbose");
        if ("true".equals(verboseArg)) {
            System.out.println("DtrulesMotivatorTest Verbose mode");
            verbose = true;
        } else {
            verbose = false;
        }
    }

    private ResultAndData testcase;
    
    public DtrulesMotivatorTest(ResultAndData testcase) {
        this.testcase = testcase;
    }

    @ParameterizedWithName.Parameters
    public static Collection<Object[]> getTestcaseFiles()
    throws IOException, RulesException {
        
        Collection<Object[]> params = new ArrayList<Object[]>();
        
        if (!testFolder.exists()) {
            throw new IllegalArgumentException("Test folder does not exist: " + testFolderArg);
        }

        // Recursively find excel files to process
        File rootDir = testFolder.getFile();
        for (Object fileObject : FileUtils.listFiles(rootDir, new String[] { XLS_EXTENSION, XLSX_EXTENSION }, true)) {
            File file = (File)fileObject;

            // Make sure to ignore any Excel work files starting with ~
            if (file.getName().startsWith("~")) {
                continue;
            }

            List<ResultAndData> testcases = readExcelFile(file);

            for (ResultAndData testcase : testcases) {
                params.add(new Object[] { testcase });
            }
        }

        // If there are zero test cases in the whole workbook, consider this to be an error... we don't
        // want to mistakenly assume tests are passing when they don't exist
        if (params.isEmpty()) {
            throw new IllegalArgumentException("No motivator test cases found");
        }

        return params;
    }
    
    @ParameterizedWithName.Name
    public String getName() {
        String filename = testcase.getContainingFile().getName();
        filename = filename.replace(".", "_");
        return String.format("%s, Sheet: %s, Row: %s]", filename, testcase.getContainingSheet().getSheetName(), testcase.getRowInSheet());
    }

    @Test
    public void testMotivators()
    throws RulesException {

        log.info("Processing DTRules test case row: " + testcase.getRowInSheet() + ", in file " + testcase.getContainingFile().getPath());

        int result = executeDtrules(testcase);

        // IF the motivator result was not equal to the expected result, fail
        assertEquals("Test failed for file: " + testcase.getContainingFile().getPath() + ", Row: " + testcase.getRowInSheet(), testcase.getResult(), result);
    }

    private static List<ResultAndData> readExcelFile(File file) throws IOException, RulesException {
        
        List<ResultAndData> testcases = new ArrayList<ResultAndData>();
        
        InputStream in = null;
        try {
            in = new FileInputStream(file);

            Workbook wb = WorkbookFactory.create(in);

            // Get the first sheet to extract the Ruleset and starting sheet
            Sheet sheet = wb.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() < 3) {
                throw new IllegalArgumentException("Rule Set, Sheet and Entity are not defined in file: " + file.getPath());
            }

            Row row = sheet.getRow(0);
            String ruleSet = getCellAsString(row.getCell(1));
            
            row = sheet.getRow(1);
            String ruleSheet = getCellAsString(row.getCell(1));

            row = sheet.getRow(2);
            String entity = getCellAsString(row.getCell(1));

            if (!StringUtils.hasText(ruleSet)) {
                throw new IllegalArgumentException("No rule set is defined on first sheet of file: " + file.getPath());
            }

            if (!StringUtils.hasText(ruleSheet)) {
                throw new IllegalArgumentException("No rule sheet is defined on first sheet of file: " + file.getPath());
            }

            if (!StringUtils.hasText(entity)) {
                throw new IllegalArgumentException("No entity is defined on first sheet of file: " + file.getPath());
            }

            // Get the sheets with rules
            if (wb.getNumberOfSheets() > 1) {
                for (int i=1; i<wb.getNumberOfSheets(); i++) {
                    extractExcelTestcases(file, wb.getSheetAt(i), ruleSet, ruleSheet, entity, testcases);
                }
            }

            // If there are zero test cases in the whole workbook, consider this to be an error... we don't
            // want to mistakenly assume tests are passing when they don't exist
            if (testcases.isEmpty()) {
                throw new IllegalArgumentException("No motivator test cases found in file: " + file.getPath());
            }
        } catch (InvalidFormatException e) {
            fail("Could not read file: " + file.getName());
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return testcases;
    }
    
    private static void extractExcelTestcases(File file, Sheet sheet, String ruleSet, String ruleSheet, String entity, List<ResultAndData> testcases) {

        if (sheet.getPhysicalNumberOfRows() < 2) {
            // Ignore this sheet as it cannot have both a row of column definitions plus any data
            log.warn(String.format("No motivator testcase data found in %s, sheet \"%s\"", file.getPath(), sheet.getSheetName()));
            return;
        }

        Row row = sheet.getRow(0);

        // Verify that the first row has column header
        Cell cell = row.getCell(0);
        String content = getCellAsString(cell);

        // If this first cell in the first row has the text "result" then
        // this sheet is expected to have test cases... build the list of data column names
        Map<Short, String> columnNames = new HashMap<Short, String>();
        if (RESULT_TEXT.equals(content.toLowerCase())) {
            for (short i=1; i<row.getLastCellNum(); i++) {
                cell = row.getCell(i);
                content = cell.getStringCellValue();

                if (StringUtils.hasLength(content)) {
                    columnNames.put(i, content);

                    if (log.isDebugEnabled()) {
                        log.debug("Found column name: " + content);
                    }
                }
            }

            // Read the data for the sheet
            int lastRowNum = sheet.getLastRowNum();
            for (int i=1; i<=lastRowNum; i++) {
                ResultAndData data = new ResultAndData(ruleSet, ruleSheet, entity, file, sheet, i+1);
                boolean foundData = false;
                row = sheet.getRow(i);

                // Get the result
                String resultAsString = getCellAsString(row.getCell(0));
                try {
                    data.setResult(Integer.parseInt(resultAsString));
                } catch (NumberFormatException e) {
                    fail("Expected result is not a valid Integer for file: " + file.getPath() + ", Row: " + (i+1) + " ... value was: " + resultAsString);
                }

                // Get the data values
                for (Short cellNum : columnNames.keySet()) {
                    cell = row.getCell(cellNum);
                    content = getCellAsString(cell);

                    if (StringUtils.hasLength(content)) {
                        foundData = true;
                        data.addData(columnNames.get(cellNum), content);
                    }
                }

                // Assuming data was found on the row, add the test data to our
                // list to be provided to the parameterized tests
                if (foundData) {
                    testcases.add(data);
                }
            }
        }        
    }

    private int executeDtrules(ResultAndData testData)
    throws RulesException {
        
        // Get rules directory from classpath
        InputStream s = DtrulesMotivatorTest.class.getClassLoader().getResourceAsStream("dtrules/DTRules.xml");

        // Create the rules directory configuration in order
        // to determine which rulesets are defined
        RulesDirectory rd = new RulesDirectory("",s);

        RuleSet rs = rd.getRuleSet(testcase.getRuleSet());
        IRSession session = rs.newSession();
        Mapping mapping = session.getMapping();

        // Create a data map
        DataMap map = new DataMap(session, "job");
        map.opentag(testData.getEntity());

        for (String key : testData.getDataMap().keySet()) {
            map.printdata(key, testData.getDataMap().get(key));
        }

        map.closetag();

        // Debug the data xml
        if (log.isDebugEnabled()) {
            log.debug(map.xmlPull(false));
        }

        DTState state   = session.getState();
        if (verbose) {
            state.setState(DTState.DEBUG | DTState.TRACE);
            state.setState(DTState.VERBOSE);
        }

        mapping.loadData(session, map);
        session.execute(testcase.getRuleSheet());

        //printReport(session, System.out);

        // Extract the job result
        IREntity job     = state.findEntity("job");
        RInteger result = (RInteger) job.get("result");

        return result.intValue();
    }

    private static String getCellAsString(Cell cell) {
        
        String value = null;
        
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        value = cell.getDateCellValue().toString();
                    } else {
                        double d = cell.getNumericCellValue();
                        
                        // If the int value is the same as the decimal value
                        // strip the zeros after the decimal point
                        int i = (int)d;
                        
                        if (i == d) {
                            value = String.valueOf(i);
                        } else {
                            value = String.valueOf(d);
                        }
                    }
                    break;
                case Cell.CELL_TYPE_BOOLEAN:
                    value = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    value = cell.getCellFormula();
                    break;
            }
        }
        
        return value;
    }

    public static void printReport(IRSession session, PrintStream _out) throws RulesException {
        DTState state   = session.getState();
        IREntity job     = state.findEntity("job");

        RInteger result = (RInteger) job.get("result");
        System.out.println("result: " + result.intValue());
    }
}
