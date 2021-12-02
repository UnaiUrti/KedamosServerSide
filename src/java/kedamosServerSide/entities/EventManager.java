package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;

/**
 * Entidad que representa al usuario que administra los eventos.
 * @author Steven Arce
 */
@Entity
public class EventManager extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Category managerCategoy;

    public Category getManagerCategoy() {
        return managerCategoy;
    }

    public void setManagerCategoy(Category managerCategoy) {
        this.managerCategoy = managerCategoy;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.managerCategoy);
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
        final EventManager other = (EventManager) obj;
        if (this.managerCategoy != other.managerCategoy) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EventManager{" + "managerCategoy=" + managerCategoy + '}';
    }
    
    
}
