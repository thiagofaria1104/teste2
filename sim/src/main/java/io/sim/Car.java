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
    private int consumo = 12; // Consumo médio do veículo

    public Car(String id, SumoTraciConnection sumo) {
        this.fuelTank = 10.0;
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
        while (true) {
            try {
                KmCompleted();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean requestRefueling() throws Exception {
        int aux = 0;
        while (true) {
            if (KmCompleted()) {
                aux++;
                if(aux == consumo ){
                    fuelTank-= 1 ; // Decrementa 1 litro a cada 12 Quilomêtros
                    System.out.println("Valor do tanque do: " + id + " " + fuelTank);
                    aux = 0;
                }
            }
            if (fuelTank <= 3) {
                getSumo().do_job_set(Vehicle.setSpeed(id, 0));
                System.out.println(id + " está parado para solicitar o abastecimento");
                return true;
            } else {
                return false;
            }
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
        double aux = (double) getSumo().do_job_get(Vehicle.getDistance(this.id));
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

    public String getId() {
        return id;
    }
    
}
