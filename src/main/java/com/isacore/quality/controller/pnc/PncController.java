package com.isacore.quality.controller.pnc;

import com.isacore.quality.model.pnc.ConsultaPncDTO;
import com.isacore.quality.model.pnc.PncDTO;
import com.isacore.quality.model.pnc.ProductoNoConforme;
import com.isacore.quality.service.pnc.IProductoNoConformeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/pncs")
public class PncController {

    //	private final  INonconformingProductService service;
    private final IProductoNoConformeService service;


    @PostMapping
    public ResponseEntity<ProductoNoConforme> crear(@RequestBody ProductoNoConforme dto) {
        ProductoNoConforme proveedorCreado = service.registrar(dto);
        return ResponseEntity.ok(proveedorCreado);
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


    //MediaType.APPLICATION_OCTET_STREAM_VALUE permite enviar el arreglo de bits en lugar de json
//		@GetMapping(value="/generarReporte/{idPnc}", produces= MediaType.APPLICATION_OCTET_STREAM_VALUE)
//		public ResponseEntity<byte[]> generarReporte( @PathVariable Integer idPnc) {
//			byte[] data= null;
//			data = service.generarReportePnc(idPnc);
//			return new ResponseEntity<byte[]>(data, HttpStatus.OK);
//		}

}
