package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Entidad que representa al usuario que administra los eventos.
 *
 * @author Steven Arce
 */
@Entity
@DiscriminatorValue("Event_manager")
@XmlRootElement
public class EventManager extends User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    private Category managerCategory;

    @OneToMany(mappedBy = "eventManager")
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
