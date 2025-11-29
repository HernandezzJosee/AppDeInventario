package com.example.sistemainventario;

public class Material {
    private int id;
    private String nombre;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;

    public Material(int id, String nombre, String descripcion, int stockActual, int stockMinimo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public int getStockActual() { return stockActual; }
    public int getStockMinimo() { return stockMinimo; }
}

