package io.sim;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//import org.python.modules.cPickle;
//import org.python.modules.thread.thread;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;

public class EnvSimulator extends Thread {
    Random random = new Random();
    private int alphaBankPort;
    private int companyPort;
    private int startingPort1 = 1024;
    private int startingPort2 = 12347;
    private SumoTraciConnection sumo;
    private FuelStation fuelStation;
    private AlphaBank alphaBank;
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
            alphaBankPort = findAvailablePort(startingPort1);

            executeSumoTimestep();
            criarDrivers();

            for (int i = 0; i < 100; i++) { 
                //listaDeDrivers.get(i).createAccount(alphaBankPort);
                listaDeDrivers.get(i).start();
                //company.adicionarDriver(listaDeDrivers.get(i).getDriverId(), listaDeDrivers.get(i));
            }

            //fuelStation = new FuelStation(alphaBankPort); // criação da Fuel Station
            //fuelStation.start(); // Inicie a estação de combustível após iniciar os motoristas
            alphaBank = new AlphaBank();
            alphaBank.startServer(alphaBankPort); // Inicie o servidor AlphaBank na porta alphaBankPort

            //companyPort = findAvailablePort(startingPort2); // StartingPort2 para escolher uma porta diferente
            //System.out.println("Porta da Company: " + companyPort);
            //Company company = new Company(companyPort, alphaBankPort); // Inicie a Company
            //company.start(); // Inicie o Run da Company

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
                    e.printStackTrace(); // Adicione tratamento de exceções adequado
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread sumoTimestepThread = new Thread(sumoTimestepTask);
        sumoTimestepThread.start();
    }
    

    public ArrayList<Driver> criarDrivers() throws Exception {
        int k=0;
        for (int i = 1; i <= 100; i++) {
            Driver driver = new Driver("Motorista " + i, sumo, alphaBankPort);
            listaDeDrivers.add(driver);
            for (int j = 0; j < 6; j++) {
                if (k < listaDeRotas.size()) {
                    driver.recebeRotas(listaDeRotas.get(k));
                    k++;
                }
            }
        }
        return listaDeDrivers;
    }

    private void verificarSolicitacoes(ArrayList<Driver> listaDeDrivers, FuelStation fuelStation) {
        int intervaloSegundos = 1; // Verificar a cada segundo
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            for (int i = 0; i < listaDeDrivers.size(); i++) {
                try {
                    if (listaDeDrivers.get(i).solicitaAbastecimento()) {
                        fuelStation.adicionaDriverNaFila(listaDeDrivers.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, intervaloSegundos, TimeUnit.SECONDS);
    }

    public static int findAvailablePort(int startingPort) throws IOException {
        for (int port = startingPort; port < 65536; port++) {
            if (isPortAvailable(port)) {
                return port;
            }
        }
        throw new IOException("Não há portas disponíveis");
    }

    public static boolean isPortAvailable(int port) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
}