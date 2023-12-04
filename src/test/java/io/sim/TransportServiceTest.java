package io.sim;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.objects.SumoColor;

public class TransportServiceTest {

    private SumoTraciConnection sumoConnection;
    private Auto auto;
    private io.sim.Route route;
    private TransportService transportService;

    @Before
    public void setUp() throws Exception {
        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";
     
        // Sumo connection
        sumoConnection = new SumoTraciConnection(sumo_bin, config_file);
        route = new io.sim.Route("route1", "edge1 edge2 edge3");
        auto = new Auto(true, "auto1", new SumoColor(255, 0, 0, 126), "driver1", sumoConnection, 500, 2, 2, 3.40, 1, 1);
        transportService = new TransportService(true, "transportService1", route, auto, sumoConnection);
    }

    @Test
    public void testIsOn_off() {
        assertTrue(transportService.isOn_off());
    }

    @Test
    public void testSetOn_off() {
        transportService.setOn_off(false);
        assertFalse(transportService.isOn_off());
    }

    @Test
    public void testGetIdTransportService() {
        assertEquals("transportService1", transportService.getIdTransportService());
    }

    @Test
    public void testGetSumo() {
        assertEquals(sumoConnection, transportService.getSumo());
    }

    @Test
    public void testGetAuto() {
        assertEquals(auto, transportService.getAuto());
    }

    @Test
    public void testGetRoute() {
        assertEquals(route, transportService.getRoute());
    }
}
