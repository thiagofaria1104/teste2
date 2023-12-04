package io.sim;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import it.polito.appeal.traci.SumoTraciConnection;

public class EnvSimulator extends Thread {
    Random random = new Random();
    private SumoTraciConnection sumo;
    private Itinerary itinerary = new Itinerary("data/dados2.xml", "0");
    private ArrayList<Route> listaDeRotas = itinerary.getListaDeRotas();
    private ArrayList<Driver> listaDeDrivers = new ArrayList<>();
    
    public EnvSimulator() {

    }
    
    public void run() {
        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";
    
        // Sumo connection
        this.sumo = new SumoTraciConnection(sumo_bin, config_file);
        sumo.addOption("start", "1");
        sumo.addOption("quit-on-end", "1");
        try {
            sumo.runServer(12345);
    
            executeSumoTimestep();
            criarDrivers();
            inicializeDrivers();
               
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeSumoTimestep() {
        Runnable sumoTimestepTask = () -> {
            while (true) {
                try {
                    this.sumo.do_timestep();
                } catch (Exception e) {
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        };
        Thread sumoTimestepThread = new Thread(sumoTimestepTask);
        sumoTimestepThread.start();
    }

    
    // Cria 1 motorista e atribui as 100 primeiras rotas do array a ele 
    public ArrayList<Driver> criarDrivers() throws Exception {
        Driver driver = new Driver("Motorista 1", sumo);
        listaDeDrivers.add(driver);
        for (int j = 0; j < 100; j++) {
            driver.recebeRotas(listaDeRotas.get(j));
        }
        return listaDeDrivers;
    }

    public void inicializeDrivers(){
        for (int i = 0; i < 1; i++) {
            listaDeDrivers.get(i).start();
        }
    }
    public ArrayList<Driver> getDrivers(){
        return listaDeDrivers;
    }

    
}