package io.sim;

public class Route {
    private String id;
    private String edges;  // Lista de pontos (edges) que formam a trajetória

    public Route(String id, String edges) {
        this.id = id;
        this.edges = edges;
    }
    public String getIdRoute()
    {
        return id;
    }
    public String getEdges(){
        return edges;
    } 
    // Método para criar e retornar um objeto Route
    public static Route getRoute(String id, String edges) {
        return new Route(id, edges);
    }

}


