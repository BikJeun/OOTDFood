package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.ReviewEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.TabChangeEvent;
import util.enumeration.CategoryEnum;

/**
 *
 * @author yuntiangu
 */
@Named(value = "viewBentoManagedBean")
@ViewScoped
public class ViewBentoManagedBean implements Serializable {

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;
    
    @Inject
    private cartManagedBean cartManagedBean;

    private List<BentoEntity> listOfBentos;
    private List<BentoEntity> listOfAllBentos;
    private List<CategoryEnum> listOfCategories;
    private String selectedCategory;
    private BentoEntity selectedBento;
    private int selectedBentoQuantity;
    
    private List<ReviewEntity> currentBentoReviews;

    @PostConstruct
    public void postConstruct() {
        this.currentBentoReviews = new ArrayList<>();
        this.listOfCategories = new ArrayList<>();
        this.listOfCategories.add(CategoryEnum.CHICKEN);
        this.listOfCategories.add(CategoryEnum.FISH);
        this.listOfCategories.add(CategoryEnum.BEEF);
        this.listOfCategories.add(CategoryEnum.PORK);
        this.listOfCategories.add(CategoryEnum.VEGETARIAN);
        this.listOfCategories.add(CategoryEnum.VEGAN);
        this.listOfCategories.add(CategoryEnum.BALANCED);
        this.setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory("CHICKEN"));
        this.setListOfAllBentos(mealEntitySessionBeanLocal.retriveAllBentos());
    }

    public ViewBentoManagedBean() {
        this.selectedBento = new BentoEntity();
        this.selectedBentoQuantity = 0;
        this.selectedCategory = "Chicken";
        
    }

    public void refreshListByTabSelected(TabChangeEvent event) {
        System.out.println("List Refreshed");
        String currentTabId = event.getTab().getTitle(); //This will be the CategoryEnum
        System.out.println("tab Title: " + currentTabId);
        setSelectedCategory(event.getTab().getTitle());
        System.out.println("current category " + getSelectedCategory());
        setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(currentTabId));
        System.out.println("No of Bentos: " + this.listOfBentos.toString());   
    }
    
  

    public void getListOfBentosByCategory(AjaxBehaviorEvent event) {
        this.setListOfBentos(mealEntitySessionBeanLocal.retrieveBentosByCategory(getSelectedCategory()));
    }
    
    public void addBentoToCart(ActionEvent event) {
        cartManagedBean.setAmtToCart(selectedBentoQuantity);
        cartManagedBean.addToCart(selectedBento);
        selectedBentoQuantity = 0;
        PrimeFaces.current().ajax().update("cartForm");
    }
    
    public void loadBentoReviews() {
        currentBentoReviews = new ArrayList<>();
        selectedBento.getReviews().size();
        currentBentoReviews = selectedBento.getReviews();
        System.out.println("reviews: " + currentBentoReviews);
    }
    /**
     * @return the listOfBentos
     */
    public List<BentoEntity> getListOfBentos() {
        return listOfBentos;
    }

    /**
     * @param listOfBentos the listOfBentos to set
     */
    public void setListOfBentos(List<BentoEntity> listOfBentos) {
        this.listOfBentos = listOfBentos;
    }

    /**
     * @return the listOfCategories
     */
    public List<CategoryEnum> getListOfCategories() {
        return listOfCategories;
    }

    /**
     * @param listOfCategories the listOfCategories to set
     */
    public void setListOfCategories(List<CategoryEnum> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }

    /**
     * @return the selectedCategory
     */
    public String getSelectedCategory() {
        return selectedCategory;
    }

    /**
     * @param selectedCategory the selectedCategory to set
     */
    public void setSelectedCategory(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    /**
     * @return the listOfAllBentos
     */
    public List<BentoEntity> getListOfAllBentos() {
        return listOfAllBentos;
    }

    /**
     * @param listOfAllBentos the listOfAllBentos to set
     */
    public void setListOfAllBentos(List<BentoEntity> listOfAllBentos) {
        this.listOfAllBentos = listOfAllBentos;
    }

    /**
     * @return the selectedBento
     */
    public BentoEntity getSelectedBento() {
        System.out.println("current selected bento: " + selectedBento);
        return selectedBento;
    }

    /**
     * @param selectedBento the selectedBento to set
     */
    public void setSelectedBento(BentoEntity selectedBento) {
        this.selectedBento = selectedBento;
    }

    /**
     * @return the selectedBentoQuantity
     */
    public int getSelectedBentoQuantity() {
        return selectedBentoQuantity;
    }

    /**
     * @param selectedBentoQuantity the selectedBentoQuantity to set
     */
    public void setSelectedBentoQuantity(int selectedBentoQuantity) {
        System.out.println("Setting selectedBentoQuantity: " + selectedBentoQuantity);
        this.selectedBentoQuantity = selectedBentoQuantity;
    }

    /**
     * @return the currentBentoReviews
     */
    public List<ReviewEntity> getCurrentBentoReviews() {
        return currentBentoReviews;
    }

    /**
     * @param currentBentoReviews the currentBentoReviews to set
     */
    public void setCurrentBentoReviews(List<ReviewEntity> currentBentoReviews) {
        this.currentBentoReviews = currentBentoReviews;
    }
}