package io.sim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import de.tudresden.sumo.cmd.Vehicle;

public class FuelStation extends Thread {
    private Semaphore fuelPumps;  
    private ArrayList<Driver> filaDeAbastecimento;
    private Account accountFuelStation; 

    public FuelStation(int alphaBankPort) {
        this.fuelPumps = new Semaphore(2, true);
        this.filaDeAbastecimento = new ArrayList<>();
        accountFuelStation = createAccountInAlphaBank(alphaBankPort);
    }
    @Override
    public void run() {
        while (true) {
            Driver driver = waitForDriver(); // Espera até que um motorista chegue à fila
            try {
                fuelCar(driver); // Abastece o carro do motorista que chegou
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void adicionaDriverNaFila(Driver driver) {
        synchronized (filaDeAbastecimento) {
            filaDeAbastecimento.add(driver);
            filaDeAbastecimento.notify();  // Notifica a thread que está aguardando
        }
    }
    
    private Driver waitForDriver() {
        Driver driver = null;
        synchronized (filaDeAbastecimento) {
            while (filaDeAbastecimento.isEmpty()) {
                try {
                    filaDeAbastecimento.wait();  // Aguarda até que um motorista chegue à fila
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            driver = filaDeAbastecimento.remove(0);  // Remove o primeiro motorista da fila
        }
        System.out.println("Motorista " + driver.getDriverId() + " chegou à estação de combustível.");
        return driver;
    }
    
    private void fuelCar(Driver driver) throws Exception {
        try {
            Car car = driver.getCar();
           
            double valor = driver.getAccount().getBalance();
    
            double fuelAmount = valor / 5.87;
    
            fuelPumps.acquire(); // Solicita permissão para abastecer

            // Cálculo do custo do abastecimento
            double fuelCost = fuelAmount * 5.87; // Preço por litro
    
            // Chama o BotPayment para registrar o pagamento ao posto de combustível
            BotPayment botPayment = new BotPayment();
            botPayment.makePaymentFuelStation(driver.getAccount(), fuelCost);

            System.out.println("Iniciando o abastecimento para o carro: " + car.getId() + " de " + fuelAmount + " litros");
    
            Thread.sleep(2000); // Simula o tempo de abastecimento do carro

            car.addFuel(fuelAmount); // Método para abastecimento
    
            System.out.println("Abastecimento concluído para o carro: " + car.getId());
    
            fuelPumps.release(); // Libera a bomba após o abastecimento
    
            // Volta a velocidade do carro para 6.95 m/s
            car.getSumo().do_job_set(Vehicle.setSpeed(car.getId(), 6.95));
    
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    private Account createAccountInAlphaBank( int alphaBankPort) {
    try (Socket socket = new Socket("localhost", alphaBankPort);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

        // Enviar solicitação para criar a conta
        out.println("CREATE:" + "Conta da Estação de Combustível " + "senha"); // Substitua "senha" pela senha desejada

        // Aguardar resposta do servidor
        String response = in.readLine();
        if (response.equals("Account created")) {
            // A conta foi criada com sucesso
            return new Account("Conta da Estação de Combustível","senha", 0.0); // Substitua "senha" pela senha usada acima
        } else {
            // Trate o caso em que a conta não pôde ser criada
            System.out.println("Falha ao criar a conta da Estação de Combustível no AlphaBank.");
        }
    } catch (IOException e) {
        e.printStackTrace();
    }

    return null;
}

    public Account getAccount(){
        return accountFuelStation;
    }
    
    
}
