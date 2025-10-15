package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class ExtraCheeseDecorator extends OrderDecorator {
    public ExtraCheeseDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", extra cheese";
    }

    @Override
    public double getCost() {
        return + 1500.00;
    }

}
