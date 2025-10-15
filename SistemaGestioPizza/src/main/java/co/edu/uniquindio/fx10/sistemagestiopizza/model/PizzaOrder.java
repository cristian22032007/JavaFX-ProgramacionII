package co.edu.uniquindio.fx10.sistemagestiopizza.model;

public class PizzaOrder implements Order {
        @Override
        public String getDescription() {
            return "Pizza";
        }

        @Override
        public double getCost() {
            return 20000.00;
        }
    }

