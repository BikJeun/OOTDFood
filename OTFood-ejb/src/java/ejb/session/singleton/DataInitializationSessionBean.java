/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.DriverEntitySessionBeanLocal;
import ejb.session.stateless.FaqSessionBeanLocal;
import ejb.session.stateless.MealEntitySessionBeanLocal;
import ejb.session.stateless.OTUserEntitySessionBeanLocal;
import ejb.session.stateless.ReviewEntitySessionBeanLocal;
import ejb.session.stateless.SaleTransactionEntitySessionBeanLocal;
import entity.BentoEntity;
import entity.DriverEntity;
import entity.FaqEntity;
import entity.MealEntity;
import entity.OTUserEntity;
import entity.ReviewEntity;
import entity.SaleTransactionEntity;
import entity.SaleTransactionLineEntity;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.exception.CreateNewSaleTransactionException;
import util.exception.DriverExistsException;
import util.exception.FaqExistException;
import util.exception.InputDataValidationException;
import util.exception.ReviewExistException;
import util.exception.UnknownPersistenceException;
import util.exception.UserExistException;
import util.exception.UserNotFoundException;

/**
 *
 * @author benny
 */
@Startup
@Singleton
@LocalBean
public class DataInitializationSessionBean {

    @EJB
    private DriverEntitySessionBeanLocal driverEntitySessionBeanLocal;

    @EJB
    private SaleTransactionEntitySessionBeanLocal saleTransactionEntitySessionBeanLocal;
    
    @EJB
    private FaqSessionBeanLocal faqSessionBean;

    @EJB(name = "ReviewEntitySessionBeanLocal")
    private ReviewEntitySessionBeanLocal reviewEntitySessionBeanLocal;

    @EJB(name = "OTUserEntitySessionBeanLocal")
    private OTUserEntitySessionBeanLocal oTUserEntitySessionBeanLocal;

    @EJB(name = "MealEntitySessionBeanLocal")
    private MealEntitySessionBeanLocal mealEntitySessionBeanLocal;

    @PersistenceContext(unitName = "OTFood-ejbPU")
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        if (em.find(MealEntity.class, 1l) == null) {
            dataInitialise();
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }

    private void dataInitialise() {
        try {
            
            // Create Users
            OTUserEntity user = new OTUserEntity("bennyphoe1998@gmail.com", "password", 90909090l, "Benny", "Phoe", new Date(), "");
            Long customerId = oTUserEntitySessionBeanLocal.createNewUser(user);
            
            // Create Driver
            DriverEntity driver = new DriverEntity("Benny", "Phoe", 24, "bennyphoe1998", "password", "");
            driverEntitySessionBeanLocal.createNewDriver(driver);
            
            // Create Meals
            List<MealEntity> bentoSets = new ArrayList<>();

            MealEntity bento1 = new BentoEntity("bento1", BigDecimal.valueOf(8.00), "this is test bento 1", 450);
            MealEntity bento2 = new BentoEntity("bento2", BigDecimal.valueOf(8.50), "this is test bento 2", 500);
            MealEntity bento3 = new BentoEntity("bento3", BigDecimal.valueOf(9.00), "this is test bento 3", 550);
            MealEntity bento4 = new BentoEntity("bento4", BigDecimal.valueOf(9.50), "this is test bento 4", 600);
            MealEntity bento5 = new BentoEntity("bento5", BigDecimal.valueOf(10.00), "this is test bento 5", 650);

            bentoSets.add(bento1);
            bentoSets.add(bento2);
            bentoSets.add(bento3);
            bentoSets.add(bento4);
            bentoSets.add(bento5);

            for (MealEntity mealEntity : bentoSets) {
                mealEntitySessionBeanLocal.createNewMeal(mealEntity);
            }
            
            // Create SaleTransactions
            List<SaleTransactionLineEntity> saleTransactionLines = new ArrayList<>();
            saleTransactionLines.add(new SaleTransactionLineEntity(1, 2, BigDecimal.valueOf(8.00), BigDecimal.valueOf(8.00 * 2), bentoSets.get(0)));
            saleTransactionLines.add(new SaleTransactionLineEntity(2, 4, BigDecimal.valueOf(8.50), BigDecimal.valueOf(8.50 * 4), bentoSets.get(1)));
            SaleTransactionEntity saleTransaction = new SaleTransactionEntity(2, 6, BigDecimal.valueOf(8.00 * 2 + 8.50 * 4), new Date(),false);
            saleTransaction.setSaleTransactionLineItemEntities(saleTransactionLines);
            saleTransactionEntitySessionBeanLocal.createNewSaleTransaction(customerId, saleTransaction); // will give error because nullable=false for driver
                    
            // Create user reviews
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 1l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 2l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 3l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 4l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 5l);
            reviewEntitySessionBeanLocal.addReview(new ReviewEntity(5, "This is amazing!", new Date()), customerId, 5l);

            // Create FAQ Entries
            faqSessionBean.createNewFaq(new FaqEntity("How can I track my order?", "You can track your order by viewing your sales transaction status. When being delivered a status marked 'DELIVERING' will appear.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("I made a mistake. Can I change my order?", "We strive to process orders as quickly as possible. But we will try to accommodate any order change. The quickest way to get a hold of us is by emailing us at ootdFood@gmail.com.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("Can i make custom orders", "Yes, of course! You can do so under our CYOB page under meals.", "Orders"));
            faqSessionBean.createNewFaq(new FaqEntity("What payment methods do you accept?", "We accept Visa and Mastercard for online orders.", "Orders"));

            faqSessionBean.createNewFaq(new FaqEntity("How long would it take to receieve my order?", "We have fixed delivery timings, lunch : 12-2pm, dinner: 6-8pm, supper : 12am-2am", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How much is delivery fee?", "We have a flat rate of $2 for regions within 5km from us and $5 for the rest", "Delivery"));
            faqSessionBean.createNewFaq(new FaqEntity("How do i know my order is on the way?", "If the 'Ordered' status changes to 'Delivering', you can look forward to having your scrumptious bentos within 30mins.", "Delivery"));

            faqSessionBean.createNewFaq(new FaqEntity("Where do we get our ingredients from?", "We source them from trustable suppliers providing only the best of the crops.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Will my Bento look exactly like the photo?", "Our chefs guarantees individual attention to detail and quality assurance in every bowl of bento to leave the kitchen.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Are there different sizes to the bento?", "Sorry but at the moment, we only offer a fixed protion size.", "Product"));
            faqSessionBean.createNewFaq(new FaqEntity("Can I visit your shop?", "Currently we are a home grown business, hence we do not have an outlet store. We do appreciate your continuous support to allow us to achieve the dream of opening our own store", "Product"));

        } catch (UserExistException | UnknownPersistenceException | InputDataValidationException | ReviewExistException | UserNotFoundException | FaqExistException | CreateNewSaleTransactionException | DriverExistsException ex) {
            Logger.getLogger(DataInitializationSessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
