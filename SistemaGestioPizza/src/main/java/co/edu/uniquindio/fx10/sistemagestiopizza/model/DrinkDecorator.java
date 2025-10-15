package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class DrinkDecorator extends OrderDecorator {
    public DrinkDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", Bebida";
    }

    @Override
    public double getCost() {
        return + 2000.00;
    }
}
