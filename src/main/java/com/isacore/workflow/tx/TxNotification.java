package com.isacore.workflow.tx;

import com.isacore.mail.Mail;
import com.isacore.quality.model.Notification;
import com.isacore.quality.service.INotificationService;
import com.isacore.security.model.Usuario;
import com.isacore.security.repository.UsuarioRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TxNotification {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private INotificationService serviceNoti;

    @Autowired
    private Mail email;

    @Autowired
    private UsuarioRepositorio serviceUser;

    private static String linkAplication = "http://192.168.4.15:81/avalon-react/#";

    public boolean sendNotification(Notification noti) {
        logger.info("> TX: SendNotification");

        Usuario userFound = this.serviceUser.findByNombreUsuario(noti.getUserImptek()).orElse(null);

        if (userFound != null) {
            if (noti.getTitle() == null || noti.getTitle() == "")
                noti.setTitle("Notificación solicitud de Ensayos");
            noti.setMessage("Estimado " + userFound.getNombre() + "\n\n"
                    + "Favor revisa tu bandeja, tiene una notificación pendiente..!!" + "\n\n"
                    + "El siguiente link te ayudará a ingresar al sistema: " + " " + this.linkAplication);

            Notification nt = this.serviceNoti.create(noti);
            if (nt != null) {
                email.sendEmailWithoutAttachment(userFound.getEmail(), noti.getTitle(),
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

        Usuario userFound = this.serviceUser.findByNombreUsuario(n.getUserImptek()).orElse(null);
        if (userFound != null) {
            n.setTitle("Notificación solicitud de Ensayos");
            n.setMessage("Estimado " + userFound.getNombre() + "\n\n"
                    + "Favor revisa tu bandeja, tiene una notificación pendiente..!!" + "\n\n"
                    + "El siguiente link te ayudará a ingresar al sistema: " + " " + this.linkAplication);

            Notification nt = this.serviceNoti.create(n);
        }
    }
}
