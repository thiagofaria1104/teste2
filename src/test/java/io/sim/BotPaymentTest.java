package io.sim;

import org.junit.Test;
import static org.junit.Assert.*;

public class BotPaymentTest {

    @Test
    public void testMakePaymentDriver() {
        Account accountCompany = new Account("company", "companyPass", 1000.0);
        Account accountDriver = new Account("driver", "driverPass", 100.0);

        BotPayment botPayment = new BotPayment();
        botPayment.makePaymentDriver(accountCompany, accountDriver, 50.0);

        assertEquals(1000.0 - (50.0 * 3.25), accountCompany.getBalance(), 0.001);
        assertEquals(100.0 + (50.0 * 3.25), accountDriver.getBalance(), 0.001);
    }

    @Test
    public void testMakePaymentFuelStation() {
        Account accountDriver = new Account("driver", "driverPass", 100.0);
        Account accountFuelStation = new Account("fuelStation", "fuelStationPass", 500.0);

        BotPayment botPayment = new BotPayment();
        botPayment.makePaymentFuelStation(accountDriver, accountFuelStation, 50.0);

        assertEquals(100.0 - (50.0 * 5.87), accountDriver.getBalance(), 0.001);
        assertEquals(500.0 + (50.0 * 5.87), accountFuelStation.getBalance(), 0.001);
    }
}
