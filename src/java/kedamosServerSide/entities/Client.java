package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidad que representa al usuario que es un cliente.
 * @author Steven Arce
 */
@NamedQueries({
    @NamedQuery(
            name = "getClientByUsername", query = "SELECT c FROM Client c WHERE c.username = :username"
    ),
    @NamedQuery(
            name = "getClientByEmail", query = "SELECT c FROM Client c WHERE c.email = :email"
    )
})
@Entity
@DiscriminatorValue("CLIENT")
@XmlRootElement
public class Client extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long accountNumber;
    private boolean isPremium;

    @OneToMany(mappedBy = "organizer", fetch = FetchType.EAGER)
    private Set<Event> myEvents;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_event", schema = "kedamosdb",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id", referencedColumnName = "event_id"))
    private Set<Event> joinEvents;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Comment> myComments;

    public Long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public boolean isIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean isPremium) {
        this.isPremium = isPremium;
    }
    
    //@XmlTransient
    public Set<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(Set<Event> myEvents) {
        this.myEvents = myEvents;
    }
    
    //@XmlTransient
    public Set<Event> getJoinEvents() {
        return joinEvents;
    }

    public void setJoinEvents(Set<Event> joinEvents) {
        this.joinEvents = joinEvents;
    }
    
    //@XmlTransient
    public Set<Comment> getMyComments() {
        return myComments;
    }

    public void setMyComments(Set<Comment> myComments) {
        this.myComments = myComments;
    }

}
