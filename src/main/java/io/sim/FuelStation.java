package io.sim;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;

import java.net.ServerSocket;
import java.net.Socket;


public class FuelStation extends Thread {
    private Semaphore fuelPumps;
    private ArrayList<Driver> filaDeAbastecimento;
    private Account accountFuelStation;
    private static FuelStation instance;
    private ServerSocket serverSocket;
    private ArrayList<Driver> listaDrivers;
    private volatile boolean serverReady = false;

    FuelStation() {
        this.fuelPumps = new Semaphore(2, true);
        this.filaDeAbastecimento = new ArrayList<>();
        accountFuelStation = new Account("Conta do Posto", "senha", 1000);
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static FuelStation getInstance() {
        if (instance == null) {
            instance = new FuelStation();
        }
        return instance;
    }

    public void adicionarDriverNaFila(Driver driver) {
        synchronized (filaDeAbastecimento) {
            System.out.println(driver.getDriverId() + " está na fila de abastecimento");
            filaDeAbastecimento.add(driver);
            filaDeAbastecimento.notify();
        }
    }

    public void abastecimento() {
        while (true) {
            Driver driver;
            synchronized (filaDeAbastecimento) {
                if (filaDeAbastecimento.isEmpty()) {
                    try {
                        filaDeAbastecimento.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                driver = filaDeAbastecimento.remove(0);
            }
            try {
                fuelCar(driver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void fuelCar(Driver driver) {
        try {
            Car car = driver.getCar();
            double valor = driver.getAccount().getBalance();
            double fuelAmount = valor / 5.87; // Abastece todo o saldo do motorista

            fuelPumps.acquire(); // Avalia a disponibilidade da bomba

            double fuelCost = fuelAmount * 5.87;

            BotPayment botPayment = new BotPayment();
            botPayment.makePaymentFuelStation(driver.getAccount(), accountFuelStation, fuelCost); // Paga o posto
            // Thread.sleep(2000);

            System.out.println("Valor do tanque antes do abastecimento " + car.getFuelTank());

            car.addFuel(fuelAmount); // Adiciona no tanque

            Thread.sleep(10000);

            System.out.println("Foi abastecido ao carro do " + driver.getDriverId());

            fuelPumps.release(); // Libera a bomba

            System.out.println("Valor do tanque após o abastecimento " + car.getFuelTank());

            car.getAuto().setSpeedAcelerar(); // Volta a acelerar

            car.run();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setListaDeDrivers(ArrayList<Driver> listaDeDrivers) {
    }

    // Método que verifica as conexões com a estação de combustível
    private void handleFuelStationConnection(Socket fuelStationSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(fuelStationSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(fuelStationSocket.getOutputStream(), true)) {

            // Receber solicitação de abastecimento em formato JSON
            String jsonRequest = reader.readLine();
            JsonObject requestJson = JsonParser.parseString(jsonRequest).getAsJsonObject();

            String requestType = requestJson.getAsJsonPrimitive("requestType").getAsString();

            if ("FUEL_REQUEST".equals(requestType)) {
                String driverId = requestJson.getAsJsonPrimitive("driverId").getAsString();

                for(int i=0; i < listaDrivers.size(); i++){
                    if(listaDrivers.get(i).getDriverId().equals(driverId)){
                        adicionarDriverNaFila(listaDrivers.get(i));
                    }
                }
                // Processar solicitação de abastecimento
                abastecimento();

                boolean abastecimentoSolicitado = true;

                // Criar objeto de resposta de abastecimento em JSON
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("success", abastecimentoSolicitado);

                // Enviar resposta para a estação de combustível
                writer.println(responseJson.toString());
            } else {
                System.out.println("Requisição de abastecimento inválida.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(1026);
            System.out.println("Servidor do Posto iniciado na porta 1026");

            setServerReady();
            while (true) {
                Socket driverSocket = serverSocket.accept();
                System.out.println("Nova conexão de motorista com o posto: " + driverSocket);

                Thread driverThread = new Thread(() -> handleFuelStationConnection(driverSocket));
                driverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para adicionar os Drivers a tabela
    public void receberDrivers(ArrayList<Driver> drivers) {
        this.listaDrivers = new ArrayList<>();
        for (Driver driver : drivers) {
            this.listaDrivers.add(driver);
        }
    }

    // Métodos para controlar o estado do servidor
    public void setServerReady() {
        this.serverReady = true;
    }

    // Método que verifica o estado do Servidor
    public boolean isServerReady() {
        return serverReady;
    }

    @Override
    public void run() {
        startServer();
    }
}
