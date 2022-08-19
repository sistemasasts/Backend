package com.isacore.workflow.tx;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isacore.quality.dto.TrayDto;
import com.isacore.quality.exception.SolicitudEnsayoErrorException;
import com.isacore.quality.model.ActionProcess;
import com.isacore.quality.model.FileDocument;
import com.isacore.quality.model.Notification;
import com.isacore.quality.model.ProcessFlow;
import com.isacore.quality.model.RequestTest;
import com.isacore.quality.model.se.ConfiguracionUsuarioRolEnsayo;
import com.isacore.quality.model.se.OrdenFlujo;
import com.isacore.quality.service.IActionProcessService;
import com.isacore.quality.service.INotificationService;
import com.isacore.quality.service.IProcessFlowService;
import com.isacore.quality.service.se.IConfiguracionUsuarioRolEnsayo;
import com.isacore.sgc.acta.model.Employee;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;
import com.isacore.util.PassFileToRepository;
import com.isacore.util.WebRequestIsa;
import com.isacore.util.WebResponseIsa;
import com.isacore.util.WebResponseMessage;

@Component
public class TxProcess {

	public static final ObjectMapper JSON_MAPPER = new ObjectMapper();

	public static final String TX_NAME_SetProcessFlow = "SetProcessFlow";
	public static final String TX_CODE_SetProcessFlow = "TxQQRsetProcessFlow";

	public static final String TX_NAME_GetTrayProcess = "GetTrayProcess";
	public static final String TX_CODE_GetTrayProcess = "TxQQRgetTrayProcess";

	public static final String TX_NAME_ValidateDeliverMaterial = "ValidateDeliverMaterial";
	public static final String TX_CODE_ValidateDeliverMaterial = "TxQQRvalidateDeliverMaterial";

	public static final String TX_NAME_ReplyProcessFlow = "ReplyProcessFlow";
	public static final String TX_CODE_ReplyProcessFlow = "TxQQRReplyProcessFlow";

	public static final String TX_NAME_OrderMP = "OrderMP";
	public static final String TX_CODE_OrderMP = "TxQQROrderMP";

	public static final String TX_NAME_AvailableMP = "AvailableMP";
	public static final String TX_CODE_AvailableMP = "TxQQRAvailableMP";

	@Autowired
	private IProcessFlowService service;

	@Autowired
	INotificationService notiService;

	@Autowired
	TxNotification txNoti;

	@Autowired
	IUserImptekService userImptekService;

	@Autowired
	IActionProcessService apService;
	
