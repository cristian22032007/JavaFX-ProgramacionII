package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public abstract class OrderDecorator implements Order {
    protected Order order;
    public OrderDecorator(Order order) {
        this.order = order;
    }
    @Override
    public abstract String getDescription();
    @Override
    public abstract double getCost();
}
