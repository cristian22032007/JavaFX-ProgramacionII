package co.edu.uniquindio.fx10;

public class Main {
    public static void main(String[] args) {
            Canal canal = new Canal();

            Usuario u1 = new Usuario("Cristian");
            Usuario u2 = new Usuario("Laura");

            canal.agregarSuscriptor(u1);
            canal.agregarSuscriptor(u2);

            canal.subirVideo("Cómo funcionan las estrellas 💫");
        }
    }
