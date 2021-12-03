/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

/**
 * Entidad que contiene todos los datos relacionados con los Eventos
 * @author Adrian Franco
 */
@Entity
@Table(name = "event", schema = "kedamosdb")
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    /**
     * Atributo clave primaria con la que se identifican los eventos
     */
    private Long event_id;
    
    /**
     * Atributo que rellena el cliente al crear un Evento para que los otros
     * usuarios vean la fecha de inicio
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp date;
    
    /**
     * Atributos para saber cuantos clientes hay apuntados al evento, el mínimo
     * para que se realize y el máximo para apuntarse
     */
    
    private Long maxParticipants, minParticipants, actualParticipants;
    
    /**
     * Breve descripcion sobre el evento que se va a crear
     */
    private String description;
    
    /**
     * Booleano para saber si el manager del evento ha dado el OK 
     * o ha borrado el Evento
     */
    private Boolean isRevised;

    /**
     * Enumeracion de todas las categorias que puede seleccionar el Cliente 
     * al crear Eventos
     */
    @Enumerated(EnumType.ORDINAL)
    private Category category;

    /**
     * Titulo que asigna el cliente al Evento
     */
    @NotNull
    private String title;
    
    /**
     * Lista de Clientes apuntados al Evento
     */
    @ManyToMany(mappedBy="joinEvents", cascade=ALL)
    private Set<Client> client;
    
    /**
     * Lista de comentarios de los usuarios sonbre el Evento
     */
    @OneToMany(mappedBy="event", cascade=ALL)
    private Set<Comment> comment;
    
    /**
     * Personal necesario para el evento
     */
    @OneToMany (mappedBy="event", cascade=ALL)
    private Set<PersonalResource> personalResource;
    
    /**
     * Lugar en el que se hace el evento
     */
    @ManyToOne
    private Place place;
    
    /**
     * Atributo que define al organizador de cada evento
     */
    @ManyToOne
    private Client organizer;

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public Long getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Long maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Long getMinParticipants() {
        return minParticipants;
    }

    public void setMinParticipants(Long minParticipants) {
        this.minParticipants = minParticipants;
    }

    public Long getActualParticipants() {
        return actualParticipants;
    }

    public void setActualParticipants(Long actualParticipants) {
        this.actualParticipants = actualParticipants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsRevised() {
        return isRevised;
    }

    public void setIsRevised(Boolean isRevised) {
        this.isRevised = isRevised;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Client> getClient() {
        return client;
    }

    public void setClient(Set<Client> client) {
        this.client = client;
    }


    public Set<Comment> getComment() {
        return comment;
    }

    public void setComment(Set<Comment> comment) {
        this.comment = comment;
    }

    public Set<PersonalResource> getPersonalResource() {
        return personalResource;
    }

    public void setPersonalResource(Set<PersonalResource> personalResource) {
        this.personalResource = personalResource;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Client getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Client organizer) {
        this.organizer = organizer;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getEvent_id() {
        return event_id;
    }

    public void setEvent_id(Long id) {
        this.event_id = event_id;
    }

    @Override
    /**
     * Representacion entera para la instancia de Evento
     */
    public int hashCode() {
        int hash = 0;
        hash += (event_id != null ? event_id.hashCode() : 0);
        return hash;
    }
    /**
     * Compara la igualdad de dos objetos de evento. Este metodo considera que una
     * cuenta es igual a otra cuando sus ids son exactamente iguales
     * @param object El otro objeto evento con el que se compara
     * @return True si las comparaciones son iguales
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.event_id == null && other.event_id != null) || (this.event_id != null && !this.event_id.equals(other.event_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Event{" + "event_id=" + event_id + ", date=" + date + ", maxParticipants=" + maxParticipants + ", minParticipants=" + minParticipants + ", actualParticipants=" + actualParticipants + ", description=" + description + ", isRevised=" + isRevised + ", category=" + category + ", title=" + title + ", client=" + client + ", comment=" + comment + ", personalResource=" + personalResource + ", place=" + place + ", organizer=" + organizer + '}';
    }

 

}
