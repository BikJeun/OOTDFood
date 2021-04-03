/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientEntity;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.IngredientEntityExistsException;
import util.exception.IngredientEntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateIngredientException;

/**
 *
 * @author yuntiangu
 */
@Stateless
public class IngredientEntitySessionBean implements IngredientEntitySessionBeanLocal {

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public IngredientEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createIngredientEntityForMeal(IngredientEntity ingre) throws IngredientEntityExistsException, UnknownPersistenceException, InputDataValidationException {

        Set<ConstraintViolation<IngredientEntity>> constraintViolations = validator.validate(ingre);
        if (constraintViolations.isEmpty()) {
            try {

                em.persist(ingre);
                em.flush();

                return ingre.getIngredientId();
            } catch (PersistenceException ex) {
                if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                    if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                        throw new IngredientEntityExistsException();
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }

    }

    @Override
    public List<IngredientEntity> retrieveAllIngredients() {
        Query query = em.createQuery("SELECT i FROM IngredientEntity i");
        return query.getResultList();
    }

    @Override
    public List<IngredientEntity> retrieveIngredientsWithMatchingName(String inputName) {
        Query query = em.createQuery("SELECT i FROM IngredientEntity i WHERE i.name LIKE '%:inputName%'");
        query.setParameter("inputName", inputName);
        return query.getResultList();
    }

    @Override
    public IngredientEntity retrieveIngredientById(Long ingreId) throws IngredientEntityNotFoundException {
        IngredientEntity ingre = em.find(IngredientEntity.class, ingreId);
        if (ingre != null) {
            return ingre;
        } else {
            throw new IngredientEntityNotFoundException("Ingredient Entity ID " + ingreId + " does not exist!");
        }
    }

    @Override
    public void updateIngredient(IngredientEntity ingred) throws InputDataValidationException, IngredientEntityNotFoundException, UpdateIngredientException {
        if (ingred != null && ingred.getIngredientId() != null) {
            Set<ConstraintViolation<IngredientEntity>> constraintViolations = validator.validate(ingred);
            if (constraintViolations.isEmpty()) {
                IngredientEntity updatedIngred = retrieveIngredientById(ingred.getIngredientId());
                System.out.println(updatedIngred.getIngredientId().compareTo(ingred.getIngredientId()));
                if (updatedIngred.getIngredientId().compareTo(ingred.getIngredientId()) == 0) {
                    updatedIngred.setName(ingred.getName());
                    updatedIngred.setCalorie(ingred.getCalorie());
                    updatedIngred.setPrice(ingred.getPrice());
                    updatedIngred.setType(ingred.getType());
                    em.flush();
                } else {
                    throw new UpdateIngredientException("Id of ingredient to update does not exist");
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));

            }

        } else {
            throw new IngredientEntityNotFoundException("Ingredient ID not provided");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<IngredientEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

    public List<IngredientEntity> retrieveListOfBases() {
        Query query = em.createQuery("SELECT ingredients FROM IngredientEntity ingredients WHERE ingredients.type = util.enumeration.IngredientTypeEnum.BASE");
        return query.getResultList();
    }

    public List<IngredientEntity> retrieveListOfMeats() {
        Query query = em.createQuery("SELECT ingredients FROM IngredientEntity ingredients WHERE ingredients.type = util.enumeration.IngredientTypeEnum.MEAT");
        return query.getResultList();
    }

    public List<IngredientEntity> retrieveListOfVegetables() {
        Query query = em.createQuery("SELECT ingredients FROM IngredientEntity ingredients WHERE ingredients.type = util.enumeration.IngredientTypeEnum.VEGE");
        return query.getResultList();
    }

    public List<IngredientEntity> retrieveListOfAddons() {
        Query query = em.createQuery("SELECT ingredients FROM IngredientEntity ingredients WHERE ingredients.type = util.enumeration.IngredientTypeEnum.ADDON");
        return query.getResultList();
    }

    public List<IngredientEntity> retrieveListOfSauces() {
        Query query = em.createQuery("SELECT ingredients FROM IngredientEntity ingredients WHERE ingredients.type = util.enumeration.IngredientTypeEnum.SAUCE");
        return query.getResultList();
    }

}
