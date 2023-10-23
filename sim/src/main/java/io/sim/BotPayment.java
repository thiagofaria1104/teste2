package io.sim;

import java.util.HashMap;
import java.util.Map;

public class BotPayment extends Thread {
    private Map<String, Double> driverPayments; // Mapeia o ID do motorista para o valor total a pagar
    private Account accountCompany;
    private Account accountFuelStation;

    public BotPayment() {
        driverPayments = new HashMap<>();
    }

    // Método para adicionar um pagamento para um motorista
    public synchronized void addPayment(String driverID, double amount) {
        if (driverPayments.containsKey(driverID)) {
            double currentAmount = driverPayments.get(driverID);
            driverPayments.put(driverID, currentAmount + amount);
        } else {
            driverPayments.put(driverID, amount);
        }
    }

    @Override
    public void run() {
        while (true) {
            // Aguarde um período de tempo antes de verificar novamente os sinais dos Cars
            try {
                Thread.sleep(10000); // Aguarde 10 segundos (tempo reduzido para fins de demonstração)
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    // Método para pagamento da Company ao Driver
    public synchronized void makePaymentDriver( Account accountDriver, double kilometers){
        double paymentAmount = kilometers * 3.25;
        accountCompany.withdraw(paymentAmount);
        accountDriver.deposit(paymentAmount);
        System.out.println("Company realizou pagamento ao Driver");
    }
    // Método para pagamento do Driver a Fuel Station
    public synchronized void makePaymentFuelStation(Account accountDriver, double kilometers){
        double paymentAmount = kilometers * 5.87;
        accountDriver.withdraw(paymentAmount);
        accountFuelStation.deposit(paymentAmount);
        System.out.println("Driver realizou pagamento a FuelStation:");
    }

}
