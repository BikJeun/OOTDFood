/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.IngredientEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.IngredientEntityExistsException;
import util.exception.IngredientEntityNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateIngredientException;

@Local
public interface IngredientEntitySessionBeanLocal {

    public List<IngredientEntity> retrieveAllIngredients();

    public List<IngredientEntity> retrieveIngredientsWithMatchingName(String inputName);

    public IngredientEntity retrieveIngredientById(Long ingreId) throws IngredientEntityNotFoundException;

    public Long createIngredientEntityForMeal(IngredientEntity ingre) throws IngredientEntityExistsException, UnknownPersistenceException, InputDataValidationException;

    public List<IngredientEntity> retrieveListOfBases();

    public List<IngredientEntity> retrieveListOfMeats();

    public List<IngredientEntity> retrieveListOfVegetables();

    public List<IngredientEntity> retrieveListOfAddons();

    public List<IngredientEntity> retrieveListOfSauces();

    public void updateIngredient(IngredientEntity ingred) throws InputDataValidationException, IngredientEntityNotFoundException, UpdateIngredientException;

}
