package com.example.sistemainventario;

public class Movimiento {
    private int id;
    private String tipo;
    private String material;
    private int cantidad;
    private String comentario;
    private String fecha;


    public Movimiento(int id, String tipo, String material, int cantidad, String comentario, String fecha) {
        this.id = id;
        this.tipo = tipo;
        this.material = material;
        this.cantidad = cantidad;
        this.comentario = comentario;
        this.fecha = fecha;
    }


    // Getters
    public int getId() { return id; }
    public String getTipo() { return tipo; }
    public String getMaterial() { return material; }
    public int getCantidad() { return cantidad; }
    public String getComentario() { return comentario; }
    public String getFecha() { return fecha; }
    


}
