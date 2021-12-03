/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

/**
 *
 * @author Irkus de la Fuente
 */
@Embeddable
public class Comment_id implements Serializable {
//Atributos
 /**
 * Atributos
 */
private Long client_id;
private Long event_id;
/**
 * Hashcode
 * @return int
 */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.client_id);
        hash = 13 * hash + Objects.hashCode(this.event_id);
        return hash;
    }
/**
 * Equals
 * @param obj
 * @return boolean
 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Comment_id other = (Comment_id) obj;
        if (!Objects.equals(this.client_id, other.client_id)) {
            return false;
        }
        return Objects.equals(this.event_id, other.event_id);
    }
/**
 * ToString
 * @return String
 */
    @Override
    public String toString() {
        return "Comment_id{" + "client_id=" + client_id + ", event_id=" + event_id + '}';
    }

    /**
     *Get cliet_id
     * @return Long
     */
    public Long getClient_id() {
        return client_id;
    }

    /**
     *Set client_id
     * @param client_id
     */
    public void setClient_id(Long client_id) {
        this.client_id = client_id;
    }

    /**
     *get event_id
     * @return Long
     */
    public Long getEvent_id() {
        return event_id;
    }

    /**
     *Set event_id
     * @param event_id
     */
    public void setEvent_id(Long event_id) {
        this.event_id = event_id;
    }

}
