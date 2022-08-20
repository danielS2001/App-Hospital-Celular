package com.example.hospitaldelcelular.Objetos;

import java.io.Serializable;

public class Citas implements Serializable {
    private int _ID;
    private String nombre;
    private String telefono;
    private String correo;
    private String dispositivo;
    private String falla;
    private String fecha;
    private String hora;
    private String idMovil;
    public Citas() { }
    public Citas(String nombre, String telefono, String correo,
                 String dispositivo, String falla, String fecha, String hora, String idMovil) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.dispositivo = dispositivo;
        this.falla = falla;
        this.fecha = fecha;
        this.hora = hora;
        this.idMovil = idMovil;
    }
    public int get_ID() {return _ID; }
    public void set_ID(int _ID) {this._ID = _ID; }

    public String getNombre() {return nombre; }
    public void setNombre(String nombre) {this.nombre = nombre; }

    public String getTelefono() {return telefono; }
    public void setTelefono(String telefono) {this.telefono = telefono; }

    public String getCorreo() {return correo; }
    public void setCorreo(String correo) {this.correo = correo; }

    public String getDispositivo() {return dispositivo; }
    public void setDispositivo(String dispositivo) {this.dispositivo = dispositivo; }

    public String getFalla() {return falla; }
    public void setFalla(String falla) {this.falla = falla;}

    public String getFecha() {return fecha; }
    public void setFecha(String fecha) {this.fecha = fecha;}

    public String getHora() {return hora; }
    public void setHora(String hora) {this.hora = hora; }

    public String getIdMovil() {return idMovil; }
    public void setIdMovil(String idMovil) {this.idMovil = idMovil; }
}
