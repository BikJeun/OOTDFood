/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AddressEntity;
import entity.CreditCardEntity;
import entity.IngredientEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineItemEntity;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.CreateNewSaleTransactionException;
import util.exception.CreditCardNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.NoAddressFoundException;
import util.exception.NoSaleTransactionFoundException;
import util.exception.UpdateSaleTransactionException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Stateless
public class SaleTransactionEntitySessionBean implements SaleTransactionEntitySessionBeanLocal {

    @EJB
    private AddressEntitySessionBeanLocal addressEntitySessionBeanLocal;

    @EJB
    private CreditCardEntitySessionBeanLocal creditCardEntitySessionBeanLocal;
            
    @EJB
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @EJB
    private IngredientEntitySessionBeanLocal ingredientEntitySessionBeanLocal;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;
    
    public SaleTransactionEntitySessionBean() {
        this.validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Override
    public Long createNewSaleTransaction(Long userId, Long ccId, Long adressId, SaleTransactionEntity saleTransaction) throws CreateNewSaleTransactionException, InputDataValidationException {
        Set<ConstraintViolation<SaleTransactionEntity>> constraintViolations = validator.validate(saleTransaction);
        if (constraintViolations.isEmpty()) {
            if (saleTransaction != null) {
                try {
                    OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
                    CreditCardEntity cc = creditCardEntitySessionBeanLocal.retrieveCardById(ccId);
                    AddressEntity addresss = addressEntitySessionBeanLocal.retrieveAddressById(adressId);
                    saleTransaction.setUser(user);
                    saleTransaction.setCreditCardEntity(cc);
                    saleTransaction.setAddress(addresss);
                    user.getSaleTransaction().add(saleTransaction);
                    em.persist(saleTransaction);
                    for (int i = 0; i < saleTransaction.getTotalLineItem(); i++) {
                        SaleTransactionLineItemEntity lineItem = saleTransaction.getSaleTransactionLineItemEntities().get(i);
                        em.persist(lineItem);
                        for (int j = 0; j < lineItem.getQuantity(); j++) {
                            MealEntity meal = lineItem.getMeal();
                            List<IngredientEntity> ingredients = meal.getIngredients();
                            for (IngredientEntity ingredient : ingredients) {
                                //ingredientEntitySessionBeanLocal.deductStockQuantity(ingredient.getIngredientId(), 1);
                            }
                        }
                    }
                    em.flush();
                    return saleTransaction.getSaleTransactionId();
                } catch (UserNotFoundException ex) {
                    throw new CreateNewSaleTransactionException("Error: User not found!");
                } catch (CreditCardNotFoundException | NoAddressFoundException ex) {
                    throw new CreateNewSaleTransactionException("Error: Address or Credit Card not found!"); //should never get this
                }
            } else {
                throw new CreateNewSaleTransactionException("Error: saleTransaction provided is null!");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<SaleTransactionEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }
    @Override
    public List<SaleTransactionEntity> retrieveAllSaleTransaction() throws NoSaleTransactionFoundException {
        try {
            Query query = em.createQuery("SELECT st FROM SaleTransactionEntity st");
            return query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoSaleTransactionFoundException("No sale transactions found!");
        }
    }
    
    @Override
    public List<SaleTransactionEntity> retrieveSaleTransactionsByUserId(Long userId) throws NoSaleTransactionFoundException {
        try {
            Query query = em.createQuery("SELECT st FROM OTUserEntity user JOIN user.saleTransaction st WHERE user.UserId = :userId");
            query.setParameter("userId", userId);
            List<SaleTransactionEntity> saleTransactions = query.getResultList();
            return saleTransactions;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoSaleTransactionFoundException("userId: " + userId + " has no sale transactions!");
        }
    }
    
    @Override
    public SaleTransactionEntity retrieveSaleTransactionByUserId(Long userId, Long transactionId) throws NoSaleTransactionFoundException {
        try {
            Query query = em.createQuery("SELECT st FROM OTUserEntity user JOIN user.saleTransaction st WHERE user.UserId = :userId AND st.saleTransactionId = :saleTransactionId");
            query.setParameter("userId", userId);
            query.setParameter("saleTransactionId", transactionId);
            SaleTransactionEntity saleTransaction = (SaleTransactionEntity) query.getSingleResult();
            return saleTransaction;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new NoSaleTransactionFoundException("userId: " + userId + " has no sale transaction of Id: " + transactionId);
        }   
    }
    
    @Override
    public void updateSaleTransaction(Long userId, SaleTransactionEntity saleTransaction) throws UpdateSaleTransactionException {
        try {
            OTUserEntity user = oTUserEntitySessionBeanLocal.retrieveUserById(userId);
            SaleTransactionEntity currentSaleTransaction = retrieveSaleTransactionByUserId(userId, saleTransaction.getSaleTransactionId());
            if (currentSaleTransaction != null) {
                currentSaleTransaction.setVoidRefund(saleTransaction.getVoidRefund());
                //What other stuff?
            } else {
                throw new UpdateSaleTransactionException("Error: Sale transaction provided is null!");
            }
        } catch (UserNotFoundException ex) {
            throw new UpdateSaleTransactionException("Error: User cannot be found!");
        } catch (NoSaleTransactionFoundException ex) {
            throw new UpdateSaleTransactionException("Error: No sale Transaction detected!");
        }
    }
    
    //delete?
    
    
    
    
}
