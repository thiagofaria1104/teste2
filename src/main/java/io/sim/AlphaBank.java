package io.sim;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlphaBank extends Thread {
    private Map<String, Account> accounts;
    private ServerSocket serverSocket;

    public AlphaBank() {
        this.accounts = new HashMap<>();
    }

    public void startServer() {
        try {
            serverSocket = new ServerSocket(1024);
            System.out.println("Servidor AlphaBank iniciado na porta " + 1024);

           while (true) {
                Socket clientSocket = serverSocket.accept();
                // Cria uma thread para lidar com as solicitações do cliente
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
                // Parse da mensagem JSON
                JsonObject requestJson = JsonParser.parseString(request).getAsJsonObject();
    
                // Verificar o tipo de solicitação
                if (requestJson.has("requestType") && requestJson.get("requestType").getAsString().equals("CREATE")) {
                    String username = requestJson.get("username").getAsString();
                    String password = requestJson.get("password").getAsString();
                    
                    // Criar a conta e enviar resposta
                    createAccount(username, password, 100);
                    writer.println("{\"status\": \"ACCOUNT_CREATED\"}");
                } else {
                    writer.println("{\"status\": \"Comando inválido\"}");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Account getAccount(String username) {
        return accounts.get(username);
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

    @Override
    public void run() {
        startServer();
    }
    
    
}
