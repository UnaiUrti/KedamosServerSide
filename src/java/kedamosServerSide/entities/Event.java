/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que contiene todos los datos relacionados con los Eventos
 *
 * @author Adrian Franco, Irkus de la Fuente
 */
@NamedQueries({
    @NamedQuery(
            name = "searchEventByDate", query = "SELECT e FROM Event e WHERE e.date=:date"
    )
    ,
    @NamedQuery(
            name = "searchEventByPrice", query = "SELECT e FROM Event e WHERE e.price=:price ORDER BY e.date ASC"
    )
    ,
        @NamedQuery(
            name = "searchEventByPlace", query = "SELECT e FROM Event e WHERE e.place.place_id=:place_id ORDER BY e.date ASC"
    )
    ,
        @NamedQuery(
            name = "searchEventByCategory", query = "SELECT e FROM Event e WHERE e.category=:category"
    )
    ,
    @NamedQuery(name = "findByEvent", query = "SELECT c FROM Comment c WHERE c.event.event_id=:event"),
    @NamedQuery(name = "findById", query = "SELECT c FROM Comment c  WHERE c.event.event_id=:event and c.client.user_id=:client")

})
@Entity
@Table(name = "event", schema = "kedamosdb")
@XmlRootElement
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
    private Date date;

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
     * Coste de unirse a un evento en concreto
     */
    private Float price;
    
    /**
     * Enumeracion de todas las categorias que puede seleccionar el Cliente al
     * crear Eventos
     */
    @Enumerated(EnumType.STRING)
    private Category category;

    /**
     * Titulo que asigna el cliente al Evento
     */
    @NotNull
    private String title;

    /**
     * Lista de Clientes apuntados al Evento
     */
    @ManyToMany(mappedBy = "joinEvents", fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    private Set<Client> client;

    /**
     * Lista de comentarios de los usuarios sonbre el Evento
     */
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    private Set<Comment> comment;

    /**
     * Personal necesario para el evento
     */
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    private Set<PersonalResource> personalResource;
    /**
     *
     */
    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade=CascadeType.MERGE)
    private Set<Revise> eventRevisions;


    @XmlTransient
    public Set<Revise> getEventRevisions() {
        return eventRevisions;
    }

    public void setEventRevisions(Set<Revise> eventRevisions) {
        this.eventRevisions = eventRevisions;
    }
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @XmlTransient
    public Set<Client> getClient() {
        return client;
    }

    public void setClient(Set<Client> client) {
        this.client = client;
    }

    @XmlTransient
    public Set<Comment> getComment() {
        return comment;
    }

    public void setComment(Set<Comment> comment) {
        this.comment = comment;
    }
    
    //Depende de como lo queramos hacer
    @XmlTransient
    public Set<PersonalResource> getPersonalResource() {
        return personalResource;
    }

    public void setPersonalResource(Set<PersonalResource> personalResource) {
        this.personalResource = personalResource;
    }
    
    @XmlTransient
    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
    
    @XmlTransient
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

    public void setEvent_id(Long event_id) {
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
     * Compara la igualdad de dos objetos de evento. Este metodo considera que
     * una cuenta es igual a otra cuando sus ids son exactamente iguales
     *
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
        return "Event{" + "event_id=" + event_id + ", date=" + date + ", maxParticipants=" + maxParticipants + ", minParticipants=" + minParticipants + ", actualParticipants=" + actualParticipants + ", description=" + description + ", price=" + price + ", category=" + category + ", title=" + title + ", client=" + client + ", comment=" + comment + ", personalResource=" + personalResource + ", eventRevisions=" + eventRevisions + ", place=" + place + ", organizer=" + organizer + '}';
    }


}