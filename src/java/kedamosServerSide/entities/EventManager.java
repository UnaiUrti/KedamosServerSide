package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Set;
import static javax.persistence.CascadeType.ALL;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 * Entidad que representa al usuario que administra los eventos.
 * @author Steven Arce
 */
@Entity
public class EventManager extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Category managerCategoy;
    
    @OneToMany(mappedBy = "user", cascade = ALL)
    private Set<Revise> myRevisions;

    public Category getManagerCategoy() {
        return managerCategoy;
    }

    public void setManagerCategoy(Category managerCategoy) {
        this.managerCategoy = managerCategoy;
    }

    public Set<Revise> getMyRevisions() {
        return myRevisions;
    }

    public void setMyRevisions(Set<Revise> myRevisions) {
        this.myRevisions = myRevisions;
    }

    
  
    
   
    
    
}
