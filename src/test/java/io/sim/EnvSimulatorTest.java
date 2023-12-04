package io.sim;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.Test;

public class EnvSimulatorTest {

    @Test
    public void testCriarDrivers() {
        EnvSimulator envSimulator = new EnvSimulator();
        try {
            ArrayList<Driver> drivers = envSimulator.criarDrivers();
            assertNotNull(drivers);
            assertFalse(drivers.isEmpty());
            assertEquals(100, drivers.size()); // Verifica se o número esperado de motoristas foi criado
        } catch (Exception e) {
            fail("Exceção não esperada: " + e.getMessage());
        }
    }

    @Test
    public void testGetDrivers() {
        EnvSimulator envSimulator = new EnvSimulator();
        try {
            ArrayList<Driver> drivers = envSimulator.getDrivers();
            assertNotNull(drivers);
            assertTrue(drivers.isEmpty()); // A lista de motoristas deve estar vazia inicialmente
        } catch (Exception e) {
            fail("Exceção não esperada: " + e.getMessage());
        }
    }
}
