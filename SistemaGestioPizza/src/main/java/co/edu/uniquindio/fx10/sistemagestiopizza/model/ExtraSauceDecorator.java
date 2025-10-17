package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class ExtraSauceDecorator extends OrderDecorator {
    public ExtraSauceDecorator(Order order) {
        super(order);
    }

    @Override
    public String getDescription() {
        return order.getDescription() + ", extra sauce";
    }

    @Override
    public double getCost() {
        return order.getCost() + 500.00;
    }


}
