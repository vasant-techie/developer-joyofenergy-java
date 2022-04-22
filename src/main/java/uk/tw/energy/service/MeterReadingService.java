package uk.tw.energy.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;

import java.util.*;

@Service
public class MeterReadingService {

    private static final Logger logger = LogManager.getLogger(MeterReadingService.class);

    private final Map<String, List<ElectricityReading>> meterAssociatedReadings;

    public MeterReadingService(Map<String, List<ElectricityReading>> meterAssociatedReadings) {
        logger.debug("Inside MeterReadingService::Constructor");
        this.meterAssociatedReadings = meterAssociatedReadings;
    }

    public Optional<List<ElectricityReading>> getReadings(String smartMeterId) {
        logger.debug("Inside MeterReadingService::getReadings({})", smartMeterId);

        return Optional.ofNullable(meterAssociatedReadings.get(smartMeterId));
    }

    public void storeReadings(String smartMeterId, List<ElectricityReading> electricityReadings) {
        logger.debug("Inside MeterReadingService::storeReadings({})", smartMeterId);

        List<ElectricityReading> electricityReadingsList = meterAssociatedReadings.computeIfAbsent(smartMeterId, k -> new ArrayList<>());
        electricityReadingsList.addAll(electricityReadings);
    }
}
