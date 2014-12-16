/*
 * Copyright (c) 2012. Purple Door Systems, BV.
 */

package com.lela.domain.document;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Bjorn Harvold
 * Date: 8/31/12
 * Time: 3:58 PM
 * Responsibility:
 */
public class Note extends AbstractNote implements Serializable {
    private static final long serialVersionUID = -3017528906822111612L;


}
