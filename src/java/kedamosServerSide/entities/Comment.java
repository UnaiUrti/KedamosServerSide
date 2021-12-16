/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad de comentarios
 *
 * @author Irkus de la Fuente
 */
@Entity
@Table(name = "comment", schema = "kedamosdb")
@XmlRootElement
public class Comment implements Serializable {
//Atribuos

    private static final long serialVersionUID = 1L;
    /**
     * Clave primaria
     */
    @EmbeddedId
    private Comment_id comment_id;
    /**
     * Campo relacional con user
     */
    //@MapsId("client_id")
    @MapsId("userId")
    @ManyToOne
    private Client client;
    /**
     * Campo relacional de eventos
     */
    /**
     * Contenido del comentario
     */
    @MapsId("eventId")
    @ManyToOne
    private Event event;
    
    private String description;
    /**
     * Fecha en la que se realizo el comentario
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date date_comment;
    /**
     * Valoracion del evento de uno a cinco
     */
    @Enumerated(EnumType.ORDINAL)
    private Mark mark;

    //Setters y Getters
    /**
     * Get id del comentario embeeded
     *
     * @return Comment_id
     */
    public Comment_id getComment_id() {
        return comment_id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(Date date_comment) {
        this.date_comment = date_comment;
    }

    public Mark getMark() {
        return mark;
    }

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.comment_id);
        hash = 79 * hash + Objects.hashCode(this.client);
        hash = 79 * hash + Objects.hashCode(this.event);
        hash = 79 * hash + Objects.hashCode(this.description);
        hash = 79 * hash + Objects.hashCode(this.date_comment);
        hash = 79 * hash + Objects.hashCode(this.mark);
        return hash;
    }

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
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.comment_id, other.comment_id)) {
            return false;
        }
        if (!Objects.equals(this.client, other.client)) {
            return false;
        }
        if (!Objects.equals(this.event, other.event)) {
            return false;
        }
        if (!Objects.equals(this.date_comment, other.date_comment)) {
            return false;
        }
        if (this.mark != other.mark) {
            return false;
        }
        return true;
    }

}
