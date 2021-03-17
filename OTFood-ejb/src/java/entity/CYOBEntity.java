/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Entity;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class CYOBEntity extends MealEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    public CYOBEntity() {
        super();
    }


    public CYOBEntity(String name, BigDecimal price, String description, Integer calorie) {
        super(name, price, description, calorie);

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (mealId != null ? mealId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CYOBEntity)) {
            return false;
        }
        CYOBEntity other = (CYOBEntity) object;
        if ((this.mealId == null && other.mealId != null) || (this.mealId != null && !this.mealId.equals(other.mealId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CYOBEntity[ id=" + mealId + " ]";
    }

}
