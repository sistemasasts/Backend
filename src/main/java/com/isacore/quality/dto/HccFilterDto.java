package com.isacore.quality.dto;

public class HccFilterDto {
    private String lote;
    private String producto;
    private String hcc;
    private String fechaInicio;
    private String fechaFin;
    private String tipoProducto;

    public String getLote() { return lote; }
    public void setLote(String lote) { this.lote = lote; }
    public String getProducto() { return producto; }
    public void setProducto(String producto) { this.producto = producto; }
    public String getHcc() { return hcc; }
    public void setHcc(String hcc) { this.hcc = hcc; }
    public String getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(String fechaInicio) { this.fechaInicio = fechaInicio; }
    public String getFechaFin() { return fechaFin; }
    public void setFechaFin(String fechaFin) { this.fechaFin = fechaFin; }
    public String getTipoProducto() { return tipoProducto; }
    public void setTipoProducto(String tipoProducto) { this.tipoProducto = tipoProducto; }
}
