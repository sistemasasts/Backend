package com.isacore.notificacion.servicio;


import com.isacore.notificacion.dominio.Mensaje;
import com.isacore.notificacion.dominio.MensajeFormato;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
public class ProveedorCorreoElectronicoOffice365 {

    private static final Log LOG = LogFactory.getLog(ProveedorCorreoElectronicoOffice365.class);

    private JavaMailSender emailSender;
    private Environment env;

    @Autowired
    public ProveedorCorreoElectronicoOffice365(JavaMailSender emailSender, Environment env) {
        this.emailSender = emailSender;
        this.env = env;
    }

    private final String[] direccionesComoArreglo(Set<String> direcciones) {
        return direcciones.toArray(new String[]{});
    }


    public String enviar(Mensaje mensaje) throws ProveedorCorreoElectronicoException {

        try {

//            log(LOG, mensaje, "Enviando mensaje...");
            MimeMessage message = this.emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setTo(direccionesComoArreglo(mensaje.getDireccionesA()));

            if (mensaje.isTieneDireccionesCC()) {
                helper.setCc(direccionesComoArreglo(mensaje.getDireccionesCC()));
            }

            helper.setSubject(mensaje.getAsunto());

            helper.setText(mensaje.getCuerpo(), mensaje.getFormato().equals(MensajeFormato.HTML));

            this.emailSender.send(message);

//            log(LOG, mensaje, "Mensaje enviado");

            return ""; // Hay que obtener un id mensaje

        } catch (MessagingException e) {
//            log(LOG, mensaje, "Mensaje no enviado");
//            return "Error";
            throw new ProveedorCorreoElectronicoException(e);
        }
    }
}
