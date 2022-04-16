package uk.tw.energy.service;

import org.springframework.stereotype.Service;
import uk.tw.energy.domain.ElectricityReading;

import java.util.*;

@Service
public class MeterReadingService {

    private final Map<String, List<ElectricityReading>> meterAssociatedReadings;

    public MeterReadingService(Map<String, List<ElectricityReading>> meterAssociatedReadings) {
        this.meterAssociatedReadings = meterAssociatedReadings;
    }

    public Optional<List<ElectricityReading>> getReadings(String smartMeterId) {
        return Optional.ofNullable(meterAssociatedReadings.get(smartMeterId));
    }

    public void storeReadings(String smartMeterId, List<ElectricityReading> electricityReadings) {
        List<ElectricityReading> electricityReadingsList = meterAssociatedReadings.computeIfAbsent(smartMeterId, k -> new ArrayList<>());
        electricityReadingsList.addAll(electricityReadings);
    }
}
