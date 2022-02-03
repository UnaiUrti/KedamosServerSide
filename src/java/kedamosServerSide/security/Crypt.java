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
 * Esta clase representa la encriptacion de las contraseñas y el hasheo.
 *
 * @author Steven Arce
 */
public class Crypt {

    private static byte[] salt = "esta es la salt!".getBytes();
    private static ResourceBundle rb = ResourceBundle.getBundle("kedamosServerSide.security.EmailCredentials");
    private static ResourceBundle rbp = ResourceBundle.getBundle("kedamosServerSide.security.Private");

    /**
     * Este metodo encripta simetricamente las credenciales del correo
     * electronico usado para el reseteo de contraseña.
     *
     * @param email Email de la cuenta kedamos
     * @param password Contraseña de la cuenta kedamos
     */
    public static void encryptSimetric(String email, String password) {

        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;
        try {
            OutputStream output = new FileOutputStream("java/kedamosServerSide/security/EmailCredentials.properties");

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

        } catch (InvalidKeyException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException
                | IllegalBlockSizeException | NoSuchPaddingException e) {
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este metodo desencripta simetricamente las credenciales de la cuenta
     * kedamos.
     *
     * @return Retorna las credenciales en un array de string.
     */
    public static String[] decryptSimetric() {

        String[] emailCredentials = null;
        byte[] emailContent = hexStringToByteArray(rb.getString("email"));
        byte[] passwordContent = hexStringToByteArray(rb.getString("password"));

        String ret = null;
        KeySpec keySpec = null;
        SecretKeyFactory secretKeyFactory = null;

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

            emailCredentials = new String[]{new String(decodedEmail), new String(decodedPassword)};

        } catch (IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException
                | NoSuchPaddingException | InvalidKeySpecException
                | InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Crypt.class.getName()).log(Level.SEVERE, null, ex);
        }

        return emailCredentials;
    }

    /**
     * Este metodo encripta asimetricamente la contraseña del lado cliente.
     *
     * @param passwd Contraseña para encriptar.
     * @return Retorna la contraseña encriptada en hexadecimal.
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

    /**
     * Este metodo desencripta asimentricamente las contraseña recibida por
     * parte del cliente.
     *
     * @param password Contraseña a desencriptar.
     * @return Retorna en texto plano la contraseña desencriptada.
     */
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

    /**
     * Este metodo pretende hashear el texto en claro de la contraseña
     * desencriptada.
     *
     * @param passwd Contraseña en texto plano.
     * @return Retorna la contraseña hasheada.
     */
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

    /**
     * Este metodo convierte un array de bytes a hexadecimal.
     *
     * @param bytes Array de bytes a convertir.
     * @return Retorna un string en hexadecimal.
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Este metodo pretende convertir un string de hexadecimal a un array de
     * bytes.
     *
     * @param s String a convertir 
     * @return Retorna un array de bytes.
     */
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Genera una contraseña de 16 caracteres.
     *
     * @return Retorna una nueva contraseña aleatoria.
     */
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

    /**
     * Este metodo pretende leer la llave publica.
     * 
     * @return Retorna la llave publica.
     */
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

    /**
     * Este metodo pretende leer la llave privada.
     * 
     * @return Retorna la llave privada.
     */
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

    /**
     * Este metodo concatena dos array de bytes.
     *
     * @param array1
     * @param array2
     * @return
     */
    private static byte[] concatArrays(byte[] array1, byte[] array2) {
        byte[] ret = new byte[array1.length + array2.length];
        System.arraycopy(array1, 0, ret, 0, array1.length);
        System.arraycopy(array2, 0, ret, array1.length, array2.length);
        return ret;
    }

}
