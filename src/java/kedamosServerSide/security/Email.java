/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
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
 *
 * @author Unai Urtiaga
 */
public class Email {
    
    public static void sendEmail(String receiver, String newPasswd){
        
        try {
            String subject = "Password Changed";
            String text = "Your new password is: " + newPasswd;
            ResourceBundle configFile;
            
            //Propiedades del Mail
            configFile = ResourceBundle.getBundle("kedamosServerSide.security.EmailProperties");
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
                    return new PasswordAuthentication(configFile.getString("sender"), Crypt.decryptSimetric());
                }
            });
            
            //Mensaje a enviar
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(configFile.getString("sender")));
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
