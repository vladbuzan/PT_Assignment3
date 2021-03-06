package Model;

/**
 * Class used to represent a product and
 * its related information(name, quantity and price).
 */
public class Product {
    private String name;
    private int quantity;
    private float price;

    /**
     * Constructor
     * @param name String representing the name of the product
     * @param quantity integer representing the quantity
     * @param price float representing the price
     */
    public Product(String name, int quantity, float price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    /**
     * No parameter constructor. Initialises all values
     * to null, 0 respectively.
     */
    public Product() {
        name = null;
        quantity = 0;
        price = 0;
    }

    /**
     * Constructor for when only the name
     * of the product is known
     * @param name
     */
    public Product(String name) {
        this.name = name;
    }

    /**
     * Getter for the name field.
     * @return String containing the name of the product.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the quantity field.
     * @return int representing the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter for the price field.
     * @return float representing the price of the product
     */
    public float getPrice() {
        return price;
    }

    /**
     * Setter for the quantity field.
     * @param quantity int representing the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Setter for the name field.
     * @param name String containing the name of the product.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for the price field.
     * @param price float representing the price of the product
     */
    public void setPrice(float price) {
        this.price = price;
    }
}
