package uk.tw.energy.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

public class ElectricityReading {

    @NotNull (message = "Time is null")
    private Instant time;
    @NotNull (message = "Reading is null")
    private BigDecimal reading; // kW

    public ElectricityReading(Instant time, BigDecimal reading) {
        this.time = time;
        this.reading = reading;
    }

    public BigDecimal getReading() {
        return reading;
    }

    public Instant getTime() {
        return time;
    }
}
