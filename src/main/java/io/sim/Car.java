package io.sim;

import java.util.Random;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;

public class Car extends Vehicle implements Runnable {
    Random random = new Random();
    private double fuelTank;// Tanque de combustível
    private Auto auto;
    private String id;
    private SumoTraciConnection sumo;
    private int kilometers = 0;
    private int consumo = 1; // Consumo médio do veículo

    public Car(String id, SumoTraciConnection sumo) {
        this.fuelTank = 4.0;
        this.id = id;
        this.setSumo(sumo); // Inicialize o sumo
    }

    
    public SumoTraciConnection getSumo() {
        return sumo;
    }

    public void setSumo(SumoTraciConnection sumo) {
        this.sumo = sumo;
    }

    @Override
    public void run() {
       
    }

    public boolean requestRefueling() throws Exception {
        int aux = 0;
        while (true) {
            //System.out.println("esta preso no request");
            if (KmCompleted()) {
                aux++;
                if (aux == consumo) {
                    fuelTank -= 1; // Decrementa 1 litro a cada 12 Quilômetros
                    aux = 0;
                    return false;
                }
            }
            if (fuelTank <= 3) {
                auto.setSpeedParar();
                System.out.println("Carro está parado para abastecimento");
                return true;
            }
            // Delay para evitar loop infinito sem operações
            Thread.sleep(1000);
        }
    }
    
    public Auto criarAutos(String idDriver) throws Exception {
        int fuelType = 2;
        int fuelPreferential = 2;
        double fuelPrice = 3.40;
        int personCapacity = 1;
        int personNumber = 1;
        int red = random.nextInt(256);    // Componente Red (0 a 255)
        int green = random.nextInt(256);  // Componente Green (0 a 255)
        int blue = random.nextInt(256);   // Componente Blue (0 a 255)
        SumoColor color = new SumoColor(red, green, blue, 126); // Cor random gerada
        auto = new Auto(true, id, color, idDriver, getSumo(), 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber);
        return auto;
    }

    public boolean KmCompleted() throws Exception {
        double aux = (double) sumo.do_job_get(Vehicle.getDistance(this.id));
        //System.out.println(aux);
        int currentKilometers = (int) (aux / 1000); // Converte de metros para Km e realiza o casting para int
        if (currentKilometers > kilometers) {
            if (currentKilometers - kilometers >= 1) {
                kilometers = currentKilometers;
                return true; // Verdadeiro se 1 quilômetro percorrido
            }
        }
        return false; // Falso se nenhum quilômetro completo adicional foi percorrido
    }

    public void addFuel(double amount) {
        fuelTank += amount;
    }

    public double getFuelTank(){
        return fuelTank;
    }
    public void setFuelTank(){
        fuelTank -= 1;
    }

    public String getId() {
        return id;
    }
    public Auto getAuto(){
        return auto;
    }
    
}
