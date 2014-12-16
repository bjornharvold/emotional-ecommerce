package com.lela.commons.service.impl;

import com.lela.commons.repository.AffiliateSaleRepository;
import com.lela.commons.service.AffiliateSaleService;
import com.lela.domain.document.AffiliateSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: ballmw
 * Date: 12/14/12
 * Time: 7:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Service("affiliateSaleService")
public class AffiliateSaleServiceImpl implements AffiliateSaleService {
    AffiliateSaleRepository affiliateSaleRepository;

    @Autowired
    public AffiliateSaleServiceImpl(AffiliateSaleRepository affiliateSaleRepository)
    {
        this.affiliateSaleRepository = affiliateSaleRepository;
    }

    public void save(AffiliateSale affiliateSale)
    {
        affiliateSaleRepository.save(affiliateSale);
    }
}
