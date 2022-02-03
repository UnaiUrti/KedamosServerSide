/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.security;

/**
 * Este clase representa un main para encriptar simetricamente las credenciales
 * de la cuenta kedamos.
 *
 * @author Steven Arce
 */
public class EncryptEmailCredentials {

    /**
     * Este metodo encripta las credenciales de la cuenta kedamos.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        // Esto es para encriptar las credenciales del correo electronico
        Crypt.encryptSimetric("kedamos2022@gmail.com", "abcd*1234");

    }

}
