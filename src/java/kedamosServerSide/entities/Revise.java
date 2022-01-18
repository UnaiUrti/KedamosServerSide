/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad revisar que se crea con la relacion de EventManager y Event
 * @author UnaiUrtiaga
 */
@NamedQueries({
    @NamedQuery(
            name="getRevisionByMail", query="SELECT r FROM Revise r WHERE r.event.event_id=:event_id AND r.eventManager.email=:email"
    )
    ,/*           EN PROCESO PERO NO OBLIGATORIO
    @NamedQuery(
            name="getRevisionByUsername", query="SELECT r FROM Revise r WHERE r.event.event_id=:event_id AND r.eventManager.username=:username"
    )*/
    @NamedQuery(
            name="getEveryEventRevisions", query="SELECT r FROM Revise r WHERE r.event.event_id=:event_id"
    )
    ,
    @NamedQuery(
            name="getEveryUserRevisions", query="SELECT r FROM Revise r WHERE r.eventManager.email=:email"
    )    
})
@Entity
@Table (name="revise", schema="kedamosdb")
@XmlRootElement
public class Revise implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * Grupo de IDs cogidas de las entidades EventManager(User) y Event
     */
    @EmbeddedId
    private ReviseId revise_id;
    
    /**
     * Atributo booleano que indica si el evento ha sido aceptado o no
     */
    @NotNull
    private Boolean isAccepted;
    
    /**
     * Atributo de texto que guarda un mensaje enviado por el event_manager
     */
    private String message;
    
    /**
     * Atributo tipo fecha que guarda cuando ha sido revisado el evento
     */
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date revisionDate;
    
    /**
     * Atributo primario que guarda el manager que ha revisado el evento
     */
    @MapsId("user_id")
    @ManyToOne
    private EventManager eventManager;
    
    /**
     * Atributo primario que guarda el evento que ha sido revisado
     */
    @MapsId("event_id")
    @ManyToOne
    private Event event;

    public Boolean getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(Boolean isAccepted) {
        this.isAccepted = isAccepted;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    
    /**
     * Get de la fecha de revision
     * @return Devuelve la fecha de la revision
     */
    public Date getRevisionDate() {
        return revisionDate;
    }

    /**
     * Set de la fecha de revision
     * @param revisionDate 
     */
    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public ReviseId getRevise_id() {
        return revise_id;
    }

    public void setRevise_id(ReviseId revise_id) {
        this.revise_id = revise_id;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * Get del manager que ha revisado el evento
     * @return Devuelve el manager
     */
    public void setEventManager(EventManager eventManager) {    
        this.eventManager = eventManager;
    }

    /**
     * Get del evento que ha sido revisado
     * @return Devuelve el evento
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Set del evento que ha sido revisado
     * @param event 
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * HashCode de todos los atributos
     * @return Devuelve un HashCode
     */
    @Override    
    public int hashCode() {
        int hash = 0;
        hash += (revise_id != null ? revise_id.hashCode() : 0);
        return hash;
    }

    /**
     * Equals que comprueba el valor de dos revisiones
     * @param obj
     * @return Devuelve un booleano
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Revise)){
            return false;
        }
        Revise other = (Revise) object;
        if ((this.revise_id == null && other.revise_id != null) || (this.revise_id != null && !this.revise_id.equals(other.revise_id))){
            return false;
        }
        return true;
    }

    /**
     * ToString para crear un string con todos los datos de la revision
     * @return Devuelve los datos de la revision
     */
    @Override
    public String toString() {
        return "Revise{" + "revise_id=" + revise_id + ", isAccepted=" + isAccepted + ", message=" + message + ", reviseDate=" + revisionDate + ", reviser=" + eventManager + ", event=" + event + '}';
    }

    
    
}
