package uk.tw.energy.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    private final Map<String, String> smartMeterToPricePlanAccounts;

    public AccountService(Map<String, String> smartMeterToPricePlanAccounts) {
        logger.debug("inside AccountService::Constructor");
        this.smartMeterToPricePlanAccounts = smartMeterToPricePlanAccounts;
    }

    public String getPricePlanIdForSmartMeterId(String smartMeterId) {
        logger.debug("inside AccountService::getPricePlanIdForSmartMeterId");
        return smartMeterToPricePlanAccounts.get(smartMeterId);
    }
}
