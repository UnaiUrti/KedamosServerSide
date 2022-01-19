/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import static kedamosServerSide.security.KeyGenerator.fileReader;

/**
 *
 * @author Steven Arce
 */
public class Crypt {

    /**
     *
     * @param passwd
     * @return
     */
    public byte[] encrypt(String passwd) {

        byte[] encodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.ENCRYPT_MODE, readPublicKey());
            //
            encodedMessage = cipher.doFinal(passwd.getBytes());
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedMessage;

    }

    public byte[] decrypt(byte[] passwd) {

        byte[] encodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.DECRYPT_MODE, readPrivateKey());
            //
            encodedMessage = cipher.doFinal(passwd);
        } catch (IllegalBlockSizeException | BadPaddingException | InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedMessage;
    }

    public String hash(String passwd) {

        byte[] hash = null;
        
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(passwd.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return bytesToHex(hash);
        
    }
   
    public String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public String generatePassword() {
        
        int length = 16;
        String symbol = ".*_"; 
        String cap_letter = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; 
        String small_letter = "abcdefghijklmnopqrstuvwxyz"; 
        String numbers = "0123456789"; 
        
        String finalString = cap_letter + small_letter + numbers + symbol; 

        Random random = new Random(); 

        char[] password = new char[length]; 

        for (int i = 0; i < length; i++) { 
            password[i] = finalString.charAt(random.nextInt(finalString.length())); 
        } 
        
        return String.valueOf(password);
        
    }
    
    public PublicKey readPublicKey() {
        PublicKey pubKey = null;
        try {
            // Obtener los bytes del archivo donde este guardado la llave publica
            byte[] pubKeyBytes = fileReader("java/kedamosServerSide/security/Public.key");
            //
            X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
            //
            pubKey = KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pubKey;
    }

    private PrivateKey readPrivateKey() {
        PrivateKey priKey = null;
        try {
            // Obtener los bytes del archivo donde este guardado la llave privada
            byte[] priKeyBytes = fileReader("java/kedamosServerSide/security/Private.key");
            //
            PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
            //
            priKey = KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return priKey;
    }

}
