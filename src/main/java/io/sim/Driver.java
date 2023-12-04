package io.sim;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;

public class Driver extends Thread {
    private ArrayList<Route> routesToExecute;
    private Car car;
    private Account accountDriver;
    private String driverId;
    private Auto auto;
    private SumoTraciConnection sumo;
    private SumoStringList carrosPresente;
    private CriarPlanilhaExcel criarPlanilhaExcel;
    private int i = 0;
    private String edgeAnterior = "...";
    private long tempoInicioSimulacao;
    private long tempoEdgeAnterior;
    private long tempoEdgeAtual;

    public Driver(String driverId, SumoTraciConnection sumo) throws Exception {
        this.driverId = driverId;
        this.sumo = sumo;
        routesToExecute = new ArrayList<>();
        car = new Car("Carro do " + driverId, sumo);
        auto = car.criarAutos(driverId);
        connectToAlphaBankAndCreateAccount(driverId, "senha");
        criarPlanilhaExcel = new CriarPlanilhaExcel();
        criarPlanilhaExcel.createSheet("Serviço de Transporte");
    }

    public void run() {
        while (true) {
            try {
                criarTransportServices(); // Executar as rotas
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void criarTransportServices() throws Exception {
        while (i < 100) {
            Route route = routesToExecute.get(i);
            TransportService tS1 = new TransportService(true, "Serviço de Transporte " + i, route, auto, sumo);
            tS1.initializeRoutes();
            iniciarSimulacao();
            tempoEdgeAnterior = tempoInicioSimulacao;
            System.out.println("Serviço de Transporte " + i + " foi iniciado da rota " + route.getIdRoute());

            if (i >= 1) {
                criarPlanilhaExcel.pularLinha("exemplo.xlsx", "Serviço de Transporte");
            }

            try {
                while (true) {

                    if (presente()) {

                        car.getAuto().atualizaSensores();

                        if (trocouDeEdge()) {
                            tempoEdgeAtual = obterTempoSimulacaoSegundos();
                            if ((tempoEdgeAtual - tempoEdgeAnterior) < 0) {
                                criarPlanilhaExcel.updateSheetCar(car.getAuto().getDrivingData(), "exemplo.xlsx",
                                        "Serviço de Transporte", i, 0);
                                tempoEdgeAnterior = tempoEdgeAtual;
                            } else {
                                criarPlanilhaExcel.updateSheetCar(car.getAuto().getDrivingData(), "exemplo.xlsx",
                                        "Serviço de Transporte", i, tempoEdgeAtual - tempoEdgeAnterior);
                                tempoEdgeAnterior = tempoEdgeAtual;
                            }

                        }
                    }
                    // Verificar se está no último edge e fora da simulação para avançar
                    if (sumo.do_job_get(Vehicle.getRoadID(car.getId())).equals(getLastEdge(route))) {
                        System.out.println(car.getId() + " terminou a rota de ID: " + route.getIdRoute());
                        while (presente()) {

                        }
                        i++;
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean trocouDeEdge() {
        try {
            String edgeAtual = (String) this.sumo.do_job_get(Vehicle.getRoadID(car.getAuto().getIdAuto()));

            // Lista de arestas na rota
            ArrayList<String> edgesNaRota = new ArrayList<>(
                    Arrays.asList("15469696#4", "15240255#3", "15240255#4", "795084572#0", "795084572#1", "795084572#2",
                            "795084572#3", "795084572#4", "795084572#5", "794061354#0"));

            // Verifica se a aresta atual está na lista de arestas da rota
            if (edgesNaRota.contains(edgeAtual)) {
                if (edgeAnterior != null && !edgeAtual.equals(edgeAnterior)) {
                    // Atualize o edge anterior para o edge atual
                    edgeAnterior = edgeAtual;
                    return true;
                }
            } else {
                // A aresta atual não pertence à rota, pode ser uma aresta intermediária
            }
            // Atualiza o edge anterior para o edge atual
            edgeAnterior = edgeAtual;
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void iniciarSimulacao() {
        tempoInicioSimulacao = System.currentTimeMillis();
    }

    public long obterTempoSimulacaoSegundos() {
        long tempoAtualMillis = System.currentTimeMillis();
        long tempoSimulacaoMillis = tempoAtualMillis - tempoInicioSimulacao;
        return tempoSimulacaoMillis / 1000; // Converte para segundos
    }

    public synchronized boolean presente() throws Exception {
        carrosPresente = (SumoStringList) this.sumo.do_job_get(Vehicle.getIDList());
        return carrosPresente.contains(car.getId());
    }

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
                this.accountDriver = new Account("Conta do motorista", password, 2000);
                System.out.println("A conta corrente do " + driverId + " foi criada");
            } else {
                System.out.println("A conta corrente do " + driverId + " não foi criada");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processaPagamentoEAbastecimento() {
        try {

            int aux = 0;
            int consumo = 1;

            if (car.KmCompleted()) {

                connectToCompany();
                aux++;

                if (aux == consumo) {
                    car.setFuelTank(); // Decrementa 1 litro a cada 12 Quilômetros
                    aux = 0;
                    if (car.getFuelTank() <= 3) {
                        car.getAuto().setSpeedParar();
                        //connectFuelStation(); Função de abastecimento retirada para facilitar extração dos dados
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro ao processar pagamento ou abastecimento.");
        }
    }

    public void connectToCompany() {

        final String companyAddress = "localhost";
        final int companyPort = 1025;

        try (Socket socket = new Socket(companyAddress, companyPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Criar objeto JSON para solicitação de pagamento
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("requestType", "PAYMENT_REQUEST");
            jsonRequest.addProperty("driverId", driverId);

            // Enviar solicitação de pagamento
            out.println(jsonRequest.toString());

            // Receber resposta da Company
            String jsonResponse = in.readLine();

            // Converter resposta de JSON para objeto
            JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
            boolean success = responseJson.getAsJsonPrimitive("success").getAsBoolean();

            if (success) {
                System.out.println("Pagamento processado com sucesso pela Company.");
            } else {
                System.out.println("Falha no processamento do pagamento pela Company.");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha na comunicação com a Company.");
        }
    }

    public void connectFuelStation() {

        final String stationAddress = "localhost";
        final int stationPort = 1026;
    
        try (Socket socket = new Socket(stationAddress, stationPort);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
    
            // Criar objeto JSON para solicitação de abastecimento
            JsonObject jsonRequest = new JsonObject();
            jsonRequest.addProperty("requestType", "FUEL_REQUEST");
            jsonRequest.addProperty("driverId", driverId);
    
            // Enviar solicitação de abastecimento
            out.println(jsonRequest.toString());
    
            // Receber resposta da Fuel Station
            String jsonResponse = in.readLine();
    
            // Converter resposta de JSON para objeto
            JsonObject responseJson = JsonParser.parseString(jsonResponse).getAsJsonObject();
            boolean success = responseJson.getAsJsonPrimitive("success").getAsBoolean();
    
            if (success) {
                System.out.println("Abastecimento solicitado com sucesso à Fuel Station.");
            } else {
                System.out.println("Falha na solicitação de abastecimento à Fuel Station.");
            }
    
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Falha na comunicação com a Fuel Station.");
        }
    }
    

    private String getLastEdge(Route route) {
        String edgesString = route.getEdges();
        String[] edgesArray = edgesString.split(" ");
        if (edgesArray.length > 0) {
            return edgesArray[edgesArray.length - 1];
        } else {
            return "";
        }
    }

    public double getAccountBalance() {
        double saldo = accountDriver.getBalance();
        return saldo;
    }

    public String getDriverId() {
        return driverId;
    }

    public Account getAccount() {
        return accountDriver;
    }

    public void recebeRotas(Route route) {
        routesToExecute.add(route);
    }

    // Método para receber rotas no formato JSON e converter para objetos Route
    public void recebeRotasJSON(String jsonRoutes) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Route route = objectMapper.readValue(jsonRoutes, Route.class);
            routesToExecute.add(route);
        } catch (Exception e) {
            e.printStackTrace(); // Lide com a exceção de desserialização adequadamente
        }
    }

    public Car getCar() {
        return car;
    }
}
