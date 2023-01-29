package com.isacore.quality.model.spp;

import com.isacore.quality.model.Area;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReporteSolicitudPPDTO implements Serializable {

    private final String[] OpcionesMotivos = {"Desarrollo Proveedores", "Disponibilidad Materias Primas", "Ampliación Portafolio", "Reclamos Clientes",
            "Rotura de Inventario", "Reducción Costos", "Mejora de Producto", "Mejora del Proceso", "Nueva Línea de Negocio",
            "Puesta a Punto Maquinaria Nueva", "Puesta a Punto Maquinaria Posterior al Mantenimiento"};

    private final String[] OpcionesMaterialDesc = {"Materia Prima", "Láminas Impermeabilizantes", "Prod. en Proceso", "Prod. Terminado",
            "Suministros", "Accesorios", "Prod. Viales", "Rev. Líquidos", "Pinturas", "Prod. Metálicos", "Paneles PUR"};

    private String codigo;
    private LocalDateTime fechaCreacion;
    private LocalDate fechaEntrega;
    private String lineaAplicacion;
    private String motivo;
    private String motivoOtro;
    private String materialLineaProceso;
    private String materialLineaProcesoOtro;
    private String descripcionProducto;
    private String variablesProceso;
    private String verificacionAdicional;
    private Area area;
    private boolean requiereInforme;
    private String nombreSolicitante;
    private String usuarioAprobador;
    private LocalDate fechaAprobacion;
    private String observacion;
    private String imagen1Ruta;
    private BigDecimal cantidadRequeridaProducir = BigDecimal.ZERO;
    private String unidadRequeridaProducir;
    private boolean contieneAdjuntoDescripcionProducto;
    private List<String> motivosSeleccionados = new ArrayList<>();
    private List<String> materialesSeleccionados = new ArrayList<>();
    private List<MaterialFormula> materialesFormula = new ArrayList<>();

    public ReporteSolicitudPPDTO(SolicitudPruebasProceso solicitud, String nombreSolicitante, String usuarioAprobador) {
        this.codigo = solicitud.getCodigo();
        this.fechaCreacion = solicitud.getFechaCreacion();
        this.fechaEntrega = solicitud.getFechaEntrega();
        this.lineaAplicacion = solicitud.getLineaAplicacion();
        this.motivo = solicitud.getMotivo();
        this.motivoOtro = solicitud.getMotivoOtro();
        this.materialLineaProceso = solicitud.getMaterialLineaProceso();
        this.materialLineaProcesoOtro = solicitud.getMaterialLineaProcesoOtro();
        this.descripcionProducto = solicitud.getDescripcionProducto();
        this.variablesProceso = solicitud.getVariablesProceso();
        this.verificacionAdicional = solicitud.getVerificacionAdicional();
        this.area = solicitud.getArea();
        this.requiereInforme = solicitud.isRequiereInforme();
        this.nombreSolicitante = nombreSolicitante;
        this.usuarioAprobador = usuarioAprobador;
        this.fechaAprobacion = solicitud.getFechaSolicitudValidada();
        this.observacion = solicitud.getObservacion();
        this.imagen1Ruta = solicitud.getImagen1Ruta();
        this.materialesFormula = solicitud.getMaterialesFormula();
        this.cantidadRequeridaProducir = solicitud.getCantidadRequeridaProducir();
        this.unidadRequeridaProducir = solicitud.getUnidadRequeridaProducir().getAbreviatura();
        this.contieneAdjuntoDescripcionProducto = solicitud.isContieneAdjuntoDescripcionProducto();
        this.motivosParaImprimir();
//        this.materialesParaImprimir();
    }

    public String getNombreArea() {
        return this.area != null ? this.area.getNameArea() : "";
    }

    private void motivosParaImprimir() {
        String[] motivosSelected = this.getMotivo().split(",");
        Boolean flag1 = false;
        for (String m : this.OpcionesMotivos) {
            flag1 = false;
            for (String mS : motivosSelected) {
                if (m.equalsIgnoreCase(mS)) {
                    this.motivosSeleccionados.add("x" + "  " + m);
                    flag1 = true;
                }
            }
            if (!flag1)
                this.motivosSeleccionados.add("    " + m);
        }
        this.buscarOtroMotivo(motivosSelected[motivosSelected.length - 1]);
    }

    private void materialesParaImprimir() {
        String[] materialDescSelected = this.materialLineaProceso.split(",");
        Boolean flag1 = false;
        for (String m : this.OpcionesMaterialDesc) {
            flag1 = false;
            for (String mS : materialDescSelected) {
                if (m.equalsIgnoreCase(mS)) {
                    this.materialesSeleccionados.add("x" + "  " + m);
                    flag1 = true;
                }
            }
            if (!flag1)
                this.materialesSeleccionados.add("    " + m);
        }
        this.buscarOtroMaterial(materialDescSelected[materialDescSelected.length - 1]);
    }

    private void buscarOtroMotivo(String option) {
        Boolean flag = false;
        for (String x : this.OpcionesMotivos) {
            if (x.equalsIgnoreCase(option)) {
                flag = true;
                break;
            }
        }
        if (!flag)
            this.motivoOtro = option;
    }

    private void buscarOtroMaterial(String option) {
        Boolean flag = false;
        for (String x : this.OpcionesMaterialDesc) {
            if (x.equalsIgnoreCase(option)) {
                flag = true;
                break;
            }
        }
        if (!flag)
            this.materialLineaProcesoOtro = option;
    }
}
