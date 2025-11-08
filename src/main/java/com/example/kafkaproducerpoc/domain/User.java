package com.example.kafkaproducerpoc.domain;

public class User {


    String id;
    String nombres;
    int edad;
    String rol;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setRol(String tipoCliente) {
        this.rol = tipoCliente;
    }

    public String getNombres() {
        return nombres;
    }

    public int getEdad() {
        return edad;
    }

    public String getRol() {
        return rol;
    }

     public String toJson() {
        return "{" +
                "id='" + id + '\'' +
                ", nombres='" + nombres + '\'' +
                ", edad=" + edad +
                ", rol='" + rol + '\'' +
                '}';
    }
}
