/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kedamosServerSide.entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;

/**
 *
 * @author Steven Arce
 */
@Entity
public class Client extends User implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Long accountNumber;
    private boolean isPremium;

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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.accountNumber);
        hash = 97 * hash + (this.isPremium ? 1 : 0);
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
        final Client other = (Client) obj;
        if (this.isPremium != other.isPremium) {
            return false;
        }
        if (!Objects.equals(this.accountNumber, other.accountNumber)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Client{" + "accountNumber=" + accountNumber + ", isPremium=" + isPremium + '}';
    }

    
    
}
