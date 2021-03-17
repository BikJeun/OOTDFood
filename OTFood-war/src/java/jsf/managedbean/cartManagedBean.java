/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.MealEntity;
import entity.OTUserEntity;
//import entity.Order;
import entity.SaleTransactionLineEntity;
import java.io.IOException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import util.exception.MealNotFoundException;

/**
 *
 * @author Mitsuki
 */
@Named(value = "cartManagedBean")
@SessionScoped
public class cartManagedBean implements Serializable {

    @EJB
    private MealEntitySessionBeanLocal mealEntitySessionBean;

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBean;

    private OTUserEntity currentUser;
//    private List<Order> orders;
    private List<SaleTransactionLineEntity> lineItems;

    private BigDecimal totalAmount;
    private int amtToCart;

    /**
     * Creates a new instance of cartManagedBean
     */
    public cartManagedBean() {
        //orders = new ArrayList<>();
        lineItems = new ArrayList<>();
        amtToCart = 0;
        totalAmount = new BigDecimal("0.00");

    }

    @PostConstruct
    public void postConstruct() {
        try {
            getFakeData();
            System.out.println(">>>>> FINISH <<<<<<");
        } catch (MealNotFoundException ex) {
            Logger.getLogger(cartManagedBean.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void addToCart(MealEntity meal) {
        //checks if the meal already exists in the cart;
        int currentIndexInCart = existInCart(meal);

        if (amtToCart == 0) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please select at least 1 item.", null));
        } else {
            if (currentIndexInCart == -1) {
//                orders.add(new Order(meal, amtToCart));
                lineItems.add(new SaleTransactionLineEntity(meal, amtToCart));
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Added to cart!.", null));
            } else {
//                int qty = orders.get(currentIndexInCart).getQuantity() + amtToCart;
//                orders.get(currentIndexInCart).setQuantity(qty);
                int qty = lineItems.get(currentIndexInCart).getQuantity() + amtToCart;
                lineItems.get(currentIndexInCart).setQuantity(qty);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Quantity updated!.", null));
            }
            amtToCart = 0;
        }
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }

    private int existInCart(MealEntity meal) {
//        for (int i = 0; i < orders.size(); i++) {
//            if (orders.get(i).getMeal().getMealId() == meal.getMealId()) {
//                return i;
//            }
//        }
        for (int i = 0; i < lineItems.size(); i++) {
            if (lineItems.get(i).getMeal().getMealId() == meal.getMealId()) {
                return i;
            }
        }
        return -1;
    }

    public BigDecimal totalAmount() {
        System.out.println(">>>>>>> Total Amount <<<<<<<<");
//        for (Order ord : this.orders) {
//            totalAmount = totalAmount.add(ord.getMeal().getPrice().multiply(BigDecimal.valueOf(ord.getQuantity())));
//        }
        for (SaleTransactionLineEntity ord : this.lineItems) {
            totalAmount = totalAmount.add(ord.getMeal().getPrice().multiply(BigDecimal.valueOf(ord.getQuantity())));
        }
        System.out.println(totalAmount);
        return totalAmount;
    }

    public void removeFromCart(ActionEvent event) {
        System.out.println(">>>>> Removing!!! <<<<<<<");
        MealEntity meal = (MealEntity) event.getComponent().getAttributes().get("bento");
        int currentIndexInCart = existInCart(meal);
//        orders.remove(currentIndexInCart);
        lineItems.remove(currentIndexInCart);

        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName());

        }

        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Meal removed!.", null));

        if (lineItems.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", true);
        }
    }

    private void getFakeData() throws MealNotFoundException {
        System.out.println(">>>>>> fake data <<<<<<<<");
        SaleTransactionLineEntity order1 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(1l), 2);
        SaleTransactionLineEntity order2 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(2l), 1);
        SaleTransactionLineEntity order3 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(3l), 4);
        SaleTransactionLineEntity order4 = new SaleTransactionLineEntity(mealEntitySessionBean.retrieveMealById(4l), 3);
//        orders.add(order1);
//        orders.add(order2);
//        orders.add(order3);
//        orders.add(order4);
        lineItems.add(order1);
        lineItems.add(order2);
        lineItems.add(order3);
        lineItems.add(order4);

        System.out.println(lineItems.size());

        for (int i = 0; i < lineItems.size(); i++) {
            System.out.println(lineItems.get(i).getMeal().getName());

        }
        totalAmount();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cartEmpty", false);
    }
//    //after checkout complete
//    public void removeEverything() {
//
//    }
//
//

    public void directToCheckout(ActionEvent event) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/userPages/checkout.xhtml");
        setCurrentUser((OTUserEntity) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("currentUser"));
    }

    public OTUserEntity getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(OTUserEntity currentUser) {
        this.currentUser = currentUser;
    }

//    public List<Order> getOrders() {
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getAmtToCart() {
        return amtToCart;
    }

    public void setAmtToCart(int amtToCart) {
        this.amtToCart = amtToCart;
    }

    public List<SaleTransactionLineEntity> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<SaleTransactionLineEntity> lineItems) {
        this.lineItems = lineItems;
    }

}
