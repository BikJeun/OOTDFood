/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import util.enumeration.DeliveryStatusEnum;

/**
 *
 * @author Ong Bik Jeun
 */
@Entity
public class SaleTransactionEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleTransactionId;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalLineItem;
    @Column(nullable = false)
    @NotNull
    @Min(1)
    private Integer totalQuantity;
    @Column(nullable = false, precision = 11, scale = 2)
    @NotNull
    @DecimalMin("0.00")
    @Digits(integer = 9, fraction = 2) // 11 - 2 digits to the left of the decimal point
    private BigDecimal totalAmount;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    private Date transactionDateTime;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    @NotNull
    @Future
    private Date deliveryDateTime;
    @Column(nullable = false)
    @NotNull
    private Boolean voidRefund;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private DeliveryStatusEnum deliveryStatus;

    @OneToMany(fetch = FetchType.EAGER)
    private List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private PromoCodeEntity promoCode;
    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)
    private DriverEntity driver;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private OTUserEntity user;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private AddressEntity address;
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private CreditCardEntity creditCardEntity;

    public SaleTransactionEntity() {
        saleTransactionLineItemEntities = new ArrayList<>();
        this.deliveryStatus = DeliveryStatusEnum.ORDER_RECIEVED;
        this.voidRefund = false;
    }

    public SaleTransactionEntity(Integer totalLineItem, Integer totalQuantity, BigDecimal totalAmount, Date transactionDateTime, Date deliveryDateTime) {

        this();
        this.totalLineItem = totalLineItem;
        this.totalQuantity = totalQuantity;
        this.totalAmount = totalAmount;
        this.transactionDateTime = transactionDateTime;
        this.deliveryDateTime = deliveryDateTime;
    }

    public Integer getTotalLineItem() {
        return totalLineItem;
    }

    public void setTotalLineItem(Integer totalLineItem) {
        this.totalLineItem = totalLineItem;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getTransactionDateTime() {
        return transactionDateTime;
    }

    public void setTransactionDateTime(Date transactionDateTime) {
        this.transactionDateTime = transactionDateTime;
    }

    public Boolean getVoidRefund() {
        return voidRefund;
    }

    public void setVoidRefund(Boolean voidRefund) {
        this.voidRefund = voidRefund;
    }

    public List<SaleTransactionLineItemEntity> getSaleTransactionLineItemEntities() {
        return saleTransactionLineItemEntities;
    }

    public void setSaleTransactionLineItemEntities(List<SaleTransactionLineItemEntity> saleTransactionLineItemEntities) {
        this.saleTransactionLineItemEntities = saleTransactionLineItemEntities;
    }

    public PromoCodeEntity getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(PromoCodeEntity promoCode) {
        this.promoCode = promoCode;
    }

    public DriverEntity getDriver() {
        return driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }

    public OTUserEntity getUser() {
        return user;
    }

    public void setUser(OTUserEntity user) {
        this.user = user;
    }

    public Long getSaleTransactionId() {
        return saleTransactionId;
    }

    public void setSaleTransactionId(Long saleTransactionId) {
        this.saleTransactionId = saleTransactionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (saleTransactionId != null ? saleTransactionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the saleTransactionId fields are not set
        if (!(object instanceof SaleTransactionEntity)) {
            return false;
        }
        SaleTransactionEntity other = (SaleTransactionEntity) object;
        if ((this.saleTransactionId == null && other.saleTransactionId != null) || (this.saleTransactionId != null && !this.saleTransactionId.equals(other.saleTransactionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.SaleTransactionEntity[ id=" + saleTransactionId + " ]";
    }

    /**
     * @return the deliveryDateTime
     */
    public Date getDeliveryDateTime() {
        return deliveryDateTime;
    }

    /**
     * @param deliveryDateTime the deliveryDateTime to set
     */
    public void setDeliveryDateTime(Date deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    /**
     * @return the address
     */
    public AddressEntity getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(AddressEntity address) {
        this.address = address;
    }

    /**
     * @return the creditCardEntity
     */
    public CreditCardEntity getCreditCardEntity() {
        return creditCardEntity;
    }

    /**
     * @param creditCardEntity the creditCardEntity to set
     */
    public void setCreditCardEntity(CreditCardEntity creditCardEntity) {
        this.creditCardEntity = creditCardEntity;
    }

    /**
     * @return the deliveryStatus
     */
    public DeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @param deliveryStatus the deliveryStatus to set
     */
    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

}
