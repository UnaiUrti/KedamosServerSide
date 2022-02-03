/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Este clase representa el envio de email para el cliente.
 *
 * @author Steven Arce, Unai Urtiaga
 */
public class Email {

    /**
     * Este metodo envia un email al cliente con la nueva contraseña generada.
     *
     * @param receiver Email del cliente.
     * @param newPasswd La nueva contraseña generada.
     */
    public static void sendEmailResetPassword(String receiver, String newPasswd) {

        try {
            String subject = "Password Changed";
            String text = "Your new password is: " + newPasswd;
            ResourceBundle configFile = ResourceBundle.getBundle("kedamosServerSide.security.EmailCredentials");

            String[] emailCredentials = Crypt.decryptSimetric();

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", configFile.getString("smtp_host"));
            props.put("mail.smtp.port", configFile.getString("smtp_port"));
            props.put("mail.smtp.ssl.trust", configFile.getString("smtp_host"));
            props.put("mail.smtp.partialfectch", false);

            //Autenticador para decirle al correo que cuenta usamos
            Session session = Session.getInstance(props, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailCredentials[0], emailCredentials[1]);
                }
            });

            //Mensaje a enviar
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailCredentials[0]));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);

            //Cuerpo del mensaje
            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html");
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Este metodo envia un email al cliente para avisa que se ha cambiado la
     * contraseña.
     *
     * @param receiver Email del cliente.
     */
    public static void sendEmailChangePassword(String receiver) {

        try {
            String subject = "Password Changed";
            String text = "Your password has been changed";
            ResourceBundle configFile = ResourceBundle.getBundle("kedamosServerSide.security.EmailCredentials");

            String[] emailCredentials = Crypt.decryptSimetric();

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", configFile.getString("smtp_host"));
            props.put("mail.smtp.port", configFile.getString("smtp_port"));
            props.put("mail.smtp.ssl.trust", configFile.getString("smtp_host"));
            props.put("mail.smtp.partialfectch", false);

            //Autenticador para decirle al correo que cuenta usamos
            Session session = Session.getInstance(props, new Authenticator() {

                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailCredentials[0], emailCredentials[1]);
                }
            });

            //Mensaje a enviar
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailCredentials[0]));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiver));
            message.setSubject(subject);

            //Cuerpo del mensaje
            Multipart multipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(text, "text/html");
            multipart.addBodyPart(mimeBodyPart);
            message.setContent(multipart);

            Transport.send(message);
        } catch (AddressException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Email.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
