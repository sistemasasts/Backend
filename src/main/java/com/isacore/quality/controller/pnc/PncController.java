package com.isacore.quality.controller.pnc;

import com.isacore.quality.model.pnc.*;
import com.isacore.quality.model.spp.SolicitudPruebaProcesoDocumento;
import com.isacore.quality.model.spp.TipoAprobacionPP;
import com.isacore.quality.service.pnc.IPncDocumentoService;
import com.isacore.quality.service.pnc.IProductoNoConformeService;
import com.isacore.util.CatalogDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/pncs")
public class PncController {

    //	private final  INonconformingProductService service;
    private final IProductoNoConformeService service;
    private final IPncDocumentoService pncDocumentoService;


    @PostMapping
    public ResponseEntity<ProductoNoConforme> crear(@RequestBody ProductoNoConforme dto) {
        ProductoNoConforme proveedorCreado = service.registrar(dto);
        return ResponseEntity.ok(proveedorCreado);
    }

    @GetMapping("/{pncId}")
    public ResponseEntity<ProductoNoConforme> listarPorId(@PathVariable("pncId") long pncId) {
        ProductoNoConforme obj = service.listarPorId(pncId);
        return ResponseEntity.ok(obj);
    }

    @PutMapping
    public ResponseEntity<ProductoNoConforme> modificar(@RequestBody ProductoNoConforme proveedor) {
        ProductoNoConforme obj = service.actualizar(proveedor);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/listarPorCriterios")
    public ResponseEntity<Page<PncDTO>> listarPorCriterios(final Pageable page, @RequestBody final ConsultaPncDTO consulta) {
        final Page<PncDTO> resultadoConsulta = this.service.listar(page, consulta);
        return ResponseEntity.ok(resultadoConsulta);
    }

    @PostMapping("/agregarDefecto")
    public ResponseEntity<List<PncDefecto>> agregarDefecto(@RequestPart("info") String info,
                                                           @RequestPart("file") MultipartFile file) throws IOException {
        List<PncDefecto> defectos = this.service.agregarDefecto(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(defectos);
    }

    @GetMapping("/defecto/verImagen/{imagenId}")
    public ResponseEntity<PncDocumento> verImagenDefecto(@PathVariable("imagenId") long imagenId) throws IOException {
        PncDocumento imagenBase64 = this.pncDocumentoService.listarDocumentoPorId(imagenId);
        return ResponseEntity.ok(imagenBase64);
    }

    @PutMapping("/actualizarDefecto")
    public ResponseEntity<List<PncDefecto>> actualizarDefecto(@RequestPart("info") String info,
                                                              @RequestPart("file") MultipartFile file) throws IOException {
        List<PncDefecto> defectos = this.service.actualizarDefecto(info, file.getBytes(), file.getOriginalFilename(), file.getContentType());
        return ResponseEntity.ok(defectos);
    }

    @DeleteMapping("/{pncId}/{defectoId}")
    public ResponseEntity<List<PncDefecto>> eliminarDefecto(@PathVariable("pncId") long pncId, @PathVariable("defectoId") long defectoId) {
        List<PncDefecto> defectos = this.service.eliminarDefecto(pncId, defectoId);
        return ResponseEntity.ok(defectos);
    }

    @GetMapping("/catalogoProcedencia")
    public ResponseEntity<List<CatalogDTO>> obtenerProcedenciaLinea() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for(ProcedenciaLinea origen : ProcedenciaLinea.values()){
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        catalgo.stream().sorted(Comparator.comparing(CatalogDTO::getLabel));
        return ResponseEntity.ok(catalgo);
    }

    @GetMapping("/catalogoLineaAfecta")
    public ResponseEntity<List<CatalogDTO>> obtenerLineaAfecta() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for(LineaAfecta origen : LineaAfecta.values()){
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        catalgo.stream().sorted(Comparator.comparing(CatalogDTO::getLabel));
        return ResponseEntity.ok(catalgo);
    }

    @GetMapping("/catalogoEstado")
    public ResponseEntity<List<CatalogDTO>> estados() {
        List<CatalogDTO> catalgo = new ArrayList<>();
        for(EstadoPnc origen : EstadoPnc.values()){
            catalgo.add(new CatalogDTO(origen.getDescripcion(), origen.toString()));
        }
        catalgo.stream().sorted(Comparator.comparing(CatalogDTO::getLabel));
        return ResponseEntity.ok(catalgo);
    }

    //MediaType.APPLICATION_OCTET_STREAM_VALUE permite enviar el arreglo de bits en lugar de json
//		@GetMapping(value="/generarReporte/{idPnc}", produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
//		public ResponseEntity<byte[]> generarReporte( @PathVariable Integer idPnc) {
//			byte[] data= null;
//			data = service.generarReportePnc(idPnc);
//			return new ResponseEntity<byte[]>(data, HttpStatus.OK);
//		}

}
