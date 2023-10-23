package io.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Company extends Thread {
    private ArrayList<Route> routesToExecute;
    private Map<String, Driver> listaDeDrivers; // Mapa de drivers com seus respectivos IDs
    private ServerSocket serverSocket;
    private Itinerary itinerary;
    private Account accountCompany;

    public Company(int companyPort, int alphaBankPort) throws IOException {
        routesToExecute = new ArrayList<>();
        serverSocket = new ServerSocket(companyPort);
        accountCompany = createAccountInAlphaBank(alphaBankPort);
        listaDeDrivers = new HashMap<>();
        itinerary = new Itinerary("data/dados2.xml", "0");
    }

    @Override
    public void run() {
        // Lógica da execução da Company
    }

    public Account getAccount() {
        return accountCompany;
    }

    public ArrayList<Route> getListaDeRotas() {
        return routesToExecute;
    }

    private Account createAccountInAlphaBank(int alphaBankPort) {
        try (Socket socket = new Socket("localhost", alphaBankPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Enviar solicitação para criar a conta
            out.println("CREATE:Conta da Company:senha");

            // Aguardar resposta do servidor
            String response = in.readLine();
            if (response != null) {
                if (response.equals("Account created")) {
                    // A conta foi criada com sucesso
                    return new Account("Conta da Company", "senha", 1000000);
                } else {
                    System.out.println("Falha ao criar a conta no AlphaBank.");
                }
            } else {
                System.out.println("Resposta inválida do AlphaBank.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha na comunicação com o AlphaBank.");
        }

        return null;
    }

    private void handleDriverConnection(Socket driverSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(driverSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(driverSocket.getOutputStream(), true)) {
    
            String message = reader.readLine();
    
            if (message.startsWith("PAYMENT_REQUEST:")) {
                String driverId = message.substring("PAYMENT_REQUEST:".length());
                boolean pagamentoProcessado = processarPagamento(driverId);
                if (pagamentoProcessado) {
                    writer.println("Payment processed"); // Responde ao driver
                } else {
                    writer.println("Payment processing failed"); // Responde ao driver em caso de falha
                }
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
            botPayment.makePaymentDriver(driverAccount, paymentAmount);
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
}
