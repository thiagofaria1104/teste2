package io.sim;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AppTest {

    private Thread serverThreadAlpha;
    private Thread simulacao;
    private Thread serverThreadCompany;

    @Before
    public void setUp() throws IOException {
        // Inicie os servidores em threads separadas antes de cada teste
        AlphaBank alphaBank = new AlphaBank();
        serverThreadAlpha = new Thread(() -> alphaBank.startServer());

        EnvSimulator envSimulator = new EnvSimulator();
        simulacao = new Thread(() -> envSimulator.start());

        Company company = new Company();
        serverThreadCompany = new Thread(() -> company.startServer());
    }

    @After
    public void tearDown() {
        // Pare os servidores após cada teste
        serverThreadAlpha.interrupt();
        simulacao.interrupt();
        serverThreadCompany.interrupt();
    }

    @Test
    public void testApp() throws InterruptedException, IOException {
        // Inicie a aplicação
        App.main(new String[] {});

        // Aguarde algum tempo para garantir que as operações assíncronas tenham tempo para executar
        Thread.sleep(5000);

        // Crie uma instância de Company para chamar o método não estático
        Company company = new Company();

        // Verifique se a Company está pronta
        assertTrue("A Company deve estar pronta", company.isServerReady());

        // Adicione mais verificações conforme necessário
    }
}
