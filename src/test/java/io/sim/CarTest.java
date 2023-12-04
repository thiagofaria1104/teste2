package io.sim;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import it.polito.appeal.traci.SumoTraciConnection;

public class CarTest {

    private SumoTraciConnection sumoConnection;
    private Car car;

    @Before
    public void setUp() throws Exception {
        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";
     
        // Sumo connection
        sumoConnection = new SumoTraciConnection(sumo_bin, config_file);
        car = new Car("car1", sumoConnection);
    }

    @Test
    public void testGetSumo() {
        assertEquals(sumoConnection, car.getSumo());
    }

    @Test
    public void testSetSumo() {
        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";
     
        // Sumo connection
        SumoTraciConnection newSumoConnection = new SumoTraciConnection(sumo_bin, config_file);
        car.setSumo(newSumoConnection);
        assertEquals(newSumoConnection, car.getSumo());
    }

    @Test
    public void testAddFuel() {
        car.addFuel(2.0);
        assertEquals(6.0, car.getFuelTank(), 0.001); // 0.001 tolerancia aceitável do teste
    }

    @Test
    public void testGetFuelTank() {
        assertEquals(4.0, car.getFuelTank(), 0.001); // 0.001 tolerancia aceitável do teste
    }

    @Test
    public void testSetFuelTank() {
        car.setFuelTank();
        assertEquals(3.0, car.getFuelTank(), 0.001); // 0.001 tolerancia aceitável do teste
    }

    @Test
    public void testGetId() {
        assertEquals("car1", car.getId());
    }

    @Test
    public void testGetAuto() throws Exception {
        Auto auto = car.criarAutos("driver1");
        assertEquals(auto, car.getAuto());
    }
}
