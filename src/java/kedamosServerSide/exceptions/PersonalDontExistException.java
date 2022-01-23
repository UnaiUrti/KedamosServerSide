/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.exceptions;

/**
 *
 * @author Irkus de la Fuente
 */
public class PersonalDontExistException extends Exception {
    public PersonalDontExistException(String msg){
        super(msg);
    }
}
