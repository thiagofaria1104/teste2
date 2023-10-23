package io.sim;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class AlphaBank {
    private Map<String, Account> accounts;
    private ServerSocket serverSocket;
    private boolean verificaLogin;
    private boolean verificaCriacao;

    public AlphaBank() {
        this.accounts = new HashMap<>();
        this.verificaLogin = false;
        this.verificaCriacao = false;
    }

    public void startServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Servidor AlphaBank iniciado na porta " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nova conexão de cliente: " + clientSocket);

                // Cria uma thread para tratar as solicitações do cliente
                Thread clientThread = new Thread(() -> handleClientRequest(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClientRequest(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String request = reader.readLine();
            if (request != null) {
                String[] parts = request.split(":");
                if (parts.length >= 3 && parts[0].equals("LOGIN")) {
                    String username = parts[1];
                    String password = parts[2];
                    boolean isAuthenticated = authenticate(username, password);
                    writer.println(isAuthenticated);
                } else if (parts.length >= 3 && parts[0].equals("CREATE")) {
                    String username = parts[1];
                    String password = parts[2];
                    boolean isAccountCreated = createAccount(username, password,0);
                    writer.println(isAccountCreated);
                } else {
                    writer.println("Comando inválido");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Account getAccount(String username) {
        return accounts.get(username);
    }

    public static void main(String[] args) {
        AlphaBank alphaBank = new AlphaBank();
        alphaBank.startServer(12345); // Inicie o servidor na porta desejada
    }

    public void connect(String alphaBankAddress, int alphaBankPort) {
    }

    public synchronized boolean authenticate(String username, String password) {
        // Verifique se a conta com o username fornecido existe no mapa de contas.
        if (accounts.containsKey(username)) {
            // Obtenha a conta correspondente.
            Account account = accounts.get(username);
            // Compare a senha fornecida com a senha da conta.
            if (account.getPassword().equals(password)) {
                // A autenticação foi bem-sucedida.
                return true;
            }
        }
    
        // Se não for encontrada uma conta correspondente ou a senha estiver incorreta, retorne false.
        return false;
    }
    public synchronized boolean createAccount(String username, String password, double balance) {
        // Verifique se o username já está em uso.
        if (accounts.containsKey(username)) {
            // O username já existe, portanto, não é possível criar a conta.
            return false;
        }
        // Crie uma nova conta com o username e a password fornecidos.
        Account newAccount = new Account(username, password, 0);
        // Adicione a nova conta ao mapa de contas.
        accounts.put(username, newAccount);
        // A conta foi criada com sucesso.
        return true;
    }
    
    
}
