package com.lela.commons.bootstrap.impl;

import com.lela.commons.bootstrap.Bootstrapper;
import com.lela.commons.bootstrap.BootstrapperException;
import com.lela.commons.service.OfferService;
import com.lela.domain.document.Offer;
import com.lela.domain.dto.Offers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Component("offerBootstrapper")
public class OfferBootstrapper extends AbstractBootstrapper implements Bootstrapper {
    private final static Logger log = LoggerFactory.getLogger(OfferBootstrapper.class);
    private static int populated = 0;
    private static int omitted = 0;
    private final Resource file = new ClassPathResource("bootstrap/offers.json");
    private final OfferService offerService;

    @Value("${bootstrapper.offer.enabled:true}")
    private Boolean enabled;

    @Autowired
    public OfferBootstrapper(OfferService offerService) {
        this.offerService = offerService;
    }

    @Override
    public void create() throws BootstrapperException {

        if (file.exists()) {
            processCreation();
        } else {
            throw new BootstrapperException("XML file could not be found");
        }

        log.info("Populated " + populated + " offers in db");
        log.info("Omitted " + omitted + " offers from db. Already exists.");
    }

    private void processCreation() throws BootstrapperException {
        try {

            persist(parseJSON());

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    private Offers parseJSON() throws Exception {
        return mapper.readValue(file.getInputStream(), Offers.class);
    }

    /**
     * Saves the admin users to the db before the application becomes active
     *
     * @param offers offers
     * @throws com.lela.commons.bootstrap.BootstrapperException
     *
     */
    private void persist(Offers offers) throws BootstrapperException {
        List<Offer> dbList = new ArrayList<Offer>();

        try {

            for (Offer offer : offers.getList()) {
                Offer tmp = offerService.findOfferByUrlName(offer.getRlnm());

                if (tmp == null) {
                    dbList.add(offer);
                    populated++;
                } else {
                    log.info("Offer already exists with url name: " + offer.getRlnm());
                    omitted++;
                }
            }

            // ready for save
            if (dbList.size() > 0) {
                offerService.saveOffers(dbList);
            }

        } catch (Exception e) {
            throw new BootstrapperException(e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "OfferBootstrapper";
    }

    @Override
    public Boolean getEnabled() {
        return enabled;
    }
}
