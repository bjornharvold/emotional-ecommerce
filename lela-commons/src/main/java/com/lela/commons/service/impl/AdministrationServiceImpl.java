/*
 * Copyright (c) 2011. Purple Door Systems, BV.
 */

package com.lela.commons.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lela.commons.service.AdministrationService;
import com.lela.commons.service.ItemService;
import com.lela.commons.service.ProductEngineService;
import com.lela.domain.document.AbstractItem;
import com.lela.domain.dto.OwnerItemsQuery;

@Service("administrationService")
public class AdministrationServiceImpl implements AdministrationService {
    private final static Logger log = LoggerFactory.getLogger(AdministrationServiceImpl.class);

    private final ProductEngineService productEngineService;
    private final ItemService itemService;

    @Autowired
    public AdministrationServiceImpl(ProductEngineService productEngineService,
                                     ItemService itemService) {
        this.productEngineService = productEngineService;
        this.itemService = itemService;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_OWNER_AS_ADMIN')")
    @Override
    public List<AbstractItem> processOwnersDataFromExcel(OwnerItemsQuery query, MultipartFile excelFile, HSSFWorkbook updateWorkBook) {
        List<AbstractItem> items = new ArrayList<AbstractItem>();

        /**
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        String firstName = null;
        String lastName = null;
        Integer a = null;
        Integer b = null;
        Integer c = null;
        Integer d = null;
        Integer e = null;
        Integer f = null;
        Integer g = null;
        List<HSSFRow> inputRows = new ArrayList<HSSFRow>();
        Map<String, List> userDataMap = new HashMap<String, List>();
        try {
            POIFSFileSystem myFileSystem = new POIFSFileSystem(excelFile.getInputStream());
            HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
            HSSFSheet mySheet = myWorkBook.getSheetAt(0);

            Iterator rowIterator = mySheet.rowIterator();
            HSSFRow myRow = (HSSFRow) rowIterator.next();
            inputRows.add(myRow);

            int counter = 0;
            while (rowIterator.hasNext()) {
                myRow = (HSSFRow) rowIterator.next();
                inputRows.add(myRow);

                firstName = myRow.getCell(0).getStringCellValue();
                lastName = myRow.getCell(1).getStringCellValue();
                a = (int) myRow.getCell(5).getNumericCellValue();
                b = (int) myRow.getCell(6).getNumericCellValue();
                c = (int) myRow.getCell(7).getNumericCellValue();
                d = (int) myRow.getCell(8).getNumericCellValue();
                e = (int) myRow.getCell(9).getNumericCellValue();
                f = (int) myRow.getCell(10).getNumericCellValue();
                g = (int) myRow.getCell(11).getNumericCellValue();

                if (principal != null && (a != null || b != null ||
                        c != null || d != null || e != null || f != null || g != null)) {

                    if (a != null) {
                        principal.getUser().getMotivator().put("A", a);
                    } else {
                        principal.getUser().getMotivator().put("A", 0);
                    }
                    if (b != null) {
                        principal.getUser().getMotivator().put("B", b);
                    } else {
                        principal.getUser().getMotivator().put("B", 0);
                    }
                    if (c != null) {
                        principal.getUser().getMotivator().put("C", c);
                    } else {
                        principal.getUser().getMotivator().put("C", 0);
                    }
                    if (d != null) {
                        principal.getUser().getMotivator().put("D", d);
                    } else {
                        principal.getUser().getMotivator().put("D", 0);
                    }
                    if (e != null) {
                        principal.getUser().getMotivator().put("E", e);
                    } else {
                        principal.getUser().getMotivator().put("E", 0);
                    }
                    if (f != null) {
                        principal.getUser().getMotivator().put("F", f);
                    } else {
                        principal.getUser().getMotivator().put("F", 0);
                    }
                    if (g != null) {
                        principal.getUser().getMotivator().put("G", g);
                    } else {
                        principal.getUser().getMotivator().put("G", 0);
                    }

                    List<RelevantItem> entryRelevantItems = null;
                    if (StringUtils.isNotBlank(query.getNm())) {
                        List<String> brandNames = Arrays.asList(query.getNm().split(" or "));
                        for (String brandName : brandNames) {
                            entryRelevantItems = productEngineService.findRelevantItemsByOwner(new RelevantOwnerItemsQuery(brandName, principal.getUser(), null, null));
                            items.addAll(entryRelevantItems);
                            userDataMap.put(firstName + "," + lastName + "," + counter, entryRelevantItems);
                            counter++;
                        }
                    } else {
                        entryRelevantItems = productEngineService.findRelevantItemsByOwner(new RelevantOwnerItemsQuery(null, principal.getUser(), null, null));
                        items.addAll(entryRelevantItems);
                        userDataMap.put(firstName + "," + lastName + "," + counter, entryRelevantItems);
                        counter++;
                    }
                } else {
                    List<Item> entryItems = null;
                    if (StringUtils.isNotBlank(query.getNm())) {
                        List<String> brandNames = Arrays.asList(query.getNm().split(" or "));
                        for (String brandName : brandNames) {
                            entryItems = itemService.findItemsByOwnerName(brandName);
                            items.addAll(entryItems);
                            userDataMap.put(firstName + "," + lastName + "," + counter, entryItems);
                            counter++;
                        }
                    } else {
                        entryItems = itemService.findItemsByOwnerName(null);
                        items.addAll(entryItems);
                        userDataMap.put(firstName + "," + lastName + "," + counter, entryItems);
                        counter++;
                    }
                }
            }

            // If results data exists then create new processed file for output of data
            if (userDataMap.size() > 0) {
                HSSFSheet updateSheet = updateWorkBook.createSheet("output");
                HSSFRow updateRow = null;
                HSSFCell updateCell = null;

                // Load input data from uploaded file into processed file
                HSSFCell inputCell = null;
                for (HSSFRow inputRow : inputRows) {
                    updateRow = updateSheet.createRow(inputRow.getRowNum());
                    for (Iterator cellIterator = inputRow.cellIterator(); cellIterator.hasNext(); ) {
                        inputCell = (HSSFCell) cellIterator.next();
                        updateCell = updateRow.createCell(inputCell.getColumnIndex());
                        if (inputCell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
                            updateCell.setCellValue(inputCell.getStringCellValue());
                        } else if (inputCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
                            updateCell.setCellValue(inputCell.getNumericCellValue());
                        }
                    }
                }

                // Load results data from uploaded file into processed file
                int lastRowNum = mySheet.getLastRowNum();
                String userNameInfo = null;
                List<AbstractItem> userEntryItems = null;
                Map<String, Object> attributesMap = null;

                // Create separator row
                HSSFCellStyle cellStyle = updateWorkBook.createCellStyle();
                cellStyle.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);
                cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                updateRow = updateSheet.createRow(++lastRowNum);
                updateRow.setRowStyle(cellStyle);

                for (String s : userDataMap.keySet()) {
                    userNameInfo = s;
                    userEntryItems = (List<AbstractItem>) userDataMap.get(userNameInfo);
                    String[] userNameArray = userNameInfo.split(",");

                    for (AbstractItem item : userEntryItems) {
                        attributesMap = item.getAttributes();

                        updateRow = updateSheet.createRow(++lastRowNum);

                        updateCell = updateRow.createCell(0);
                        updateCell.setCellValue(userNameArray[0]);
                        updateCell = updateRow.createCell(1);
                        updateCell.setCellValue(userNameArray[1]);

                        updateCell = updateRow.createCell(2);
                        updateCell.setCellValue(item.getCtgry().getNm());
                        updateCell = updateRow.createCell(3);
                        updateCell.setCellValue(item.getWnr().getNm());
                        updateCell = updateRow.createCell(4);
                        updateCell.setCellValue((String) attributesMap.get("ProductModelName"));
                        updateCell = updateRow.createCell(5);
                        updateCell.setCellValue((String) attributesMap.get("ProductUrl"));

                        updateCell = updateRow.createCell(6);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("A") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("A"));
                        }
                        updateCell = updateRow.createCell(7);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("B") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("B"));
                        }
                        updateCell = updateRow.createCell(8);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("C") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("C"));
                        }
                        updateCell = updateRow.createCell(9);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("D") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("D"));
                        }
                        updateCell = updateRow.createCell(10);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("E") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("E"));
                        }
                        updateCell = updateRow.createCell(11);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("F") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("F"));
                        }
                        updateCell = updateRow.createCell(12);
                        if ((item.getMotivator() != null) && (item.getMotivator().get("G") != null)) {
                            updateCell.setCellValue(item.getMotivator().get("G"));
                        }

                        updateCell = updateRow.createCell(14);
                        updateCell.setCellValue((String) attributesMap.get("CRTested"));
                        updateCell = updateRow.createCell(15);
                        updateCell.setCellValue((String) attributesMap.get("CROverallScore"));
                        updateCell = updateRow.createCell(16);
                        updateCell.setCellValue((String) attributesMap.get("BrandFacebookLikes"));
                        updateCell = updateRow.createCell(17);
                        updateCell.setCellValue((String) attributesMap.get("BrandTwitterFollowers"));
                        updateCell = updateRow.createCell(18);
                        updateCell.setCellValue((String) attributesMap.get("BBBrandRating"));
                        updateCell = updateRow.createCell(19);
                        updateCell.setCellValue((String) attributesMap.get("DesignStyle"));
                        updateCell = updateRow.createCell(20);
                        updateCell.setCellValue((String) attributesMap.get("NumberOfColorsVeryBrightOrPrimary"));
                        updateCell = updateRow.createCell(21);
                        updateCell.setCellValue((String) attributesMap.get("MediaSpotlight"));
                        updateCell = updateRow.createCell(22);
                        updateCell.setCellValue((String) attributesMap.get("CelebrityStatus"));
                        updateCell = updateRow.createCell(23);
                        if (attributesMap.get("ListPrice") != null) {
                            updateCell.setCellValue(NumberUtils.safeDouble(attributesMap.get("ListPrice")));
                        }

                        if ((item instanceof RelevantItem) && (((RelevantItem) item).getMtvtrrlvncy() != null)) {
                            // Create next work with relevancy data
                            updateRow = updateSheet.createRow(++lastRowNum);
                            updateCell = updateRow.createCell(6);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("A") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("A"));
                            }
                            updateCell = updateRow.createCell(7);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("B") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("B"));
                            }
                            updateCell = updateRow.createCell(8);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("C") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("C"));
                            }
                            updateCell = updateRow.createCell(9);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("D") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("D"));
                            }
                            updateCell = updateRow.createCell(10);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("E") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("E"));
                            }
                            updateCell = updateRow.createCell(11);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("F") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("F"));
                            }
                            updateCell = updateRow.createCell(12);
                            if (((RelevantItem) item).getMtvtrrlvncy().get("G") != null) {
                                updateCell.setCellValue(((RelevantItem) item).getMtvtrrlvncy().get("G"));
                            }
                            updateCell = updateRow.createCell(13);
                            updateCell.setCellValue(((RelevantItem) item).getTtlrlvncy());
                        }
                        // Create separator row
                        updateRow = updateSheet.createRow(++lastRowNum);
                        updateRow.setRowStyle(cellStyle);
                    }
                }
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        */

        return items;
    }

    @PreAuthorize("hasAnyRole('RIGHT_READ_OWNER_AS_ADMIN')")
    @Override
    public List<AbstractItem> processOwnersDataFromInputs(OwnerItemsQuery query) {
        List<AbstractItem> items = new ArrayList<AbstractItem>();

        /*
        Principal principal = SpringSecurityHelper.getSecurityContextPrincipal();
        if (principal != null && (query.getA() != null || query.getB() != null ||
                query.getC() != null || query.getD() != null || query.getE() != null || query.getF() != null || query.getG() != null)) {

            if (query.getA() != null) {
                principal.getUser().getMotivator().put("A", query.getA());
            } else {
                principal.getUser().getMotivator().put("A", 0);
            }
            if (query.getB() != null) {
                principal.getUser().getMotivator().put("B", query.getB());
            } else {
                principal.getUser().getMotivator().put("B", 0);
            }
            if (query.getC() != null) {
                principal.getUser().getMotivator().put("C", query.getC());
            } else {
                principal.getUser().getMotivator().put("C", 0);
            }
            if (query.getD() != null) {
                principal.getUser().getMotivator().put("D", query.getD());
            } else {
                principal.getUser().getMotivator().put("D", 0);
            }
            if (query.getE() != null) {
                principal.getUser().getMotivator().put("E", query.getE());
            } else {
                principal.getUser().getMotivator().put("E", 0);
            }
            if (query.getF() != null) {
                principal.getUser().getMotivator().put("F", query.getF());
            } else {
                principal.getUser().getMotivator().put("F", 0);
            }
            if (query.getG() != null) {
                principal.getUser().getMotivator().put("G", query.getG());
            } else {
                principal.getUser().getMotivator().put("G", 0);
            }

            if (StringUtils.isNotBlank(query.getNm())) {
                List<String> brandNames = Arrays.asList(query.getNm().split(" or "));
                for (String brandName : brandNames) {
                    items.addAll(productEngineService.findRelevantItemsByOwner(new RelevantOwnerItemsQuery(brandName, principal.getUser(), null, null)));
                }
            } else {
                items.addAll(productEngineService.findRelevantItemsByOwner(new RelevantOwnerItemsQuery(null, principal.getUser(), null, null)));
            }
        } else {

            if (StringUtils.isNotBlank(query.getNm())) {
                List<String> brandNames = Arrays.asList(query.getNm().split(" or "));
                for (String brandName : brandNames) {
                    items.addAll(itemService.findItemsByOwnerName(brandName));
                }
            } else {
                items.addAll(itemService.findItemsByOwnerName(null));
            }
        }

        */

        return items;
    }
}
