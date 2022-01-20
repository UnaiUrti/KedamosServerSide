/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
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
import java.util.Base64;
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

/**
 *
 * @author Steven Arce
 */
public class Crypt {

    private static byte[] salt = "esta es la salt!".getBytes();

    public static byte[] encryptSimetric(String passwd) {

        byte[] encodedMessage = null;
        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        ResourceBundle clave = ResourceBundle.getBundle("kedamosServerSide.security.SimetricKey");
        try {

            keySpec = new PBEKeySpec(clave.getString("key").toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encodedMessage = cipher.doFinal(passwd.getBytes());

            byte[] iv = cipher.getIV();
            byte[] combined = concatArrays(iv, encodedMessage);

            fileWriter("java/kedamosServerSide/security/EmailSimetricPasswd.dat", combined);
            ret = new String(encodedMessage);

        } catch (InvalidKeyException | NoSuchAlgorithmException | 
                InvalidKeySpecException | BadPaddingException | 
                IllegalBlockSizeException | NoSuchPaddingException e) {
        }

        return encodedMessage;

    }

    public static String decryptSimetric() {

        byte[] fileContent = fileReader("java/kedamosServerSide/security/EmailSimetricPasswd.dat");
        byte[] decodedMessage = null;
        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        ResourceBundle clave = ResourceBundle.getBundle("KedamosServerSide.security.SimetricKey");
        try {

            keySpec = new PBEKeySpec(clave.getString("key").toCharArray(), salt, 65536, 128);
            secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] key = secretKeyFactory.generateSecret(keySpec).getEncoded();
            SecretKey privateKey = new SecretKeySpec(key, 0, key.length, "AES");
            
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec ivParam = new IvParameterSpec(Arrays.copyOfRange(fileContent, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, privateKey, ivParam);
            decodedMessage = cipher.doFinal(Arrays.copyOfRange(fileContent, 16, fileContent.length));
            ret = new String(decodedMessage);
            
        } catch (IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeySpecException | 
                InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }

    /**
     *
     * @param passwd
     * @return
     */
    public static byte[] encryptAsimetric(String passwd) {

        byte[] encodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.ENCRYPT_MODE, readPublicKey());
            //
            encodedMessage = cipher.doFinal(passwd.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException | 
                InvalidKeyException | NoSuchAlgorithmException | 
                NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedMessage;

    }

    public static byte[] decryptAsimetric(byte[] passwd) {

        byte[] encodedMessage = null;

        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            //
            cipher.init(Cipher.DECRYPT_MODE, readPrivateKey());
            //
            encodedMessage = cipher.doFinal(passwd);
        } catch (IllegalBlockSizeException | BadPaddingException | 
                InvalidKeyException | NoSuchAlgorithmException | 
                NoSuchPaddingException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return encodedMessage;
    }

    public static String hash(String passwd) {

        byte[] hash = null;

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            hash = md.digest(passwd.getBytes(StandardCharsets.UTF_8));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return bytesToHex(hash);

    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
            byte[] priKeyBytes = fileReader("java/kedamosServerSide/security/Private.key");
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

    /**
     * Escribe un fichero
     * 
     * @param path Path del fichero
     * @param text Texto a escibir
     */
    public static void fileWriter(String path, byte[] text) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
                fos.write(text);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /**
     * Retorna el contenido de un fichero
     * 
     * @param path Path del fichero
     * @return El texto del fichero
     */
    public static byte[] fileReader(String path) {
        byte ret[] = null;
        File file = new File(path);
        try {
            ret = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
}
