/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import javax.persistence.Embeddable;

/**
 *
 * @author UnaiUrtiaga
 */
@Embeddable
class ReviseId implements Serializable{
    
    private Long user_id;
    private Long event_id;
    
}
