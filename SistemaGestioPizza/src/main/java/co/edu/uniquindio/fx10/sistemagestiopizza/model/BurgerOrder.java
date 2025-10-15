package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class BurgerOrder implements Order {
    @Override
    public String getDescription() {
        return "Hamburguesa";
    }

    @Override
    public double getCost() {
        return 18000.00;
    }
}
