package io.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Company extends Thread {
    private ArrayList<Route> routesToExecute = new ArrayList<>();
    private Map<String, Driver> listaDeDrivers; // Mapa de drivers com seus respectivos IDs
    private ServerSocket serverSocket;
    private Itinerary itinerary;
    private Account accountCompany;
    private volatile boolean serverReady = false;

    public Company() throws IOException {
        serverSocket = new ServerSocket();
        connectToAlphaBankAndCreateAccount("Company", "senha");
        listaDeDrivers = new HashMap<>();
        itinerary = new Itinerary("data/dados2.xml", "0");
        routesToExecute = itinerary.getListaDeRotas();
    }

    @Override
    public void run() {
        startServer();
    }

  
    public void startServer() {
        try {
            serverSocket = new ServerSocket(1025);
            System.out.println("Servidor da Company iniciado na porta 1025");

            setServerReady();
            while (true) {
                Socket driverSocket = serverSocket.accept();
                System.out.println("Nova conexão de motorista: " + driverSocket);

                Thread driverThread = new Thread(() -> handleDriverConnection(driverSocket));
                driverThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método que faz a conexão e cria a conta da company
    private void connectToAlphaBankAndCreateAccount(String username, String password) {
        final String ALPHA_BANK_HOST = "localhost";
        final int ALPHA_BANK_PORT = 1024;

        try (Socket socket = new Socket(ALPHA_BANK_HOST, ALPHA_BANK_PORT);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Criar um objeto JSON para a solicitação
            JsonObject requestJson = new JsonObject();
            requestJson.addProperty("requestType", "CREATE");
            requestJson.addProperty("username", username);
            requestJson.addProperty("password", password);

            // Enviar a solicitação como uma string JSON
            writer.println(requestJson.toString());

            // Aguardar resposta do AlphaBank
            String response = reader.readLine();

            // Parse da resposta JSON
            JsonObject responseJson = JsonParser.parseString(response).getAsJsonObject();

            // Verificar o conteúdo da resposta
            if (responseJson.has("status") && responseJson.get("status").getAsString().equals("ACCOUNT_CREATED")) {
                // Se a conta foi criada com sucesso no servidor, atualize a conta do motorista
                this.accountCompany = new Account("Conta do motorista", password, 2000);
            } else {
                System.out.println("A conta corrente da Company não foi criada");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Método que verifica as conexões com o servidor
    private void handleDriverConnection(Socket driverSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(driverSocket.getInputStream()));
            PrintWriter writer = new PrintWriter(driverSocket.getOutputStream(), true)) {

            // Receber solicitação de pagamento em formato JSON
            String jsonRequest = reader.readLine();
            JsonObject requestJson = JsonParser.parseString(jsonRequest).getAsJsonObject();
            
            String requestType = requestJson.getAsJsonPrimitive("requestType").getAsString();

            if ("PAYMENT_REQUEST".equals(requestType)) {
                String driverId = requestJson.getAsJsonPrimitive("driverId").getAsString();

                // Processar pagamento
                boolean pagamentoProcessado = processarPagamento(driverId);

                // Criar objeto de resposta de pagamento em JSON
                JsonObject responseJson = new JsonObject();
                responseJson.addProperty("success", pagamentoProcessado);

                // Enviar resposta para o driver
                writer.println(responseJson.toString());
            } else {
                System.out.println("Requisição de pagamento inválida.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para processar o pagamento ao driver a cada quilômetro
    private boolean processarPagamento(String driverId) {
        Driver driver = listaDeDrivers.get(driverId);
        if (driver != null) {
            Account driverAccount = driver.getAccount();
            double paymentAmount = 3.25; // Valor fixo por quilômetro

            // Chame o BotPayment para efetuar o pagamento
            BotPayment botPayment = new BotPayment();
            botPayment.makePaymentDriver(this.accountCompany,driverAccount, paymentAmount);

            System.out.println("Company realizou pagamento ao Driver " + driverId + " pelo quilômetro percorrido.");
            return true;
        } else {
            System.out.println("Driver não encontrado na lista de motoristas da Company: " + driverId);
            return false;
        }
    }

    // Método para adicionar um driver à listaDeDrivers quando ele solicita conexão
    public void adicionarDriver(String driverId, Driver driver) {
        listaDeDrivers.put(driverId, driver);
    }
    
    // Método para obter um driver da listaDeDrivers com base no ID do driver
    public Driver getDriver(String driverId) {
        return listaDeDrivers.get(driverId);
    }

    // Método para obter a conta da Company
    public Account getAccount() {
        return accountCompany;
    }

    // Método para obter as rotas a serem executadas
    public ArrayList<Route> getListaDeRotas() {
        return routesToExecute;
    }

    // Método para obter as rotas a serem executadas JSON
    public String getListaDeRotasJSON() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(routesToExecute);
        } catch (Exception e) {
            e.printStackTrace(); 
            return "[]"; // Retorna um array vazio em caso de erro
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

    // Método para adicionar os Drivers a tabela
    public void receberDrivers(ArrayList<Driver> drivers) {
        this.listaDeDrivers = new HashMap<>();
    
        for (Driver driver : drivers) {
            this.listaDeDrivers.put(driver.getDriverId(), driver);
        }
    }
}
