package uk.tw.energy.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class MeterReadings {

    @NotEmpty (message = "electricityReadings value is empty")
    private List<@Valid ElectricityReading> electricityReadings;

    @NotBlank (message = "smartMeterId value is blank")
    private String smartMeterId;

    public MeterReadings(String smartMeterId, List<ElectricityReading> electricityReadings) {
        this.smartMeterId = smartMeterId;
        this.electricityReadings = electricityReadings;
    }

    public List<ElectricityReading> getElectricityReadings() {
        return electricityReadings;
    }

    public String getSmartMeterId() {
        return smartMeterId;
    }

}
