/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entidad de comentarios
 *
 * @author Irkus de la Fuente
 */
@Entity
@Table(name = "comment", schema = "kedamosdb")
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
    @MapsId("user_id")
    @ManyToOne
    private User user;
    /**
     * Campo relacional de eventos
     */
    @MapsId("event_id")
    @ManyToOne
    private Event event;
    /**
     * Contenido del comentario
     */
    private String description;
    /**
     * Fecha en la que se realizo el comentario
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date_comment;
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

    /**
     * Set id del comentario embeeded
     *
     * @param comment_id
     */

    public void setComment_id(Comment_id comment_id) {
        this.comment_id = comment_id;
    }

    /**
     * Get de la descripcion
     *
     * @return String
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set de la descripcion
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get de la fecha del comentario
     *
     * @return Timestamp
     */
    public Timestamp getDate_comment() {
        return date_comment;
    }

    /**
     * Set de la fecha de comentario
     *
     * @param date_comment
     */
    public void setDate_comment(Timestamp date_comment) {
        this.date_comment = date_comment;
    }

    /**
     * Get de la valoracion
     *
     * @return Mark
     */
    public Mark getMark() {
        return mark;
    }

    /**
     * Set de la valoracion
     *
     * @param mark
     */
    public void setMark(Mark mark) {
        this.mark = mark;
    }

    /**
     * Get del usuario campo relacional
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Set del usuario campo relacional
     *
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Get del evento campo relacional
     *
     * @return Event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * Set del evento campo relacional
     *
     * @param event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * ToString
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Comment{" + "comment_id=" + comment_id + ", description=" + description + ", date_comment=" + date_comment + ", mark=" + mark + ", user=" + user + ", event=" + event + '}';
    }

    /**
     * Hashcode
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.comment_id);
        hash = 97 * hash + Objects.hashCode(this.description);
        hash = 97 * hash + Objects.hashCode(this.date_comment);
        hash = 97 * hash + Objects.hashCode(this.mark);
        hash = 97 * hash + Objects.hashCode(this.user);
        hash = 97 * hash + Objects.hashCode(this.event);
        return hash;
    }

    /**
     * Equals
     *
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
        final Comment other = (Comment) obj;
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.comment_id, other.comment_id)) {
            return false;
        }
        if (!Objects.equals(this.date_comment, other.date_comment)) {
            return false;
        }
        if (this.mark != other.mark) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        return Objects.equals(this.event, other.event);
    }

}
