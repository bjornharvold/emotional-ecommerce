package com.lela.data.domain.entity;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(table="affiliate_report")
@AttributeOverride(name = "id", column = @Column(name = "AffiliateReportId", columnDefinition = "int(11)"))
public class AffiliateReport extends AbstractEntity{

    @Column(name = "FileName")
    private String fileName;

    @Column(name = "FilePath")
    private String filePath;

    @Column(name = "ReceivedDate")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
    private Date receivedDate;

    @OneToMany(mappedBy = "affiliateReport", fetch = FetchType.LAZY)
    private Set<AffiliateTransaction> affiliateTransaction;
}
