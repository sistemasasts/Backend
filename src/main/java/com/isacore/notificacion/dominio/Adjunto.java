package com.isacore.notificacion.dominio;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Adjunto {
    private String nombre;
    private String contentType;
    private byte[] archivo;
    private String ruta;

    public Adjunto(String nombre, byte[] archivo) {
        this.nombre = nombre;
        this.archivo = archivo;
    }

    public Adjunto(String nombre, byte[] archivo, String contentType) {
        this.nombre = nombre;
        this.contentType = contentType;
        this.archivo = archivo;
    }

    public Adjunto(String nombre, byte[] archivo, String contentType, String ruta) {
        this.nombre = nombre;
        this.contentType = contentType;
        this.archivo = archivo;
        this.ruta = ruta;
    }
}
