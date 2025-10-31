package co.edu.uniquindio.fx10.sistemamotos.model;

import java.util.Date;

public class Moto {
    private String placa;
    private String marca;
    private int modelo;

    private Moto(Builder builder) {
        this.placa = builder.placa;
        this.marca = builder.marca;
        this.modelo = builder.modelo;
    }
    public static class Builder {
        private String placa;
        private String marca;
        private int modelo;

        public Builder placa(String placa) {this.placa = placa; return this;}
        public Builder marca(String marca) {this.marca = marca; return this;}
        public Builder modelo(int modelo) {this.modelo = modelo;return this;}
        public Moto build() {return new Moto(this);}
    }
    public String getPlaca() {return placa;}
    public String getMarca() {return marca;}
    public int getModelo() {return modelo;}
    public void setPlaca(String placa) {this.placa = placa;}
    public void setMarca(String marca) {this.marca = marca;}
    public void setModelo(int modelo) {this.modelo = modelo;}

}

