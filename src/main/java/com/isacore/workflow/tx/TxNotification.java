package com.isacore.workflow.tx;

import org.apache.tomcat.jni.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.isacore.mail.Mail;
import com.isacore.quality.model.Notification;
import com.isacore.quality.service.INotificationService;
import com.isacore.sgc.acta.model.UserImptek;
import com.isacore.sgc.acta.service.IUserImptekService;

@Component
public class TxNotification {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private INotificationService serviceNoti;

	@Autowired
	private Mail email;

	@Autowired
	private IUserImptekService serviceUser;

	private static String linkAplication = "http://192.168.4.15:81/avalon-react/#";

	public boolean sendNotification(Notification noti) {
		logger.info("> TX: SendNotification");

		UserImptek ui = new UserImptek();
		ui.setIdUser(noti.getUserImptek());
		UserImptek userFound = this.serviceUser.findById(ui);

		if (userFound != null) {
			if (noti.getTitle() == null || noti.getTitle() == "")
				noti.setTitle("Notificación solicitud de Ensayos");
			noti.setMessage("Estimado " + userFound.getEmployee().getCompleteName() + "\n\n"
					+ "Favor revisa tu bandeja, tiene una notificación pendiente..!!" + "\n\n"
					+ "El siguiente link te ayudará a ingresar al sistema: " + " " + this.linkAplication);

			Notification nt = this.serviceNoti.create(noti);
			if (nt != null) {
				email.sendEmailWithoutAttachment(userFound.getEmployee().getEmail(), noti.getTitle(),
						noti.getMessage());
				return true;
			} else {
				return false;
			}

		} else {
			return false;
		}

	}

	public void saveNotificationWithothEmail(Notification n) {
		logger.info("> TX: SendNotification");

		UserImptek ui = new UserImptek();
		ui.setIdUser(n.getUserImptek());
		UserImptek userFound = this.serviceUser.findById(ui);
		if (userFound != null) {
			n.setTitle("Notificación solicitud de Ensayos");
			n.setMessage("Estimado " + userFound.getEmployee().getCompleteName() + "\n\n"
					+ "Favor revisa tu bandeja, tiene una notificación pendiente..!!" + "\n\n"
					+ "El siguiente link te ayudará a ingresar al sistema: " + " " + this.linkAplication);

			Notification nt = this.serviceNoti.create(n);
		}
	}
}
