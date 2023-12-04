package io.sim;

import org.junit.Test;
import static org.junit.Assert.*;

public class RouteTest {

    @Test
    public void testGetIdRoute() {
        String id = "r1";
        String edges = "edge1 edge2 edge3";
        Route route = new Route(id, edges);

        assertEquals(id, route.getIdRoute());
    }

    @Test
    public void testGetEdges() {
        String id = "r1";
        String edges = "edge1 edge2 edge3";
        Route route = new Route(id, edges);

        assertEquals(edges, route.getEdges());
    }

    @Test
    public void testGetRoute() {
        String id = "r1";
        String edges = "edge1 edge2 edge3";

        Route route = Route.getRoute(id, edges);

        assertNotNull(route);
        assertEquals(id, route.getIdRoute());
        assertEquals(edges, route.getEdges());
    }
}
