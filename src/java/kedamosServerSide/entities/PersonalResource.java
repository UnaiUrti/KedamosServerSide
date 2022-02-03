
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import static javax.persistence.CascadeType.MERGE;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *Esta es la entidad de personal Resource
 * @author Irkus de la Fuente
 */
@NamedQueries({
    //Buscar personal por tipo
@NamedQuery(
    name="findPersonalByType",query="SELECT a FROM PersonalResource a WHERE  a.type=:type"
     
),//Buscar personal por precio
    @NamedQuery(
   name="findPersonalByPrice",query="SELECT p FROM PersonalResource p WHERE p.price=:price"),
    //buscar por evento y tipo
    @NamedQuery(
    name="findPersonalByEventAndType",query="SELECT p FROM PersonalResource p WHERE p.type=:type AND p.event.event_id=:event"),
    @NamedQuery(
    //borrar personal por tipo
    name="deletePersonalByType",query="DELETE  FROM PersonalResource p WHERE p.type=:type"),
    @NamedQuery(
            //modificar la cantidad de un personal
    name="updateQuantityOfAPErsonal",query="SELECT  p FROM PersonalResource p WHERE p.personalresource_id=:personalresource_id"),
    @NamedQuery(
            //obtener todos los personales de un evento
    name="getPersonalByEvent",query="SELECT  p FROM PersonalResource p WHERE p.event.event_id=:event_id"),
})

/**
 *Entidad PersonalResource 
 * @author Irkus de la Fuente
 */
@Entity
@Table(name="personalresource",schema="kedamosdb")
@XmlRootElement
public class PersonalResource implements Serializable {
    //Atributos
    private static final long serialVersionUID = 1L;
    /**
     * Clave primaria de la entidad
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long personalresource_id;
    /**
     * Cantidad del personal requerida
     */
    private Long quantity;
    /**
     * Enumeracion que indica que tipo de personal es
     */

    @Enumerated(EnumType.STRING)
    private Type type;
    /**
     * Precio del personal
     */
    private Float price;
    /**
     * Fecha de fin de contrato
     */
    @Temporal(TemporalType.DATE)
    private Date dateExpired;
    /**
     * Fecha contratado
     */
    @Temporal(TemporalType.DATE)
    private Date dateHired;
    
    //Campo relacional
    /**
     * Este es el campo relacional el cual une los eventos mediante la id
     */
    @ManyToOne
    private Event event;
    
    //Getters y setters
    /**
     * Get de la id
     * @return personalresource_id
     */
    public Long getPersonalresource_id() {
        return personalresource_id;
    }
/**
 * Get de la cantidad
 * @return quantity
 */
    public Long getQuantity() {
        return quantity;
    }
/**
 * Set de la cantidad
 * @param quantity 
 */
    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
/**
 * Set del tipo
 * @return type
 */
    public Type getType() {
        return type;
    }
/**
 * Set del tipo
 * @param type 
 */
    public void setType(Type type) {
        this.type = type;
    }
/**
 * Get del precio
 * @return price
 */
    public Float getPrice() {
        return price;
    }
/**
 * Set del precio
 * @param price 
 */
    public void setPrice(Float price) {
        this.price = price;
    }
/**
 * Get de la fecha de fin de contrato
 * @return dateExpired
 */
    public Date getDateExpired() {
        return dateExpired;
    }
/**
 * Set de la fecha de fin de contrato
 * @param dateExpired 
 */
    public void setDateExpired(Date dateExpired) {
        this.dateExpired = dateExpired;
    }
/**
 * Get de fecha de contratacion
 * @return dateHired
 */
    public Date getDateHired() {
        return dateHired;
    }
/**
 * Set de fecha de contratacion
 * @param dateHired 
 */
    public void setDateHired(Date dateHired) {
        this.dateHired = dateHired;
    }
/**
 * Get del campo relacional
 * @return event
 */
     //@XmlTransient
    public Event getEvent() {
        return event;
    }
/**
 * Set del campo relacional
 * @param event 
 */
    public void setEvent(Event event) {
        this.event = event;
    }
/**
 * Set del id
     * @param personalresource_id 
 */
    public void setPersonalresource_id(Long personalresource_id) {
        this.personalresource_id = personalresource_id;
    }
    //HashCode
/**
 * Hashcode
 * @return hash
 */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.personalresource_id);
        hash = 37 * hash + Objects.hashCode(this.quantity);
        hash = 37 * hash + Objects.hashCode(this.type);
        hash = 37 * hash + Objects.hashCode(this.price);
        hash = 37 * hash + Objects.hashCode(this.dateExpired);
        hash = 37 * hash + Objects.hashCode(this.dateHired);
        hash = 37 * hash + Objects.hashCode(this.event);
        return hash;
    }

  //Equals
/**
 * Equals
 * @param object
 * @return boolean
 */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PersonalResource)) {
            return false;
        }
        PersonalResource other = (PersonalResource) object;
        if ((this.personalresource_id == null && other.personalresource_id != null) || (this.personalresource_id != null && !this.personalresource_id.equals(other.personalresource_id))) {
            return false;
        }
        return true;
    }
    //ToStirng
/**
 * ToString
 * @return String
 */
    @Override
    public String toString() {
        return "PersonalResource{" + "id=" + personalresource_id + ", quantity=" + quantity + ", type=" + type + ", price=" + price + ", dateExpired=" + dateExpired + ", dateHired=" + dateHired + ", event=" + event + '}';
    }

    
    
}
