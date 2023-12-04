package io.sim;

import java.io.IOException;

/**
 * Hello world!
 *
 */

public class App {
    public static void main(String[] args) throws IOException {

        // Iniciar o AlphaBank
        AlphaBank alphaBank = new AlphaBank();
        alphaBank.start();

        // Iniciar a Simulação
        EnvSimulator simulator = new EnvSimulator();
        simulator.start();

        // Iniciar a Company
        Company company = new Company();
        company.start();

        // Iniciar a FuelStation
        FuelStation fuelStation = new FuelStation();
        fuelStation.start();

        // Esperar até que a Company e o Posto esteja pronta para receber os drivers
        while (!company.isServerReady()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Enviar os drivers para a Company e ao Posto
        company.receberDrivers(simulator.getDrivers());
        fuelStation.receberDrivers(simulator.getDrivers());

    }

}
