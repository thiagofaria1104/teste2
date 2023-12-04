package io.sim;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DrivingDataTest {

    @Test
    public void testGetSpeed() {
        DrivingData drivingData = new DrivingData("123", "driver123", System.currentTimeMillis(),
                10.0, 20.0, "road123", "route123", 60.0, 100.0, 5.0, 8.0, 1, 2.0, 30.0, 25.0, 4, 3);

        assertEquals(60.0, drivingData.getSpeed(), 0.01);
    }

    @Test
    public void testGetOdometer() {
        DrivingData drivingData = new DrivingData("123", "driver123", System.currentTimeMillis(),
                10.0, 20.0, "road123", "route123", 60.0, 100.0, 5.0, 8.0, 1, 2.0, 30.0, 25.0, 4, 3);

        assertEquals(100.0, drivingData.getOdometer(), 0.01);
    }

    @Test
    public void testGetFuelConsumption() {
        DrivingData drivingData = new DrivingData("123", "driver123", System.currentTimeMillis(),
                10.0, 20.0, "road123", "route123", 60.0, 100.0, 5.0, 8.0, 1, 2.0, 30.0, 25.0, 4, 3);

        assertEquals(5.0, drivingData.getFuelConsumption(), 0.01);
    }

}

