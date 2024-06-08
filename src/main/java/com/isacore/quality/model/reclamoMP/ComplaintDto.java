package com.isacore.quality.model.reclamoMP;

import com.isacore.util.date.MoreDates;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class ComplaintDto implements Serializable {

    private long id;
    private Integer idProduct;
    private Integer idProvider;
    private String batchProvider;
    private String palletNumber;
    private String affectedProduct;
    private double porcentComplaint;
    private double affectedAmount;
    private double totalAmount;
    private String place;
    private LocalDateTime dateComplaint;
    private boolean applyReturn;
    private String nombreProducto;
    private String detailNCP;
    private long unidadMedidaId;
    private String unidadMedidaDescripcion;
    private String otherProvider;
    private ComplaintEstado state;
    private String estadoTexto;
    private String nombreProveedor;
    private String ordenCompra;
    private List<ProblemDto> problemas;
    private List<ExecutedAction> listExecutedActons;
    private List<ProviderActionPlanDto> listActionsPlanProvider;

    private String observacion;
    private ComplaintOrdenFlujo orden;
    private ComplaintEstado accion;
    private long number;
    private List<String> destinatarios;
    private String mensaje;
    private String asunto;

    private String aprobadorCalidadCompleto;
    private String aprobadorComprasCompleto;
    private LocalDateTime fechaAprobadorCalidad;
    private LocalDateTime fechaAprobadorCompras;
    private String userName;
    private LocalDateTime dateCreateComplaint;
    private Long kpiTime;

}
