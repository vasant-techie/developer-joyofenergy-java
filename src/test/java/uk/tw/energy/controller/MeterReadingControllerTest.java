package uk.tw.energy.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import uk.tw.energy.builders.MeterReadingsBuilder;
import uk.tw.energy.domain.ElectricityReading;
import uk.tw.energy.domain.MeterReadings;
import uk.tw.energy.service.MeterReadingService;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MeterReadingControllerTest {

    private static final String SMART_METER_ID = "10101010";
    private MeterReadingController meterReadingController;
    private MeterReadingService meterReadingService;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        this.meterReadingService = new MeterReadingService(new HashMap<>());
        this.meterReadingController = new MeterReadingController(meterReadingService);
        ValidatorFactory vf = Validation.buildDefaultValidatorFactory();
        this.validator = vf.getValidator();
    }

    @Test
    public void givenNoMeterIdIsSuppliedWhenStoringShouldReturnErrorResponse() {
        MeterReadings meterReadings = new MeterReadings(null, Collections.emptyList());
        Set<ConstraintViolation<MeterReadings>> result = this.validator.validate(meterReadings);
        assertThat(result.size()).isEqualTo(2);
        ConstraintViolation<MeterReadings> smartMeterIdPropViolation = result.stream().filter(x -> x.getPropertyPath().toString().equalsIgnoreCase("smartMeterId")).collect(Collectors.toList()).get(0);
        assertThat(smartMeterIdPropViolation.getMessage()).isEqualToIgnoringCase("smartMeterId value is blank");
    }

    @Test
    public void givenEmptyMeterReadingShouldReturnErrorResponse() {
        MeterReadings meterReadings = new MeterReadings(SMART_METER_ID, Collections.emptyList());
        Set<ConstraintViolation<MeterReadings>> violations = this.validator.validate(meterReadings);
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<MeterReadings> violation = violations.stream().findFirst().get();
        assertThat(violation.getMessage()).isEqualToIgnoringCase("electricityReadings value is empty");
    }

    @Test
    public void givenNullReadingsAreSuppliedWhenStoringShouldReturnErrorResponse() {
        MeterReadings meterReadings = new MeterReadings(SMART_METER_ID, null);
        Set<ConstraintViolation<MeterReadings>> result = this.validator.validate(meterReadings);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.stream().findFirst().get().getMessage()).isEqualToIgnoringCase("electricityReadings value is empty");
    }

    @Test
    public void givenMultipleBatchesOfMeterReadingsShouldStore() {
        MeterReadings meterReadings = new MeterReadingsBuilder().setSmartMeterId(SMART_METER_ID)
                .generateElectricityReadings()
                .build();

        MeterReadings otherMeterReadings = new MeterReadingsBuilder().setSmartMeterId(SMART_METER_ID)
                .generateElectricityReadings()
                .build();

        meterReadingController.storeReadings(meterReadings);
        meterReadingController.storeReadings(otherMeterReadings);

        List<ElectricityReading> expectedElectricityReadings = new ArrayList<>();
        expectedElectricityReadings.addAll(meterReadings.getElectricityReadings());
        expectedElectricityReadings.addAll(otherMeterReadings.getElectricityReadings());

        assertThat(meterReadingService.getReadings(SMART_METER_ID).get()).isEqualTo(expectedElectricityReadings);
    }

    @Test
    public void givenMeterReadingsAssociatedWithTheUserShouldStoreAssociatedWithUser() {
        MeterReadings meterReadings = new MeterReadingsBuilder().setSmartMeterId(SMART_METER_ID)
                .generateElectricityReadings()
                .build();

        MeterReadings otherMeterReadings = new MeterReadingsBuilder().setSmartMeterId("00001")
                .generateElectricityReadings()
                .build();

        meterReadingController.storeReadings(meterReadings);
        meterReadingController.storeReadings(otherMeterReadings);

        assertThat(meterReadingService.getReadings(SMART_METER_ID).get()).isEqualTo(meterReadings.getElectricityReadings());
    }

    @Test
    public void givenMeterIdThatIsNotRecognisedShouldReturnNotFound() {
        assertThat(meterReadingController.readReadings(SMART_METER_ID).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenMeterIdRecognisedShouldReturn() {
        meterReadingService.storeReadings("00001", Arrays.asList(new ElectricityReading(Instant.now(), BigDecimal.ONE)));
        assertThat(meterReadingController.readReadings("00001").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
