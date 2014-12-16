package com.lela.data.web;

import com.lela.data.domain.dto.SalesTotals;
import com.lela.data.domain.entity.AffiliateTransaction;
import org.joda.time.DateMidnight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;

@RequestMapping("/affiliatereporting/**")
@Controller
public class AffiliateReportingController {

    final static Logger logger = LoggerFactory.getLogger(AffiliateReportingController.class);

    @RequestMapping(value = "/", produces = "text/html", method = RequestMethod.GET)
    public String index() {
        return "affiliatereporting/index";
    }

    @RequestMapping(value = "/detail", produces = "text/html", method = RequestMethod.GET)
    public String list(Model uiModel) {
        try
        {
        List<AffiliateTransaction> transactions = AffiliateTransaction.findAllAffiliateTransactions();
        Collections.sort(transactions, new Comparator<AffiliateTransaction>()
        {
            public int compare(AffiliateTransaction obj1, AffiliateTransaction obj2)
            {
                return obj1.getProcessDate().compareTo(obj2.getProcessDate());
            }
        });
        uiModel.addAttribute("transactions", transactions);
        }
        catch(RuntimeException e)
        {
            logger.error(e.getMessage(), e);
        }
        return "affiliatereporting/list";
    }

    @RequestMapping(value = "/monthly", produces = "text/html", method = RequestMethod.GET)
    public String salesPerMonth(Model uiModel)
    {
        List<AffiliateTransaction> transactions = AffiliateTransaction.findAllAffiliateTransactions();

        Map<DateMidnight, SalesTotals> monthlySalesTotals = new HashMap<DateMidnight, SalesTotals>();
        for(AffiliateTransaction transaction:transactions)
        {
            DateMidnight first = new DateMidnight(transaction.getProcessDate());
            first = first.withDayOfMonth(1);


            SalesTotals salesTotals = monthlySalesTotals.get(first);
            if(salesTotals == null)
            {
                salesTotals = new SalesTotals();
                salesTotals.setMonth(first.toDate());
            }

            salesTotals.setQuantity(salesTotals.getQuantity() + transaction.getQuantity());
            salesTotals.setSalesTotal(salesTotals.getSalesTotal().add(transaction.getSalesAmount()));
            salesTotals.setCommissionTotal(salesTotals.getCommissionTotal().add(transaction.getCommissionAmount()));
            monthlySalesTotals.put(first, salesTotals);
        }
        uiModel.addAttribute("salesTotals", monthlySalesTotals.values());

        return "affiliatereporting/monthly";
    }

}
