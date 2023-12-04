package io.sim;

public class BotPayment extends Thread {

    public BotPayment() {
        
    }

    @Override
    public void run() {
       
    }

    // Método para pagamento da Company ao Driver
    public synchronized void makePaymentDriver(Account accountCompany, Account accountDriver, double kilometers){
        double paymentAmount = kilometers * 3.25;
        accountCompany.withdraw(paymentAmount);
        accountDriver.deposit(paymentAmount);
    }

    // Método para pagamento do Driver a Fuel Station
    public synchronized void makePaymentFuelStation(Account accountDriver,Account accountFuelStation, double kilometers){
        double paymentAmount = kilometers * 5.87;
        accountDriver.withdraw(paymentAmount);
        accountFuelStation.deposit(paymentAmount);
        System.out.println("Driver realizou pagamento a FuelStation:");
    }

}
