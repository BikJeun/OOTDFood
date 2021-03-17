/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class SaleTransactionLineEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionLineItemId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer quantity;
//    @Column(nullable = false, precision = 11, scale = 2)
//    @NotNull
//    @DecimalMin("0.00")
//    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
//    private BigDecimal unitPrice;
//    @Column(nullable = false, precision = 11, scale = 2)
//    @NotNull
//    @DecimalMin("0.00")
//    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
//    private BigDecimal subTotal;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private MealEntity meal;

    public SaleTransactionLineEntity() {
    }


    public SaleTransactionLineEntity(MealEntity meal, Integer quantity) {
        this.quantity = quantity;
//        this.unitPrice = unitPrice;
//        this.subTotal = subTotal;
        this.meal = meal;
    }

    public Long getSaleTransactionLineItemId() {
        return saleTransactionLineItemId;
    }

    public void setSaleTransactionLineItemId(Long saleTransactionLineItemId) {
        this.saleTransactionLineItemId = saleTransactionLineItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public MealEntity getMeal() {
        return meal;
    }

    public void setMeal(MealEntity meal) {
        this.meal = meal;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransactionLineItemId != null ? saleTransactionLineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SaleTransactionLineEntity)) {
            return false;
        }
        SaleTransactionLineEntity other = (SaleTransactionLineEntity) object;
        if ((this.saleTransactionLineItemId == null && other.saleTransactionLineItemId != null) || (this.saleTransactionLineItemId != null && !this.saleTransactionLineItemId.equals(other.saleTransactionLineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransactionLineEntity[ id=" + saleTransactionLineItemId + " ]";
    }

}
