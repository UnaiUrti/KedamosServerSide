package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 * Entidad que representa al usuario que es un cliente. 
 * @author Steven Arce
 */
@Entity
public class Client extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long accountNumber;
    private boolean isPremium;

    @OneToMany(mappedBy = "organizer", cascade = ALL)
    private Set<Event> myEvents;
    
    @ManyToMany(cascade = ALL)
    @JoinTable(name = "user_event", schema = "kedamosdb",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "event_id"))
    private Set<Event> joinEvents; 
    
    @OneToMany(mappedBy = "user", cascade = ALL)
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

    public Set<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(Set<Event> myEvents) {
        this.myEvents = myEvents;
    }

    public Set<Event> getJoinEvents() {
        return joinEvents;
    }

    public void setJoinEvents(Set<Event> joinEvents) {
        this.joinEvents = joinEvents;
    }

    public Set<Comment> getMyComments() {
        return myComments;
    }

    public void setMyComments(Set<Comment> myComments) {
        this.myComments = myComments;
    }
    
}
