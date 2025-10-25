package com.pinguela.rentexpres.service.impl;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.pinguela.rentexpres.config.ConfigManager;
import com.pinguela.rentexpres.service.MailService;

public class MailServiceImpl implements MailService {

    private static final Logger logger = LogManager.getLogger(MailServiceImpl.class);

    @Override
    public boolean send(String destinatario, String asunto, String cuerpo) {
        String fromEmail = ConfigManager.getValue("mail.email");
        String password = ConfigManager.getValue("mail.password");
        String host = ConfigManager.getValue("mail.smtp.server.name");
        String portValue = ConfigManager.getValue("mail.smtp.server.port");

        if (isBlank(destinatario) || isBlank(fromEmail) || isBlank(password) || isBlank(host) || isBlank(portValue)) {
            logger.error("No se pudo enviar el correo: configuración o destinatario inválidos");
            return false;
        }

        final int port;
        try {
            port = Integer.parseInt(portValue.trim());
        } catch (NumberFormatException e) {
            logger.error("Puerto SMTP inválido: {}", portValue, e);
            return false;
        }

        try {
            Email email = new SimpleEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthenticator(new DefaultAuthenticator(fromEmail, password));
            email.setStartTLSEnabled(true);
            email.setFrom(fromEmail);
            email.setSubject(asunto);
            email.setMsg(cuerpo);
            email.addTo(destinatario);
            email.send();

            logger.info("Correo enviado a: " + destinatario + " | Asunto: " + asunto);
            return true;

        } catch (EmailException e) {
            logger.error("Error al enviar el correo a " + destinatario + ": " + e.getMessage(), e);
            return false;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}

