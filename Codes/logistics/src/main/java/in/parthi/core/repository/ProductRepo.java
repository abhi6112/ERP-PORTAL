package in.parthi.core.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import in.parthi.common.Properties;
import in.parthi.core.model.product.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ProductRepo {

    private static final Logger logger = LoggerFactory.getLogger(ProductRepo.class);
    List<Product> productList = new ArrayList<>();

    /**
     * This method take a an procuct id and retrieve the product from the database.
     * 
     * @param String id with which the product needs to be found
     * @return Returns the product
     * @throws RuntimeException if the product is unavailable in the database.
     */
    public Product getProduct(String id) throws RuntimeException {// Create a NotFound Exception
        Product product = null;
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Logistic");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        product = entityManager.find(Product.class, id);

        for (Product tmpProduct : productList) {
            if (tmpProduct.getId().equalsIgnoreCase(id)) {
                product = tmpProduct;
                logger.info("Product with id: " + id + " found in database");
            }

        }

        if (product == null) {
            logger.warn("Product with id: " + id + " not found in database");
            throw new RuntimeException("The product with id " + id + " Not Found");
        }

        entityManager.close();
        entityManagerFactory.close();

        return product;

    }

    /**
     * This method take a product details and add it to the database.
     * 
     * @param Product that need to be added to the database
     * @return Returns the choice of the user
     * @throws RuntimeException if the product is already available in the database.
     */
    public String addProduct(Product product) throws RuntimeException {// Create a NotFound Exception
        //
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Logistic");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        String response = "";
        boolean hasProduct = false;
        for (Product tmpProduct : productList) {
            if (tmpProduct.getId().equalsIgnoreCase(product.getId())) {
                hasProduct = true;
                break;
            }

        }
        if (hasProduct) {
            logger.warn("Product with id: " + product.getId() + " already available in database");

            throw new RuntimeException("The product with id " + product.getId() + " Already present in database, Unable to create the product");



        }

        //add product and save to db
        entityManager.persist(product);
        entityManager.getTransaction().commit();
         response = "Product added successfully";
        logger.info("Product with id: " + product.getId() + " added to the database");
        entityManager.close();
        entityManagerFactory.close();
        return response;
    }


    /**
     * This method take a product id and check with the database.
     * 
     * @param Productid that needs to be checked to the database
     * @return Returns responce
     * @throws RuntimeException if the product is not available in the database.
     */


    public String returnToVendor(String id) throws RuntimeException {
        
        boolean hasProduct = true;
        String response = null;

        for (Product tempProduct : productList) {
            if (tempProduct.getId().equalsIgnoreCase(id)) {
                tempProduct.setStatus(Properties.STATUS_RETURNED);
                LocalDate today = LocalDate.now();
                tempProduct.setStockOutDate(today);
                hasProduct = true;
                response = "Product with id "+id+" updated successfully";
                logger.info(response);
                break;
            }


            if (!hasProduct) {
                logger.warn("Product with id "+id+" not found: Update failed");
                throw new RuntimeException("Product with id "+id+" not found: Update failed");
            }

        }

        return response;

    }



}
