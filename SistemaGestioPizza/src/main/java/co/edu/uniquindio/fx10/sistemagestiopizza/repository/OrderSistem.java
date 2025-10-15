package co.edu.uniquindio.fx10.sistemagestiopizza.repository;

import co.edu.uniquindio.fx10.sistemagestiopizza.model.*;

import java.util.ArrayList;
import java.util.List;

public class OrderSistem {
    private static OrderSistem instacia;
    private List<Order> orders;
    private OrderSistem() {
        orders = new ArrayList<>();
    }
    public static OrderSistem getInstance() {
        if (instacia == null) {
            instacia = new OrderSistem();
        }
        return instacia;
    }
    public List<Order> getOrders() {
        return orders;
    }
    public void addOrder(Order order) {
        orders.add(order);
    }

}
