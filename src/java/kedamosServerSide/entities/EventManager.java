package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que representa al usuario que administra los eventos.
 * @author Steven Arce
 */
@NamedQueries({
    @NamedQuery(
            name = "getEventManagerByUsername", query = "SELECT e FROM EventManager e WHERE e.username = :username"
    ),
    @NamedQuery(
            name = "getEventManagerByEmail", query = "SELECT e FROM EventManager e WHERE e.email = :email"
    )
})
@Entity
@DiscriminatorValue("EVENT_MANAGER")
@XmlRootElement
public class EventManager extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    private Category managerCategory;

    @OneToMany(mappedBy = "eventManager", fetch = FetchType.EAGER)
    private Set<Revise> myRevisions;

    public Category getManagerCategory() {
        return managerCategory;
    }

    public void setManagerCategory(Category managerCategory) {
        this.managerCategory = managerCategory;
    }

    //@XmlTransient
    public Set<Revise> getMyRevisions() {
        return myRevisions;
    }

    public void setMyRevisions(Set<Revise> myRevisions) {
        this.myRevisions = myRevisions;
    }

}
