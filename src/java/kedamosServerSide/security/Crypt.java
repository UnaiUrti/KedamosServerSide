/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import static kedamosServerSide.security.KeyGenerator.fileReader;

/**
 *
 * @author Steven Arce
 */
public class Crypt {

    private static byte[] salt = "esta es la salt!".getBytes();
    private static ResourceBundle rb = ResourceBundle.getBundle("kedamosServerSide.security.EmailCredentials");
    private static ResourceBundle rbp = ResourceBundle.getBundle("kedamosServerSide.security.Private");
    
    public static void encryptSimetric(String email, String password) {

        //String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            // Creamos el archivo de configuracion con los antiguos datos       
            OutputStream output = new FileOutputStream("java/kedamosServerSide/security/EmailCredentials.properties");

            // Creamos la clase properties y le pasamos el archivo de configuracion
            Properties prop = new Properties();

            keySpec = new PBEKeySpec(rb.getString("key").toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            
            byte[] encodedEmail = cipher.doFinal(email.getBytes());
            byte[] iv = cipher.getIV();
            byte[] emailCombined = concatArrays(iv, encodedEmail);
            
            byte[] encodedPassword = cipher.doFinal(password.getBytes());
            byte[] passwordCombined = concatArrays(iv, encodedPassword);
            
            /**
             * Insertamos en la clase properties los valores antiguos mas las
             * credenciales ecriptadas
             */
            prop.setProperty("smtp_host", rb.getString("smtp_host"));
            prop.setProperty("smtp_port", rb.getString("smtp_port"));
            prop.setProperty("key", rb.getString("key"));
            prop.setProperty("email", byteArrayToHexString(emailCombined));
            prop.setProperty("password", byteArrayToHexString(passwordCombined));
    
            // Guardamos los datos en el archivo de configuracion
            prop.store(output, null);

            //fileWriter("java/kedamosServerSide/security/EmailSimetricPasswd.dat", combined);
            //ret = new String(encodedMessage);
        } catch (InvalidKeyException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException e) {
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        //return encodedMessage;
    }

    public static String[] decryptSimetric() {

        //byte[] fileContent = fileReader("java/kedamosServerSide/security/EmailSimetricPasswd.dat");
        String[] emailCredentials = null;
        byte[] emailContent = hexStringToByteArray(rb.getString("email"));
        byte[] passwordContent = hexStringToByteArray(rb.getString("password"));
        //byte[] decodedMessage = null;
        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        //ResourceBundle clave = ResourceBundle.getBundle("KedamosServerSide.security.SimetricKey");
        try {

            keySpec = new PBEKeySpec(rb.getString("key").toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(passwordContent, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
            byte[] decodedEmail = cipher.doFinal(Arrays.copyOfRange(emailContent, 16, emailContent.length));
            byte[] decodedPassword = cipher.doFinal(Arrays.copyOfRange(passwordContent, 16, passwordContent.length));

            //et = new String(decodedPassword);

            emailCredentials = new String[]{new String(decodedEmail),new String(decodedPassword)};
            
        } catch (IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return emailCredentials;
    }

    /**
     *
     * @param passwd
     * @return
     */
    public static String encryptAsimetric(String passwd) {

        byte[] encodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.ENCRYPT_MODE, readPublicKey());
            //
            encodedMessage = cipher.doFinal(passwd.getBytes());
            
        } catch (IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return byteArrayToHexString(encodedMessage);

    }

    public static String decryptAsimetric(String password) {

        byte[] passwordContent = hexStringToByteArray(password);
        byte[] decodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.DECRYPT_MODE, readPrivateKey());
            //
            decodedMessage = cipher.doFinal(passwordContent);
        } catch (IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new String(decodedMessage);
    }

    public static String hash(String passwd) {

        byte[] hash = null;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(passwd.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return byteArrayToHexString(hash);

    }

    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    public static String generatePassword() {

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

    public static PublicKey readPublicKey() {
        PublicKey pubKey = null;
        try {
            // Obtener los bytes del archivo donde este guardado la llave publica
            byte[] pubKeyBytes = fileReader("java/kedamosServerSide/security/Public.key");
            //
            X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(pubKeyBytes);
            //
            pubKey = KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pubKey;
    }

    private static PrivateKey readPrivateKey() {
        PrivateKey priKey = null;
        try {
            // Obtener los bytes del archivo donde este guardado la llave privada
            byte[] priKeyBytes = hexStringToByteArray(rbp.getString("privateKey"));
            //
            PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(priKeyBytes);
            //
            priKey = KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
        return priKey;
    }

    private static byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] ret = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, ret, 0, array1.length);
        System.arraycopy(array2, 0, ret, array1.length, array2.length);
        return ret;
    }

}
