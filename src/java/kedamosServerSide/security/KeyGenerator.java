/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static kedamosServerSide.security.Crypt.byteArrayToHexString;

/**
 *
 * @author Steven Arce
 */
public class KeyGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        try {

            // La clase KeyPairGenerator se utiliza para generar PARES de claves públicas y privadas
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");

            /**
             * El tamaño mínimo para las claves RSA seguras en el conjunto de
             * datos de clave es de 1024 bits.
             *
             * Inicializa el generador de pares de claves para un determinado
             * tamaño de clave (..se utiliza una fuente de aleatoriedad
             * proporcionada por el sistema)
             */
            keyPairGenerator.initialize(1024);

            // Genera un par de claves
            KeyPair keyPair = keyPairGenerator.genKeyPair();

            // Obtener clave pública y clave privada
            byte[] pubKey = keyPair.getPublic().getEncoded();
            byte[] priKey = keyPair.getPrivate().getEncoded();
            
            // Se guarda la clave pública y la clave privada en el archivo especificado
            fileWriter("java/kedamosServerSide/security/Public.key", pubKey);
            
            OutputStream output = new FileOutputStream("java/kedamosServerSide/security/Private.properties");
            Properties prop = new Properties();
            prop.setProperty("privateKey", byteArrayToHexString(priKey));
            prop.store(output, null);
            
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(KeyGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
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