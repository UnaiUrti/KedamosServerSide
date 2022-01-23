/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

/**
 *
 * @author Freak
 */
public class EncryptEmailCredentials {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Esto es para encriptar las credenciales del correo electronico
        Crypt.encryptSimetric("kedamos2022@gmail.com","abcd*1234");

    }

}
