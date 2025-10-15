package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class OrderFactory {
    public static Order createOrder(String type) {
        return switch (type.toLowerCase()) {
            case "pizza" -> new PizzaOrder();
            case "sushi" -> new SushiOrder();
            case "hamburguesa" -> new BurgerOrder();
            default -> null;
        };
    }
}