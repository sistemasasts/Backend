package com.isacore.quality.tx;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.ReportProcessTestRequestDto;
import com.isacore.quality.model.Complaint;
import com.isacore.quality.model.FileDocument;
import com.isacore.quality.model.ProcessTestRequest;
import com.isacore.quality.report.GenerateReportQuality;
import com.isacore.quality.service.IProcessTestRequestService;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxProcessTestRequest {

	public static final String TX_NAME_GenerateReportProcessTestRequest = "GenerateReportProcessTestRequest";
	public static final String TX_CODE_GenerateReportProcessTestRequest = "TxQQRgenerateReportProcessTestRequest";

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private IProcessTestRequestService ProcessTestRequestService;
	/**
	 * TX NAME: Generate Report Process Test Request.
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRGenerateReportProcessTestRequest(WebRequestIsa wri) {
		logger.info("> TX: TxQQRGenerateReportComplaint");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GenerateReportProcessTestRequest);
		wrei.setTransactionCode(TX_CODE_GenerateReportProcessTestRequest);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();
			
				try {
					logger.info("> mapeando json a la clase: " + Complaint.class);
					ProcessTestRequest pTR = JSON_MAPPER.readValue(jsonValue, ProcessTestRequest.class);
					
					ProcessTestRequest cc = this.ProcessTestRequestService.findById(pTR);
					
					if (cc != null) {
						logger.info(">> Reclamo de MP obtenido correctamente");
						//this.problemService.dataTratamientImagesReport(cc.getListProblems());
						ReportProcessTestRequestDto report = new ReportProcessTestRequestDto();
						report.setProcessTest(cc);
						report.motivosForReportPrint();
						report.materialDescForReportPrint();
						
						String fileReport= GenerateReportQuality.runGenerateReportProcessTestRequest(report);
												
						if(!fileReport.equals(GenerateReportQuality.REPORT_ERROR)) {
							logger.info(">> Reporte generado correctamente");
							
							String name= new File(fileReport).getName();
							String [] nameFile= fileReport.split("/");
							String ext = this.getExtensionFile(name);
							String fileb64=PassFileToRepository.fileToBase64(fileReport, ext);
							FileDocument fd= new FileDocument();
							fd.setBase64File(fileb64);
							fd.setName(name);
							
							
							String jsonR = JSON_MAPPER.writeValueAsString(fd);
							 
							wrei.setParameters(jsonR);
							wrei.setMessage("DDP04::" + cc.getProductGetting() + "::: creado satisfactoriamente");
							wrei.setStatus(WebResponseMessage.STATUS_OK);
							return new ResponseEntity<Object>(wrei, HttpStatus.OK);
						}else {
							logger.info(">> No se ha podido generar el reporte");
							wrei.setMessage("No se pudo Generar el DDP04::" + cc.getProductGetting());
							wrei.setStatus(WebResponseMessage.STATUS_ERROR);
							return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
						}
					} else {
						logger.error(">> Error al consultar Reclamo de MP");
						wrei.setMessage("Error al consultar Reclamo de MP");
						wrei.setStatus(WebResponseMessage.STATUS_ERROR);
						return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (IOException e) {
					logger.error("> No se ha podido serializar el JSON a la clase: " + Complaint.class);
					wrei.setMessage(WebResponseMessage.ERROR_TO_CLASS);
					wrei.setStatus(WebResponseMessage.STATUS_ERROR);
					return new ResponseEntity<Object>(wrei, HttpStatus.BAD_REQUEST);
				}
			
		}
	}
	
	private static String getExtensionFile(String filename) {
        File f = new File(filename);
        if (f == null || f.isDirectory()){
              return "nulo o directorio";
        }else if (f.isFile()){
              int index = filename.lastIndexOf('.');
              if (index == -1) {
                    return "";
              } else {
                    return filename.substring(index + 1);
              }
        }else{
              return "que has enviado?";
        }
  }
}
