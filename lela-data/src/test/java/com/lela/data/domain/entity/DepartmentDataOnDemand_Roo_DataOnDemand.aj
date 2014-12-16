// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Department;
import com.lela.data.domain.entity.DepartmentDataOnDemand;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;

privileged aspect DepartmentDataOnDemand_Roo_DataOnDemand {
    
    declare @type: DepartmentDataOnDemand: @Component;
    
    private Random DepartmentDataOnDemand.rnd = new SecureRandom();
    
    private List<Department> DepartmentDataOnDemand.data;
    
    public Department DepartmentDataOnDemand.getNewTransientDepartment(int index) {
        Department obj = new Department();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setDepartmentName(obj, index);
        setDepartmentOrder(obj, index);
        setDepartmentStatus(obj, index);
        setDepartmentURLName(obj, index);
        setDirty(obj, index);
        setLelaUrl(obj, index);
        setNavbar(obj, index);
        return obj;
    }
    
    public void DepartmentDataOnDemand.setDateCreated(Department obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void DepartmentDataOnDemand.setDateModified(Department obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void DepartmentDataOnDemand.setDepartmentName(Department obj, int index) {
        String departmentName = "departmentName_" + index;
        obj.setDepartmentName(departmentName);
    }
    
    public void DepartmentDataOnDemand.setDepartmentOrder(Department obj, int index) {
        Integer departmentOrder = new Integer(index);
        obj.setDepartmentOrder(departmentOrder);
    }
    
    public void DepartmentDataOnDemand.setDepartmentStatus(Department obj, int index) {
        String departmentStatus = "departmentStatus_" + index;
        obj.setDepartmentStatus(departmentStatus);
    }
    
    public void DepartmentDataOnDemand.setDepartmentURLName(Department obj, int index) {
        String departmentURLName = "departmentURLName_" + index;
        obj.setDepartmentURLName(departmentURLName);
    }
    
    public void DepartmentDataOnDemand.setDirty(Department obj, int index) {
        Boolean dirty = Boolean.TRUE;
        obj.setDirty(dirty);
    }
    
    public void DepartmentDataOnDemand.setLelaUrl(Department obj, int index) {
        String lelaUrl = "lelaUrl_" + index;
        obj.setLelaUrl(lelaUrl);
    }
    
    public Department DepartmentDataOnDemand.getSpecificDepartment(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Department obj = data.get(index);
        Long id = obj.getId();
        return Department.findDepartment(id);
    }
    
    public Department DepartmentDataOnDemand.getRandomDepartment() {
        init();
        Department obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Department.findDepartment(id);
    }
    
    public boolean DepartmentDataOnDemand.modifyDepartment(Department obj) {
        return false;
    }
    
    public void DepartmentDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Department.findDepartmentEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Department' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Department>();
        for (int i = 0; i < 10; i++) {
            Department obj = getNewTransientDepartment(i);
            try {
                obj.persist();
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
    
}
