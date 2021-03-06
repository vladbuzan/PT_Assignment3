package Model;

/**
 * Class containing order information(quantity and paid amount)
 */
public class OrderItem {
    private int quantity;
    private float paid;

    /**
     * Constructor
     * @param quantity integer containing the bought quantity
     * @param paid float containing the amount paid in the transaction
     */
    public OrderItem(int quantity, float paid) {
        this.quantity = quantity;
        this.paid = paid;
    }

    /**
     * No parameter contructor. Sets all fields to 0.
     */
    public OrderItem() {
        quantity = 0;
        paid = 0;
    }

    /**
     * Setter method for the quantity field.
     * @param quantity integer containing the bought quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Setter method for the paid field.
     * @param paid float containing the amount paid in the transaction
     */
    public void setPaid(float paid) {
        this.paid = paid;
    }

    /**
     * Getter method for the quantity field.
     * @return integer containing the bought quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Getter method for the paid field.
     * @return float containing the amount paid in the transaction
     */
    public float getPaid() {
        return paid;
    }
}
