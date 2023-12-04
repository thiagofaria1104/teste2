package io.sim;


public class Account {
    private String password;
    private double balance;

    public Account(String username, String password, double balance) {
        this.password = password;
        this.balance = balance; 
    }

    // Método que retorna o saldo da conta
    public synchronized double getBalance() {
        return balance;
    }

    // Método que faz o deposito na conta
    public synchronized void deposit(double amount) {
        balance +=amount;
    }

    // Método que realiza o saque
    public synchronized boolean withdraw(double amount) {
        
        // Verificar se o valor do saque é positivo e menor ou igual ao saldo atual
        if (amount > 0 && amount <= balance) {
            // Realizar o saque
            balance -= amount;
            return true; // O saque foi bem-sucedido
        }
        return false; // O saque não foi bem-sucedido devido a um valor inválido ou saldo insuficiente
    }

    public Object getPassword() {
        return password;
    }
    
}
