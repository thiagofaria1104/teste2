    package io.sim;

    import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import it.polito.appeal.traci.SumoTraciConnection;

    public class Driver extends Thread {
        private int companyPort;
        private ArrayList<Route> routesToExecute = new ArrayList<>();
        private ArrayList<Route> routesExecuted = new ArrayList<>();
        private ArrayList<Route> routesInProgress = new ArrayList<>();
        private Car car;
        private Account accountDriver;  // Corrigido o nome do atributo
        private String driverId;
        private Auto auto;
        private SumoTraciConnection sumo;
        private boolean abastecer;

        public Driver(String driverId, SumoTraciConnection sumo, int alphaBankPort) throws Exception {
            this.driverId = driverId;
            this.sumo = sumo; // Inicialize o campo sumo
            car = new Car("Carro do " + driverId, sumo);
            auto = car.criarAutos(driverId);
        }

        public void run() {
            while (true) {
                try {
                    criarTransportServices();
                    //solicitaPagamento();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void criarTransportServices() throws Exception {
            int i = 0;

            while (i < routesToExecute.size()) {
                Route route = routesToExecute.get(i);
                TransportService tS1 = new TransportService(true, "Serviço de Transporte" + i, route, auto, sumo);
                tS1.start();
                routesInProgress.add(i, route);
                routesToExecute.remove(route);
                try {
                    while (!car.getRouteID(car.getId()).equals(getLastEdge(route))) {
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                route = routesInProgress.get(i);
                routesExecuted.add(i, route);
                i++;
            }
        }

        private Account createAccountInAlphaBank(String driverId, int alphaBankPort) {
        try (Socket socket = new Socket("localhost", alphaBankPort);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
                
            // Enviar solicitação para criar a conta
            out.println("CREATE:" + "Conta do "+driverId + ":senha"); // Substitua "senha" pela senha desejada

            // Aguardar resposta do servidor
            String response = in.readLine();
            if (response.equals("Account created")) {
                // A conta foi criada com sucesso
                return new Account("Conta do "+driverId, "senha", 0.0); // Substitua "senha" pela senha usada acima
            } else {
                // Trate o caso em que a conta não pôde ser criada
                System.out.println("Falha ao criar a conta no AlphaBank.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
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

        public boolean solicitaAbastecimento() throws Exception {
            abastecer = car.requestRefueling();
            return abastecer;
        }

        // Método relacionado ao monitoramento do percurso e solicitação de pagamento
        public void solicitaPagamentoAoCompany() {
                try (Socket socket = new Socket("localhost", companyPort);
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                        String driverId = getDriverId();
                        out.println("PAYMENT_REQUEST:" + driverId);
                       String response = in.readLine();
            if (response.equals("Payment processed")) {
                System.out.println("Pagamento processado com sucesso para o motorista " + driverId);
         } else {
                System.out.println("Falha ao processar o pagamento para o motorista " + driverId);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        }

        // Método relacionado ao monitoramento do percurso
        public boolean solicitaPagamento() throws Exception {
            boolean pagamentoSolicitado = car.KmCompleted();
            if (pagamentoSolicitado) {
                solicitaPagamentoAoCompany(); // Chama a função para solicitar o pagamento
            }
            return pagamentoSolicitado;
        }
        
        // Método para criar conta
        public void createAccount(int alphaBankPort){
            accountDriver = createAccountInAlphaBank("Conta do "+ driverId, alphaBankPort);
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
        
        public Car getCar(){
            return car;
        }
}