	@Autowired
	IConfiguracionUsuarioRolEnsayo configuracionService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * TX NAME: SetProcessFlow crea y actualiza el proceso iniciado en la base de
	 * datos
	 * 
	 * @param wri
	 * @return
	 * @throws JSONException
	 */
	public ResponseEntity<Object> TxQQRsetProcessFlow(WebRequestIsa wri) {
		logger.info("> TX: TxQQRsetProcessFlow");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_SetProcessFlow);
		wrei.setTransactionCode(TX_CODE_SetProcessFlow);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + ProcessFlow.class);

				ProcessFlow pF = JSON_MAPPER.readValue(jsonValue, ProcessFlow.class);
				logger.info("> objeto a guardar: " + pF.toString());
				ActionProcess ap = pF.getListActionsProcess().get(0);
				ap.setComment("Solicitud de Ensayo enviado");
				ProcessFlow pp = new ProcessFlow();
				Notification n = new Notification();

				if (pF.getTestRequest() != null) {
					pF.getTestRequest().setDateRegistration(LocalDateTime.now());
					
					ConfiguracionUsuarioRolEnsayo configResponderSolicitud = null;
					if(pF.getIdProcess() == null) {
						configResponderSolicitud = configuracionService.buscarConfiguracionResponderSolicitud();
						if(configResponderSolicitud == null)
							throw new SolicitudEnsayoErrorException(String.format("Configuración %s no encontrada", OrdenFlujo.RESPONDER_SOLICITUD));
					}
					
					pp = this.service.create(pF);
					Notification notiTmp2 = new Notification();
					notiTmp2.setTitle("DDP " + pp.getTestRequest().getMaterialDetail() + " "
							+ pp.getTestRequest().getProviderName());
					notiTmp2.setIdProcess(pp.getIdProcess());
					notiTmp2.setUserImptek(pp.getListActionsProcess().get(0).getUserImptek());
					notiTmp2.setIdActionProcess(pp.getListActionsProcess().get(0).getIdactionProcess());
					n = this.notiService.create(notiTmp2);

					// Verificamos el area al que pertenece el usuario
					UserImptek useToFind = new UserImptek();
					useToFind.setIdUser(pp.getTestRequest().getAsUser());
					UserImptek user = this.userImptekService.findById(useToFind);

					if (!user.getEmployee().getArea().getNameArea().equalsIgnoreCase("Calidad")) {
						Notification notiTmp = new Notification();
						notiTmp.setTitle("DDP " + pp.getTestRequest().getMaterialDetail() + " "
								+ pp.getTestRequest().getProviderName());
						notiTmp.setIdProcess(pp.getIdProcess());
						//notiTmp.setUserImptek("vpillajo");
						notiTmp.setUserImptek(configResponderSolicitud.getUsuario().getIdUser());
						notiTmp.setIdActionProcess(pp.getListActionsProcess().get(0).getIdactionProcess());
						this.txNoti.sendNotification(notiTmp);
					}

				}

				if (pF.getProcessTestRequest() != null) {

					String url = PassFileToRepository.base64ToFile(pF.getProcessTestRequest().getImgBase64(),
							pF.getProcessTestRequest().getNameImg(), pF.getProcessTestRequest().getExtImg());
					pF.getProcessTestRequest().setUrlImg(url);
					pF.getProcessTestRequest().setDateRegistration(LocalDateTime.now());
					
					ConfiguracionUsuarioRolEnsayo configAprobarSolicitud = null;
					if(pF.getIdProcess()== null) {
						configAprobarSolicitud = configuracionService.buscarConfiguracionAprobarInforme();
						if(configAprobarSolicitud == null)
							throw new SolicitudEnsayoErrorException(String.format("Configuración %s no encontrada", OrdenFlujo.APROBAR_INFORME));
					}
					pp = this.service.create(pF);

					if (pp.getSubProcess() != null) {
						List<Notification> m = this.notiService
								.findByIdProcessAndState(pp.getSubProcess().getIdProcess(), "Pendiente");
						if (m != null) {
							for (Notification n1 : m) {
								if (n1.getUserImptek().equalsIgnoreCase(configAprobarSolicitud.getUsuarioId()))
									n1.setState("Revisado");
								if (n1.getUserImptek().equalsIgnoreCase(pp.getProcessTestRequest().getAsUser()))
									n1.setState("Revisado");
								this.notiService.update(n1);
							}
						} else {
							logger.error("> Prueba en proceso- sin Sub proceso");
						}
					}
					String identify = pp.getProcessTestRequest().getLineBelong();
					Notification notiTmp2 = new Notification();
					if (identify != null) {
						notiTmp2.setTitle(
								"DDP-04(" + identify + ")" + pp.getProcessTestRequest().getProductGetting() + " ");
					} else {
						notiTmp2.setTitle("DDP-04 " + pp.getProcessTestRequest().getProductGetting() + " ");
					}

					notiTmp2.setIdProcess(pp.getIdProcess());
					notiTmp2.setUserImptek(pp.getListActionsProcess().get(0).getUserImptek());
					notiTmp2.setIdActionProcess(pp.getListActionsProcess().get(0).getIdactionProcess());
					n = this.notiService.create(notiTmp2);

					Notification notiTmp = new Notification();
					notiTmp.setTitle("DDP-04 " + pp.getProcessTestRequest().getProductGetting() + " ");
					notiTmp.setIdProcess(pp.getIdProcess());
					//notiTmp.setUserImptek("svillacis");
					notiTmp.setUserImptek(configAprobarSolicitud.getUsuarioId());
					notiTmp.setIdActionProcess(pp.getListActionsProcess().get(0).getIdactionProcess());
					this.txNoti.sendNotification(notiTmp);
				}

				TrayDto tr = new TrayDto();
				tr.setProcess(pp);
				tr.setNoti(n);
				tr.setActionProcess(pp.getListActionsProcess().get(0));

				String json = JSON_MAPPER.writeValueAsString(tr);

				wrei.setMessage(WebResponseMessage.CREATE_SOLICITUD_UPDATE_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + ProcessFlow.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * TX NAME: GetTray Obtiene la bandeja de mensajes de acuerdo al usuario
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRgetTrayProcessByUser(WebRequestIsa wri) {
		logger.info("> TX: TxQQRgetTrayProcess");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_GetTrayProcess);
		wrei.setTransactionCode(TX_CODE_GetTrayProcess);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + TrayDto.class);
				TrayDto tU = JSON_MAPPER.readValue(jsonValue, TrayDto.class);
				UserImptek u = new UserImptek();
				u.setIdUser(tU.getUser());
				UserImptek ui = this.userImptekService.findById(u);
				Employee emp = ui.getEmployee();
				List<TrayDto> bandeja = new ArrayList<>();

				switch (emp.getArea().getNameArea().toUpperCase()) {
				case "CALIDAD":
					List<Notification> listNoti = this.notiService.findbyIdUserAndState(tU.getUser(), "Pendiente");

					for (Notification noti : listNoti) {
						TrayDto td = new TrayDto();
						td.setNoti(noti);
						ProcessFlow pf = new ProcessFlow();
						pf.setIdProcess(noti.getIdProcess());
						td.setProcess(this.service.findById(pf));
						ActionProcess act = new ActionProcess();
						act.setIdactionProcess(noti.getIdActionProcess());
						td.setActionProcess(this.apService.findById(act));
						UserImptek uTmp = new UserImptek();
						uTmp.setIdUser(td.getActionProcess().getUserImptek());
						UserImptek uiTmp = this.userImptekService.findById(uTmp);
						String[] name = uiTmp.getEmployee().getName().split(" ");
						String[] lastName = uiTmp.getEmployee().getLastName().split(" ");
						td.setUser(name[0] + " " + lastName[0]);
						bandeja.add(td);
					}
					logger.info("> objeto a enviar: " + bandeja.toString());
					if (bandeja.isEmpty() || bandeja == null) {
						logger.info("> no se encontro notificaciones");
						wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
						wrei.setStatus(WebResponseMessage.STATUS_INFO);
						return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					} else {
						String json = JSON_MAPPER.writeValueAsString(bandeja);

						wrei.setMessage(WebResponseMessage.SEARCHING_OK);
						wrei.setParameters(json);
						wrei.setStatus(WebResponseMessage.STATUS_OK);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);

					}
				default:
					List<Notification> listNotiCompras = this.notiService.findbyIdUserAndState(tU.getUser(),
							"Pendiente");

					for (Notification noti : listNotiCompras) {
						TrayDto td = new TrayDto();
						td.setNoti(noti);
						ProcessFlow pf = new ProcessFlow();
						pf.setIdProcess(noti.getIdProcess());
						td.setProcess(this.service.findById(pf));
						ActionProcess act = new ActionProcess();
						act.setIdactionProcess(noti.getIdActionProcess());
						td.setActionProcess(this.apService.findById(act));
						UserImptek uTmp = new UserImptek();
						uTmp.setIdUser(td.getActionProcess().getUserImptek());
						UserImptek uiTmp = this.userImptekService.findById(uTmp);
						String[] name = uiTmp.getEmployee().getName().split(" ");
						String[] lastName = uiTmp.getEmployee().getLastName().split(" ");
						td.setUser(name[0] + " " + lastName[0]);
						bandeja.add(td);
					}
					logger.info("> objeto a enviar: " + bandeja.toString());
					if (bandeja.isEmpty() || bandeja == null) {
						logger.info("> no se encontro notificaciones");
						wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
						wrei.setStatus(WebResponseMessage.STATUS_INFO);
						return new ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					} else {
						String json = JSON_MAPPER.writeValueAsString(bandeja);

						wrei.setMessage(WebResponseMessage.SEARCHING_OK);
						wrei.setParameters(json);
						wrei.setStatus(WebResponseMessage.STATUS_OK);
						return new ResponseEntity<Object>(wrei, HttpStatus.OK);

					}
					// List<Notification>listNoti=
					// this.notiService.findbyIdUserAndState(tU.getUser(), "Pendiente");

					/*
					 * default: logger.info("> no se encontro notificaciones");
					 * wrei.setMessage(WebResponseMessage.OBJECT_NOT_FOUND);
					 * wrei.setStatus(WebResponseMessage.STATUS_INFO); return new
					 * ResponseEntity<Object>(wrei, HttpStatus.INTERNAL_SERVER_ERROR);
					 */
				}

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + TrayDto.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * TX NAME: Valida la entrega de materiales para la solicitud de ensayos
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRValidateDeliverMaterial(WebRequestIsa wri) {
		logger.info("> TX: TxQQRValidateDeliverMaterial");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_ValidateDeliverMaterial);
		wrei.setTransactionCode(TX_CODE_ValidateDeliverMaterial);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + TrayDto.class);
				TrayDto pF = JSON_MAPPER.readValue(jsonValue, TrayDto.class);
				RequestTest rT = pF.getProcess().getTestRequest();
				LocalDate dateValidate = LocalDate.now();
				rT.setDeliverDateValidation(dateValidate);

				ActionProcess apT = new ActionProcess();
				apT.setIdactionProcess(this.apService.generateId());
				apT.setDateAP(LocalDateTime.now());
				apT.setComment("Entrega de muestras realizado");
				apT.setDetail(pF.getUserReplay() + "Valida Entrega de muestras");
				apT.setUserImptek(pF.getUserReplay());
				apT.setState("En Proceso");
				switch (pF.getProcess().getTestRequest().getDeliverType()) {
				case "Inmediato":
					apT.setTimeRespond(5);
					break;
				case "Medio":
					apT.setTimeRespond(15);
					break;
				case "Bajo":
					apT.setTimeRespond(25);
					break;
				default:
					break;

				}

				apT.setListFileDocument(pF.getProcess().getListActionsProcess().get(0).getListFileDocument());

				List<ActionProcess> lAP = pF.getProcess().getListActionsProcess();
				lAP.add(apT);
				pF.getProcess().setListActionsProcess(lAP);
				logger.info("> objeto a guardar: " + pF.toString());
				ProcessFlow pp = this.service.update(pF.getProcess());

				notificationManagment(pp, pF.getStateReply(), pF.getUserReplay()); // Envio de notificaciones
				pF.setActionProcess(apT);
				pF.setProcess(pp);

				String json = JSON_MAPPER.writeValueAsString(pF);

				wrei.setMessage(WebResponseMessage.CREATE_UPDATE_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + ProcessFlow.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * TX NAME: Respuesta a solicitud de ensayos Enviar el Informe a Svillacis
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRReplyProcessFlow(WebRequestIsa wri) {
		logger.info("> TX: TxQQRReplyProcessFlow");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_ReplyProcessFlow);
		wrei.setTransactionCode(TX_CODE_ReplyProcessFlow);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {
			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + TrayDto.class);
				TrayDto tray = JSON_MAPPER.readValue(jsonValue, TrayDto.class);
				logger.info("> objeto a guardar: " + tray.getProcess().toString());
				String json = "";

				switch (tray.getStateReply().toUpperCase()) {
				case "APROBADO":
					ActionProcess apTA = tray.getActionProcessReply();
					apTA.setIdactionProcess(this.apService.generateId());
					apTA.setDateAP(LocalDateTime.now());
					apTA.setState("Aprobado");
					List<FileDocument> lfd = new ArrayList<>();
					if (tray.getActionProcessReply().getListFileDocument().isEmpty()) {
						for (FileDocument f : tray.getActionProcess().getListFileDocument()) {
							FileDocument fTmp = new FileDocument();
							fTmp.setExtension(f.getExtension());
							fTmp.setName(f.getName());
							fTmp.setUrl(f.getUrl());
							fTmp.setType(f.getType());
							lfd.add(fTmp);
						}
						apTA.setListFileDocument(lfd);
					} else {
						this.tratamientFiles(apTA.getListFileDocument());
					}
					tray.getProcess().setFinishDate(LocalDate.now());
					tray.getProcess().getListActionsProcess().add(apTA);
					ProcessFlow ppApr = this.service.update(tray.getProcess());

					notificationManagment(ppApr, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																								// Notificaciones
																								// antiguas y
																								// creacion de las
																								// nuevas
					tray.setActionProcess(apTA);
					tray.setProcess(ppApr);
					json = JSON_MAPPER.writeValueAsString(tray);
					break;
				case "NO APROBADO":
					ActionProcess apT = tray.getActionProcessReply();
					apT.setIdactionProcess(this.apService.generateId());
					apT.setState("No Aprobado");
					apT.setDateAP(LocalDateTime.now());
					this.tratamientFiles(apT.getListFileDocument()); // Tratamiento de los files para moverlos al
																		// repositorio
					tray.getProcess().getListActionsProcess().add(apT);
					ProcessFlow pp = this.service.update(tray.getProcess());

					notificationManagment(pp, tray.getStateReply(), tray.getUserReplay());
					tray.setActionProcess(apT);
					tray.setProcess(pp);
					json = JSON_MAPPER.writeValueAsString(tray);

					break;
				case "POR APROBAR":
					ActionProcess apTPA = tray.getActionProcessReply();
					apTPA.setIdactionProcess(this.apService.generateId());
					apTPA.setState("Por Aprobar");
					apTPA.setDateAP(LocalDateTime.now());
					apTPA.setTimeRespond(2);
					this.tratamientFiles(apTPA.getListFileDocument()); // Tratamiento de los files para moverlos al
																		// // repositorio

					List<ActionProcess> apAux = tray.getProcess().getListActionsProcess();
					apAux.add(apTPA);
					ProcessFlow pfAux = tray.getProcess();

					pfAux.setListActionsProcess(apAux);
					ProcessFlow ppA = this.service.update(pfAux);

					notificationManagment(ppA, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																							// Notificaciones
																							// antiguas y creacion

					tray.setActionProcess(apTPA);
					tray.setProcess(ppA);
					json = JSON_MAPPER.writeValueAsString(tray);

					break;
				case "APROBADO INICIAR DDP-04":
					ActionProcess apTADDP04 = tray.getActionProcessReply();
					apTADDP04.setIdactionProcess(this.apService.generateId());
					apTADDP04.setDateAP(LocalDateTime.now());
					apTADDP04.setState("Aprobado-Envio-DDP04");

					List<FileDocument> lfdDDP04 = new ArrayList<>();
					if (tray.getActionProcessReply().getListFileDocument().isEmpty()) {
						for (FileDocument f : tray.getActionProcess().getListFileDocument()) {
							FileDocument fTmp = new FileDocument();
							fTmp.setExtension(f.getExtension());
							fTmp.setName(f.getName());
							fTmp.setUrl(f.getUrl());
							fTmp.setType(f.getType());
							lfdDDP04.add(fTmp);
						}
						apTADDP04.setListFileDocument(lfdDDP04);
					} else {
						this.tratamientFiles(apTADDP04.getListFileDocument());
					}
					tray.getProcess().getProcessTestRequest().setNameUserAprove(tray.getUserFullNameReplay());
					tray.getProcess().getProcessTestRequest().setApproveDate(LocalDate.now());
					tray.getProcess().getListActionsProcess().add(apTADDP04);
					ProcessFlow ppDDP04 = this.service.update(tray.getProcess());
					notificationManagment(ppDDP04, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																								// Notificaciones
																								// antiguas y
																								// creacion de las
																								// nuevas
					tray.setActionProcess(apTADDP04);
					tray.setProcess(ppDDP04);
					json = JSON_MAPPER.writeValueAsString(tray);

					break;
				case "POR APROBAR DDP04":
					ActionProcess apTPA04 = tray.getActionProcessReply();
					apTPA04.setIdactionProcess(this.apService.generateId());
					apTPA04.setState("Por Aprobar DDP04");
					apTPA04.setDateAP(LocalDateTime.now());
					this.tratamientFiles(apTPA04.getListFileDocument()); // Tratamiento de los files para moverlos
																			// al
																			// repositorio
					tray.getProcess().getListActionsProcess().add(apTPA04);
					ProcessFlow ppA04 = this.service.update(tray.getProcess());

					notificationManagment(ppA04, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																								// Notificaciones
																								// antiguas y
																								// creacion

					tray.setActionProcess(apTPA04);
					tray.setProcess(ppA04);
					json = JSON_MAPPER.writeValueAsString(tray);
					break;
				case "INFORME-DDP05-ENVIADO":
					ActionProcess apTADDP05 = tray.getActionProcessReply();
					apTADDP05.setIdactionProcess(this.apService.generateId());
					apTADDP05.setDateAP(LocalDateTime.now());
					apTADDP05.setState("Informe-DDP05-Enviado");
					this.tratamientFiles(apTADDP05.getListFileDocument()); // Tratamiento de los files para moverlos
																			// al
																			// repositorio
					tray.getProcess().getListActionsProcess().add(apTADDP05);
					ProcessFlow ppDDP05 = this.service.update(tray.getProcess());

					notificationManagment(ppDDP05, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																								// Notificaciones
																								// antiguas y
																								// creacion

					tray.setActionProcess(apTADDP05);
					tray.setProcess(ppDDP05);
					json = JSON_MAPPER.writeValueAsString(tray);

					break;

				case "POR APROBAR Y COMUNICAR":
					ActionProcess apTPAC = tray.getActionProcessReply();
					apTPAC.setIdactionProcess(this.apService.generateId());
					apTPAC.setDateAP(LocalDateTime.now());
					apTPAC.setState("Por Aprobar y Comunicar");
					this.tratamientFiles(apTPAC.getListFileDocument()); // Tratamiento de los files para moverlos
																		// al
																		// repositorio
					List<FileDocument> lstLastAP = new ArrayList<>();
					if (tray.getActionProcess().getState().equalsIgnoreCase("Informe-DDP05-Enviado")) {
						lstLastAP = tray.getActionProcess().getListFileDocument();

					} else {

						for (ActionProcess ac :  tray.getProcess().getListActionsProcess()) {
							if (ac.getState().equalsIgnoreCase("Informe-DDP05-Enviado"))
								lstLastAP = ac.getListFileDocument();
						}
					}

					for (FileDocument f : lstLastAP) {
						FileDocument fd = new FileDocument();
						fd.setExtension(f.getExtension());
						fd.setName(f.getName());
						fd.setUrl(f.getUrl());
						fd.setType(f.getType());
						apTPAC.getListFileDocument().add(fd);
					}

					tray.getProcess().getListActionsProcess().add(apTPAC);
					ProcessFlow PPAC = this.service.update(tray.getProcess());

					notificationManagment(PPAC, tray.getStateReply(), tray.getUserReplay()); // Seteo de
																								// Notificaciones
																								// antiguas y
																								// creacion
					tray.setActionProcess(apTPAC);
					tray.setProcess(PPAC);
					json = JSON_MAPPER.writeValueAsString(tray);
					break;
				case "APROBADO Y COMUNICADO":
					ProcessFlow preProcess = tray.getProcess();
					preProcess.setFinishDate(LocalDate.now());
					this.service.update(preProcess); // Actualizo el anterior proceso

					ActionProcess apAC = tray.getActionProcessReply();
					apAC.setIdactionProcess(this.apService.generateId());
					apAC.setDateAP(LocalDateTime.now());
					apAC.setState("Aprobado y Comunicado");
					if (apAC.getListFileDocument().size() > 0) {
						apAC.getListFileDocument().get(0).setType("Informe Final");
					}
					List<ActionProcess> laux = new ArrayList<>();
					laux.add(apAC);
					ProcessFlow pFTmp = new ProcessFlow();
					pFTmp.setState("Comunicado");
					pFTmp.setListActionsProcess(laux);
					pFTmp.setSubProcess(preProcess);
					pFTmp = this.service.create(pFTmp); // Guardo el nuevo proceso
					List<Notification> m = new ArrayList<>();
					if (pFTmp.getSubProcess() != null) {
						m = this.notiService.findByIdProcessAndState(pFTmp.getSubProcess().getIdProcess(), "Pendiente");
						if (m != null) {
							for (Notification n1 : m) {
								if (n1.getUserImptek().equalsIgnoreCase("svillacis"))
									n1.setState("Revisado");
								if (n1.getUserImptek().equalsIgnoreCase("vpillajo"))
									n1.setState("Revisado");
								if (n1.getUserImptek().equalsIgnoreCase("nesteves"))
									n1.setState("Revisado");
								if (n1.getUserImptek().equalsIgnoreCase("jnacimba"))
									n1.setState("Revisado");
								if (n1.getUserImptek().equalsIgnoreCase("wloachamin"))
									n1.setState("Revisado");
								this.notiService.update(n1);
							}
						} else {
							logger.error("> Prueba en proceso- sin Sub proceso");
						}
					}

					tray.setProcess(pFTmp);
					tray.setActionProcess(apAC);
					for (Notification n1 : m) {
						if (n1.getUserImptek().equalsIgnoreCase("svillacis")
								|| n1.getUserImptek().equalsIgnoreCase("vpillajo")
								|| n1.getUserImptek().equalsIgnoreCase("nesteves")) {
							Notification notiTmp2 = new Notification();
							notiTmp2.setTitle("Proceso de comunicación");
							notiTmp2.setIdProcess(pFTmp.getIdProcess());
							notiTmp2.setUserImptek(n1.getUserImptek());
							notiTmp2.setIdActionProcess(pFTmp.getListActionsProcess().get(0).getIdactionProcess());
							this.notiService.create(notiTmp2);
						}

					}
					tray.setActionProcess(apAC);
					tray.setProcess(pFTmp);
					json = JSON_MAPPER.writeValueAsString(tray);

					// notificationManagment(pFTmp, tray.getStateReply(), tray.getUserReplay()); //
					// Seteo de Notificaciones antiguas y creacion

					break;
				default:
					break;
				}

				wrei.setMessage(WebResponseMessage.RESPOND_SOLICITUD_UPDATE_OK);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + ProcessFlow.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * TX NAME: Solicitar materia prima para prueba en proceso
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQROrderMP(WebRequestIsa wri) {
		logger.info("> TX: TxQQROrderMP");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_OrderMP);
		wrei.setTransactionCode(TX_CODE_OrderMP);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + TrayDto.class);
				TrayDto pF = JSON_MAPPER.readValue(jsonValue, TrayDto.class);

				ActionProcess apT = pF.getActionProcessReply();
				apT.setIdactionProcess(this.apService.generateId());
				apT.setDateAP(LocalDateTime.now());
				apT.setComment("Materia Prima solicitada");
				apT.setUserImptek(pF.getUserReplay());
				apT.setState("MP Solicitada");
				List<FileDocument> lfd = pF.getProcess().getListActionsProcess()
						.get(pF.getProcess().getListActionsProcess().size() - 1).getListFileDocument();
				List<FileDocument> lfdSave = new ArrayList<>();
				for (FileDocument f : lfd) {
					FileDocument fd = new FileDocument();
					fd.setName(f.getName());
					fd.setUrl(f.getUrl());
					fd.setExtension(f.getExtension());
					fd.setType(f.getType());
					lfdSave.add(fd);
				}

				apT.setListFileDocument(lfdSave);

				List<ActionProcess> lAP = pF.getProcess().getListActionsProcess();
				lAP.add(apT);
				pF.getProcess().setListActionsProcess(lAP);
				logger.info("> objeto a guardar: " + pF.toString());
				ProcessFlow pp = this.service.update(pF.getProcess());

				this.notificationManagment(pp, apT.getState(), pF.getUserReplay());

				pF.setActionProcess(apT);
				pF.setProcess(pp);

				String json = JSON_MAPPER.writeValueAsString(pF);

				wrei.setMessage(WebResponseMessage.SUCCESS_SMP_ORDER);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + ProcessFlow.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

	}

	/**
	 * TX NAME: TxQQRAvailableMP(Materia prima esta disponible para prueba en
	 * proceso : Producción)
	 * 
	 * @param wri
	 * @return
	 */
	public ResponseEntity<Object> TxQQRAvailableMP(WebRequestIsa wri) {
		logger.info("> TX: TxQQRAvailableMP");

		WebResponseIsa wrei = new WebResponseIsa();
		wrei.setTransactionName(TX_NAME_AvailableMP);
		wrei.setTransactionCode(TX_CODE_AvailableMP);

		if (wri.getParameters().isEmpty() || wri.getParameters() == null) {
			logger.info("> Objeto vacío");
			wrei.setMessage(WebResponseMessage.WITHOUT_PARAMS_TO_CREATE_UPDATE);
			wrei.setStatus(WebResponseMessage.STATUS_INFO);
			return new ResponseEntity<Object>(wrei, HttpStatus.NOT_ACCEPTABLE);
		} else {

			String jsonValue = wri.getParameters();

			try {
				logger.info("> mapeando json a la clase: " + TrayDto.class);
				TrayDto pF = JSON_MAPPER.readValue(jsonValue, TrayDto.class);

				ActionProcess apT = new ActionProcess();
				apT.setIdactionProcess(this.apService.generateId());
				apT.setDateAP(LocalDateTime.now());
				apT.setComment("Materia prima solicitada, ya disponible para prueba en proceso");
				apT.setUserImptek(pF.getUserReplay());
				apT.setState("MP Disponible");
				ActionProcess apAux = null;
				for (ActionProcess ap : pF.getProcess().getListActionsProcess()) {
					if (ap.getState().equalsIgnoreCase("APROBADO")) {
						apAux = ap;
					}
				}
				apT.setListFileDocument(apAux.getListFileDocument());
				List<FileDocument> lfdSave = new ArrayList<>();
				for (FileDocument f : apAux.getListFileDocument()) {
					FileDocument fd = new FileDocument();
					fd.setName(f.getName());
					fd.setUrl(f.getUrl());
					fd.setExtension(f.getExtension());
					fd.setType(f.getType());
					lfdSave.add(fd);
				}
				apT.setListFileDocument(lfdSave);

				List<ActionProcess> lAP = pF.getProcess().getListActionsProcess();
				lAP.add(apT);
				pF.getProcess().setListActionsProcess(lAP);
				logger.info("> objeto a guardar: " + pF.toString());
				ProcessFlow pp = this.service.update(pF.getProcess());

				this.notificationManagment(pp, apT.getState(), pF.getUserReplay());

				pF.setActionProcess(apT);
				pF.setProcess(pp);

				String json = JSON_MAPPER.writeValueAsString(pF);

				wrei.setMessage(WebResponseMessage.SUCCESS_SMP_DISPONIBLE);
				wrei.setParameters(json);
				wrei.setStatus(WebResponseMessage.STATUS_OK);
				return new ResponseEntity<Object>(wrei, HttpStatus.OK);

			} catch (IOException e) {
				logger.error("> No se ha podido serializar el JSON a la clase: " + ProcessFlow.class);
				e.printStackTrace();
				wrei.setStatus(WebResponseMessage.STATUS_ERROR);
				wrei.setMessage(WebResponseMessage.ERROR_TO_JSON);
				return new ResponseEntity<Object>(wri, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		}
	}

	/**
	 * ================================== F U N C I O N E S U I T I L I T A R I A
	 * S============================
	 * 
	 * Método: Mueve los archivos al repositorio
	 * 
	 */
	private void tratamientFiles(List<FileDocument> lFD) {
		if (!lFD.isEmpty()) {
			for (FileDocument fd : lFD) {
				String uri = PassFileToRepository.base64ToFile(fd.getBase64File(), fd.getName(), fd.getExtension());
				fd.setUrl(uri);
			}
		}
	}

	/*
	 * Método para Administrar las notificaciones que se enviaran a los usuairos
	 * 
	 */
	private void notificationManagment(ProcessFlow process, String stateReply, String userReply) {

		/*
		 * Modifico le estado de la notificacion a Revisado
		 * 
		 */
		String identiti = "";

		List<Notification> lNoti = this.notiService.findByIdProcess(process.getIdProcess());
		for (Notification nt : lNoti) {
			nt.setState("Revisado");
			this.notiService.update(nt);
		}

		if (process.getProcessTestRequest() != null) {
			if (process.getProcessTestRequest().getLineBelong() != null) {
				identiti = process.getProcessTestRequest().getLineBelong().substring(0, 1);
			}
		}

		/*
		 * Se extrae lista sin elementos duplicados
		 */
		HashMap<String, ActionProcess> map = new HashMap<String, ActionProcess>();
		List<ActionProcess> listAP = new ArrayList<>();
		for (ActionProcess apI : process.getListActionsProcess()) {
			map.put(apI.getUserImptek(), apI);
		}

		Set<Entry<String, ActionProcess>> set = map.entrySet();
		for (Entry<String, ActionProcess> entry : set) {
			listAP.add(entry.getValue());
		}
		
		ConfiguracionUsuarioRolEnsayo configAprobarSolicitud = configuracionService.buscarConfiguracionAprobarInforme();
		if(configAprobarSolicitud == null)
			throw new SolicitudEnsayoErrorException(String.format("Configuración %s no encontrada", OrdenFlujo.APROBAR_INFORME));
		

		switch (stateReply.toUpperCase()) {
		case "VALIDAR ENTREGA MATERIAL":

			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Validación de entrega de muestras");
				nt.setTitle("Validación");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				this.notiService.create(nt);
			}
			break;

		case "APROBADO":
			ActionProcess firstAP = process.getListActionsProcess().get(0);
			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("El informe ha sido aprobado");
				nt.setTitle("Informe aprobado");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				if (firstAP.getIdactionProcess().equals(a.getIdactionProcess())) {
					nt.setTitle("DDP " + process.getTestRequest().getMaterialDetail() + " "
							+ process.getTestRequest().getProviderName());
					this.txNoti.sendNotification(nt);
				} else {
					this.notiService.create(nt);
				}
			}

			break;
		case "NO APROBADO":

			ActionProcess lastAP = process.getListActionsProcess().get(process.getListActionsProcess().size() - 2);
			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("El informe no ha sido aprobado");
				nt.setTitle("Informe no aprobado");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				if (lastAP.getIdactionProcess().equals(a.getIdactionProcess())) {
					if (process.getTestRequest() != null) {
						nt.setTitle("DDP " + process.getTestRequest().getMaterialDetail() + " "
								+ process.getTestRequest().getProviderName());
					}
					if (process.getProcessTestRequest() != null) {
						if (identiti.equalsIgnoreCase(""))
							nt.setTitle("DDP-04 " + process.getProcessTestRequest().getProductGetting());
						else
							nt.setTitle(
									"DDP-04 (" + identiti + ") " + process.getProcessTestRequest().getProductGetting());
					}
					this.txNoti.sendNotification(nt);
				} else {
					this.notiService.create(nt);
				}
			}

			break;
		case "POR APROBAR":

			// Creo las nuevas Notficaciones para todo las personas involucradas en el
			// proceso
			boolean userRepet = false;

			for (ActionProcess a : listAP) {
				if (a.getUserImptek().equalsIgnoreCase(configAprobarSolicitud.getUsuarioId())) {
					userRepet = true;
				}
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Se ha emitido el informe final para su aprobación");
				nt.setTitle("Informe Enviado para aprobación");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				this.notiService.create(nt);
			}
			if (!userRepet) {
				Notification nt = new Notification();
				nt.setIdProcess(process.getIdProcess());
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setUserImptek(configAprobarSolicitud.getUsuarioId());
				nt.setTitle("DDP: " + process.getTestRequest().getMaterialDetail() + " "
						+ process.getTestRequest().getProviderName());
				this.txNoti.sendNotification(nt);
			}

			break;

		case "MP SOLICITADA":
			String userBeginProcess = process.getTestRequest().getAsUser();
			for (ActionProcess a : listAP) {

				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Solicitud de materia prima para prueba en proceso");
				nt.setTitle("Solicitud de MP");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				if (a.getUserImptek().equalsIgnoreCase(userBeginProcess)) {
					nt.setTitle("DDP: " + process.getTestRequest().getMaterialDetail() + " "
							+ process.getTestRequest().getProviderName());
					this.txNoti.sendNotification(nt);
				} else {
					this.notiService.create(nt);
				}
			}

			break;

		case "MP DISPONIBLE":
			String userOrderMP = null;
			for (ActionProcess x : process.getListActionsProcess()) {
				if (x.getState().equalsIgnoreCase("MP SOLICITADA")) {
					userOrderMP = x.getUserImptek();
				}
			}

			for (ActionProcess a : listAP) {

				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Materia prima disponible para prueba en proceso");
				nt.setTitle("Solicitud de MP");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				if (a.getUserImptek().equalsIgnoreCase(userOrderMP)) {
					nt.setTitle("DDP: " + process.getTestRequest().getMaterialDetail() + " "
							+ process.getTestRequest().getProviderName());
					this.txNoti.sendNotification(nt);
				} else {
					this.notiService.create(nt);
				}
			}

			break;

		case "APROBADO INICIAR DDP-04":

			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Solicitud de DDP-04 Iniciado");
				if (identiti.equalsIgnoreCase(""))
					nt.setTitle("DDP-04");
				else
					nt.setTitle("DDP-04 (" + identiti + ")");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				this.notiService.create(nt);
			}

			Notification ntDDP = new Notification();
			ntDDP.setIdProcess(process.getIdProcess());
			ntDDP.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
					.getIdactionProcess());
			ntDDP.setUserImptek("jnorona");

			if (identiti.equalsIgnoreCase(""))
				ntDDP.setTitle("DDP-04: " + process.getProcessTestRequest().getProductGetting());
			else
				ntDDP.setTitle("DDP-04 (" + identiti + "): " + process.getProcessTestRequest().getProductGetting());
			this.txNoti.sendNotification(ntDDP);

			break;
		case "POR APROBAR DDP04":
			ActionProcess lastAPDDP04 = process.getListActionsProcess().get(process.getListActionsProcess().size() - 2);
			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				nt.setMessage("Se ha realizado la corrección del DDP04");
				nt.setTitle("Informe Enviado para aprobación");
				nt.setState("Pendiente");
				nt.setUserImptek(a.getUserImptek());
				if (lastAPDDP04.getIdactionProcess().equals(a.getIdactionProcess())) {
					if (identiti.equalsIgnoreCase(""))
						nt.setTitle("DDP-04: " + process.getProcessTestRequest().getProductGetting());
					else
						nt.setTitle(
								"DDP-04 (" + identiti + "): " + process.getProcessTestRequest().getProductGetting());
					this.txNoti.sendNotification(nt);
				} else {
					this.notiService.create(nt);
				}
			}
			break;
		case "INFORME-DDP05-ENVIADO":

			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				if (identiti.equalsIgnoreCase(""))
					nt.setTitle("DDP-04: " + process.getProcessTestRequest().getProductGetting());
				else
					nt.setTitle("DDP-04 (" + identiti + "): " + process.getProcessTestRequest().getProductGetting());
				nt.setUserImptek(a.getUserImptek());
				if (userReply.equalsIgnoreCase(a.getUserImptek()))
					this.notiService.create(nt);
				else
					this.txNoti.sendNotification(nt);
			}
			break;

		case "POR APROBAR Y COMUNICAR":
			for (ActionProcess a : listAP) {
				Notification nt = new Notification();
				nt.setIdActionProcess(process.getListActionsProcess().get(process.getListActionsProcess().size() - 1)
						.getIdactionProcess());
				nt.setIdProcess(process.getIdProcess());
				nt.setDate(LocalDateTime.now());
				if (identiti.equalsIgnoreCase(""))
					nt.setTitle("DDP-04: " + process.getProcessTestRequest().getProductGetting());
				else
					nt.setTitle("DDP-04 (" + identiti + "): " + process.getProcessTestRequest().getProductGetting());
				nt.setUserImptek(a.getUserImptek());
				if (a.getUserImptek().equalsIgnoreCase(configAprobarSolicitud.getUsuarioId()))
					this.txNoti.sendNotification(nt);
				else
					this.notiService.create(nt);

			}
			break;

		default:
			break;
		}
	}

}
