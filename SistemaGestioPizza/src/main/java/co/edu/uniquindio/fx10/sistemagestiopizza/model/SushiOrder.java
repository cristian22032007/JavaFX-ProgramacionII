package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class SushiOrder implements Order {
    @Override
    public String getDescription() {
        return "Sushi";
    }

    @Override
    public double getCost() {
        return 25000.00;
    }
}
