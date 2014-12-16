// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.lela.data.domain.entity;

import com.lela.data.domain.entity.Network;
import com.lela.data.domain.entity.NetworkDataOnDemand;
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

privileged aspect NetworkDataOnDemand_Roo_DataOnDemand {
    
    declare @type: NetworkDataOnDemand: @Component;
    
    private Random NetworkDataOnDemand.rnd = new SecureRandom();
    
    private List<Network> NetworkDataOnDemand.data;
    
    public Network NetworkDataOnDemand.getNewTransientNetwork(int index) {
        Network obj = new Network();
        setDateCreated(obj, index);
        setDateModified(obj, index);
        setEnabled(obj, index);
        setNetworkName(obj, index);
        setPopshopsDesignator(obj, index);
        return obj;
    }
    
    public void NetworkDataOnDemand.setDateCreated(Network obj, int index) {
        Date dateCreated = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateCreated(dateCreated);
    }
    
    public void NetworkDataOnDemand.setDateModified(Network obj, int index) {
        Date dateModified = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH), Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), Calendar.getInstance().get(Calendar.SECOND) + new Double(Math.random() * 1000).intValue()).getTime();
        obj.setDateModified(dateModified);
    }
    
    public void NetworkDataOnDemand.setEnabled(Network obj, int index) {
        Boolean enabled = Boolean.TRUE;
        obj.setEnabled(enabled);
    }
    
    public void NetworkDataOnDemand.setNetworkName(Network obj, int index) {
        String networkName = "networkName_" + index;
        if (networkName.length() > 25) {
            networkName = networkName.substring(0, 25);
        }
        obj.setNetworkName(networkName);
    }
    
    public void NetworkDataOnDemand.setPopshopsDesignator(Network obj, int index) {
        String popshopsDesignator = "pop_" + index;
        if (popshopsDesignator.length() > 5) {
            popshopsDesignator = popshopsDesignator.substring(0, 5);
        }
        obj.setPopshopsDesignator(popshopsDesignator);
    }
    
    public Network NetworkDataOnDemand.getSpecificNetwork(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Network obj = data.get(index);
        Long id = obj.getId();
        return Network.findNetwork(id);
    }
    
    public Network NetworkDataOnDemand.getRandomNetwork() {
        init();
        Network obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Network.findNetwork(id);
    }
    
    public boolean NetworkDataOnDemand.modifyNetwork(Network obj) {
        return false;
    }
    
    public void NetworkDataOnDemand.init() {
        int from = 0;
        int to = 10;
        data = Network.findNetworkEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Network' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Network>();
        for (int i = 0; i < 10; i++) {
            Network obj = getNewTransientNetwork(i);
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